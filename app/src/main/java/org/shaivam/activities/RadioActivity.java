package org.shaivam.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.shaivam.R;
import org.shaivam.adapter.RadioAdapter;
import org.shaivam.interfaces.VolleyCallback;
import org.shaivam.loaders.SongProvider;
import org.shaivam.model.CurrentRadio;
import org.shaivam.model.RadioUrl;
import org.shaivam.model.Selected_songs;
import org.shaivam.playback.EqualizerUtils;
import org.shaivam.playback.MusicNotificationManager;
import org.shaivam.playback.PlaybackInfoListener;
import org.shaivam.playback.PlayerAdapter;
import org.shaivam.service.MusicService;
import org.shaivam.utils.AppConfig;
import org.shaivam.utils.LogUtils;
import org.shaivam.utils.MyApplication;
import org.shaivam.utils.MyContextWrapper;
import org.shaivam.utils.PrefManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RadioActivity extends MainActivity implements LoaderManager.LoaderCallbacks<List<Selected_songs>> {
    private Toolbar toolbar;

    public static List<Selected_songs> radioUrls = new ArrayList<Selected_songs>();
    public static PlayerAdapter mPlayerAdapter;
    public static MusicService mMusicService;
    public static PlaybackListener mPlaybackListener;
    public static MusicNotificationManager mMusicNotificationManager;
    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            if (mMusicService == null)
                mMusicService = ((MusicService.LocalBinder) iBinder).getInstance(RadioActivity.this);
            if (mPlayerAdapter == null)
                mPlayerAdapter = mMusicService.getMediaPlayerHolder();
            if (mMusicNotificationManager == null) {
                mMusicNotificationManager = mMusicService.getMusicNotificationManager(RadioActivity.this);
                mMusicNotificationManager.setAccentColor(R.color.colorPrimaryDark);
            }
            if (mPlaybackListener == null) {
                mPlaybackListener = new PlaybackListener();
                mPlayerAdapter.setPlaybackInfoListener(mPlaybackListener);
            }
            getSupportLoaderManager().initLoader(SongProvider.ARTISTS_LOADER, null, RadioActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mMusicService = null;
        }
    };
    private boolean mIsBound;
    List<CurrentRadio> currentRadioArrayList = new ArrayList<CurrentRadio>();
    List<RadioUrl> radioUrlArrayList = new ArrayList<RadioUrl>();

    private static RadioAdapter adapter;
    public RecyclerView recycle_radio;
    CoordinatorLayout radio_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio);
        ButterKnife.bind(this);
        LogUtils.amplitudeLog(this, "Radio Screen");
        toolbar = findViewById(R.id.toolbar);
        TextView textCustomTitle = (TextView) findViewById(R.id.custom_title);
        Typeface customFont = Typeface.createFromAsset(this.getAssets(), AppConfig.FONT_KAVIVANAR);
        textCustomTitle.setTypeface(customFont);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        radio_main = findViewById(R.id.radio_main);
        if (AppConfig.isConnectedToInternet(MyApplication.getInstance())) {
            AppConfig.showProgDialiog(this);
            MyApplication.gsonVolley.Request(MyApplication.getInstance(), AppConfig.URL_BASE, AppConfig.URL_RADIO_URL,
                    MyApplication.jsonHelper.createCurrentRadioJson(),
                    radiourlCallBack, com.android.volley.Request.Method.POST);
        } else {
            snackBar(AppConfig.getTextString(this, AppConfig.connection_message));
        }

        doBindService();

        recycle_radio = findViewById(R.id.recycle_radio);
        adapter = new RadioAdapter(this, radioUrls);
        recycle_radio.setLayoutManager(new LinearLayoutManager(this));
        recycle_radio.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    VolleyCallback radiourlCallBack = new VolleyCallback() {
        @Override
        public void Success(int stauscode, String response) {
            try {
                JSONObject jObj = new JSONObject(response);
                if (jObj.has("Status") && jObj.getString("Status").equalsIgnoreCase("true")) {
                    radioUrlArrayList.clear();
                    List<RadioUrl> data = Arrays.asList(MyApplication.gson.fromJson(jObj.getJSONArray("data").toString(),
                            RadioUrl[].class));
                    radioUrlArrayList.addAll(data);
                    radioUrls.clear();

                    for (int j = 0; j < radioUrlArrayList.size(); j++) {
                        radioUrls.add(new Selected_songs(radioUrlArrayList.get(j).getRadiourl(), radioUrlArrayList.get(j).getRadioname(),
                                "", ""));
                    }
                    adapter.notifyDataSetChanged();


                    for (int j = 0; j < radioUrls.size(); j++) {
                        if (AppConfig.isConnectedToInternet(MyApplication.getInstance())) {
                            AppConfig.showProgDialiog(RadioActivity.this);
                            MyApplication.gsonVolley.Request(MyApplication.getInstance(), AppConfig.URL_BASE, AppConfig.URL_RADIO_CURRENT_NEW,
                                    MyApplication.jsonHelper.createCurrentRadioNewJson(radioUrls.get(j).getAuthor_name()),
                                    radioCurrentSuccessCallBack, com.android.volley.Request.Method.POST);
                        } else {
                            snackBar(AppConfig.getTextString(RadioActivity.this, AppConfig.connection_message));
                        }
                    }

                } else {
                    radioUrls.add(new Selected_songs(AppConfig.Url_Radio_Shaivalahari, AppConfig.Radio_Shaivalahari,
                            "", ""));
                    radioUrls.add(new Selected_songs(AppConfig.Url_Radio_Thiruneriya_Thamizhosai, AppConfig.Radio_Thiruneriya_Thamizhosai,
                            "", ""));
                    adapter.notifyDataSetChanged();


                    for (int j = 0; j < radioUrls.size(); j++) {
                        if (AppConfig.isConnectedToInternet(MyApplication.getInstance())) {
                            AppConfig.showProgDialiog(RadioActivity.this);
                            MyApplication.gsonVolley.Request(MyApplication.getInstance(), AppConfig.URL_BASE, AppConfig.URL_RADIO_CURRENT_NEW,
                                    MyApplication.jsonHelper.createCurrentRadioNewJson(radioUrls.get(j).getAuthor_name()),
                                    radioCurrentSuccessCallBack, com.android.volley.Request.Method.POST);
                        } else {
                            snackBar(AppConfig.getTextString(RadioActivity.this, AppConfig.connection_message));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                snackBar(AppConfig.getTextString(RadioActivity.this, AppConfig.went_wrong));
            }
        }

        @Override
        public void Failure(int stauscode, String errorResponse) {
            snackBar(errorResponse);
        }
    };

    VolleyCallback radioCurrentSuccessCallBack = new VolleyCallback() {
        @Override
        public void Success(int stauscode, String response) {
            try {
                JSONObject jObj = new JSONObject(response);
                if (jObj.has("Status") && jObj.getString("Status").equalsIgnoreCase("true")) {
                    currentRadioArrayList.clear();
                    List<CurrentRadio> data = Arrays.asList(MyApplication.gson.fromJson(jObj.getJSONArray("data").toString(), CurrentRadio[].class));
                    currentRadioArrayList.addAll(data);
                    for (int i = 0; i < radioUrls.size(); i++) {
                        if (currentRadioArrayList.size() > 0) {
                            if (currentRadioArrayList.get(0).getRadioTitles().equalsIgnoreCase(radioUrls.get(i).getAuthor_name())) {
                                radioUrls.get(i).setTitle(currentRadioArrayList.get(0).getProgram());
                                radioUrls.get(i).setThalam(currentRadioArrayList.get(0).getLanguage());
                            }
                        }

                    }
                    adapter.notifyDataSetChanged();

                } else {
                    snackBar(jObj.getString("Message"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                snackBar(AppConfig.getTextString(RadioActivity.this, AppConfig.went_wrong));
            }
        }

        @Override
        public void Failure(int stauscode, String errorResponse) {
            snackBar(errorResponse);
        }
    };

    @Override
    public void onBackPressed() {
        ActivityManager mngr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);

        if (isTaskRoot()) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            //// There are more activities in stack
        }

        super.onBackPressed();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPlayerAdapter != null && mPlayerAdapter.isMediaPlayer()) {
            mPlayerAdapter.onPauseActivity();
        }
    }

    public void updatePlayingNotificationStatus() {
        AppConfig.showProgDialiog(this);
        mPlayerAdapter.setCurrentSong(radioUrls.get(RadioAdapter.currentRadioPosition), radioUrls);
        mPlayerAdapter.initMediaPlayer(RadioActivity.this);
        adapter.drawable = mPlayerAdapter.getState() != PlaybackInfoListener.State.PAUSED ? R.drawable.ic_shaivam_fm_stop : R.drawable.ic_shaivam_fm_play;
        adapter.notifyDataSetChanged();
    }

    public boolean checkIsPlayer() {

        boolean isPlayer = mPlayerAdapter.isMediaPlayer();
        if (!isPlayer) {
            EqualizerUtils.notifyNoSessionId(this);
        }
        return isPlayer;
    }

    private void updatePlayingInfo(boolean restore, boolean startPlay) {

        if (startPlay) {
            mPlayerAdapter.getMediaPlayer().start();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mMusicService.startForeground(MusicNotificationManager.NOTIFICATION_ID,
                                mMusicNotificationManager.createNotification(RadioActivity.this));
                    }
                }
            }, 250);
        }

        if (restore) {
            adapter.updatePlayingStatus();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //stop foreground if coming from pause state
                    if (mMusicService.isRestoredFromPause()) {
                        mMusicService.stopForeground(false);
                        mMusicService.getMusicNotificationManager(RadioActivity.this)
                                .getNotificationManager().notify(MusicNotificationManager.NOTIFICATION_ID,
                                mMusicService.getMusicNotificationManager(RadioActivity.this).getNotificationBuilder().build());
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
        return new SongProvider.AsyncSongLoaderString(this, radioUrls);
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

    @Override
    protected void onResume() {
        super.onResume();
        AppConfig.customeLanguage(this);
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    public class PlaybackListener extends PlaybackInfoListener {

        @Override
        public void onPositionChanged(int position) {

        }

        @Override
        public void onStateChanged(@State int state) {

            adapter.updatePlayingStatus();
            if (mPlayerAdapter.getState() != State.RESUMED && mPlayerAdapter.getState() != State.PAUSED) {
                updatePlayingInfo(false, true);
            }
        }

        @Override
        public void onPlaybackCompleted() {
        }
    }
}
