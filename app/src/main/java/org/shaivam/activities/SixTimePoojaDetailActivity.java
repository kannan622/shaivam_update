package org.shaivam.activities;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.shaivam.R;
import org.shaivam.loaders.SongProvider;
import org.shaivam.model.Selected_songs;
import org.shaivam.playback.EqualizerUtils;
import org.shaivam.playback.MusicNotificationManager;
import org.shaivam.playback.PlaybackInfoListener;
import org.shaivam.playback.PlayerAdapter;
import org.shaivam.service.MusicService;
import org.shaivam.utils.AppConfig;
import org.shaivam.utils.MyApplication;
import org.shaivam.utils.MyContextWrapper;
import org.shaivam.utils.PrefManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static org.shaivam.activities.RadioActivity.mMusicNotificationManager;
import static org.shaivam.activities.RadioActivity.mMusicService;
import static org.shaivam.activities.RadioActivity.mPlayerAdapter;

public class SixTimePoojaDetailActivity extends MainActivity implements LoaderManager.LoaderCallbacks<List<Selected_songs>> {
    private Toolbar toolbar;
    private CoordinatorLayout snooze_main;
    private ImageView play_pause;
    private TextView six_pooja_text, pooja_text_hindi, pooja_text_tamil, pooja_text_hindi_last, pooja_text_tamil_last;
    LinearLayout pooja_text_hindi_linear_image, pooja_text_hindi_linear;
    private static PlayerAdapter mPlayerAdapter;
    private static MusicService mMusicService;
    private static PlaybackListener mPlaybackListener;
    private static MusicNotificationManager mMusicNotificationManager;


