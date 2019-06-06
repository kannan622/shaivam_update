package org.shaivam.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.shaivam.R;
import org.shaivam.adapter.SearchTirumuraiAdapter;
import org.shaivam.interfaces.VolleyCallback;
import org.shaivam.model.Synchronize;
import org.shaivam.model.Thirumurai_songs;
import org.shaivam.utils.AppConfig;
import org.shaivam.utils.LogUtils;
import org.shaivam.utils.MyApplication;
import org.shaivam.utils.MyContextWrapper;
import org.shaivam.utils.PrefManager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TirumuraiActivity extends MainActivity {
    private Toolbar toolbar;

    @BindView(R.id.teivaram_linear)
    ConstraintLayout teivaram_linear;
    @BindView(R.id.thiruvasam_linear)
    ConstraintLayout thiruvasam_linear;
    @BindView(R.id.thirukovaiyar_linear)
    ConstraintLayout thirukovaiyar_linear;
    @BindView(R.id.thiruvisaipa_linear)
    ConstraintLayout thiruvisaipa_linear;
    @BindView(R.id.thirumanthiram_linear)
    ConstraintLayout thirumanthiram_linear;
    @BindView(R.id.pathinoram_thirumurai_linear)
    ConstraintLayout pathinoram_thirumurai_linear;
    @BindView(R.id.periyapuranam_linear)
    ConstraintLayout periyapuranam_linear;
    @BindView(R.id.edit_search)
    AutoCompleteTextView edit_search;
    @BindView(R.id.search_recyclerView)
    RecyclerView search_recyclerView;
    @BindView(R.id.home_linear)
    LinearLayout home_linear;
    @BindView(R.id.search_clear_btn)
    Button search_clear_btn;

    @BindView(R.id.thirumurai_main)
    CoordinatorLayout thirumurai_main;
    SearchTirumuraiAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tirumurai);
        ButterKnife.bind(this);
        LogUtils.amplitudeLog(this, "Thirumurai Screen");

        toolbar = findViewById(R.id.toolbar);
        TextView textCustomTitle = (TextView) findViewById(R.id.custom_title);
        Typeface customFont = Typeface.createFromAsset(this.getAssets(), AppConfig.FONT_KAVIVANAR);
        textCustomTitle.setTypeface(customFont);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        search_recyclerView.setHasFixedSize(true);
        search_recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new SearchTirumuraiAdapter(this, "", new ArrayList<Thirumurai_songs>(), new ArrayList<String>());

        search_recyclerView.setAdapter(adapter);
        edit_search.setHint(AppConfig.getTextString(this, AppConfig.search_hint_thirumurai));
   /*     edit_search.addTextChangedListener(new TextWatcher() {
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
        });*/
        search_clear_btn.setText(AppConfig.getTextString(this, AppConfig.go));
        search_clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterGoSearch(edit_search.getText().toString());
            }
        });
        edit_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                filterGoSearch(v.getText().toString());
                return true;

            }
        });
        callDatabaseUpdate(this, false);
    }

    private void filterGoSearch(String text) {
        if (text.length() >= 3) {
            Intent intent = new Intent(TirumuraiActivity.this, ThirumuraiSearchActivity.class);
            intent.putExtra("search_text", text);
            startActivity(intent);
            edit_search.setText("");
        }else
            Toast.makeText(this, getResources().getText(R.string.search_length), Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        AppConfig.customeLanguage(this);

    }

    private void init() {
        teivaram_linear.setBackground(getResources().getDrawable(R.drawable.background_tirumurai));
        thiruvasam_linear.setBackground(getResources().getDrawable(R.drawable.background_tirumurai));
        thirukovaiyar_linear.setBackground(getResources().getDrawable(R.drawable.background_tirumurai));
        thiruvisaipa_linear.setBackground(getResources().getDrawable(R.drawable.background_tirumurai));
        thirumanthiram_linear.setBackground(getResources().getDrawable(R.drawable.background_tirumurai));
        pathinoram_thirumurai_linear.setBackground(getResources().getDrawable(R.drawable.background_tirumurai));
        periyapuranam_linear.setBackground(getResources().getDrawable(R.drawable.background_tirumurai));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            teivaram_linear.setElevation(0);
            thiruvasam_linear.setElevation(0);
            thirukovaiyar_linear.setElevation(0);
            thiruvisaipa_linear.setElevation(0);
            thirumanthiram_linear.setElevation(0);
            pathinoram_thirumurai_linear.setElevation(0);
            periyapuranam_linear.setElevation(0);
        }
    }

    @OnClick(R.id.teivaram_linear)
    public void onTeivaramClick(View view) {
        init();
        teivaram_linear.setBackground(getResources().getDrawable(R.drawable.background_button));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            teivaram_linear.setElevation(getResources().getDimension(R.dimen._4sdp));
        }
        Intent in = new Intent(this, ThirumuraiListActivity.class);
        in.putExtra("list", AppConfig.thevaram);
        startActivity(in);
    }

    @OnClick(R.id.thiruvasam_linear)
    public void onTiruvasamiClick(View view) {
        init();
        thiruvasam_linear.setBackground(getResources().getDrawable(R.drawable.background_button));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            thiruvasam_linear.setElevation(getResources().getDimension(R.dimen._4sdp));
        }
     /*   Intent in = new Intent(this, ThirumuraiListActivity.class);
        in.putExtra("list", AppConfig.thiruvasagam);
        startActivity(in);*/

        Intent intent = new Intent(this, ThirumuraiSongsListActivity.class);
        intent.putExtra("thirumurai_id", 8);
        startActivity(intent);
    }

    @OnClick(R.id.thirukovaiyar_linear)
    public void onThirukovaiyarClick(View view) {
        init();
        thirukovaiyar_linear.setBackground(getResources().getDrawable(R.drawable.background_button));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            thirukovaiyar_linear.setElevation(getResources().getDimension(R.dimen._4sdp));
        }
    /*    Intent in = new Intent(this, ThirumuraiListActivity.class);
        in.putExtra("list", AppConfig.thirukovaiyar);
        startActivity(in);*/
        Intent intent = new Intent(this, ThirumuraiSongsListActivity.class);
        intent.putExtra("thirumurai_id", 9);
        startActivity(intent);
    }

    @OnClick(R.id.thiruvisaipa_linear)
    public void onThiruvisaipaClick(View view) {
        init();
        thiruvisaipa_linear.setBackground(getResources().getDrawable(R.drawable.background_button));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            thiruvisaipa_linear.setElevation(getResources().getDimension(R.dimen._4sdp));
        }
        Intent in = new Intent(this, ThirumuraiListActivity.class);
        in.putExtra("list", AppConfig.thiruvisaipa);
        startActivity(in);
        /*Intent intent = new Intent(this, ThirumuraiSongsListActivity.class);
        intent.putExtra("thirumurai_id", 10);
        startActivity(intent);*/
    }

    @OnClick(R.id.thirumanthiram_linear)
    public void onThirumanthiramClick(View view) {
        init();
        thirumanthiram_linear.setBackground(getResources().getDrawable(R.drawable.background_button));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            thirumanthiram_linear.setElevation(getResources().getDimension(R.dimen._4sdp));
        }
       /* Intent in = new Intent(this, ThirumuraiListActivity.class);
        in.putExtra("list", AppConfig.thirumanthiram);
        startActivity(in);*/
        Intent intent = new Intent(this, ThirumuraiSongsListActivity.class);
        intent.putExtra("thirumurai_id", 12);
        startActivity(intent);
    }

    @OnClick(R.id.pathinoram_thirumurai_linear)
    public void onPathinoramTirumuraiClick(View view) {
        init();
        pathinoram_thirumurai_linear.setBackground(getResources().getDrawable(R.drawable.background_button));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            pathinoram_thirumurai_linear.setElevation(getResources().getDimension(R.dimen._4sdp));
        }
      /*  Intent in = new Intent(this, ThirumuraiListActivity.class);
        in.putExtra("list", AppConfig.prabandham);
        startActivity(in);*/
        Intent intent = new Intent(this, ThirumuraiSongsListActivity.class);
        intent.putExtra("thirumurai_id", 13);
        startActivity(intent);
    }

    @OnClick(R.id.periyapuranam_linear)
    public void onPeriyapuranamClick(View view) {
        init();
        periyapuranam_linear.setBackground(getResources().getDrawable(R.drawable.background_button));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            periyapuranam_linear.setElevation(getResources().getDimension(R.dimen._4sdp));
        }
        /*Intent in = new Intent(this, ThirumuraiListActivity.class);
        in.putExtra("list", AppConfig.periyapuranam);
        startActivity(in);*/
        Intent intent = new Intent(this, ThirumuraiSongsListActivity.class);
        intent.putExtra("thirumurai_id", 14);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (search_recyclerView.isShown()) {
            home_linear.setVisibility(View.VISIBLE);
            search_recyclerView.setVisibility(View.GONE);
            edit_search.setText("");
        } else
            super.onBackPressed();
    }
}
