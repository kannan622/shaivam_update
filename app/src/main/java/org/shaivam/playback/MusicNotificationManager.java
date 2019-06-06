package org.shaivam.playback;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.app.NotificationCompat.MediaStyle;
import android.text.Spanned;

import org.shaivam.R;
import org.shaivam.activities.HomeActivity;
import org.shaivam.activities.RadioActivity;
import org.shaivam.activities.RadioDetailActivity;
import org.shaivam.activities.SixTimePoojaDetailActivity;
import org.shaivam.activities.ThirumuraiSongsDetailActivity;
import org.shaivam.model.Selected_songs;
import org.shaivam.service.MusicService;
import org.shaivam.utils.AppConfig;


public class MusicNotificationManager {

    public static final int NOTIFICATION_ID = 101;
    static final String PLAY_PAUSE_ACTION = "com.iven.musicplayergo.PLAYPAUSE";
    static final String NEXT_ACTION = "com.iven.musicplayergo.NEXT";
    static final String PREV_ACTION = "com.iven.musicplayergo.PREV";
    private final String CHANNEL_ID = "com.iven.musicplayergo.CHANNEL_ID";
    private final int REQUEST_CODE = 100;
    private final NotificationManager mNotificationManager;
    private final MusicService mMusicService;
    private NotificationCompat.Builder mNotificationBuilder;
    private int mAccent;

    public MusicNotificationManager(@NonNull final MusicService musicService) {
        mMusicService = musicService;
        mNotificationManager = (NotificationManager) mMusicService.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void setAccentColor(final int color) {
        mAccent = ContextCompat.getColor(mMusicService, color);
    }

    public final NotificationManager getNotificationManager() {
        return mNotificationManager;
    }

    public final NotificationCompat.Builder getNotificationBuilder() {
        return mNotificationBuilder;
    }

    private PendingIntent playerAction(String action) {
        final Selected_songs song = mMusicService.getMediaPlayerHolder().getCurrentSong();

        final Intent pauseIntent = new Intent();
        pauseIntent.setAction(action);
        return PendingIntent.getBroadcast(mMusicService, REQUEST_CODE, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Notification createNotification(Context context) {

        final Selected_songs song = mMusicService.getMediaPlayerHolder().getCurrentSong();

        mNotificationBuilder = new NotificationCompat.Builder(mMusicService, CHANNEL_ID);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }
        Intent openPlayerIntent;
      /*  openPlayerIntent = new Intent(mMusicService, ThirumuraiSongsDetailActivity.class);
        openPlayerIntent.putExtra("title", song.getTitle());
    */
        if (context instanceof ThirumuraiSongsDetailActivity) {
            openPlayerIntent = new Intent(mMusicService, ThirumuraiSongsDetailActivity.class);
            openPlayerIntent.putExtra("title", song.getTitle());
        } else if (context instanceof RadioActivity) {
            openPlayerIntent = new Intent(mMusicService, RadioActivity.class);
        } else if (context instanceof RadioDetailActivity) {
            openPlayerIntent = new Intent(mMusicService, RadioActivity.class);
        } else if (context instanceof SixTimePoojaDetailActivity) {
            openPlayerIntent = new Intent(mMusicService, SixTimePoojaDetailActivity.class);
            openPlayerIntent.putExtra("requestcode", song.getTitle());

        }else
            openPlayerIntent = new Intent(mMusicService, HomeActivity.class);

        openPlayerIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final PendingIntent contentIntent = PendingIntent.getActivity(mMusicService, REQUEST_CODE,
                openPlayerIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        final String artist = song.getAuthor_name();
        final String songTitle = song.getTitle();

        final Spanned spanned = AppConfig.buildSpanned(mMusicService.getString(R.string.playing_song, songTitle, artist));

        mNotificationBuilder
                .setShowWhen(false)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(getLargeIcon())
                .setColor(mAccent)
                .setContentTitle(spanned)
                .setContentText(song.getThalam())
                .setContentIntent(contentIntent);
        mNotificationBuilder.addAction(notificationAction(PREV_ACTION))
                .addAction(notificationAction(PLAY_PAUSE_ACTION))
                .addAction(notificationAction(NEXT_ACTION));

     /*   if (context instanceof ThirumuraiSongsDetailActivity)
            mNotificationBuilder.addAction(notificationAction(PREV_ACTION))
                    .addAction(notificationAction(PLAY_PAUSE_ACTION))
                    .addAction(notificationAction(NEXT_ACTION));
        else if (context instanceof RadioActivity)
            mNotificationBuilder.addAction(notificationAction(PLAY_PAUSE_ACTION));

      */  mNotificationBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

                mNotificationBuilder.setStyle(new MediaStyle().setShowActionsInCompactView(0, 1, 2));

      /*  if (context instanceof ThirumuraiSongsDetailActivity)
            mNotificationBuilder.setStyle(new MediaStyle().setShowActionsInCompactView(0, 1, 2));
        else if (context instanceof RadioActivity)
            mNotificationBuilder.setStyle(new MediaStyle().setShowActionsInCompactView(0));

    */
      AppConfig.hideProgDialog();
      return mNotificationBuilder.build();
    }

    @NonNull
    private NotificationCompat.Action notificationAction(final String action) {

        int icon;

        switch (action) {
            default:
            case PREV_ACTION:
                icon = R.drawable.ic_previous_yellow;
                break;
            case PLAY_PAUSE_ACTION:

                icon = mMusicService.getMediaPlayerHolder().getState() != PlaybackInfoListener.State.PAUSED ? R.drawable.ic_pause : R.drawable.ic_play;
                break;
            case NEXT_ACTION:
                icon = R.drawable.ic_next_yellow;
                break;
        }
        return new NotificationCompat.Action.Builder(icon, action, playerAction(action)).build();
    }

    @RequiresApi(26)
    private void createNotificationChannel() {

        if (mNotificationManager.getNotificationChannel(CHANNEL_ID) == null) {
            final NotificationChannel notificationChannel =
                    new NotificationChannel(CHANNEL_ID,
                            mMusicService.getString(R.string.app_name),
                            NotificationManager.IMPORTANCE_LOW);

            notificationChannel.setDescription(
                    mMusicService.getString(R.string.app_name));

            notificationChannel.enableLights(false);
            notificationChannel.enableVibration(false);
            notificationChannel.setShowBadge(false);

            mNotificationManager.createNotificationChannel(notificationChannel);
        }
    }

    //https://gist.github.com/Gnzlt/6ddc846ef68c587d559f1e1fcd0900d3
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private Bitmap getLargeIcon() {

        final BitmapDrawable bitmapDrawable = (BitmapDrawable) mMusicService.getDrawable(R.drawable.shaivam_logo);

        final int largeIconSize = mMusicService.getResources().getDimensionPixelSize(R.dimen._256sdp);
        final Bitmap bitmap = Bitmap.createBitmap(largeIconSize, largeIconSize, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);

        if (bitmapDrawable != null) {
            bitmapDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
         /*   bitmapDrawable.setTint(mAccent);
            bitmapDrawable.setAlpha(100);
          */
            bitmapDrawable.draw(canvas);
        }

        return bitmap;
    }
}
