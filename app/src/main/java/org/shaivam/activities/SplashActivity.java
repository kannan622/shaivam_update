package org.shaivam.activities;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.messaging.FirebaseMessaging;

import org.shaivam.R;
import org.shaivam.utils.AppConfig;
import org.shaivam.utils.MyApplication;
import org.shaivam.utils.NotificationUtils;
import org.shaivam.utils.PrefManager;

import io.fabric.sdk.android.Fabric;

/**
 * Created by root on 30/8/16.
 */

public class SplashActivity extends AppCompatActivity {
    protected int _splashTime = 2000;
    Context mContext;

    static Dialog dialog;

    private static final String TAG = SplashActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = SplashActivity.this;
        Fabric.with(this, new Crashlytics());

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(mContext, WelcomeActivity.class));
                finish();
            }
        }, secondsDelayed * _splashTime);
        dialog = new Dialog(this);

        mRegistrationBroadcastReceiver = new

                BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        // checking for type intent filter
                        if (intent.getAction().equals(AppConfig.REGISTRATION_COMPLETE)) {
                            // gcm successfully registered
                            // now subscribe to `global` topic to receive app wide notifications
                            FirebaseMessaging.getInstance().subscribeToTopic(AppConfig.TOPIC_GLOBAL);
                        } else if (intent.getAction().equals(AppConfig.PUSH_NOTIFICATION)) {
                            // new push notification is received
                          /*  String message = intent.getStringExtra("message");
                            String title = intent.getStringExtra("title");
                            String imageUrl = intent.getStringExtra("imageUrl");
                            String categoryurl = intent.getStringExtra("categoryurl");

                            AppConfig.customNotificationAlert(dialog, SplashActivity.this, title, message, imageUrl, categoryurl);
                   */     }
                    }
                }

        ;
        changeStatusBarColor();

    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


        if (dialog == null)
            dialog = new Dialog(this);

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(AppConfig.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(AppConfig.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

}

