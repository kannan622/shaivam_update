package org.shaivam.playback;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import org.shaivam.activities.RadioActivity;
import org.shaivam.activities.RadioDetailActivity;
import org.shaivam.activities.SixTimePoojaDetailActivity;
import org.shaivam.activities.ThirumuraiSongsDetailActivity;
import org.shaivam.adapter.RadioAdapter;
import org.shaivam.model.Selected_songs;
import org.shaivam.service.MusicService;
import org.shaivam.utils.AppConfig;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static android.content.Context.AUDIO_SERVICE;

/**
 * Exposes the functionality of the {@link MediaPlayer} and implements the {@link PlayerAdapter}
 */
public final class MediaPlayerHolder implements PlayerAdapter, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {

    // The volume we set the media player to when we lose audio focus, but are
    // allowed to reduce the volume instead of stopping playback.
    private static final float VOLUME_DUCK = 0.2f;
    // The volume we set the media player when we have audio focus.
    private static final float VOLUME_NORMAL = 1.0f;
    // we don't have audio focus, and can't duck (play at a low volume)
    private static final int AUDIO_NO_FOCUS_NO_DUCK = 0;
    // we don't have focus, but can duck (play at a low volume)
    private static final int AUDIO_NO_FOCUS_CAN_DUCK = 1;
    // we have full audio focus
    private static final int AUDIO_FOCUSED = 2;
    private Context activityContext;

    private final MusicService mMusicService;
    private final AudioManager mAudioManager;
    private MediaPlayer mMediaPlayer;
    private PlaybackInfoListener mPlaybackInfoListener;
    private ScheduledExecutorService mExecutor;
    private Runnable mSeekBarPositionUpdateTask;
    private Selected_songs mSelectedSong;
    private List<Selected_songs> mSongs;
    private boolean sReplaySong = false;
    private @PlaybackInfoListener.State
    int mState;
    private NotificationReceiver mNotificationActionsReceiver;
    private MusicNotificationManager mMusicNotificationManager;
    private int mCurrentAudioFocusState = AUDIO_NO_FOCUS_NO_DUCK;
    private boolean mPlayOnFocusGain;
    private final AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {

                @Override
                public void onAudioFocusChange(int focusChange) {

                    switch (focusChange) {
                        case AudioManager.AUDIOFOCUS_GAIN:
                            mCurrentAudioFocusState = AUDIO_FOCUSED;
                            break;
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                            // Audio focus was lost, but it's possible to duck (i.e.: play quietly)
                            mCurrentAudioFocusState = AUDIO_NO_FOCUS_CAN_DUCK;
                            break;
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                            // Lost audio focus, but will gain it back (shortly), so note whether
                            // playback should resume
                            mCurrentAudioFocusState = AUDIO_NO_FOCUS_NO_DUCK;
                            mPlayOnFocusGain = isMediaPlayer() && mState == PlaybackInfoListener.State.PLAYING || mState == PlaybackInfoListener.State.RESUMED;
                            break;
                        case AudioManager.AUDIOFOCUS_LOSS:
                            // Lost audio focus, probably "permanently"
                            mCurrentAudioFocusState = AUDIO_NO_FOCUS_NO_DUCK;
                            break;
                    }

                    if (mMediaPlayer != null) {
                        // Update the player state based on the change
                        configurePlayerState();
                    }

                }
            };


    public MediaPlayerHolder(@NonNull final MusicService musicService) {
        mMusicService = musicService;
//        mContext = mMusicService;
        mAudioManager = (AudioManager) musicService.getSystemService(AUDIO_SERVICE);
    }

