package org.shaivam.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;
import org.shaivam.R;
import org.shaivam.interfaces.VolleyCallback;
import org.shaivam.model.Synchronize;
import org.shaivam.utils.AppConfig;
import org.shaivam.utils.LogUtils;
import org.shaivam.utils.MyApplication;
import org.shaivam.utils.NotificationUtils;
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
import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HomeActivity extends AppCompatActivity {

  @BindView(R.id.tirumurai_text)
  TextView tirumurai_text;
  @BindView(R.id.temple_text)
  TextView temple_text;
  @BindView(R.id.radio_text)
  TextView radio_text;
  @BindView(R.id.events_text)
  TextView events_text;
  @BindView(R.id.sixtimepooja_text)
  TextView sixtimepooja_text;
  @BindView(R.id.search_text)
  TextView search_text;
  @BindView(R.id.quiz_text)
  TextView quiz_text;
  @BindView(R.id.drawer_layout)
  DrawerLayout drawer;

  private ProgressDialog pDialog;
  public static final int progress_bar_type = 0;

  public static Boolean isLanguageClicked = false;

  public static List<Synchronize> synchronizeList = new ArrayList<Synchronize>();

  private BroadcastReceiver mRegistrationBroadcastReceiver;
  static Dialog dialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    ButterKnife.bind(this);
    Fabric.with(this, new Crashlytics());

    LogUtils.amplitudeLog(this, "Dashboard Screen");

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    toolbar.setTitle("");
    dialog = new Dialog(this);

    drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.setDrawerListener(toggle);
    toggle.syncState();
    toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setBackgroundColor(Color.TRANSPARENT);
    setSupportActionBar(toolbar);
    findViewById(R.id.appBarLayout).bringToFront();
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        callDatabaseUpdate();

    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    View header = navigationView.inflateHeaderView(R.layout.navigation_header_home);
    LinearLayout navigation_header = header.findViewById(R.id.navigation_header);
    navigation_header.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (AppConfig.isConnectedToInternet(HomeActivity.this)) {
          Intent intent = new Intent(HomeActivity.this, WebViewActivity.class);
          intent.putExtra("url", AppConfig.URL_HOME_WEB);
          startActivity(intent);
        } else
          snackBar(HomeActivity.this, AppConfig.getTextString(HomeActivity.this, AppConfig.connection_message));

      }
    });
    navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_contact) {
          Intent intent = new Intent(HomeActivity.this, ContactUsActivity.class);
          startActivity(intent);
        } else if (id == R.id.nav_search_site) {
          if (AppConfig.isConnectedToInternet(HomeActivity.this)) {
            Intent intent = new Intent(HomeActivity.this, WebViewActivity.class);
            intent.putExtra("url", AppConfig.URL_SITE_SEARCH);
            intent.putExtra("page", "Site Search");
            startActivity(intent);
          } else
            snackBar(HomeActivity.this, AppConfig.getTextString(HomeActivity.this, AppConfig.connection_message));
        } else if (id == R.id.nav_keyboard) {
          Intent intent = new Intent(HomeActivity.this, TamilKeyboardActivity.class);
          startActivity(intent);
        } else if (id == R.id.nav_my_favorites) {
          Intent intent = new Intent(HomeActivity.this, MyFavouritesActivity.class);
          startActivity(intent);
        } else if (id == R.id.nav_database_update) {
          callDatabaseUpdate();
        } else if (id == R.id.nav_language) {
          final Intent in = new Intent(HomeActivity.this, HomeActivity.class);

          android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(
              HomeActivity.this);
          alertDialog.setTitle("Shaivam.org");
          alertDialog.setIcon(R.mipmap.ic_launcher);
          alertDialog.setMessage(Html
              .fromHtml("<font color='#720000'>" + AppConfig.getTextString(HomeActivity.this, AppConfig.choose_language) + "</font>"));
          alertDialog.setPositiveButton("தமிழ்",
              new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                  isLanguageClicked = true;
                  Toast.makeText(getApplicationContext(), AppConfig.getTextString(getApplicationContext(), AppConfig.select_tamil),
                      Toast.LENGTH_SHORT).show();
                  MyApplication.prefManager.save(PrefManager.LANGUAGE_ISO_CODE, AppConfig.LANG_TN);
                  startActivity(in);
                }
              });
          alertDialog.setNegativeButton("English",
              new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                  isLanguageClicked = true;
                  Toast.makeText(getApplicationContext(), AppConfig.getTextString(getApplicationContext(), AppConfig.select_english),
                      Toast.LENGTH_SHORT).show();
                  MyApplication.prefManager.save(PrefManager.LANGUAGE_ISO_CODE, AppConfig.LANG_EN);
                  startActivity(in);
                }
              });
          alertDialog.show();
          return true;

        } else if (id == R.id.nav_about_app) {
          Intent intent = new Intent(HomeActivity.this, AboutAppActivity.class);
          startActivity(intent);
        } else if (id == R.id.nav_shaivam) {
          if (AppConfig.isConnectedToInternet(HomeActivity.this)) {
            Intent intent = new Intent(HomeActivity.this, WebViewActivity.class);
            intent.putExtra("url", AppConfig.URL_HOME_WEB);
            startActivity(intent);
          } else
            snackBar(HomeActivity.this, AppConfig.getTextString(HomeActivity.this, AppConfig.connection_message));
        } else if (id == R.id.nav_share) {
          if (AppConfig.isConnectedToInternet(HomeActivity.this)) {
            try {
              Intent i = new Intent(Intent.ACTION_SEND);
              i.setType("text/plain");
              i.putExtra(Intent.EXTRA_SUBJECT, "My application name");
              String sAux = "I am using Shaivam.org mobile app. I am sure you will find this useful. Please install using this link below.\n\n";
              sAux = sAux + "https://play.google.com/store/apps/details?id=" + getPackageName();
              i.putExtra(Intent.EXTRA_TEXT, sAux);
              startActivity(Intent.createChooser(i, "choose one"));
            } catch (Exception e) {
              e.printStackTrace();
            }
          } else
            snackBar(HomeActivity.this, AppConfig.getTextString(HomeActivity.this, AppConfig.connection_message));
        } else if (id == R.id.nav_rating) {
          if (AppConfig.isConnectedToInternet(HomeActivity.this)) {
            Uri uri = Uri.parse("market://details?id=" + getPackageName());

            Log.d("URI", getPackageName().toString());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back
            // button,
            // to taken back to our application, we need to add following
            // flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY
                | Intent.FLAG_ACTIVITY_NEW_DOCUMENT
                | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
              startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
              Log.d("PACKAGE", getPackageName());
              startActivity(new Intent(
                  Intent.ACTION_VIEW,
                  Uri.parse("http://play.google.com/store/apps/details?id="
                      + getPackageName())));

            }

            return true;

          } else
            snackBar(HomeActivity.this, AppConfig.getTextString(HomeActivity.this, AppConfig.connection_message));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.END);
        return true;
      }
    });
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
                       /*     // new push notification is received
                            String message = intent.getStringExtra("message");
                            String title = intent.getStringExtra("title");
                            String imageUrl = intent.getStringExtra("imageUrl");
                            String categoryurl = intent.getStringExtra("categoryurl");

                            AppConfig.customNotificationAlert(dialog, HomeActivity.this, title, message, imageUrl, categoryurl);
                      */
            }
          }
        }

    ;

    if (!MyApplication.prefManager.getBoolean(PrefManager.LANGUAGE_SELECTED_PREF)) {
      final Intent in = new Intent(HomeActivity.this, HomeActivity.class);

      android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(
          HomeActivity.this);
      alertDialog.setTitle("Shaivam.org");
      alertDialog.setIcon(R.mipmap.ic_launcher);
      alertDialog.setCancelable(false);
      alertDialog.setMessage(Html
          .fromHtml("<font color='#720000'>" + AppConfig.getTextString(HomeActivity.this, AppConfig.app_Language) + "</font>"));
      alertDialog.setPositiveButton("தமிழ்",
          new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
              isLanguageClicked = true;
              Toast.makeText(getApplicationContext(), AppConfig.getTextString(getApplicationContext(), AppConfig.select_tamil),
                  Toast.LENGTH_SHORT).show();
              MyApplication.prefManager.save(PrefManager.LANGUAGE_ISO_CODE, AppConfig.LANG_TN);
              startActivity(in);
              MyApplication.prefManager.saveBoolean(PrefManager.LANGUAGE_SELECTED_PREF, true);
            }
          });
      alertDialog.setNegativeButton("English",
          new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
              isLanguageClicked = true;
              Toast.makeText(getApplicationContext(), AppConfig.getTextString(getApplicationContext(), AppConfig.select_english),
                  Toast.LENGTH_SHORT).show();
              MyApplication.prefManager.save(PrefManager.LANGUAGE_ISO_CODE, AppConfig.LANG_EN);
              startActivity(in);
              MyApplication.prefManager.saveBoolean(PrefManager.LANGUAGE_SELECTED_PREF, true);
            }
          });
      alertDialog.show();
    } else
      callVersionUpdate();
  }

  private void callDatabaseUpdate() {
    if (AppConfig.isConnectedToInternet(MyApplication.getInstance())) {
      AppConfig.showProgDialiog(HomeActivity.this);
      MyApplication.gsonVolley.Request(MyApplication.getInstance(), AppConfig.URL_BASE, AppConfig.URL_SYNCHRONIZE,
          null, synchronizeCallBack, com.android.volley.Request.Method.POST);
    } else {
      HomeActivity.snackBar(HomeActivity.this, AppConfig.connection_message);
    }
  }

  VolleyCallback synchronizeCallBack = new VolleyCallback() {
    @Override
    public void Success(int stauscode, String response) {
      try {
        JSONObject jObj = new JSONObject(response);
        if (jObj.has("Status") && jObj.getString("Status").equalsIgnoreCase("true")) {
          synchronizeList.clear();
          List<Synchronize> data = Arrays.asList(MyApplication.gson.fromJson(jObj.getJSONArray("data").toString(), Synchronize[].class));
          synchronizeList.addAll(data);
          if (synchronizeList.size() > 0) {
            final int version = synchronizeList.get(0).getVersion();
            if (MyApplication.prefManager.getInt(PrefManager.DATABASE_VERSION,
                AppConfig.DATABASE_VERSION) != version) {
              android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(
                  HomeActivity.this);
              alertDialog.setTitle("Shaivam.org");
              alertDialog.setIcon(R.mipmap.ic_launcher);
              alertDialog.setMessage(Html
                  .fromHtml("<font color='#720000'>" + AppConfig.getTextString(HomeActivity.this, AppConfig.need_database_update) +
                      "</font>"));
              alertDialog.setPositiveButton("Yes",
                  new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                      new DownloadFileFromURL(version).execute(synchronizeList.get(0).getUrl());
                    }
                  });
              alertDialog.setNegativeButton("No",
                  new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                      dialog.cancel();
                    }
                  });
              alertDialog.show();
            } else
              Toast.makeText(HomeActivity.this, AppConfig.getTextString(HomeActivity.this,
                  AppConfig.database_already_update), Toast.LENGTH_LONG).show();
          }
        } else {
          snackBar(HomeActivity.this, jObj.getString("Message"));
        }
      } catch (Exception e) {
        e.printStackTrace();
        snackBar(HomeActivity.this, AppConfig.went_wrong);
      }
    }

    @Override
    public void Failure(int stauscode, String errorResponse) {
      snackBar(HomeActivity.this, errorResponse);
    }
  };

  private void callVersionUpdate() {
    if (AppConfig.isConnectedToInternet(MyApplication.getInstance())) {
//            AppConfig.showProgDialiog(HomeActivity.this);
      MyApplication.gsonVolley.Request(MyApplication.getInstance(), AppConfig.URL_BASE, AppConfig.URL_VERSION,
          MyApplication.jsonHelper.createVersionJson(), versionCallBack, com.android.volley.Request.Method.POST);
    } else {
      HomeActivity.snackBar(HomeActivity.this, AppConfig.connection_message);
    }
  }

  VolleyCallback versionCallBack = new VolleyCallback() {
    @Override
    public void Success(int stauscode, String response) {
      try {
        JSONObject jObj = new JSONObject(response);
        if (jObj.has("Status") && jObj.getString("Status").equalsIgnoreCase("true"))
          Toast.makeText(HomeActivity.this, jObj.getString("data"), Toast.LENGTH_SHORT).show();
        else {
          AppConfig.customAlertPlaystore(dialog, HomeActivity.this, getResources().getText(R.string.app_name).toString(),
              jObj.getString("data"));
        }
      } catch (Exception e) {
        e.printStackTrace();
        snackBar(HomeActivity.this, AppConfig.went_wrong);
      }
    }

    @Override
    public void Failure(int stauscode, String errorResponse) {
      snackBar(HomeActivity.this, errorResponse);
    }
  };


  public static void snackBar(Context context, String response) {
    final Snackbar snackbar = Snackbar.make(((HomeActivity) context).drawer, response, Snackbar.LENGTH_SHORT);
    AppConfig.createSnackBarView(context, snackbar);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    if (item.getItemId() == android.R.id.home) {
      if (drawer.isDrawerOpen(Gravity.END)) {
        drawer.closeDrawer(Gravity.END);
      } else {
        drawer.openDrawer(Gravity.END);
      }

    }
    return super.onOptionsItemSelected(item);
  }


  @Override
  protected void onResume() {
    super.onResume();
    if (isLanguageClicked) {
      isLanguageClicked = false;
      super.recreate();
    }
    AppConfig.customeLanguage(this);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      getWindow().setStatusBarColor(Color.WHITE);
      getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    Typeface tf = Typeface.createFromAsset(getAssets(), AppConfig.FONT_KAVIVANAR);
    tirumurai_text.setTypeface(tf);
    tirumurai_text.setText(AppConfig.getTextString(this, AppConfig.thiru_head));

    temple_text.setTypeface(tf);
    temple_text.setText(AppConfig.getTextString(this, AppConfig.temple));

    radio_text.setTypeface(tf);
    radio_text.setText(AppConfig.getTextString(this, AppConfig.nayanmargal));

    events_text.setTypeface(tf);
    events_text.setText(AppConfig.getTextString(this, AppConfig.calander));

    sixtimepooja_text.setTypeface(tf);
    sixtimepooja_text.setText(AppConfig.getTextString(this, AppConfig.others));

    quiz_text.setTypeface(tf);
    quiz_text.setText(AppConfig.getTextString(this, AppConfig.quiz));

    search_text.setTypeface(tf);
    search_text.setText(AppConfig.getTextString(this, AppConfig.search));

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
  protected void onPause() {
    LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    super.onPause();
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
  protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
  }

  @Override
  public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.END)) {
      drawer.closeDrawer(GravityCompat.END);
    } else {
      AppConfig.onApplicationExit(this);
    }
  }

  @OnClick(R.id.tirumurai_linear)
  public void onTirumuraiClick(View view) {
    Intent in = new Intent(this, TirumuraiActivity.class);
    startActivity(in);
  }


  @OnClick(R.id.temple_linear)
  public void onTempleClick(View view) {
    Intent in = new Intent(this, TempleActivity.class);
    startActivity(in);
  }


  @OnClick(R.id.radio_linear)
  public void onRadioClick(View view) {
    Intent in = new Intent(this, RadioActivity.class);
    startActivity(in);
  }


  @OnClick(R.id.events_linear)
  public void onEventsClick(View view) {
    Intent in = new Intent(this, EventsActivity.class);
    startActivity(in);
  }


  @OnClick(R.id.sixtimepooja_linear)
  public void onSixTimePoojaClick(View view) {
    Intent in = new Intent(this, SixTimePoojaActivity.class);
    startActivity(in);
  }


  @OnClick(R.id.search_linear)
  public void onSearchClick(View view) {
    if (AppConfig.isConnectedToInternet(HomeActivity.this)) {
      Intent intent = new Intent(HomeActivity.this, WebViewActivity.class);
      intent.putExtra("url", AppConfig.URL_SITE_SEARCH);
      intent.putExtra("page", "Site Search");
      startActivity(intent);
    } else
      snackBar(HomeActivity.this, AppConfig.getTextString(HomeActivity.this, AppConfig.connection_message));
  }

  @OnClick(R.id.quiz_linear)
  public void onQuizClick(View view) {
    if (AppConfig.isConnectedToInternet(HomeActivity.this)) {
      Intent in = new Intent(HomeActivity.this, QuizActivity.class);
      startActivity(in);
    } else
      snackBar(HomeActivity.this, AppConfig.getTextString(HomeActivity.this, AppConfig.connection_message));
  }

  /**
   * Showing Dialog
   */

  @Override
  protected Dialog onCreateDialog(int id) {
    switch (id) {
      case progress_bar_type: // we set this to 0
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Downloading file. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setMax(100);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setCancelable(false);
        pDialog.show();
        return pDialog;
      default:
        return null;
    }
  }

  /**
   * Background Async Task to download file
   */
  class DownloadFileFromURL extends AsyncTask<String, String, String> {
    int version;

    /**
     * Before starting background thread Show Progress Bar Dialog
     */

    DownloadFileFromURL() {
    }

    DownloadFileFromURL(int version) {
      this.version = version;
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      showDialog(progress_bar_type);
    }

    /**
     * Downloading file in background thread
     */
    @Override
    protected String doInBackground(String... f_url) {
      int count;
      try {
        URL url = new URL(f_url[0]);
        URLConnection conection = url.openConnection();
        conection.connect();

        // this will be useful so that you can show a tipical 0-100%
        // progress bar
        int lenghtOfFile = conection.getContentLength();

        // download the file
        InputStream input = new BufferedInputStream(url.openStream(),
            8192);

        File sdcard = Environment.getExternalStorageDirectory();
//                String dbfile = "/data/data/org.shaivam/databases/thriumurai.sqlite";


        // Output stream
        OutputStream output = new FileOutputStream(getCacheDir() + "/thriumurai.sqlite");
//                OutputStream output = new FileOutputStream(dbfile);

        byte data[] = new byte[1024];

        long total = 0;

        while ((count = input.read(data)) != -1) {
          total += count;
          // publishing the progress....
          // After this onProgressUpdate will be called
          publishProgress("" + (int) ((total * 100) / lenghtOfFile));

          // writing data to file
          output.write(data, 0, count);
        }

        // flushing output
        output.flush();

        // closing streams
        output.close();
        input.close();
      } catch (Exception e) {
        Log.e("Error: ", e.getMessage());
      }

      return null;
    }

    /**
     * Updating progress bar
     */
    protected void onProgressUpdate(String... progress) {
      // setting progress percentage
      pDialog.setProgress(Integer.parseInt(progress[0]));
    }

    /**
     * After completing background task Dismiss the progress dialog
     **/
    @Override
    protected void onPostExecute(String file_url) {
      // dismiss the dialog after the file was downloaded
      MyApplication.prefManager.saveInt(PrefManager.DATABASE_VERSION, version);
      dismissDialog(progress_bar_type);
      pDialog.cancel();
      new MoveDownloadedFile(version).execute(file_url);
    }

  }

  class MoveDownloadedFile extends AsyncTask<String, String, String> {
    int version;

    /**
     * Before starting background thread Show Progress Bar Dialog
     */

    MoveDownloadedFile() {
    }

    MoveDownloadedFile(int version) {
      this.version = version;
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      showDialog(progress_bar_type);
    }

    /**
     * Downloading file in background thread
     */
    @Override
    protected String doInBackground(String... f_url) {
      moveFile();
      return null;
    }

    private void moveFile() {

      InputStream in = null;
      OutputStream out = null;
      try {

               /* //create output directory if it doesn't exist
                File dir = new File (outputPath);
                if (!dir.exists())
                {
                    dir.mkdirs();
                }
*/

        in = new FileInputStream(getCacheDir() + "/thriumurai.sqlite");
        out = new FileOutputStream("/data/data/org.shaivam/databases/thriumurai.sqlite");

        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
          out.write(buffer, 0, read);
        }
        in.close();
        in = null;

        // write the output file
        out.flush();
        out.close();
        out = null;

        // delete the original file
        new File(Environment.getExternalStorageDirectory() + "/thriumurai.sqlite").delete();


      } catch (FileNotFoundException fnfe1) {
        Log.e("tag", fnfe1.getMessage());
      } catch (Exception e) {
        Log.e("tag", e.getMessage());
      }

    }

    /**
     * Updating progress bar
     */
    protected void onProgressUpdate(String... progress) {
      // setting progress percentage
      pDialog.setProgress(Integer.parseInt(progress[0]));
    }

    /**
     * After completing background task Dismiss the progress dialog
     **/
    @Override
    protected void onPostExecute(String file_url) {
      // dismiss the dialog after the file was downloaded
      MyApplication.prefManager.saveInt(PrefManager.DATABASE_VERSION, version);
      dismissDialog(progress_bar_type);
      pDialog.cancel();
      AppConfig.updateFavouritesDatabase();
      Toast.makeText(HomeActivity.this, AppConfig.getTextString(HomeActivity.this,
          AppConfig.database_success), Toast.LENGTH_LONG).show();
      Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
      startActivity(intent);
    }

  }
}
