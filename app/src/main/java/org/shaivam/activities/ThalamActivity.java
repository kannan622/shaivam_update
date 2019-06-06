package org.shaivam.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.CoordinatorLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.shaivam.R;
import org.shaivam.adapter.ThalamChildAdapter;
import org.shaivam.adapter.ThalamParentAdapter;
import org.shaivam.model.Thirumurai;
import org.shaivam.model.Thirumurai_songs;
import org.shaivam.utils.AppConfig;
import org.shaivam.utils.MyApplication;
import org.shaivam.utils.SnappingLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class ThalamActivity extends MainActivity {
    private Toolbar toolbar;
    private ThalamChildAdapter adapter;
    public RecyclerView recycle_thalam_list;
    List<Thirumurai_songs> thirumuraiList = new ArrayList<Thirumurai_songs>();

//    List<Thirumurai> thirumuraiList = new ArrayList<Thirumurai>();
    TextView thalam_header_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thalam);
        toolbar = findViewById(R.id.toolbar);
        TextView textCustomTitle = (TextView) findViewById(R.id.custom_title);
        Typeface customFont = Typeface.createFromAsset(this.getAssets(), AppConfig.FONT_KAVIVANAR);
        textCustomTitle.setTypeface(customFont);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        thalam_header_text = findViewById(R.id.thalam_header_text);
/*
        thirumuraiList.clear();
        if (ThirumuraiListActivity.selected_page.equalsIgnoreCase(AppConfig.thevaram)) {
            thirumuraiList.add(MyApplication.repo.repoThirumurais.getById("1"));
            thirumuraiList.add(MyApplication.repo.repoThirumurais.getById("2"));
            thirumuraiList.add(MyApplication.repo.repoThirumurais.getById("3"));
            thirumuraiList.add(MyApplication.repo.repoThirumurais.getById("4"));
            thirumuraiList.add(MyApplication.repo.repoThirumurais.getById("5"));
            thirumuraiList.add(MyApplication.repo.repoThirumurais.getById("6"));
            thirumuraiList.add(MyApplication.repo.repoThirumurais.getById("7"));
        } else if (ThirumuraiListActivity.selected_page.equalsIgnoreCase(AppConfig.thiruvasagam)) {
            thirumuraiList.add(MyApplication.repo.repoThirumurais.getById("8"));
        } else if (ThirumuraiListActivity.selected_page.equalsIgnoreCase(AppConfig.thirukovaiyar)) {
            thirumuraiList.add(MyApplication.repo.repoThirumurais.getById("9"));
        } else if (ThirumuraiListActivity.selected_page.equalsIgnoreCase(AppConfig.thiruvisaipa)) {
            thirumuraiList.add(MyApplication.repo.repoThirumurais.getById("10"));
            thirumuraiList.add(MyApplication.repo.repoThirumurais.getById("11"));
        } else if (ThirumuraiListActivity.selected_page.equalsIgnoreCase(AppConfig.thirumanthiram)) {
            thirumuraiList.add(MyApplication.repo.repoThirumurais.getById("12"));
        } else if (ThirumuraiListActivity.selected_page.equalsIgnoreCase(AppConfig.prabandham)) {
            thirumuraiList.add(MyApplication.repo.repoThirumurais.getById("13"));
        } else if (ThirumuraiListActivity.selected_page.equalsIgnoreCase(AppConfig.periyapuranam)) {
            thirumuraiList.add(MyApplication.repo.repoThirumurais.getById("14"));
        }*/
        Intent intent = getIntent();
        if (intent.hasExtra("country")) {
            thalam_header_text.setText(intent.getStringExtra("country"));
            thirumuraiList.clear();
            List<Thirumurai_songs> thirumurai_songs = MyApplication.repo.repoThirumurai_songs
                    .getThalamByCountry(intent.getStringExtra("country"));
            for (int i = 0; i < thirumurai_songs.size(); i++) {
                if (thirumurai_songs.get(i).getThirumuraiId() <= 7)
                    if (!thirumurai_songs.get(i).getThalam().equalsIgnoreCase(""))
                        thirumuraiList.add(thirumurai_songs.get(i));
            }
        }else {
            thalam_header_text.setVisibility(View.GONE);
            thirumuraiList.clear();
            if (ThirumuraiListActivity.selected_page.equalsIgnoreCase(AppConfig.thevaram)) {
                List<Thirumurai_songs> thirumurai_songs = MyApplication.repo.repoThirumurai_songs
                        .getThalam(7);
                for (int i = 0; i < thirumurai_songs.size(); i++) {
                    if (thirumurai_songs.get(i).getThirumuraiId() <= 7)
                        if (!thirumurai_songs.get(i).getThalam().equalsIgnoreCase(""))
                            thirumuraiList.add(thirumurai_songs.get(i));
                }
            } else if (ThirumuraiListActivity.selected_page.equalsIgnoreCase(AppConfig.thiruvisaipa)) {
                List<Thirumurai_songs> thirumurai_songs = MyApplication.repo.repoThirumurai_songs
                        .getThalam(11);
                for (int i = 0; i < thirumurai_songs.size(); i++) {
                    if (thirumurai_songs.get(i).getThirumuraiId() > 9 && thirumurai_songs.get(i).getThirumuraiId() <= 11)
                        if (!thirumurai_songs.get(i).getThalam().equalsIgnoreCase(""))
                            thirumuraiList.add(thirumurai_songs.get(i));
                }
            }
        }
        recycle_thalam_list = findViewById(R.id.recycle_thalam_list);
        adapter = new ThalamChildAdapter(this, thirumuraiList);
        recycle_thalam_list.setLayoutManager(new SnappingLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycle_thalam_list.setNestedScrollingEnabled(false);
        recycle_thalam_list.setAdapter(adapter);
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
