package org.shaivam.activities;

import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.shaivam.R;
import org.shaivam.adapter.SearchTirumuraiAdapter;
import org.shaivam.model.Thirumurai_songs;
import org.shaivam.utils.AppConfig;
import org.shaivam.utils.MyApplication;
import org.shaivam.utils.MyContextWrapper;
import org.shaivam.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ThirumuraiSearchActivity extends MainActivity {
    private Toolbar toolbar;
    private SearchTirumuraiAdapter adapter;
    public RecyclerView recycle_thirumurai_search;
    AutoCompleteTextView edit_search;
    Button search_clear_btn;
    String search_text = "";
    CoordinatorLayout thirumurai_search_main;
    NestedScrollView nested_scroll_thirumurai_search, nested_scroll_no_thirumurai;


    public static List<Thirumurai_songs> filterdNames = new ArrayList<>();
    public static List<String> filterdTypes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thirumurai_search);
        ButterKnife.bind(this);

        toolbar = findViewById(R.id.toolbar);
        TextView textCustomTitle = (TextView) findViewById(R.id.custom_title);
        Typeface customFont = Typeface.createFromAsset(this.getAssets(), AppConfig.FONT_KAVIVANAR);
        textCustomTitle.setTypeface(customFont);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent.hasExtra("search_text")) {
            search_text = intent.getStringExtra("search_text");
        }
        search_clear_btn = findViewById(R.id.search_clear_btn);
        edit_search = findViewById(R.id.edit_search);
        recycle_thirumurai_search = findViewById(R.id.recycle_thirumurai_search);
        thirumurai_search_main = findViewById(R.id.thirumurai_search_main);
        nested_scroll_thirumurai_search = findViewById(R.id.nested_scroll_thirumurai_search);
        nested_scroll_no_thirumurai = findViewById(R.id.nested_scroll_no_thirumurai);

        search_clear_btn.setText(getResources().getText(R.string.go));
        edit_search.setHint(AppConfig.getTextString(this, AppConfig.search_hint_thirumurai));

        adapter = new SearchTirumuraiAdapter(this, search_text, filterdNames, filterdTypes);
        recycle_thirumurai_search.setLayoutManager(new LinearLayoutManager(this));
        recycle_thirumurai_search.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (filterdNames.size() > 0) {
            nested_scroll_thirumurai_search.setVisibility(View.VISIBLE);
            nested_scroll_no_thirumurai.setVisibility(View.GONE);
        } else {
            nested_scroll_thirumurai_search.setVisibility(View.GONE);
            nested_scroll_no_thirumurai.setVisibility(View.VISIBLE);
        }

        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
                filter(editable.toString());
            }
        });
        search_clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_text = edit_search.getText().toString();
                filterGoSearch(search_text);
            }
        });

        edit_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                search_text = edit_search.getText().toString();
                filterGoSearch(search_text);
                return true;

            }
        });

        filterGoSearch(search_text);
    }

    private void filterGoSearch(String text) {
        if (text.trim().length() > 0) {
            AppConfig.showProgDialiog(this);
            filterdNames.clear();
            filterdTypes.clear();

            for (Thirumurai_songs s : MyApplication.repo.repoThirumurai_songs.getSongsBySearchKeyByThalam(text)) {
                //if the existing elements contains the search input
                if (s.getThalam().toLowerCase().contains(text.toLowerCase())) {
                    //adding the element to filtered list
                    filterdNames.add(s);
                    filterdTypes.add(AppConfig.getTextString(this, AppConfig.thalam));
                }
            }
            for (Thirumurai_songs s : MyApplication.repo.repoThirumurai_songs.getSongsBySearchKeyByPann(text)) {
                //if the existing elements contains the search input
                if (s.getPann().toLowerCase().contains(text.toLowerCase())) {
                    //adding the element to filtered list
                    filterdNames.add(s);
                    filterdTypes.add(AppConfig.getTextString(this, AppConfig.pann));
                }
            }
            for (Thirumurai_songs s : MyApplication.repo.repoThirumurai_songs.getSongsBySearchKeyByCountry(text)) {
                //if the existing elements contains the search input
                if (s.getCountry().toLowerCase().contains(text.toLowerCase())) {
                    //adding the element to filtered list
                    filterdNames.add(s);
                    filterdTypes.add(AppConfig.getTextString(this, AppConfig.nadu));
                }
            }
            for (Thirumurai_songs s : MyApplication.repo.repoThirumurai_songs.getSongsBySearchKeyByAuthor(text)) {
                //if the existing elements contains the search input
                if (s.getAuthor().toLowerCase().contains(text.toLowerCase())) {
                    //adding the element to filtered list
                    filterdNames.add(s);
                    filterdTypes.add(AppConfig.getTextString(this, AppConfig.aruliyavar));
                }
            }
            for (Thirumurai_songs s : MyApplication.repo.repoThirumurai_songs.getSongsBySearchKeyByTitle(text)) {
                //if the existing elements contains the search input
                if (s.getTitle().toLowerCase().contains(text.toLowerCase())) {
                    //adding the element to filtered list
                    filterdNames.add(s);
                    filterdTypes.add(AppConfig.getTextString(this, AppConfig.paadal));
                }
            }
            for (Thirumurai_songs s : MyApplication.repo.repoThirumurai_songs.getSongsBySearchKeyBySongName(text)) {
                //if the existing elements contains the search input
                if (s.getSong().toLowerCase().contains(text.toLowerCase())) {
                    //adding the element to filtered list
                    filterdNames.add(s);
                    filterdTypes.add(AppConfig.getTextString(this, AppConfig.paadal_varigal));
                }
            }
            //calling a method of the adapter class and passing the filtered list
            if (filterdNames.size() > 0 && filterdTypes.size() > 0) {
                adapter.filterList(search_text, filterdNames, filterdTypes);
                AppConfig.hideProgDialog();
                nested_scroll_thirumurai_search.setVisibility(View.VISIBLE);
                nested_scroll_no_thirumurai.setVisibility(View.GONE);
            } else {
                AppConfig.hideProgDialog();
                Toast.makeText(this, AppConfig.getTextString(this, AppConfig.no_thirumurai), Toast.LENGTH_SHORT).show();
                nested_scroll_thirumurai_search.setVisibility(View.GONE);
                nested_scroll_no_thirumurai.setVisibility(View.VISIBLE);
            }
        }
    }

    private void filter(String text) {
        List<Thirumurai_songs> filterdNames = new ArrayList<>();
        List<String> filterdTypes = new ArrayList<>();

        for (Thirumurai_songs s :filterdNames) {
            //if the existing elements contains the search input
            if (s.getTitle() != null && s.getTitle().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s);
                filterdTypes.add(filterdTypes.get(filterdNames.indexOf(s)));
            }
        }
        //calling a method of the adapter class and passing the filtered list
        if (filterdNames.size() >= 0) {
            adapter.filterList(search_text, filterdNames, filterdTypes);
            nested_scroll_thirumurai_search.setVisibility(View.VISIBLE);
            nested_scroll_no_thirumurai.setVisibility(View.GONE);
        } else {
            nested_scroll_thirumurai_search.setVisibility(View.GONE);
            nested_scroll_no_thirumurai.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppConfig.customeLanguage(this);

    }
}