    private void registerActionsReceiver() {
        mNotificationActionsReceiver = new NotificationReceiver();
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MusicNotificationManager.PREV_ACTION);
        intentFilter.addAction(MusicNotificationManager.PLAY_PAUSE_ACTION);
        intentFilter.addAction(MusicNotificationManager.NEXT_ACTION);
       /* if (activityContext instanceof ThirumuraiSongsDetailActivity) {
            intentFilter.addAction(MusicNotificationManager.PREV_ACTION);
            intentFilter.addAction(MusicNotificationManager.PLAY_PAUSE_ACTION);
            intentFilter.addAction(MusicNotificationManager.NEXT_ACTION);
        } else if (activityContext instanceof RadioActivity) {
            intentFilter.addAction(MusicNotificationManager.PLAY_PAUSE_ACTION);
        }*/
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        intentFilter.addAction(Intent.ACTION_HEADSET_PLUG);
        intentFilter.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY);

        mMusicService.registerReceiver(mNotificationActionsReceiver, intentFilter);
    }

    private void unregisterActionsReceiver() {
        if (mMusicService != null && mNotificationActionsReceiver != null) {
            try {
                mMusicService.unregisterReceiver(mNotificationActionsReceiver);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void registerNotificationActionsReceiver(final boolean isReceiver) {

        if (isReceiver) {
            registerActionsReceiver();
        } else {
            unregisterActionsReceiver();
        }
    }

    @Override
    public final Selected_songs getCurrentSong() {
        return mSelectedSong;
    }

    @Override
    public void setCurrentSong(@NonNull final Selected_songs song, @NonNull final List<Selected_songs> songs) {
        mSelectedSong = song;
        mSongs = songs;
    }

    @SuppressLint("NewApi")
    @Override
    public void onCompletion(@NonNull final MediaPlayer mediaPlayer) {
        if (mPlaybackInfoListener != null) {
            mPlaybackInfoListener.onStateChanged(PlaybackInfoListener.State.COMPLETED);
            mPlaybackInfoListener.onPlaybackCompleted();
        }

        if (activityContext instanceof ThirumuraiSongsDetailActivity) {
            if (sReplaySong) {
                if (isMediaPlayer()) {
                    resetSong();
                }
                sReplaySong = false;
            } else {
                skip(true);
            }
        } else if (activityContext instanceof SixTimePoojaDetailActivity) {
            stop();
        }
    }

    @Override
    public void onResumeActivity() {
        startUpdatingCallbackWithPosition();
    }

    @Override
    public void onPauseActivity() {
        stopUpdatingCallbackWithPosition();
    }

    private void tryToGetAudioFocus() {

        final int result = mAudioManager.requestAudioFocus(
                mOnAudioFocusChangeListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            mCurrentAudioFocusState = AUDIO_FOCUSED;
        } else {
            mCurrentAudioFocusState = AUDIO_NO_FOCUS_NO_DUCK;
        }
    }

    private void giveUpAudioFocus() {
        if (mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener)
                == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            mCurrentAudioFocusState = AUDIO_NO_FOCUS_NO_DUCK;
        }
    }

    public void setPlaybackInfoListener(@NonNull final PlaybackInfoListener listener) {
        mPlaybackInfoListener = listener;
    }

    private void setStatus(final @PlaybackInfoListener.State int state) {

        mState = state;
        if (mPlaybackInfoListener != null) {
            mPlaybackInfoListener.onStateChanged(state);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void resumeMediaPlayer() {
        if (!isPlaying()) {
            if (mMediaPlayer != null) {
                mMediaPlayer.start();
                setStatus(PlaybackInfoListener.State.RESUMED);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mMusicService.startForeground(MusicNotificationManager.NOTIFICATION_ID,
                            mMusicNotificationManager.createNotification(activityContext));
                }
            } else {
                AppConfig.showProgDialiog(activityContext);
                initMediaPlayer(activityContext);
            }
        }
    }

    private void pauseMediaPlayer() {
        setStatus(PlaybackInfoListener.State.PAUSED);
        mMediaPlayer.pause();
        mMusicService.stopForeground(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mMusicNotificationManager.getNotificationManager().notify(MusicNotificationManager.NOTIFICATION_ID,
                    mMusicNotificationManager.createNotification(activityContext));
        }
    }

    public void resetSong() {
        if (AppConfig.isConnectedToInternet(activityContext)) {
            mMediaPlayer.seekTo(0);
            mMediaPlayer.start();
            setStatus(PlaybackInfoListener.State.PLAYING);
        } else {
            AppConfig.hideProgDialog();
            Toast.makeText(activityContext, AppConfig.getTextString(activityContext, AppConfig.connection_message_audio), Toast.LENGTH_LONG).show();
        }
    }

    public void stop() {
        mMediaPlayer.seekTo(0);
        mMediaPlayer.start();
        setStatus(PlaybackInfoListener.State.PLAYING);
        mMediaPlayer.pause();
        setStatus(PlaybackInfoListener.State.PAUSED);
    }

    /**
     * Syncs the mMediaPlayer position with mPlaybackProgressCallback via recurring task.
     */
    private void startUpdatingCallbackWithPosition() {
        if (mExecutor == null) {
            mExecutor = Executors.newSingleThreadScheduledExecutor();
        }
        if (mSeekBarPositionUpdateTask == null) {
            mSeekBarPositionUpdateTask = new Runnable() {
                @Override
                public void run() {
                    updateProgressCallbackTask();
                }
            };
        }

        mExecutor.scheduleAtFixedRate(
                mSeekBarPositionUpdateTask,
                0,
                1000,
                TimeUnit.MILLISECONDS
        );
    }

    // Reports media playback position to mPlaybackProgressCallback.
    private void stopUpdatingCallbackWithPosition() {
        if (mExecutor != null) {
            mExecutor.shutdownNow();
            mExecutor = null;
            mSeekBarPositionUpdateTask = null;
        }
    }

    private void updateProgressCallbackTask() {
        if (isMediaPlayer() && mMediaPlayer.isPlaying()) {
            int currentPosition = mMediaPlayer.getCurrentPosition();
            if (mPlaybackInfoListener != null) {
                mPlaybackInfoListener.onPositionChanged(currentPosition);
            }
        }
    }

    @Override
    public void instantReset() {
        if (isMediaPlayer()) {
            if (mMediaPlayer.getCurrentPosition() < 5000) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    skip(false);
                }
            } else {
                if (activityContext instanceof RadioActivity)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        skip(false);
                    } else resetSong();
            }
        }
    }

    /**
     * Once the {@link MediaPlayer} is released, it can't be used again, and another one has to be
     * object has to be created. That's why this method is private, and called by load(int) and
     * not the constructor.
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void initMediaPlayer(Context context) {
        activityContext = context;
        if (AppConfig.isConnectedToInternet(activityContext)) {
            try {
                if (mMediaPlayer != null) {
                    mMediaPlayer.reset();
                } else {
                    mMediaPlayer = new MediaPlayer();
                    EqualizerUtils.openAudioEffectSession(activityContext, mMediaPlayer.getAudioSessionId());

                    mMediaPlayer.setOnPreparedListener(this);
                    mMediaPlayer.setOnCompletionListener(this);
                    mMediaPlayer.setWakeMode(activityContext, PowerManager.PARTIAL_WAKE_LOCK);

                    mMediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)

                            .build());
                    mMusicNotificationManager = mMusicService.getMusicNotificationManager(activityContext);
                }
                tryToGetAudioFocus();
                if (!mSelectedSong.getAudioUrl().equalsIgnoreCase("")) {
                    mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mMediaPlayer.setDataSource(mSelectedSong.getAudioUrl());


                    mMediaPlayer.prepareAsync();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
//        AppConfig.hideProgDialog();
        } else {
            AppConfig.hideProgDialog();
            Toast.makeText(activityContext, AppConfig.getTextString(activityContext, AppConfig.connection_message_audio), Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public final MediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {

        startUpdatingCallbackWithPosition();
        setStatus(PlaybackInfoListener.State.PLAYING);
    }

    @Override
    public void openEqualizer(@NonNull final Activity activity) {
        EqualizerUtils.openEqualizer(activity, mMediaPlayer);
    }

    @Override
    public void release() {
        if (isMediaPlayer()) {
            EqualizerUtils.closeAudioEffectSession(activityContext, mMediaPlayer.getAudioSessionId());
            mMediaPlayer.release();
            mMediaPlayer = null;
            giveUpAudioFocus();
            unregisterActionsReceiver();
        }
    }

    @Override
    public boolean isPlaying() {
        return isMediaPlayer() && mMediaPlayer.isPlaying();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void resumeOrPause() {
        if (isPlaying()) {
            pauseMediaPlayer();
        } else {
            if (activityContext != null) {
                if (AppConfig.isConnectedToInternet(activityContext)) {
                    resumeMediaPlayer();
                } else
                    Toast.makeText(activityContext, AppConfig.getTextString(activityContext, AppConfig.connection_message_audio), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public final @PlaybackInfoListener.State
    int getState() {
        return mState;
    }

    @Override
    public boolean isMediaPlayer() {
        return mMediaPlayer != null;
    }

    @Override
    public void reset() {
        sReplaySong = !sReplaySong;
    }

    @Override
    public boolean isReset() {
        return sReplaySong;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void skip(final boolean isNext) {
        getSkipSong(isNext);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void getSkipSong(final boolean isNext) {
        final int currentIndex = mSongs.indexOf(mSelectedSong);

        int index;

        try {
            index = isNext ? currentIndex + 1 : currentIndex - 1;
            if (index < mSongs.size()) {
                mSelectedSong = mSongs.get(index);
//                ThirumuraiSongsDetailActivity.position = index;
                if (activityContext instanceof ThirumuraiSongsDetailActivity)
                    ThirumuraiSongsDetailActivity.position = index;
                else if (activityContext instanceof RadioActivity) {
                    RadioAdapter.currentRadioPosition = index;
                    ((RadioActivity) activityContext).updatePlayingNotificationStatus();
                } else if (activityContext instanceof RadioDetailActivity) {
                    RadioDetailActivity.selected_songs = mSelectedSong;
                    ((RadioDetailActivity) activityContext).updatePlayingNotificationStatus(true);
                } else if (activityContext instanceof SixTimePoojaDetailActivity) {
                    SixTimePoojaDetailActivity.selected_songs = mSelectedSong;
                    ((SixTimePoojaDetailActivity) activityContext).updatePlayingNotificationStatus(true);
                }
            } else {
                index = currentIndex != 0 ? 0 : mSongs.size() - 1;
                mSelectedSong = mSongs.get(index);
//                ThirumuraiSongsDetailActivity.position = index;
                if (activityContext instanceof ThirumuraiSongsDetailActivity)
                    ThirumuraiSongsDetailActivity.position = index;
                else if (activityContext instanceof RadioActivity) {
                    RadioAdapter.currentRadioPosition = index;
                    ((RadioActivity) activityContext).updatePlayingNotificationStatus();
                } else if (activityContext instanceof RadioDetailActivity) {
                    RadioDetailActivity.selected_songs = mSelectedSong;
                    ((RadioDetailActivity) activityContext).updatePlayingNotificationStatus(true);
                } else if (activityContext instanceof SixTimePoojaDetailActivity) {
                    SixTimePoojaDetailActivity.selected_songs = mSelectedSong;
                    ((SixTimePoojaDetailActivity) activityContext).updatePlayingNotificationStatus(true);
                }
            }
        } catch (IndexOutOfBoundsException e) {
            index = currentIndex != 0 ? 0 : mSongs.size() - 1;
            mSelectedSong = mSongs.get(index);
//            ThirumuraiSongsDetailActivity.position = index;
            if (activityContext instanceof ThirumuraiSongsDetailActivity)
                ThirumuraiSongsDetailActivity.position = index;
            else if (activityContext instanceof RadioActivity) {
                RadioAdapter.currentRadioPosition = index;
                ((RadioActivity) activityContext).updatePlayingNotificationStatus();
            } else if (activityContext instanceof RadioDetailActivity) {
                RadioDetailActivity.selected_songs = mSelectedSong;
                ((RadioDetailActivity) activityContext).updatePlayingNotificationStatus(true);
            } else if (activityContext instanceof SixTimePoojaDetailActivity) {
                SixTimePoojaDetailActivity.selected_songs = mSelectedSong;
                ((SixTimePoojaDetailActivity) activityContext).updatePlayingNotificationStatus(true);
            }
        }
//        initMediaPlayer();
    }

    @Override
    public void seekTo(final int position) {
        if (isMediaPlayer()) {
            mMediaPlayer.seekTo(position);
        }
    }

    @Override
    public int getPlayerPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    @Override
    public int getDuration() {
        return mMediaPlayer.getDuration();
    }

    /**
     * Reconfigures the player according to audio focus settings and starts/restarts it. This method
     * starts/restarts the MediaPlayer instance respecting the current audio focus state. So if we
     * have focus, it will play normally; if we don't have focus, it will either leave the player
     * paused or set it to a low volume, depending on what is permitted by the current focus
     * settings.
     */
    private void configurePlayerState() {

        if (mCurrentAudioFocusState == AUDIO_NO_FOCUS_NO_DUCK) {
            // We don't have audio focus and can't duck, so we have to pause
            pauseMediaPlayer();
        } else {

            if (mCurrentAudioFocusState == AUDIO_NO_FOCUS_CAN_DUCK) {
                // We're permitted to play, but only if we 'duck', ie: play softly
                mMediaPlayer.setVolume(VOLUME_DUCK, VOLUME_DUCK);
            } else {
                mMediaPlayer.setVolume(VOLUME_NORMAL, VOLUME_NORMAL);
            }

            // If we were playing when we lost focus, we need to resume playing.
            if (mPlayOnFocusGain) {
                resumeMediaPlayer();
                mPlayOnFocusGain = false;
            }
        }
    }

    private class NotificationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            final String action = intent.getAction();

            if (action != null) {

                switch (action) {
                    case MusicNotificationManager.PREV_ACTION:
//                        resetSong();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            skip(false);
                            if (activityContext instanceof ThirumuraiSongsDetailActivity && ThirumuraiSongsDetailActivity.author_spinner != null)
                                ThirumuraiSongsDetailActivity.author_spinner.setSelection(ThirumuraiSongsDetailActivity.position);

                            else if (activityContext instanceof RadioActivity)
                                ((RadioActivity) activityContext).updatePlayingNotificationStatus();
                            else if (activityContext instanceof RadioDetailActivity) {
                                ((RadioDetailActivity) activityContext).updatePlayingNotificationStatus(true);
                            } else if (activityContext instanceof SixTimePoojaDetailActivity) {
                                ((SixTimePoojaDetailActivity) activityContext).updatePlayingNotificationStatus(true);
                            } else
                                stop();
                           /* if (ThirumuraiSongsDetailActivity.author_spinner != null)
                                ThirumuraiSongsDetailActivity.author_spinner.setSelection(ThirumuraiSongsDetailActivity.position);
                            else
                                stop();*/
                        }
                        break;
                    case MusicNotificationManager.PLAY_PAUSE_ACTION:
                        resumeOrPause();
                        if (activityContext instanceof RadioDetailActivity) {
                            ((RadioDetailActivity) activityContext).updatePlayingNotificationStatus(false);
                        } else if (activityContext instanceof SixTimePoojaDetailActivity) {
                            ((SixTimePoojaDetailActivity) activityContext).updatePlayingNotificationStatus(false);
                        }
                        break;
                    case MusicNotificationManager.NEXT_ACTION:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            skip(true);
                            if (activityContext instanceof ThirumuraiSongsDetailActivity && ThirumuraiSongsDetailActivity.author_spinner != null)
                                ThirumuraiSongsDetailActivity.author_spinner.setSelection(ThirumuraiSongsDetailActivity.position);
                            else if (activityContext instanceof RadioActivity)
                                ((RadioActivity) activityContext).updatePlayingNotificationStatus();
                            else if (activityContext instanceof RadioDetailActivity) {
                                ((RadioDetailActivity) activityContext).updatePlayingNotificationStatus(true);
                            } else if (activityContext instanceof SixTimePoojaDetailActivity) {
                                ((SixTimePoojaDetailActivity) activityContext).updatePlayingNotificationStatus(true);
                            } else
                                stop();
                           /* if (ThirumuraiSongsDetailActivity.author_spinner != null)
                                ThirumuraiSongsDetailActivity.author_spinner.setSelection(ThirumuraiSongsDetailActivity.position);
                            else
                                stop();*/
                        }
                        break;

                    case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                        if (mSelectedSong != null) {
                            pauseMediaPlayer();
                        }
                        break;
                    case BluetoothDevice.ACTION_ACL_CONNECTED:
                        if (mSelectedSong != null && !isPlaying()) {
                            resumeMediaPlayer();
                        }
                        break;
                    case Intent.ACTION_HEADSET_PLUG:
                        if (mSelectedSong != null) {
                            switch (intent.getIntExtra("state", -1)) {
                                //0 means disconnected
                                case 0:
                                    pauseMediaPlayer();
                                    break;
                                //1 means connected
                                case 1:
                                    if (!isPlaying()) {
                                        resumeMediaPlayer();
                                    }
                                    break;
                            }
                        }
                        break;
                    case AudioManager.ACTION_AUDIO_BECOMING_NOISY:
                        if (isPlaying()) {
                            pauseMediaPlayer();
                        }
                        break;
                }
            }
        }
    }
}
