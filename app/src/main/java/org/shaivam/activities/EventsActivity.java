package org.shaivam.activities;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.shaivam.R;
import org.shaivam.fragments.AddEventFragment;
import org.shaivam.fragments.CalendarFragment;
import org.shaivam.fragments.EventsFragment;
import org.shaivam.utils.AppConfig;
import org.shaivam.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class EventsActivity extends MainActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CoordinatorLayout event_main;

    private int[] tabIcons = {
            R.drawable.ic_tab_calendar,
            R.drawable.ic_tab_events,  R.drawable.ic_add_event_red};
    private int[] tabUnSelectedIcons = {
            R.drawable.ic_tab_calendar_unselect,
            R.drawable.ic_tab_events_unselect, R.drawable.ic_add_event_yellow};
    private int[] tabTitles = {R.string.calander, R.string.event, R.string.addevents};

    public static String textViewDatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        ButterKnife.bind(this);
        toolbar = findViewById(R.id.toolbar);
        LogUtils.amplitudeLog(this, "Calendar and Events Screen");

        TextView textCustomTitle = (TextView) findViewById(R.id.custom_title);
        Typeface customFont = Typeface.createFromAsset(this.getAssets(), AppConfig.FONT_KAVIVANAR);
        textCustomTitle.setTypeface(customFont);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        event_main = findViewById(R.id.event_main);
        viewPager = findViewById(R.id.view_pager);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(2);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        AppConfig.hideKeyboard(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
    @Override
    protected void onResume() {
        super.onResume();
        AppConfig.customeLanguage(this);
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new CalendarFragment(), getResources().getString(R.string.calander));
        adapter.addFrag(new EventsFragment(), getResources().getString(R.string.event));
        adapter.addFrag(new AddEventFragment(), getResources().getString(R.string.addevents));
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
