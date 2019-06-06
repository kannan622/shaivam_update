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
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.shaivam.R;
import org.shaivam.utils.AppConfig;
import org.shaivam.utils.MyApplication;
import org.shaivam.utils.MyContextWrapper;
import org.shaivam.utils.PrefManager;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ContactUsActivity extends MainActivity {
    private Toolbar toolbar;
    TextView email_text, website_address;
    LinearLayout contact_us_linear, website_linear;
    CoordinatorLayout contact_us_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        toolbar = findViewById(R.id.toolbar);
        TextView textCustomTitle = (TextView) findViewById(R.id.custom_title);
        Typeface customFont = Typeface.createFromAsset(this.getAssets(), AppConfig.FONT_KAVIVANAR);
        textCustomTitle.setTypeface(customFont);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        contact_us_main = findViewById(R.id.contact_us_main);
        email_text = findViewById(R.id.email_text);
        contact_us_linear = findViewById(R.id.contact_us_linear);
        website_address = findViewById(R.id.website_address);
        website_linear = findViewById(R.id.website_linear);

        email_text.setText(AppConfig.ORG_EMAIL_ID);
        contact_us_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", AppConfig.ORG_EMAIL_ID, null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, AppConfig.ORG_EMAIL_SUBJECT_CONTACT);
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });

        website_address.setText(AppConfig.URL_HOME_WEB);
        website_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppConfig.isConnectedToInternet(ContactUsActivity.this)) {
                    Intent intent = new Intent(ContactUsActivity.this, WebViewActivity.class);
                    intent.putExtra("url", AppConfig.URL_HOME_WEB);
                    startActivity(intent);
                } else
                    snackBar( AppConfig.getTextString(ContactUsActivity.this, AppConfig.connection_message));

            }
        });
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
