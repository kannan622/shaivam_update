package org.shaivam.activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.shaivam.R;
import org.shaivam.adapter.SongsAdapter;
import org.shaivam.model.Thirumurai_songs;
import org.shaivam.utils.AppConfig;
import org.shaivam.utils.MyApplication;
import org.shaivam.utils.MyContextWrapper;
import org.shaivam.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ThirumuraiSongsListActivity extends MainActivity {
    private Toolbar toolbar;
    public static List<Thirumurai_songs> thirumuraiDetails = new ArrayList<Thirumurai_songs>();
    private SongsAdapter adapter;
    public RecyclerView recycle_song_list;
    TextView song_header_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thirumurai_songs_list);
        toolbar = findViewById(R.id.toolbar);
        TextView textCustomTitle = (TextView) findViewById(R.id.custom_title);
        Typeface customFont = Typeface.createFromAsset(this.getAssets(), AppConfig.FONT_KAVIVANAR);
        textCustomTitle.setTypeface(customFont);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        song_header_text = findViewById(R.id.song_header_text);
        thirumuraiDetails.clear();
        Intent intent = getIntent();
        if (intent.hasExtra("thirumurai_id") && intent.hasExtra("pann")) {
            if (MyApplication.repo.repoThirumurais
                    .getById(String.valueOf(intent.getIntExtra("thirumurai_id", 0))) != null)
                textCustomTitle.setText(MyApplication.repo.repoThirumurais
                        .getById(String.valueOf(intent.getIntExtra("thirumurai_id", 0)))
                        .getThirumuraiName());
            song_header_text.setText(intent.getStringExtra("pann"));
            thirumuraiDetails.addAll(MyApplication.repo.repoThirumurai_songs.getByThirumurai_Id_And_Pan(intent.getIntExtra("thirumurai_id", 0),
                    intent.getStringExtra("pann")));
        } else if (intent.hasExtra("country")) {
            if (intent.hasExtra("thirumurai_id")) {
                if (MyApplication.repo.repoThirumurais
                        .getById(String.valueOf(intent.getIntExtra("thirumurai_id", 0))) != null)
                    textCustomTitle.setText(MyApplication.repo.repoThirumurais
                            .getById(String.valueOf(intent.getIntExtra("thirumurai_id", 0)))
                            .getThirumuraiName());
                song_header_text.setText(intent.getStringExtra("country"));
                thirumuraiDetails.addAll(MyApplication.repo.repoThirumurai_songs
                        .getByThirumurai_Id_And_Country(intent.getIntExtra("thirumurai_id", 0),
                                intent.getStringExtra("country")));
            } else {
                textCustomTitle.setText(intent.getStringExtra("country"));
                song_header_text.setText(intent.getStringExtra("country"));
                thirumuraiDetails.addAll(MyApplication.repo.repoThirumurai_songs.getSongsBy_Country(intent.getStringExtra("country")));
            }
        } else if (intent.hasExtra("thalam")) {
            if (intent.hasExtra("thirumurai_id")) {
                if (MyApplication.repo.repoThirumurais
                        .getById(String.valueOf(intent.getIntExtra("thirumurai_id", 0))) != null)
                    textCustomTitle.setText(MyApplication.repo.repoThirumurais
                            .getById(String.valueOf(intent.getIntExtra("thirumurai_id", 0)))
                            .getThirumuraiName());
                song_header_text.setText(intent.getStringExtra("thalam"));
                thirumuraiDetails.addAll(MyApplication.repo.repoThirumurai_songs
                        .getByThirumurai_Id_And_Thalam(intent.getIntExtra("thirumurai_id", 0),
                                intent.getStringExtra("thalam")));
            } else {
                textCustomTitle.setText(intent.getStringExtra("thalam"));
                song_header_text.setText(intent.getStringExtra("thalam"));
                thirumuraiDetails.addAll(MyApplication.repo.repoThirumurai_songs.getSongsBy_Thalam(intent.getStringExtra("thalam")));
            }
        } else if (intent.hasExtra("thirumurai_id")) {
            song_header_text.setVisibility(View.GONE);
            if (MyApplication.repo.repoThirumurais
                    .getById(String.valueOf(intent.getIntExtra("thirumurai_id", 0))) != null)
                textCustomTitle.setText(MyApplication.repo.repoThirumurais
                        .getById(String.valueOf(intent.getIntExtra("thirumurai_id", 0)))
                        .getThirumuraiName());
            thirumuraiDetails.addAll(MyApplication.repo.repoThirumurai_songs.getByThirumurai_Id(intent.getIntExtra("thirumurai_id", 0)));
            if (intent.getIntExtra("thirumurai_id", 0) == 10)
                thirumuraiDetails.addAll(MyApplication.repo.repoThirumurai_songs.getByThirumurai_Id(11));

        } else if (intent.hasExtra("author")) {
            textCustomTitle.setText(intent.getStringExtra("author"));
            song_header_text.setText(intent.getStringExtra("author"));
            thirumuraiDetails.addAll(MyApplication.repo.repoThirumurai_songs.getSongsBy_Author(intent.getStringExtra("author")));
        } else
            song_header_text.setVisibility(View.GONE);
        recycle_song_list = findViewById(R.id.recycle_song_list);
        adapter = new SongsAdapter(this, thirumuraiDetails);
        recycle_song_list.setLayoutManager(new LinearLayoutManager(this));
        recycle_song_list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppConfig.customeLanguage(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
