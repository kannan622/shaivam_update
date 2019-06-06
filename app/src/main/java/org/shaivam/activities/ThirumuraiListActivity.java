package org.shaivam.activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.shaivam.R;
import org.shaivam.fragments.AkarathiFragment;
import org.shaivam.fragments.PanmuraiFragment;
import org.shaivam.fragments.ThalamuraiFragment;
import org.shaivam.utils.AppConfig;
import org.shaivam.utils.MyApplication;
import org.shaivam.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

public class ThirumuraiListActivity extends MainActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.ic_panmurai,
            R.drawable.ic_thalamurai,
            R.drawable.ic_akarathi};
    private int[] tabUnSelectedIcons = {
            R.drawable.ic_panmurai_unselect,
            R.drawable.ic_thalamurai_unselect,
            R.drawable.ic_akarathi_unselect};
    private int[] tabTitles = {R.string.panmurai, R.string.thalamurai, R.string.akarathi};
    private Toolbar toolbar;
    public static String selected_page = "";
CoordinatorLayout thirumurai_list_main;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thirumurai_list);
//        MyApplication.repo = new Repo(this);

        toolbar = findViewById(R.id.toolbar);
        TextView textCustomTitle = (TextView) findViewById(R.id.custom_title);
        Typeface customFont = Typeface.createFromAsset(this.getAssets(), AppConfig.FONT_KAVIVANAR);
        textCustomTitle.setTypeface(customFont);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("list")) {
            selected_page = intent.getStringExtra("list");
            textCustomTitle.setText(AppConfig.getTextString(this, selected_page));
        }

        thirumurai_list_main = findViewById(R.id.thirumurai_list_main);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(2);

        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
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

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private void setupTabIcons() {
//        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
//        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            if (i == 0) {
                final LinearLayout tabLinearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
                TextView tabContent = (TextView) tabLinearLayout.findViewById(R.id.tabContent);
                tabContent.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                tabContent.setTextColor(getResources().getColorStateList(R.color.Yellow));
                tabContent.setText("  " + getApplicationContext().getResources().getString(tabTitles[i]));
                tabContent.setCompoundDrawablesWithIntrinsicBounds(0, tabUnSelectedIcons[i], 0, 0);
                tabLayout.getTabAt(i).setCustomView(tabContent);
            } else {
                final LinearLayout tabLinearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
                TextView tabContent = (TextView) tabLinearLayout.findViewById(R.id.tabContent);
                tabContent.setBackgroundColor(getResources().getColor(R.color.background_Gray));
                tabContent.setTextColor(getResources().getColorStateList(R.color.colorPrimaryDark));
                tabContent.setText("  " + getApplicationContext().getResources().getString(tabTitles[i]));
                tabContent.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[i], 0, 0);
                tabLayout.getTabAt(i).setCustomView(tabContent);
            }
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView tabContent = (TextView) tab.getCustomView();
                tabContent.setTextColor(getResources().getColorStateList(R.color.Yellow));
                tabContent.setCompoundDrawablesWithIntrinsicBounds(0, tabUnSelectedIcons[tab.getPosition()], 0, 0);
                tabContent.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView tabContent = (TextView) tab.getCustomView();
                tabContent.setTextColor(getResources().getColorStateList(R.color.colorPrimaryDark));
                tabContent.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[tab.getPosition()], 0, 0);
                tabContent.setBackgroundColor(getResources().getColor(R.color.background_Gray));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        if (ThirumuraiListActivity.selected_page.equalsIgnoreCase(AppConfig.thevaram)) {
            adapter.addFrag(new PanmuraiFragment(), "Panmurai");
            adapter.addFrag(new ThalamuraiFragment(), "Thalamurai");
            adapter.addFrag(new AkarathiFragment(), "Akarathi");
            tabLayout.setVisibility(View.VISIBLE);
        }else {
            adapter.addFrag(new PanmuraiFragment(), "Panmurai");
            tabLayout.setVisibility(View.GONE);

//            adapter.addFrag(new AkarathiFragment(), "Akarathi");
        }
        viewPager.setAdapter(adapter);
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


}