    public static List<Selected_songs> selected_songsList = new ArrayList<>();
    public static Selected_songs selected_songs = new Selected_songs();

    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            if (mMusicService == null)
                mMusicService = ((MusicService.LocalBinder) iBinder).getInstance(SixTimePoojaDetailActivity.this);
            if (mPlayerAdapter == null)
                mPlayerAdapter = mMusicService.getMediaPlayerHolder();
            if (mMusicNotificationManager == null) {
                mMusicNotificationManager = mMusicService.getMusicNotificationManager(SixTimePoojaDetailActivity.this);
                mMusicNotificationManager.setAccentColor(R.color.colorPrimaryDark);
            }
            if (mPlaybackListener == null) {
                mPlaybackListener = new PlaybackListener();
                mPlayerAdapter.setPlaybackInfoListener(mPlaybackListener);
            }
            getSupportLoaderManager().initLoader(SongProvider.ARTISTS_LOADER, null, SixTimePoojaDetailActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mMusicService = null;
        }
    };
    private boolean mIsBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_six_time_pooja_detail);
        toolbar = findViewById(R.id.toolbar);
        TextView textCustomTitle = (TextView) findViewById(R.id.custom_title);
        Typeface customFont = Typeface.createFromAsset(this.getAssets(), AppConfig.FONT_KAVIVANAR);
        textCustomTitle.setTypeface(customFont);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        snooze_main = findViewById(R.id.snooze_main);
        play_pause = findViewById(R.id.play_pause);
        six_pooja_text = findViewById(R.id.six_pooja_text);

        pooja_text_hindi = findViewById(R.id.pooja_text_hindi);
        pooja_text_tamil = findViewById(R.id.pooja_text_tamil);
        pooja_text_hindi_last = findViewById(R.id.pooja_text_hindi_last);
        pooja_text_tamil_last = findViewById(R.id.pooja_text_tamil_last);

        pooja_text_hindi_linear_image = findViewById(R.id.pooja_text_hindi_linear_image);
        pooja_text_hindi_linear = findViewById(R.id.pooja_text_hindi_linear);
        play_pause.setOnClickListener(pausePlay);

        String url = AppConfig.Six_Pooja_6_Am_Url;
        String pooja_text = getResources().getString(R.string.ushat);
        String pooja_text_hindi = getResources().getString(R.string.ushat_hindi);
        String pooja_text_tamil = getResources().getString(R.string.ushat_tamil);
        String pooja_text_hindi_last = getResources().getString(R.string.ushat_hindi_last);
        String pooja_text_tamil_last = getResources().getString(R.string.ushat_tamil_last);
        int pooja_timing = AppConfig.Six_Pooja_6_Am_hours;

        Intent intent = getIntent();
        if (getIntent() != null) {
            String requestcode = intent.getStringExtra("requestcode");
            if (requestcode.equalsIgnoreCase(String.valueOf(AppConfig.Six_Pooja_6_Am_hours))) {
                url = AppConfig.Six_Pooja_6_Am_Url;
                pooja_text = getResources().getString(R.string.ushat);
                pooja_text_hindi = getResources().getString(R.string.ushat_hindi);
                pooja_text_tamil = getResources().getString(R.string.ushat_tamil);
                pooja_text_hindi_last = getResources().getString(R.string.ushat_hindi_last);
                pooja_text_tamil_last = getResources().getString(R.string.ushat_tamil_last);
                pooja_timing = AppConfig.Six_Pooja_6_Am_hours;
            } else if (requestcode.equalsIgnoreCase(String.valueOf(AppConfig.Six_Pooja_8_Am_hours))) {
                url = AppConfig.Six_Pooja_8_Am_Url;
                pooja_text = getResources().getString(R.string.kaala);
                pooja_text_hindi = getResources().getString(R.string.kaala_hindi);
                pooja_text_tamil = getResources().getString(R.string.kaala_tamil);
                pooja_text_hindi_last = getResources().getString(R.string.kaala_hindi_last);
                pooja_text_tamil_last = getResources().getString(R.string.kaala_tamil_last);
                pooja_timing = AppConfig.Six_Pooja_8_Am_hours;
            } else if (requestcode.equalsIgnoreCase(String.valueOf(AppConfig.Six_Pooja_12_Pm_hours))) {
                url = AppConfig.Six_Pooja_12_Pm_Url;
                pooja_text = getResources().getString(R.string.uchi);
                pooja_text_hindi = getResources().getString(R.string.uchi_hindi);
                pooja_text_tamil = getResources().getString(R.string.uchi_tamil);
                pooja_text_hindi_last = getResources().getString(R.string.uchi_hindi_last);
                pooja_text_tamil_last = getResources().getString(R.string.uchi_tamil_last);
                pooja_timing = AppConfig.Six_Pooja_12_Pm_hours;
            } else if (requestcode.equalsIgnoreCase(String.valueOf(AppConfig.Six_Pooja_6_Pm_hours))) {
                url = AppConfig.Six_Pooja_6_Pm_Url;
                pooja_text = getResources().getString(R.string.saya);
                pooja_text_hindi = "";
                pooja_text_tamil = getResources().getString(R.string.saya_tamil);
                pooja_text_hindi_last = "";
                pooja_text_tamil_last = getResources().getString(R.string.saya_tamil_last);
                pooja_timing = AppConfig.Six_Pooja_6_Pm_hours;
            } else if (requestcode.equalsIgnoreCase(String.valueOf(AppConfig.Six_Pooja_8_Pm_hours))) {
                url = AppConfig.Six_Pooja_8_Pm_Url;
                pooja_text = getResources().getString(R.string.iranda);
                pooja_text_hindi = getResources().getString(R.string.iranda_hindi);
                pooja_text_tamil = getResources().getString(R.string.iranda_tamil);
                pooja_text_hindi_last = getResources().getString(R.string.iranda_hindi_last);
                pooja_text_tamil_last = getResources().getString(R.string.iranda_tamil_last);
                pooja_timing = AppConfig.Six_Pooja_8_Pm_hours;
            } else if (requestcode.equalsIgnoreCase(String.valueOf(AppConfig.Six_Pooja_9_Pm_hours))) {
                url = AppConfig.Six_Pooja_9_Pm_Url;
                pooja_text = getResources().getString(R.string.ardha);
                pooja_text_hindi = getResources().getString(R.string.ardha_hindi);
                pooja_text_tamil = getResources().getString(R.string.ardha_tamil);
                pooja_text_hindi_last = getResources().getString(R.string.ardha_hindi_last);
                pooja_text_tamil_last = getResources().getString(R.string.ardha_tamil_last);
                pooja_timing = AppConfig.Six_Pooja_9_Pm_hours;
            }

            six_pooja_text.setText(pooja_text);
            if (pooja_text_hindi.equalsIgnoreCase("")) {
                pooja_text_hindi_linear_image.setVisibility(View.VISIBLE);
                pooja_text_hindi_linear.setVisibility(View.GONE);
            } else {
                pooja_text_hindi_linear_image.setVisibility(View.GONE);
                pooja_text_hindi_linear.setVisibility(View.VISIBLE);
                this.pooja_text_hindi.setText(pooja_text_hindi);
                this.pooja_text_hindi_last.setText(pooja_text_hindi_last);
            }
            this.pooja_text_tamil.setText(pooja_text_tamil);
            this.pooja_text_tamil_last.setText(pooja_text_tamil_last);

            selected_songsList.add(new Selected_songs(url, pooja_text,
                    String.valueOf(pooja_timing), ""));
            selected_songs = selected_songsList.get(0);
        }


        doBindService();


        if (mPlayerAdapter != null && mPlayerAdapter.getCurrentSong() != null
                && mPlayerAdapter.getCurrentSong().getTitle().equalsIgnoreCase(selected_songs.getTitle())
                && mPlayerAdapter.isPlaying()) {
            play_pause.setBackgroundResource(R.drawable.ic_shaivam_fm_stop);
        } else {
            play_pause.setBackgroundResource(R.drawable.ic_shaivam_fm_play);
        }


    }

    private View.OnClickListener pausePlay = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            // TODO Auto-generated method stub
            if (mPlayerAdapter != null && mPlayerAdapter.getCurrentSong() != null
                    && mPlayerAdapter.getCurrentSong().getTitle().equalsIgnoreCase(selected_songs.getTitle())
                    && mPlayerAdapter.isPlaying()) {
                mPlayerAdapter.resumeOrPause();
                play_pause.setBackgroundResource(R.drawable.ic_shaivam_fm_play);
            } else {
                AppConfig.showProgDialiog(SixTimePoojaDetailActivity.this);
                mPlayerAdapter.setCurrentSong(selected_songs, selected_songsList);
                mPlayerAdapter.initMediaPlayer(SixTimePoojaDetailActivity.this);
                play_pause.setBackgroundResource(R.drawable.ic_shaivam_fm_stop);

            }
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        if (mPlayerAdapter != null && mPlayerAdapter.isPlaying()) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
       /* } else {

        }*/
    }

    public void updatePlayingNotificationStatus(Boolean status) {

        if (mPlayerAdapter != null && mPlayerAdapter.isPlaying()) {
            play_pause.setBackgroundResource(R.drawable.ic_shaivam_fm_stop);

        } else {
            play_pause.setBackgroundResource(R.drawable.ic_shaivam_fm_play);
        }
        if (status) {
            AppConfig.showProgDialiog(SixTimePoojaDetailActivity.this);
            mPlayerAdapter.setCurrentSong(selected_songs, selected_songsList);
            mPlayerAdapter.initMediaPlayer(SixTimePoojaDetailActivity.this);
        }
    }

    public boolean checkIsPlayer() {

        boolean isPlayer = mPlayerAdapter.isMediaPlayer();
        if (!isPlayer) {
            EqualizerUtils.notifyNoSessionId(this);
        }
        return isPlayer;
    }

    private void updatePlayingStatus() {
//        adapter.updatePlayingStatus();
    }

    private void updatePlayingInfo(boolean restore, boolean startPlay) {

        if (startPlay) {
            mPlayerAdapter.getMediaPlayer().start();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mMusicService.startForeground(MusicNotificationManager.NOTIFICATION_ID,
                                mMusicNotificationManager.createNotification(SixTimePoojaDetailActivity.this));
                    }
                }
            }, 250);
        }

        if (restore) {
            updatePlayingStatus();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //stop foreground if coming from pause state
                    if (mMusicService.isRestoredFromPause()) {
                        mMusicService.stopForeground(false);
                        mMusicService.getMusicNotificationManager(SixTimePoojaDetailActivity.this)
                                .getNotificationManager().notify(MusicNotificationManager.NOTIFICATION_ID,
                                mMusicService.getMusicNotificationManager(SixTimePoojaDetailActivity.this).getNotificationBuilder().build());
                        mMusicService.setRestoredFromPause(false);
                    }
                }
            }, 250);
        }
    }

    private void restorePlayerStatus() {
        //if we are playing and the activity was restarted
        //update the controls panel
        if (mPlayerAdapter != null && mPlayerAdapter.isMediaPlayer()) {

            mPlayerAdapter.onResumeActivity();
            updatePlayingInfo(true, false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppConfig.customeLanguage(this);
    }

    private void doBindService() {
        // Establish a connection with the service.  We use an explicit
        // class name because we want a specific service implementation that
        // we know will be running in our own process (and thus won't be
        // supporting component replacement by other applications).
        bindService(new Intent(this,
                MusicService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;

        final Intent startNotStickyIntent = new Intent(this, MusicService.class);
        startService(startNotStickyIntent);
    }

    private void doUnbindService() {
        if (mIsBound) {
            // Detach our existing connection.
            unbindService(mConnection);
            mIsBound = false;
        }
    }

    @Override
    @NonNull
    public Loader<List<Selected_songs>> onCreateLoader(int id, Bundle args) {
        return new SongProvider.AsyncSongLoaderString(this, selected_songsList);
    }

    @Override
    public void onLoadFinished
            (@NonNull Loader<List<Selected_songs>> loader, List<Selected_songs> songs) {
        if (!isFinishing()) {
            if (songs.isEmpty()) {
                if (isFinishing()) {
                    return;
                }
                AppConfig.hideProgDialog();
            }
        }
    }


    @Override
    public void onLoaderReset(@NonNull Loader loader) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlaybackListener = null;
        doUnbindService();
    }


    class PlaybackListener extends PlaybackInfoListener {

        @Override
        public void onPositionChanged(int position) {

        }

        @Override
        public void onStateChanged(@State int state) {

            updatePlayingStatus();
            if (mPlayerAdapter.getState() != State.RESUMED && mPlayerAdapter.getState() != State.PAUSED) {
                updatePlayingInfo(false, true);
            }
        }

        @Override
        public void onPlaybackCompleted() {
        }
    }
}