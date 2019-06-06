package org.shaivam.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.shaivam.R;
import org.shaivam.utils.AppConfig;

import static android.app.Activity.RESULT_OK;

public class AddEventFragment extends Fragment {

     WebView mywebview;
    private ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> uploadMessage;
    public static final int REQUEST_SELECT_FILE = 100;
    private final static int FILECHOOSER_RESULTCODE = 1;

    public AddEventFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_events, container, false);

        mywebview = view.findViewById(R.id.webview);
        // mywebview.loadUrl("http://www.javatpoint.com/");

        /*
         * String data =
         * "<html><body><h1>Hello, Javatpoint!</h1></body></html>";
         * mywebview.loadData(data, "text/html", "UTF-8");
         */

        // http://shaivam.org/temples-special

        WebSettings webSettings = mywebview.getSettings();
        webSettings.setBuiltInZoomControls(true);
        mywebview.getSettings().setJavaScriptEnabled(true);

        //mywebview.loadUrl("http://shaivam.org/temples-of-lord-shiva/lord-shiva-temples-of-chennai-district");
        //	mywebview.loadUrl("https://shaivam.org/temples-special");

        mywebview.loadUrl("https://shaivam.org/eventpage.php");

        mywebview.getSettings().setBuiltInZoomControls(false);
        mywebview.setVerticalScrollBarEnabled(false);
        mywebview.clearCache(true);
        mywebview.setFocusable(true);
        mywebview.setHorizontalScrollBarEnabled(false);
        mywebview.getSettings().setPluginState(WebSettings.PluginState.ON);
        mywebview.getSettings().setJavaScriptEnabled(true);
        mywebview.getSettings().setAppCacheEnabled(true);
        mywebview.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        mywebview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mywebview.getSettings().setAllowFileAccess(true);
        mywebview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mywebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        // mywebview.loadDataWithBaseURL("", "hihi", "text/html","utf-8", "");

        mywebview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView viewx, String urlx) {
                viewx.loadUrl(urlx);
                return false;
            }
        });


        mywebview.setWebChromeClient(new WebChromeClient() {
            // For 3.0+ Devices (Start)
            // onActivityResult attached before constructor
            @SuppressWarnings("unused")
            protected void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
            }


            // For Lollipop 5.0+ Devices
            public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(null);
                    uploadMessage = null;
                }

                uploadMessage = filePathCallback;

                Intent intent = fileChooserParams.createIntent();
                try {
                    startActivityForResult(intent, REQUEST_SELECT_FILE);
                } catch (ActivityNotFoundException e) {
                    uploadMessage = null;
                    Toast.makeText(getContext(), AppConfig.getTextString(getContext(), AppConfig.cannot_open_file), Toast.LENGTH_LONG).show();
                    return false;
                }
                return true;
            }
            });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode == REQUEST_SELECT_FILE) {
                if (uploadMessage == null)
                    return;
                uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
                uploadMessage = null;
            }
        } else if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            // Use MainActivity.RESULT_OK if you're implementing WebView inside Fragment
            // Use RESULT_OK only if you're implementing WebView inside an Activity
            Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        } else
            Toast.makeText(getContext(),  AppConfig.getTextString(getContext(), AppConfig.upload_failed), Toast.LENGTH_LONG).show();
    }
}