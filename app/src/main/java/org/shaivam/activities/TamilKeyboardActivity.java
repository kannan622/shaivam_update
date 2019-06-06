package org.shaivam.activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.shaivam.R;
import org.shaivam.utils.AppConfig;
import org.shaivam.utils.MyApplication;
import org.shaivam.utils.MyContextWrapper;
import org.shaivam.utils.PrefManager;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Launch activity
 */
public class TamilKeyboardActivity extends MainActivity {
    private Toolbar toolbar;
    private Button enable_keyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tamil_keyboard);
        toolbar = findViewById(R.id.toolbar);
        TextView textCustomTitle = (TextView) findViewById(R.id.custom_title);
        Typeface customFont = Typeface.createFromAsset(this.getAssets(), AppConfig.FONT_KAVIVANAR);
        textCustomTitle.setTypeface(customFont);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        enable_keyboard = findViewById(R.id.enable_keyboard);

        enable_keyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent enableInputMethodIntent = new Intent(
                        Settings.ACTION_INPUT_METHOD_SETTINGS);
                enableInputMethodIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                enableInputMethodIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(enableInputMethodIntent);
            }
        });
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
