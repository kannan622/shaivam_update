package org.shaivam.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import org.shaivam.R;
import org.shaivam.utils.AppConfig;
import org.shaivam.utils.MyApplication;
import org.shaivam.utils.MyContextWrapper;
import org.shaivam.utils.PrefManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class WelcomeActivity extends AppCompatActivity {

    @BindView(R.id.layoutDots)
    LinearLayout layoutDots;
    @BindView(R.id.view_pager)
    ViewPager view_pager;
    @BindView(R.id.btn_next)
    Button btn_next;
    @BindView(R.id.btn_skip)
    Button btn_skip;

    private MyViewPagerAdapter myViewPagerAdapter;
    private TextView[] dots;
    private int[] layouts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        Fabric.with(this, new Crashlytics());
        // Checking for first time launch - before calling setContentView()
        if (MyApplication.prefManager.getBoolean(PrefManager.IS_FIRST_TIME_LAUNCH)) {
            launchHomeScreen();
            finish();
        }else
            MyApplication.prefManager.saveInt(PrefManager.SONG_FONT_SIZE, AppConfig.SMALL_SONG_FONT_SIZE);


        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }


        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.welcomescreen1,
                R.layout.welcomescreen2,
                R.layout.welcomescreen3};

        // adding bottom dots
        addBottomDots(0);

        // making notification bar transparent
        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        view_pager.setAdapter(myViewPagerAdapter);
        view_pager.addOnPageChangeListener(viewPagerPageChangeListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick(R.id.btn_skip)
    public void onSkipClick(View view) {
        launchHomeScreen();
    }

    @OnClick(R.id.btn_next)
    public void onNextClick(View view) {
        int current = getItem(+1);
        if (current < layouts.length) {
            // move to next screen
            view_pager.setCurrentItem(current);
        } else {
            launchHomeScreen();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        String isoCode = MyApplication.prefManager.getValue(PrefManager.LANGUAGE_ISO_CODE);
        newBase = MyContextWrapper.wrap(newBase, isoCode);
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        layoutDots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            layoutDots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return view_pager.getCurrentItem() + i;
    }

    private void launchHomeScreen() {
        MyApplication.prefManager.save(PrefManager.APP_VERSION, AppConfig.APP_VERSION);
        MyApplication.prefManager.saveBoolean(PrefManager.IS_FIRST_TIME_LAUNCH, true);
        startActivity(new Intent(WelcomeActivity.this, HomeActivity.class));
        finish();
    }

    //	viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
            if (position == layouts.length - 1) {
                btn_next.setText(getString(R.string.got_it));
                btn_skip.setVisibility(View.VISIBLE);
            } else {
                btn_next.setText(getString(R.string.next));
                btn_skip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
