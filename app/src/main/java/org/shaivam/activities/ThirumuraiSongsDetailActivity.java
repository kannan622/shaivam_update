package org.shaivam.activities;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.shaivam.R;
import org.shaivam.adapter.SongsLyricsAdapter;
import org.shaivam.adapter.SongsSpinnerCustomAdapter;
import org.shaivam.interfaces.VolleyCallback;
import org.shaivam.loaders.SongProvider;
import org.shaivam.model.Oduvar;
import org.shaivam.model.Selected_songs;
import org.shaivam.model.Thirumurai_songs;
import org.shaivam.playback.EqualizerUtils;
import org.shaivam.playback.MusicNotificationManager;
import org.shaivam.playback.PlaybackInfoListener;
import org.shaivam.playback.PlayerAdapter;
import org.shaivam.service.MusicService;
import org.shaivam.utils.AppConfig;
import org.shaivam.utils.MyApplication;
import org.shaivam.utils.PrefManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static org.shaivam.utils.MyApplication.prefManager;

public class ThirumuraiSongsDetailActivity extends MainActivity implements LoaderManager.LoaderCallbacks<List<Selected_songs>>,
        AdapterView.OnItemSelectedListener {
    private Toolbar toolbar;
    public static List<Thirumurai_songs> thirumuraiDetails = new ArrayList<Thirumurai_songs>();
    public static List<Selected_songs> selected_songsList = new ArrayList<Selected_songs>();

    @BindView(R.id.recycle_song_line)
    RecyclerView recycle_song_line;

    @BindView(R.id.pann_header_text)
    TextView pann_header_text;
    @BindView(R.id.title_header_text)
    TextView title_header_text;
    @BindView(R.id.thirichitramplam_text)
    TextView thirichitramplam_text;
    @BindView(R.id.aruliyavar_direction)
    TextView aruliyavar_direction;
    @BindView(R.id.thirumurai_direction)
    TextView thirumurai_direction;
    @BindView(R.id.pann_direction)
    TextView pann_direction;
    @BindView(R.id.nadu_direction)
    TextView nadu_direction;
    @BindView(R.id.thalam_direction)
    TextView thalam_direction;

    @BindView(R.id.addon_vertical_view)
    View addon_vertical_view;
    @BindView(R.id.addon_linear)
    LinearLayout addon_linear;
    @BindView(R.id.addon_direction)
    TextView addon_direction;

    @BindView(R.id.aruliyavar_text)
    TextView aruliyavar_text;
    @BindView(R.id.thirumurai_text)
    TextView thirumurai_text;
    @BindView(R.id.pann_text)
    TextView pann_text;
    @BindView(R.id.nadu_text)
    TextView nadu_text;
    @BindView(R.id.thalam_text)
    TextView thalam_text;

    public static Spinner author_spinner;

    @BindView(R.id.bookmark)
    ImageView bookmark;

    @BindView(R.id.no_music_bookmark)
    ImageView no_music_bookmark;


    @BindView(R.id.stop)
    ImageView stop;
    @BindView(R.id.play_pause)
    ImageView play_pause;
    @BindView(R.id.play_previous)
    ImageView play_previous;
    @BindView(R.id.play_next)
    ImageView play_next;


    @BindView(R.id.no_music_play_previous)
    TextView no_music_play_previous;
    @BindView(R.id.no_music_play_next)
    TextView no_music_play_next;


    @BindView(R.id.song_position)
    TextView song_position;
    @BindView(R.id.duration)
    TextView duration_text;
    @BindView(R.id.seekTo)
    SeekBar seekTo;

    @BindView(R.id.font_settings)
    ImageView font_settings;
    @BindView(R.id.thirumurai_share)
    ImageView thirumurai_share;

    @BindView(R.id.music_main_linear)
    LinearLayout music_main_linear;

    @BindView(R.id.no_music_main_linear)
    LinearLayout no_music_main_linear;

    @BindView(R.id.song_lyrics_main_linear)
    LinearLayout song_lyrics_main_linear;

    @BindView(R.id.pann_linear)
    LinearLayout pann_linear;
    @BindView(R.id.nadu_linear)
    LinearLayout nadu_linear;
    @BindView(R.id.thalam_linear)
    LinearLayout thalam_linear;

    @BindView(R.id.pann_vertical_view)
    View pann_vertical_view;
    @BindView(R.id.nadu_vertical_view)
    View nadu_vertical_view;

    Thirumurai_songs thirumurai_songs = new Thirumurai_songs();
    static Selected_songs selected_songs = new Selected_songs();
    static Selected_songs previous_selected_songs = new Selected_songs();
    public static int position = 0;

    private PlayerAdapter mPlayerAdapter;
    private SongsLyricsAdapter adapter;
    static SongsSpinnerCustomAdapter dataAdapter;

    private boolean mUserIsSeeking = false;
    private static MusicService mMusicService;
    private static PlaybackListener mPlaybackListener;
    private static MusicNotificationManager mMusicNotificationManager;

    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            if (mMusicService == null)
                mMusicService = ((MusicService.LocalBinder) iBinder).getInstance(ThirumuraiSongsDetailActivity.this);
            if (mPlayerAdapter == null)
                mPlayerAdapter = mMusicService.getMediaPlayerHolder();
            if (mMusicNotificationManager == null) {
                mMusicNotificationManager = mMusicService.getMusicNotificationManager(ThirumuraiSongsDetailActivity.this);
                mMusicNotificationManager.setAccentColor(R.color.colorPrimaryDark);
            }
            if (mPlaybackListener == null) {
                mPlaybackListener = new ThirumuraiSongsDetailActivity.PlaybackListener();
                mPlayerAdapter.setPlaybackInfoListener(mPlaybackListener);
            }
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
        setContentView(R.layout.activity_thirumurai_songs_detail);
        ButterKnife.bind(this);

        toolbar = findViewById(R.id.toolbar);
        TextView textCustomTitle = (TextView) findViewById(R.id.custom_title);
        Typeface customFont = Typeface.createFromAsset(this.getAssets(), AppConfig.FONT_KAVIVANAR);
        textCustomTitle.setTypeface(customFont);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setFont();

        if (!prefManager.getBoolean(PrefManager.FAVOURITES_PREF))
            AppConfig.addFavourites();
        else {
            AppConfig.updateFavouritesDatabase();
        }

        author_spinner = findViewById(R.id.author_spinner);

        thirumuraiDetails.clear();

        Intent intent = getIntent();
        if (intent.hasExtra("title")) {
//            AppConfig.showProgDialiog(ThirumuraiSongsDetailActivity.this);
            Log.e("ssasas", intent.getStringExtra("title"));
            final List<Thirumurai_songs> songs = MyApplication.repo.repoThirumurai_songs.getSongsByTitle(intent.getStringExtra("title"));
            if (songs != null) {
                if (songs.size() > 0)
                    textCustomTitle.setText(MyApplication.repo.repoThirumurais.getById(String.valueOf(songs.get(0).getThirumuraiId()))
                            .getThirumuraiName());
                for (int i = 0; i < songs.size(); i++) {
//                    if (!songs.get(i).getAudioUrl().equalsIgnoreCase(""))
                    thirumuraiDetails.add(songs.get(i));
                }
                if (intent.hasExtra("search_item"))
                    adapter = new SongsLyricsAdapter(this, intent.getStringExtra("search_item"), thirumuraiDetails);
                else
                    adapter = new SongsLyricsAdapter(this, "", thirumuraiDetails);

                recycle_song_line.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                recycle_song_line.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                recycle_song_line.setNestedScrollingEnabled(false);

                font_settings.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog dialog1 = new Dialog(ThirumuraiSongsDetailActivity.this);
                        dialog1.setContentView(R.layout.dialog_font_settings);
                        final SeekBar seekBar = dialog1.findViewById(R.id.seekBar);
                        if (prefManager.getInt(PrefManager.SONG_FONT_SIZE,
                            AppConfig.SMALL_SONG_FONT_SIZE) == AppConfig.SMALL_SONG_FONT_SIZE)
                            seekBar.setProgress(25);
                        else if (prefManager.getInt(PrefManager.SONG_FONT_SIZE,
                            AppConfig.SMALL_SONG_FONT_SIZE) == AppConfig.MEDIUM_SONG_FONT_SIZE)
                            seekBar.setProgress(50);
                        else if (prefManager.getInt(PrefManager.SONG_FONT_SIZE,
                            AppConfig.SMALL_SONG_FONT_SIZE) == AppConfig.LARGE_SONG_FONT_SIZE)
                            seekBar.setProgress(75);
                        else if (prefManager.getInt(PrefManager.SONG_FONT_SIZE,
                            AppConfig.SMALL_SONG_FONT_SIZE) == AppConfig.EXTRA_LARGE_SONG_FONT_SIZE)
                            seekBar.setProgress(100);

                        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress,
                                                          boolean fromUser) {
                                if (progress <= 25)
                                    prefManager.saveInt(PrefManager.SONG_FONT_SIZE, AppConfig.SMALL_SONG_FONT_SIZE);
                                else if (progress <= 50)
                                    prefManager.saveInt(PrefManager.SONG_FONT_SIZE, AppConfig.MEDIUM_SONG_FONT_SIZE);
                                else if (progress <= 75)
                                    prefManager.saveInt(PrefManager.SONG_FONT_SIZE, AppConfig.LARGE_SONG_FONT_SIZE);
                                else if (progress <= 100)
                                    prefManager.saveInt(PrefManager.SONG_FONT_SIZE, AppConfig.EXTRA_LARGE_SONG_FONT_SIZE);

                                if (adapter != null)
                                    adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {
                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {
                            }
                        });

                        final Button ok_button = dialog1.findViewById(R.id.ok_button);
                        ok_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog1.cancel();
                            }
                        });
                        dialog1.show();
                    }
                });
                if (thirumuraiDetails.size() > 0) {
                    thirumurai_songs = thirumuraiDetails.get(0);

                    thirumurai_share.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent sharingIntent = new Intent(
                                    android.content.Intent.ACTION_SEND);
                            sharingIntent.setType("text/plain");
                            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                                    thirumurai_songs.getTitle()
                                            + " - I want to share this Thirumurai with you.");
                            sharingIntent
                                    .putExtra(
                                            android.content.Intent.EXTRA_TEXT,
                                            thirumurai_songs.getTitle()
                                                    + " - I want to share this Thirumurai with you."
                                                    + "\n"
                                                    + "இந்தத் திருமுறையை Shaivam.org Mobile செயலியில் படித்தேன். மிகவும் பிடித்திருந்தது. பகிர்கின்றேன். படித்து மகிழவும்."
                                                    + thirumurai_songs.getUrl() + "\n\nஇந்த இலவசச் செயலியைப் பதிவிறக்கம் செய்க: https://play.google.com/store/apps/details?id=org.shaivam ");
                            startActivity(Intent.createChooser(sharingIntent, "Share via"));
                        }
                    });

                    if (previous_selected_songs.getTitle() == null || !(previous_selected_songs.getAudioUrl().equalsIgnoreCase(thirumurai_songs.getAudioUrl()))) {
                        selected_songsList.clear();
                        selected_songs = new Selected_songs();
                        if (dataAdapter != null)
                            dataAdapter.notifyDataSetChanged();
                        if (AppConfig.isConnectedToInternet(MyApplication.getInstance())) {
//                            AppConfig.showProgDialiog(this);
                            music_main_linear.setVisibility(View.GONE);
                            no_music_main_linear.setVisibility(View.VISIBLE);
                            MyApplication.gsonVolley.Request(MyApplication.getInstance(), AppConfig.URL_BASE, AppConfig.URL_ODUVAR,
                                    MyApplication.jsonHelper.createOduvarJson(String.valueOf(thirumurai_songs.getThirumuraiId()),
                                            thirumurai_songs.getTitleNo()),
                                    oduvarCallBack, com.android.volley.Request.Method.POST);
                        } else {
                            snackBar(AppConfig.getTextString(ThirumuraiSongsDetailActivity.this, AppConfig.connection_message_audio));
                            music_main_linear.setVisibility(View.GONE);
                            no_music_main_linear.setVisibility(View.VISIBLE);
                        }
                        position = 0;
                    } else {
                        dataAdapter = new SongsSpinnerCustomAdapter(this, selected_songsList);
                        // attaching data adapter to spinner
                        author_spinner.setAdapter(dataAdapter);
                        author_spinner.setSelection(position);
                        dataAdapter.notifyDataSetChanged();

                        no_music_main_linear.setVisibility(View.GONE);
                        music_main_linear.setVisibility(View.VISIBLE);
                    }
                    if (thirumurai_songs.getPann().equalsIgnoreCase("")) {
                        pann_linear.setVisibility(View.GONE);
                        pann_header_text.setVisibility(View.INVISIBLE);
                        pann_vertical_view.setVisibility(View.GONE);
                    } else {
                        pann_vertical_view.setVisibility(View.VISIBLE);
                        pann_linear.setVisibility(View.VISIBLE);
                        pann_header_text.setVisibility(View.VISIBLE);
                        pann_header_text.setText(thirumurai_songs.getPann() + " > ");
                    }

                    if (thirumurai_songs.getCountry().equalsIgnoreCase("")) {
                        nadu_linear.setVisibility(View.GONE);
                        nadu_vertical_view.setVisibility(View.GONE);
                    } else {
                        nadu_vertical_view.setVisibility(View.VISIBLE);
                        nadu_linear.setVisibility(View.VISIBLE);
                    }

                    if (thirumurai_songs.getThalam().equalsIgnoreCase("")) {
                        thalam_linear.setVisibility(View.GONE);
                        addon_vertical_view.setVisibility(View.GONE);
                    } else {
                        addon_vertical_view.setVisibility(View.VISIBLE);
                        thalam_linear.setVisibility(View.VISIBLE);
                    }

                    title_header_text.setText(thirumurai_songs.getTitle());

                    aruliyavar_direction.setText(": " + thirumurai_songs.getAuthor());
                    thirumurai_direction.setText(": " + MyApplication.repo.repoThirumurais.getById(String.valueOf(thirumurai_songs.getThirumuraiId())).getThirumuraiName());
                    pann_direction.setText(": " + thirumurai_songs.getPann());
                    nadu_direction.setText(": " + thirumurai_songs.getCountry());
                    thalam_direction.setText(": " + thirumurai_songs.getThalam());
                    if (thirumurai_songs.getAddon() != null && thirumurai_songs.getAddon().equalsIgnoreCase("")) {
                        addon_direction.setVisibility(View.GONE);
                        addon_linear.setVisibility(View.GONE);
                    } else {
                        addon_direction.setVisibility(View.VISIBLE);
                        addon_linear.setVisibility(View.VISIBLE);
                        addon_direction.setText(thirumurai_songs.getAddon());
                    }
                    /*no_music_main_linear.setVisibility(View.GONE);
                    music_main_linear.setVisibility(View.VISIBLE);
                    song_lyrics_main_linear.setVisibility(View.VISIBLE);*/
                    if (thirumurai_songs.getFavorites()) {
                        bookmark.setImageDrawable(getResources().getDrawable(R.drawable.ic_bookmark_white));
                        bookmark.setBackgroundResource(R.color.colorPrimary);
                        no_music_bookmark.setImageDrawable(getResources().getDrawable(R.drawable.ic_bookmark_white));
                        no_music_bookmark.setBackgroundResource(R.color.colorPrimary);
                    } else {
                        bookmark.setImageDrawable(getResources().getDrawable(R.drawable.ic_bookmark));
                        bookmark.setBackgroundResource(R.color.bg_white);
                        no_music_bookmark.setImageDrawable(getResources().getDrawable(R.drawable.ic_bookmark));
                        no_music_bookmark.setBackgroundResource(R.color.bg_white);
                    }
                    bookmark.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (thirumurai_songs.getFavorites()) {
                                AppConfig.showFavoritesDialog(ThirumuraiSongsDetailActivity.this,
                                        thirumurai_songs, false, bookmark);
                            } else {
                                AppConfig.showFavoritesDialog(ThirumuraiSongsDetailActivity.this,
                                        thirumurai_songs, true, bookmark);
                            }

                        }
                    });
                    no_music_bookmark.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (thirumurai_songs.getFavorites()) {
                                AppConfig.showFavoritesDialog(ThirumuraiSongsDetailActivity.this,
                                        thirumurai_songs, false, no_music_bookmark);
                            } else {
                                AppConfig.showFavoritesDialog(ThirumuraiSongsDetailActivity.this,
                                        thirumurai_songs, true, no_music_bookmark);
                            }

                        }
                    });
                } else {
                    music_main_linear.setVisibility(View.GONE);
                    no_music_main_linear.setVisibility(View.VISIBLE);
                    song_lyrics_main_linear.setVisibility(View.GONE);
                }
            } else {
                music_main_linear.setVisibility(View.GONE);
                no_music_main_linear.setVisibility(View.VISIBLE);
                song_lyrics_main_linear.setVisibility(View.GONE);
            }
        }
        initializeSeekBar();

        doBindService();
    }

    VolleyCallback oduvarCallBack = new VolleyCallback() {
        @Override
        public void Success(int stauscode, String response) {
            try {
                JSONObject jObj = new JSONObject(response);
                if (jObj.has("Status") && jObj.getString("Status").equalsIgnoreCase("true")) {
                    List<Oduvar> data = Arrays.asList(MyApplication.gson.fromJson(jObj.getJSONArray("data").toString(),
                            Oduvar[].class));
                    selected_songsList.clear();
                    for (int i = 0; i < data.size(); i++) {
                        Boolean notPresent = true;
                        for (int j = 0; j < selected_songsList.size(); j++) {
                            if (selected_songsList.get(j).getAuthor_name().equalsIgnoreCase(data.get(i).getOdhuvartamilname()))
                                notPresent = false;
                        }
                        if (notPresent)
                            selected_songsList.add(new Selected_songs(data.get(i).getAudiourl(), data.get(i).getOdhuvartamilname(),
                                    data.get(i).getPathigam(), data.get(i).getThirumurai()));

                    }
                    if (dataAdapter != null)
                        dataAdapter.notifyDataSetChanged();
                    if (selected_songsList.size() > 0)
                        thirumurai_songs.setAudioUrl(selected_songsList.get(0).getAudioUrl());
                    getSupportLoaderManager().initLoader(SongProvider.ARTISTS_LOADER, null, ThirumuraiSongsDetailActivity.this);
                    music_main_linear.setVisibility(View.VISIBLE);
                    no_music_main_linear.setVisibility(View.GONE);
                } else {
                    snackBar(jObj.getString("Message"));
//                    Toast.makeText(ThirumuraiSongsDetailActivity.this, jObj.getString("Message"), Toast.LENGTH_LONG).show();
                    music_main_linear.setVisibility(View.GONE);
                    no_music_main_linear.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                music_main_linear.setVisibility(View.GONE);
                no_music_main_linear.setVisibility(View.VISIBLE);
                snackBar(AppConfig.getTextString(ThirumuraiSongsDetailActivity.this, AppConfig.went_wrong));
            }
        }

        @Override
        public void Failure(int stauscode, String errorResponse) {
            music_main_linear.setVisibility(View.GONE);
            no_music_main_linear.setVisibility(View.VISIBLE);
            snackBar(errorResponse);
        }
    };


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (!seekTo.isEnabled()) {
            seekTo.setEnabled(true);
        }
        selected_songs = selected_songsList.get(position);
        if (previous_selected_songs.getTitle() == null || !(previous_selected_songs.getAudioUrl().equalsIgnoreCase(selected_songs.getAudioUrl()))) {
//                && previous_selected_songs.getAuthor_name().equalsIgnoreCase(selected_songs.getAuthor_name()))) {

            if (AppConfig.isConnectedToInternet(MyApplication.getInstance()) && mPlayerAdapter != null) {
                ThirumuraiSongsDetailActivity.position = position;
                previous_selected_songs = selected_songs;
                AppConfig.showProgDialiog(this);
                mPlayerAdapter.setCurrentSong(selected_songs, selected_songsList);
                mPlayerAdapter.initMediaPlayer(ThirumuraiSongsDetailActivity.this);
            } else {
                music_main_linear.setVisibility(View.GONE);
                no_music_main_linear.setVisibility(View.VISIBLE);
            }
        } else
            AppConfig.hideProgDialog();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    public void setFont() {
        Typeface tf = Typeface.createFromAsset(getAssets(), AppConfig.FONT_KAVIVANAR);
        aruliyavar_text.setTypeface(tf);
        thirumurai_text.setTypeface(tf);
        pann_text.setTypeface(tf);
        nadu_text.setTypeface(tf);
        thalam_text.setTypeface(tf);
        pann_header_text.setTypeface(tf);
        title_header_text.setTypeface(tf);
        thirichitramplam_text.setTypeface(tf);
        aruliyavar_direction.setTypeface(tf);
        thirumurai_direction.setTypeface(tf);
        pann_direction.setTypeface(tf);
        nadu_direction.setTypeface(tf);
        thalam_direction.setTypeface(tf);
        addon_direction.setTypeface(tf);
    }

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

    private void initializeSeekBar() {
        seekTo.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    final int currentPositionColor = song_position.getCurrentTextColor();
                    int userSelectedPosition = 0;

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        mUserIsSeeking = true;
                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                        if (fromUser) {
                        userSelectedPosition = progress;
                        song_position.setTextColor(ContextCompat.getColor(ThirumuraiSongsDetailActivity.this, R.color.bg_white));
//                        }
                        song_position.setText(Thirumurai_songs.formatDuration(progress));
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                        if (mUserIsSeeking) {
                            song_position.setTextColor(currentPositionColor);
                        }
                        mUserIsSeeking = false;
                        mPlayerAdapter.seekTo(userSelectedPosition);
                    }
                });
    }

    @OnClick(R.id.stop)
    public void onStopClick(View view) {
        if (checkIsPlayer()) {
            mPlayerAdapter.stop();
            updateResetStatus(false);
        }
    }

    @OnClick(R.id.play_previous)
    public void onPreviousClick(View view) {
     /*   if (checkIsPlayer()) {
            mPlayerAdapter.resetSong();
            author_spinner.setSelection(position);
        }*/
        Intent intent = getIntent();
        if (intent.hasExtra("title")) {
//            AppConfig.showProgDialiog(ThirumuraiSongsDetailActivity.this);
            Log.e("ssasas", intent.getStringExtra("title"));
            final List<Thirumurai_songs> songs = MyApplication.repo.repoThirumurai_songs.getSongsByTitle(intent.getStringExtra("title"));
            if (songs != null && songs.size() > 0) {
                List<Thirumurai_songs> thirumurai_song = MyApplication.repo.repoThirumurai_songs.getByThirumurai_Id(songs.get(0).getThirumuraiId());
                if (thirumurai_song != null) {
                    for (int i = 0; i < thirumurai_song.size(); i++) {
                        if (thirumurai_song.get(i).getTitle().equalsIgnoreCase(intent.getStringExtra("title"))) {
                            if (i > 0) {
                                Intent intents = new Intent(this, ThirumuraiSongsDetailActivity.class);
                                intents.putExtra("title", thirumurai_song.get(i - 1).getTitle());
                                startActivity(intents);
                                finish();
                            } else
                                Toast.makeText(this, getResources().getText(R.string.no_thirumurai), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else
                    Toast.makeText(this, getResources().getText(R.string.no_thirumurai), Toast.LENGTH_SHORT).show();

            } else
                Toast.makeText(this, getResources().getText(R.string.no_thirumurai), Toast.LENGTH_SHORT).show();

        }

    }

    @OnClick(R.id.no_music_play_previous)
    public void onNoMusicPreviousClick(View view) {
     /*   if (checkIsPlayer()) {
            mPlayerAdapter.resetSong();
            author_spinner.setSelection(position);
        }*/
        Intent intent = getIntent();
        if (intent.hasExtra("title")) {
//            AppConfig.showProgDialiog(ThirumuraiSongsDetailActivity.this);
            Log.e("ssasas", intent.getStringExtra("title"));
            final List<Thirumurai_songs> songs = MyApplication.repo.repoThirumurai_songs.getSongsByTitle(intent.getStringExtra("title"));
            if (songs != null && songs.size() > 0) {
                List<Thirumurai_songs> thirumurai_song = MyApplication.repo.repoThirumurai_songs.getByThirumurai_Id(songs.get(0).getThirumuraiId());
                if (thirumurai_song != null) {
                    for (int i = 0; i < thirumurai_song.size(); i++) {
                        if (thirumurai_song.get(i).getTitle().equalsIgnoreCase(intent.getStringExtra("title"))) {
                            if (i > 0) {
                                Intent intents = new Intent(this, ThirumuraiSongsDetailActivity.class);
                                intents.putExtra("title", thirumurai_song.get(i - 1).getTitle());
                                startActivity(intents);
                                finish();
                            } else
                                Toast.makeText(this, getResources().getText(R.string.no_thirumurai), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else
                    Toast.makeText(this, getResources().getText(R.string.no_thirumurai), Toast.LENGTH_SHORT).show();

            } else
                Toast.makeText(this, getResources().getText(R.string.no_thirumurai), Toast.LENGTH_SHORT).show();

        }

    }

    @OnClick(R.id.play_pause)
    public void onPlay_pauseClick(View view) {
        if (AppConfig.isConnectedToInternet(MyApplication.getInstance())) {
            if (!seekTo.isEnabled()) {
                seekTo.setEnabled(true);
            }
            if (previous_selected_songs.getTitle() != null && previous_selected_songs.getAudioUrl().equalsIgnoreCase(selected_songs.getAudioUrl()) && checkIsPlayer()) {
                mPlayerAdapter.resumeOrPause();
            } else {
                author_spinner.setOnItemSelectedListener(this);

                position = author_spinner.getSelectedItemPosition();
                selected_songs = selected_songsList.get(position);
                previous_selected_songs = selected_songs;
                mPlayerAdapter.setCurrentSong(selected_songs, selected_songsList);
                AppConfig.showProgDialiog(this);
                mPlayerAdapter.initMediaPlayer(ThirumuraiSongsDetailActivity.this);

            }
        } else {
            snackBar(AppConfig.getTextString(ThirumuraiSongsDetailActivity.this, AppConfig.connection_message_audio));
            music_main_linear.setVisibility(View.GONE);
            no_music_main_linear.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.play_next)
    public void onNextClick(View view) {
      /*  if (checkIsPlayer()) {
            mPlayerAdapter.skip(true);
            author_spinner.setSelection(position);
        }*/

        Intent intent = getIntent();
        if (intent.hasExtra("title")) {
//            AppConfig.showProgDialiog(ThirumuraiSongsDetailActivity.this);
            Log.e("ssasas", intent.getStringExtra("title"));
            final List<Thirumurai_songs> songs = MyApplication.repo.repoThirumurai_songs.getSongsByTitle(intent.getStringExtra("title"));
            if (songs != null && songs.size() > 0) {
                List<Thirumurai_songs> thirumurai_song = MyApplication.repo.repoThirumurai_songs.getByThirumurai_Id(songs.get(0).getThirumuraiId());
                if (thirumurai_song != null) {
                    for (int i = 0; i < thirumurai_song.size(); i++) {
                        if (thirumurai_song.get(i).getTitle().equalsIgnoreCase(intent.getStringExtra("title"))) {
                            if (i + 1 != thirumurai_song.size()) {
                                Intent intents = new Intent(this, ThirumuraiSongsDetailActivity.class);
                                intents.putExtra("title", thirumurai_song.get(i + 1).getTitle());
                                startActivity(intents);
                                finish();
                            } else
                                Toast.makeText(this, getResources().getText(R.string.no_thirumurai), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else
                    Toast.makeText(this, getResources().getText(R.string.no_thirumurai), Toast.LENGTH_SHORT).show();

            } else
                Toast.makeText(this, getResources().getText(R.string.no_thirumurai), Toast.LENGTH_SHORT).show();

        }
    }

    @OnClick(R.id.no_music_play_next)
    public void onNoMusicNextClick(View view) {
      /*  if (checkIsPlayer()) {
            mPlayerAdapter.skip(true);
            author_spinner.setSelection(position);
        }*/

        Intent intent = getIntent();
        if (intent.hasExtra("title")) {
//            AppConfig.showProgDialiog(ThirumuraiSongsDetailActivity.this);
            Log.e("ssasas", intent.getStringExtra("title"));
            final List<Thirumurai_songs> songs = MyApplication.repo.repoThirumurai_songs.getSongsByTitle(intent.getStringExtra("title"));
            if (songs != null && songs.size() > 0) {
                List<Thirumurai_songs> thirumurai_song = MyApplication.repo.repoThirumurai_songs.getByThirumurai_Id(songs.get(0).getThirumuraiId());
                if (thirumurai_song != null) {
                    for (int i = 0; i < thirumurai_song.size(); i++) {
                        if (thirumurai_song.get(i).getTitle().equalsIgnoreCase(intent.getStringExtra("title"))) {
                            if (i + 1 != thirumurai_song.size()) {
                                Intent intents = new Intent(this, ThirumuraiSongsDetailActivity.class);
                                intents.putExtra("title", thirumurai_song.get(i + 1).getTitle());
                                startActivity(intents);
                                finish();
                            } else
                                Toast.makeText(this, getResources().getText(R.string.no_thirumurai), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else
                    Toast.makeText(this, getResources().getText(R.string.no_thirumurai), Toast.LENGTH_SHORT).show();

            } else
                Toast.makeText(this, getResources().getText(R.string.no_thirumurai), Toast.LENGTH_SHORT).show();

        }
    }

    private boolean checkIsPlayer() {

        boolean isPlayer = mPlayerAdapter.isMediaPlayer();
        if (!isPlayer) {
            EqualizerUtils.notifyNoSessionId(this);
        }
        return isPlayer;
    }

    private void updateResetStatus(boolean onPlaybackCompletion) {
        final int color = onPlaybackCompletion ? Color.BLACK : mPlayerAdapter.isReset() ? Color.WHITE : Color.BLACK;
        stop.post(new Runnable() {
            @Override
            public void run() {
                stop.getDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
            }
        });
    }

    private void updatePlayingStatus() {
        final int drawable = mPlayerAdapter.getState() != PlaybackInfoListener.State.PAUSED ? R.drawable.ic_pause : R.drawable.ic_play;
        play_pause.post(new Runnable() {
            @Override
            public void run() {
                play_pause.setImageResource(drawable);
            }
        });
    }

    private void updatePlayingInfo(boolean restore, boolean startPlay) {

        if (startPlay) {
            mPlayerAdapter.getMediaPlayer().start();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mMusicService.startForeground(MusicNotificationManager.NOTIFICATION_ID,
                                mMusicNotificationManager.createNotification(ThirumuraiSongsDetailActivity.this));
                    }
                }
            }, 250);
        }

        selected_songs = mPlayerAdapter.getCurrentSong();


        final int duration = mPlayerAdapter.getDuration();
        seekTo.setMax(duration);
        AppConfig.updateTextView(duration_text, Thirumurai_songs.formatDuration(duration));

        if (restore) {
            seekTo.setProgress(mPlayerAdapter.getPlayerPosition());
            updatePlayingStatus();
            updateResetStatus(false);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    //stop foreground if coming from pause state
                    if (mMusicService.isRestoredFromPause()) {
                        mMusicService.stopForeground(false);
                        mMusicService.getMusicNotificationManager(ThirumuraiSongsDetailActivity.this)
                                .getNotificationManager().notify(MusicNotificationManager.NOTIFICATION_ID,
                                mMusicService.getMusicNotificationManager(ThirumuraiSongsDetailActivity.this).getNotificationBuilder().build());
                        mMusicService.setRestoredFromPause(false);
                    }
                }
            }, 250);
        }
/*
        if (mPlayerAdapter.isPlaying())
            mPlayerAdapter.resumeOrPause();*/
    }

    private void restorePlayerStatus() {

        if (seekTo != null && mPlayerAdapter != null)
            seekTo.setEnabled(mPlayerAdapter.isMediaPlayer());

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
        if (dataAdapter != null)
            dataAdapter.notifyDataSetChanged();
        if (mPlayerAdapter != null)
            restorePlayerStatus();
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
        return new SongProvider.AsyncSongLoader(this, thirumurai_songs);
    }

    @Override
    public void onLoadFinished
            (@NonNull Loader<List<Selected_songs>> loader, List<Selected_songs> songs) {
        if (!isFinishing()) {
            if (songs.isEmpty()) {
                if (isFinishing()) {
                    return;
                }
                Toast.makeText(this, AppConfig.getTextString(this, AppConfig.error_no_music), Toast.LENGTH_SHORT)
                        .show();
                AppConfig.hideProgDialog();
                no_music_main_linear.setVisibility(View.VISIBLE);
                music_main_linear.setVisibility(View.GONE);
//                song_lyrics_main_linear.setVisibility(View.GONE);
            } else {

//                selected_songsList = songs;

                dataAdapter = new SongsSpinnerCustomAdapter(this, selected_songsList);
                // attaching data adapter to spinner
                author_spinner.setAdapter(dataAdapter);
                author_spinner.setSelection(position);
                dataAdapter.notifyDataSetChanged();
                no_music_main_linear.setVisibility(View.GONE);
                music_main_linear.setVisibility(View.VISIBLE);
//                song_lyrics_main_linear.setVisibility(View.VISIBLE);
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
            if (!mUserIsSeeking) {
                seekTo.setProgress(position);
            }
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
            updateResetStatus(true);
        }
    }
}