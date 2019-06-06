package org.shaivam.activities;

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
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.shaivam.R;
import org.shaivam.adapter.TempleAdapter;
import org.shaivam.model.Temple;
import org.shaivam.utils.AppConfig;
import org.shaivam.utils.LogUtils;
import org.shaivam.utils.MyApplication;
import org.shaivam.utils.MyContextWrapper;
import org.shaivam.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TempleListActivity extends MainActivity {
    private Toolbar toolbar;
    public static List<Temple> temples = new ArrayList<Temple>();
    private TempleAdapter adapter;
    public RecyclerView recycle_temple_list;
    AutoCompleteTextView edit_search;
    Button search_clear_btn;
    CoordinatorLayout temple_list_main;
    LinearLayout no_temple_linear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temple_list);
        ButterKnife.bind(this);
        LogUtils.amplitudeLog(this, "Temple Listing");

        toolbar = findViewById(R.id.toolbar);
        TextView textCustomTitle = (TextView) findViewById(R.id.custom_title);
        Typeface customFont = Typeface.createFromAsset(this.getAssets(), AppConfig.FONT_KAVIVANAR);
        textCustomTitle.setTypeface(customFont);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        temple_list_main = findViewById(R.id.temple_list_main);
        search_clear_btn = findViewById(R.id.search_clear_btn);
        edit_search = findViewById(R.id.edit_search);
        no_temple_linear = findViewById(R.id.no_temple_linear);
        recycle_temple_list = findViewById(R.id.recycle_temple_list);
        adapter = new TempleAdapter(this, TempleActivity.temples);
        recycle_temple_list.setLayoutManager(new LinearLayoutManager(this));
        recycle_temple_list.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (TempleActivity.temples.size() > 0) {
            recycle_temple_list.setVisibility(View.VISIBLE);
            no_temple_linear.setVisibility(View.GONE);
        } else {
            recycle_temple_list.setVisibility(View.GONE);
            no_temple_linear.setVisibility(View.VISIBLE);
        }

        edit_search.setHint(AppConfig.getTextString(this, AppConfig.search_hint));

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
                edit_search.setText("");
            }
        });
    }

    private void filter(String text) {
        List<Temple> filterdNames = new ArrayList<>();

        for (Temple s : TempleActivity.temples) {
            //if the existing elements contains the search input
            if (s.getName().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s);
            }
        }
        //calling a method of the adapter class and passing the filtered list
        if (filterdNames.size() > 0) {
            adapter.filterList(filterdNames);
            recycle_temple_list.setVisibility(View.VISIBLE);
            no_temple_linear.setVisibility(View.GONE);
        } else {
            recycle_temple_list.setVisibility(View.GONE);
            no_temple_linear.setVisibility(View.VISIBLE);
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
