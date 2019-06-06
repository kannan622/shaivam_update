package org.shaivam.activities;

import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.shaivam.R;
import org.shaivam.adapter.MyFavouritesSongsAdapter;
import org.shaivam.model.Thirumurai_songs;
import org.shaivam.utils.AppConfig;
import org.shaivam.utils.MyApplication;
import org.shaivam.utils.MyContextWrapper;
import org.shaivam.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static org.shaivam.utils.MyApplication.prefManager;

public class MyFavouritesActivity extends MainActivity {
    private Toolbar toolbar;
    private CoordinatorLayout my_favorites_main;

    public static List<Thirumurai_songs> thirumuraiDetails = new ArrayList<Thirumurai_songs>();
    private MyFavouritesSongsAdapter adapter;
    public RecyclerView recycle_song_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favourites);
        toolbar = findViewById(R.id.toolbar);
        TextView textCustomTitle = (TextView) findViewById(R.id.custom_title);
        Typeface customFont = Typeface.createFromAsset(this.getAssets(), AppConfig.FONT_KAVIVANAR);
        textCustomTitle.setTypeface(customFont);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if (!prefManager.getBoolean(PrefManager.FAVOURITES_PREF))
            AppConfig.addFavourites();
        else {
            AppConfig.updateFavouritesDatabase();
        }

        my_favorites_main = findViewById(R.id.my_favorites_main);
        recycle_song_list = findViewById(R.id.recycle_song_list);
        adapter = new MyFavouritesSongsAdapter(this, thirumuraiDetails);
        recycle_song_list.setLayoutManager(new LinearLayoutManager(this));
        recycle_song_list.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }

    @Override
    public void onResume() {
        super.onResume();
        AppConfig.customeLanguage(this);
        thirumuraiDetails.clear();
        thirumuraiDetails.addAll(MyApplication.repo.repoThirumurai_songs.getAllFavouritesThirumurai());
        adapter.notifyDataSetChanged();
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
}
