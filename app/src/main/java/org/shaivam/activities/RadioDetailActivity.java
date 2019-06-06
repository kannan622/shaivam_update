package org.shaivam.activities;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.shaivam.R;
import org.shaivam.adapter.RadioDetailAdapter;
import org.shaivam.fragments.RadioTanricFragment;
import org.shaivam.interfaces.VolleyCallback;
import org.shaivam.loaders.SongProvider;
import org.shaivam.model.Radio;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static org.shaivam.activities.RadioActivity.mMusicNotificationManager;
import static org.shaivam.activities.RadioActivity.mMusicService;
import static org.shaivam.activities.RadioActivity.mPlayerAdapter;

public class RadioDetailActivity extends MainActivity implements LoaderManager.LoaderCallbacks<List<Selected_songs>> {
    public static TabLayout tabLayout;
    ViewPager viewPager;
    private Toolbar toolbar;
    public static PlaybackListener mPlaybackListener;
    public static ViewPagerAdapter adapter;

    public static List<String> dateStrs = new ArrayList<>();
    public static List<String> dateStrsTitle = new ArrayList<>();

    public static List<List<Radio>> radios = new ArrayList<List<Radio>>();

    /*
    private static PlayerAdapter mPlayerAdapter;
    private static MusicService mMusicService;
    private static PlaybackListener mPlaybackListener;
    private static MusicNotificationManager mMusicNotificationManager;*/
    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            if (mMusicService == null)
                mMusicService = ((MusicService.LocalBinder) iBinder).getInstance(RadioDetailActivity.this);
            if (mPlayerAdapter == null)
                mPlayerAdapter = mMusicService.getMediaPlayerHolder();
            if (mMusicNotificationManager == null) {
                mMusicNotificationManager = mMusicService.getMusicNotificationManager(RadioDetailActivity.this);
                mMusicNotificationManager.setAccentColor(R.color.colorPrimaryDark);
            }
            if (mPlaybackListener == null) {
                mPlaybackListener = new PlaybackListener();
                mPlayerAdapter.setPlaybackInfoListener(mPlaybackListener);
            }
            getSupportLoaderManager().initLoader(SongProvider.ARTISTS_LOADER, null, RadioDetailActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mMusicService = null;
        }
    };
    private boolean mIsBound;

    CoordinatorLayout radio_detail_main;
    GifImageView animation_image;
    ImageView play_pause_click;
    TextView radio_header_text, radio_title;
    CircleImageView previouse_click, next_click;

    public static List<Selected_songs> selected_songsList = new ArrayList<>();
    public static Selected_songs selected_songs = new Selected_songs();
    GifDrawable gifDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio_detail);
        ButterKnife.bind(this);
        toolbar = findViewById(R.id.toolbar);
        TextView textCustomTitle = (TextView) findViewById(R.id.custom_title);
        Typeface customFont = Typeface.createFromAsset(this.getAssets(), AppConfig.FONT_KAVIVANAR);
        textCustomTitle.setTypeface(customFont);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        radio_detail_main = findViewById(R.id.radio_detail_main);

        doBindService();


        viewPager = (ViewPager) findViewById(R.id.view_pager);
        setupViewPager(viewPager);
//        viewPager.setOffscreenPageLimit(0);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(onTabSelectedListener(viewPager));

//        setupTabIcons();

        animation_image = findViewById(R.id.animation_image);
        radio_header_text = findViewById(R.id.radio_header_text);
        play_pause_click = findViewById(R.id.play_pause_click);
        previouse_click = findViewById(R.id.previouse_click);
        next_click = findViewById(R.id.next_click);
        radio_title = findViewById(R.id.radio_title);

        if (selected_songs.getAuthor_name() != null && selected_songs.getAuthor_name().equalsIgnoreCase("radio-thiruneriya-thamizhosai"))
            radio_header_text.setText(AppConfig.Radio_Thiruneriya_Thamizhosai);
        else
            radio_header_text.setText(AppConfig.Radio_Shaivalahari);

        radio_title.setText(selected_songs.getTitle());
        LogUtils.amplitudeLog(this, selected_songs.getAuthor_name());

