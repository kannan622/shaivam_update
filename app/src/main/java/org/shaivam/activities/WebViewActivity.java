package org.shaivam.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.crashlytics.android.Crashlytics;

import org.shaivam.R;
import org.shaivam.utils.AppConfig;
import org.shaivam.utils.LogUtils;
import org.shaivam.utils.WebViewClientImpl;

import io.fabric.sdk.android.Fabric;

public class WebViewActivity extends AppCompatActivity {
    private WebView webView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        Bundle bundle = getIntent().getExtras();
        Fabric.with(this, new Crashlytics());

        if(getIntent()!=null && getIntent().hasExtra("page")){
            if(getIntent().getStringExtra("page").equalsIgnoreCase("Site Search"))
            LogUtils.amplitudeLog(this, "Site Search");
        }
        this.webView = (WebView) findViewById(R.id.webview);
        AppConfig.showProgDialiog(this);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        this.webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    AppConfig.hideProgDialog();
                }
            }
        });
        WebViewClientImpl webViewClient = new WebViewClientImpl(this);
        webView.setWebViewClient(webViewClient);

        webView.loadUrl(bundle.getString("url"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppConfig.customeLanguage(this);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.webView.canGoBack()) {
            this.webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}