//        animation_image.setFreezesAnimation(true);
        animation_image.setImageResource(R.drawable.radio_detail_animation);
        gifDrawable = (GifDrawable) animation_image.getDrawable();
        gifDrawable.setLoopCount(1000);
        gifDrawable.seekToFrameAndGet(5);
        gifDrawable.stop();

        if (mPlayerAdapter != null && mPlayerAdapter.getCurrentSong() != null
                && (mPlayerAdapter.getCurrentSong().getTitle() != null && mPlayerAdapter.getCurrentSong().getTitle().equalsIgnoreCase(selected_songs.getTitle()))
                && mPlayerAdapter.isPlaying()) {
            play_pause_click.setImageResource(R.drawable.ic_stop_yellow);
            gifDrawable.start();
        } else {
            play_pause_click.setImageResource(R.drawable.ic_play);
            animation_image.setFreezesAnimation(true);
            gifDrawable.stop();
        }

        play_pause_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mPlayerAdapter != null && mPlayerAdapter.getCurrentSong() != null
                        && (mPlayerAdapter.getCurrentSong().getTitle() != null && mPlayerAdapter.getCurrentSong().getTitle().equalsIgnoreCase(selected_songs.getTitle()))
                        && mPlayerAdapter.isPlaying()) {
                    mPlayerAdapter.resumeOrPause();
                    play_pause_click.setImageResource(R.drawable.ic_play);
                    animation_image.setFreezesAnimation(true);
                    gifDrawable.stop();
                } else {
                    AppConfig.showProgDialiog(RadioDetailActivity.this);
                    mPlayerAdapter.setCurrentSong(selected_songs, selected_songsList);
                    mPlayerAdapter.initMediaPlayer(RadioDetailActivity.this);
                    play_pause_click.setImageResource(R.drawable.ic_stop_yellow);
                    gifDrawable.start();
                }
            }
        });

        previouse_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkIsPlayer()) {
//                    mPlayerAdapter.resetSong();
                    mPlayerAdapter.skip(false);
                }
            }
        });
        next_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkIsPlayer()) {
                    mPlayerAdapter.skip(true);
                }
            }
        });
    }

    private TabLayout.OnTabSelectedListener onTabSelectedListener(final ViewPager viewPager) {

        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };
    }

    private void setupViewPager(ViewPager viewPager) {


        adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        adapter.addFrag(new RadioTanricFragment(), sdf.format(new Date()));
        dateStrs.clear();
        radios.clear();

        for (int i = 0; i < 5; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, i);
            Date to_date = calendar.getTime();

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");

            String dateOutput = format.format(to_date);
            String dateOutput1 = format1.format(to_date);

            dateStrs.add(dateOutput);
            dateStrsTitle.add(dateOutput1);
            radios.add(new ArrayList<Radio>());
        }

        RadioTanricFragment radioTanricFragment1 = new RadioTanricFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", dateStrs.get(0));
        bundle.putInt("position", 0);
        radioTanricFragment1.setArguments(bundle);
        adapter.addFrag(radioTanricFragment1, dateStrsTitle.get(0));

        RadioTanricFragment radioTanricFragment2 = new RadioTanricFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putString("title", dateStrs.get(1));
        bundle.putInt("position", 1);
        radioTanricFragment2.setArguments(bundle2);
        adapter.addFrag(radioTanricFragment2, dateStrsTitle.get(1));

        RadioTanricFragment radioTanricFragment3 = new RadioTanricFragment();
        Bundle bundle3 = new Bundle();
        bundle3.putString("title", dateStrs.get(2));
        bundle.putInt("position", 2);
        radioTanricFragment3.setArguments(bundle3);
        adapter.addFrag(radioTanricFragment3, dateStrsTitle.get(2));

        RadioTanricFragment radioTanricFragment4 = new RadioTanricFragment();
        Bundle bundle4 = new Bundle();
        bundle4.putString("title", dateStrs.get(3));
        bundle.putInt("position", 3);
        radioTanricFragment4.setArguments(bundle4);
        adapter.addFrag(radioTanricFragment4, dateStrsTitle.get(3));

        RadioTanricFragment radioTanricFragment5 = new RadioTanricFragment();
        Bundle bundle5 = new Bundle();
        bundle5.putString("title", dateStrs.get(4));
        bundle.putInt("position", 4);
        radioTanricFragment5.setArguments(bundle5);
        adapter.addFrag(radioTanricFragment5, dateStrsTitle.get(4));

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(0);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        onTabSelectedListener(viewPager);
      /*  viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // your tite is: mAdapter.getPageTitle(position));
                // TODO: update title
                currentPageTitle =;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/
    }

    public static String sendData() {
        return String.valueOf(adapter.getPageTitle(tabLayout.getSelectedTabPosition()));
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPlayerAdapter != null && mPlayerAdapter.isMediaPlayer()) {
            mPlayerAdapter.onPauseActivity();
        }
    }

    public void updatePlayingNotificationStatus(Boolean status) {

        if (mPlayerAdapter != null && mPlayerAdapter.isPlaying()) {
//            mPlayerAdapter.resumeOrPause();

            play_pause_click.setImageResource(R.drawable.ic_stop_yellow);
            gifDrawable.start();
            if (selected_songs.getAuthor_name() != null && selected_songs.getAuthor_name().equalsIgnoreCase("radio-thiruneriya-thamizhosai"))
                radio_header_text.setText(AppConfig.Radio_Thiruneriya_Thamizhosai);
            else
                radio_header_text.setText(AppConfig.Radio_Shaivalahari);
            radio_title.setText(selected_songs.getTitle());

        } else {/*
            mPlayerAdapter.setCurrentSong(selected_songs, selected_songsList);
            mPlayerAdapter. initMediaPlayer(RadioDetailActivity.this);*/
            play_pause_click.setImageResource(R.drawable.ic_play);
            animation_image.setFreezesAnimation(true);
            gifDrawable.stop();
            if (selected_songs.getAuthor_name() != null && selected_songs.getAuthor_name().equalsIgnoreCase("radio-thiruneriya-thamizhosai"))
                radio_header_text.setText(AppConfig.Radio_Thiruneriya_Thamizhosai);
            else
                radio_header_text.setText(AppConfig.Radio_Shaivalahari);
            radio_title.setText(selected_songs.getTitle());
        }
        if (status) {
            AppConfig.showProgDialiog(RadioDetailActivity.this);
            mPlayerAdapter.setCurrentSong(selected_songs, selected_songsList);
            mPlayerAdapter.initMediaPlayer(RadioDetailActivity.this);
            setupViewPager(viewPager);
        }
        /*
        if (play_pause_click.getDrawable().getConstantState().equals
                (getResources().getDrawable(R.drawable.ic_play).getConstantState())) {
            play_pause_click.setImageResource(R.drawable.ic_stop_yellow);
            gifDrawable.start();
        } else {
            play_pause_click.setImageResource(R.drawable.ic_play);
            animation_image.setFreezesAnimation(true);
            gifDrawable.stop();
        }*/
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
                                mMusicNotificationManager.createNotification(RadioDetailActivity.this));
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
                        mMusicService.getMusicNotificationManager(RadioDetailActivity.this)
                                .getNotificationManager().notify(MusicNotificationManager.NOTIFICATION_ID,
                                mMusicService.getMusicNotificationManager(RadioDetailActivity.this).getNotificationBuilder().build());
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
