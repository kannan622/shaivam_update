package org.shaivam.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;
import org.shaivam.R;
import org.shaivam.interfaces.VolleyCallback;
import org.shaivam.model.Synchronize;
import org.shaivam.utils.AppConfig;
import org.shaivam.utils.MyApplication;
import org.shaivam.utils.MyContextWrapper;
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

import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {
  static Dialog dialog;

  private ProgressDialog pDialog;
  public static final int progress_bar_type = 0;
  public static List<Synchronize> synchronizeList = new ArrayList<Synchronize>();
  private BroadcastReceiver mRegistrationBroadcastReceiver;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    dialog = new Dialog(this);
    Fabric.with(this, new Crashlytics());
  }

  @Override
  protected void attachBaseContext(Context newBase) {
    String isoCode = MyApplication.prefManager.getValue(PrefManager.LANGUAGE_ISO_CODE);
    newBase = MyContextWrapper.wrap(newBase, isoCode);
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

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
          /*    String message = intent.getStringExtra("message");
              String title = intent.getStringExtra("title");
              String imageUrl = intent.getStringExtra("imageUrl");
              String categoryurl = intent.getStringExtra("categoryurl");

              AppConfig.customNotificationAlert(dialog, MainActivity.this, title, message, imageUrl,
                  categoryurl);*/
            }
          }
        }

    ;
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
  protected void onPause() {
    LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    super.onPause();
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main_menu, menu);

    menu.add(0, R.id.nav_search_site, 1, AppConfig.menuIconWithText(getResources()
        .getDrawable(R.drawable.ic_search), getResources().getString(R.string.search)));
    menu.add(0, R.id.nav_my_favorites, 1, AppConfig.menuIconWithText(getResources()
        .getDrawable(R.drawable.ic_nav_favourite_red), getResources().getString(R.string.fav)));
    menu.add(0, R.id.nav_rating, 1, AppConfig.menuIconWithText(getResources()
        .getDrawable(R.drawable.ic_nav_ratings_red), getResources().getString(R.string.rating)));
    menu.add(0, R.id.nav_share, 1, AppConfig.menuIconWithText(getResources()
        .getDrawable(R.drawable.ic_share), getResources().getString(R.string.action_share)));
    menu.add(0, R.id.nav_language, 1, AppConfig.menuIconWithText(getResources()
        .getDrawable(R.drawable.ic_nav_language_red), getResources().getString(R.string.lang)));
    menu.add(0, R.id.nav_database_update, 1, AppConfig.menuIconWithText(getResources()
        .getDrawable(R.drawable.ic_nav_database_update), getResources().getString(R.string.nav_database_update)));
    menu.add(0, R.id.nav_keyboard, 1, AppConfig.menuIconWithText(getResources()
        .getDrawable(R.drawable.ic_nav_keyboard_red), getResources().getString(R.string.tamilkey)));
    menu.add(0, R.id.nav_contact, 1, AppConfig.menuIconWithText(getResources()
        .getDrawable(R.drawable.ic_nav_contact_us_red), getResources().getString(R.string.contactus)));
    menu.add(0, R.id.nav_about_app, 1, AppConfig.menuIconWithText(getResources()
        .getDrawable(R.drawable.ic_nav_about_app_red), getResources().getString(R.string.About)));
    menu.add(0, R.id.nav_shaivam, 1, AppConfig.menuIconWithText(getResources()
        .getDrawable(R.drawable.ic_nav_shaivam_red), getResources().getString(R.string.home)));


    MenuItem searchViewItem = menu.findItem(R.id.options_menu_main_search);
    final SearchView searchViewAndroidActionBar = (SearchView) MenuItemCompat.getActionView(searchViewItem);
    searchViewAndroidActionBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        searchViewAndroidActionBar.clearFocus();
        if (query.length() >= 3) {
          Intent intent = new Intent(MainActivity.this, ThirumuraiSearchActivity.class);
          intent.putExtra("search_text", query);
          startActivity(intent);
        } else
          Toast.makeText(MainActivity.this, getResources().getText(R.string.search_length), Toast.LENGTH_SHORT).show();
        return true;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
        return false;
      }
    });

    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.nav_contact) {
      Intent intent = new Intent(this, ContactUsActivity.class);
      startActivity(intent);
    } else if (id == R.id.nav_search_site) {
      if (AppConfig.isConnectedToInternet(this)) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("url", AppConfig.URL_SITE_SEARCH);
        intent.putExtra("page", "Site Search");
        startActivity(intent);
      } else
        snackBar(AppConfig.getTextString(this, AppConfig.connection_message));
    } else if (id == R.id.nav_keyboard) {
      Intent intent = new Intent(this, TamilKeyboardActivity.class);
      startActivity(intent);
    } else if (id == R.id.nav_my_favorites) {
      Intent intent = new Intent(this, MyFavouritesActivity.class);
      startActivity(intent);
    } else if (id == R.id.nav_language) {
      final Intent in = new Intent(this, HomeActivity.class);

      android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(
          this);
      alertDialog.setTitle("Shaivam.org");
      alertDialog.setIcon(R.mipmap.ic_launcher);
      alertDialog.setMessage(Html
          .fromHtml("<font color='#720000'>" + AppConfig.getTextString(this, AppConfig.choose_language) + "</font>"));
      alertDialog.setPositiveButton("தமிழ்",
          new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
              HomeActivity.isLanguageClicked = true;
              Toast.makeText(MainActivity.this, AppConfig.getTextString(MainActivity.this, AppConfig.select_tamil),
                  Toast.LENGTH_SHORT).show();
              MyApplication.prefManager.save(PrefManager.LANGUAGE_ISO_CODE, AppConfig.LANG_TN);
              startActivity(in);
            }
          });
      alertDialog.setNegativeButton("English",
          new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
              HomeActivity.isLanguageClicked = true;
              Toast.makeText(MainActivity.this, AppConfig.getTextString(MainActivity.this, AppConfig.select_english),
                  Toast.LENGTH_SHORT).show();
              MyApplication.prefManager.save(PrefManager.LANGUAGE_ISO_CODE, AppConfig.LANG_EN);
              startActivity(in);
            }
          });
      alertDialog.show();
      return true;

    } else if (id == R.id.nav_about_app) {
      Intent intent = new Intent(this, AboutAppActivity.class);
      startActivity(intent);
    } else if (id == R.id.nav_shaivam) {
      if (AppConfig.isConnectedToInternet(this)) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("url", AppConfig.URL_HOME_WEB);
        startActivity(intent);
      } else
        snackBar(AppConfig.getTextString(this, AppConfig.connection_message));
    } else if (id == R.id.nav_share) {
      if (AppConfig.isConnectedToInternet(this)) {
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
        snackBar(AppConfig.getTextString(this, AppConfig.connection_message));
    } else if (id == R.id.nav_database_update) {
      callDatabaseUpdate(this, true);
    } else if (id == R.id.nav_rating) {
      if (AppConfig.isConnectedToInternet(this)) {
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
        snackBar(AppConfig.getTextString(this, AppConfig.connection_message));
    } else if (id == android.R.id.home) {
      onBackPressed();
    }
    return super.onOptionsItemSelected(item);
  }


  public void snackBar(String response) {
    AppConfig.customAlert(dialog, getApplicationContext(), getResources().getString(R.string.app_name), response);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (dialog != null) {
      dialog.dismiss();
      dialog = null;
    }
  }


  public void callDatabaseUpdate(Context context, boolean progressState) {
    if (AppConfig.isConnectedToInternet(MyApplication.getInstance())) {
      if (progressState)
        AppConfig.showProgDialiog(context);
      MyApplication.gsonVolley.Request(MyApplication.getInstance(), AppConfig.URL_BASE, AppConfig.URL_SYNCHRONIZE,
          null, synchronizeCallBack, com.android.volley.Request.Method.POST);
    } else {
      snackBar(AppConfig.getTextString(this, AppConfig.connection_message));
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
              android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(MainActivity.this);
              alertDialog.setTitle("Shaivam.org");
              alertDialog.setIcon(R.mipmap.ic_launcher);
              alertDialog.setMessage(Html
                  .fromHtml("<font color='#720000'>" + AppConfig.getTextString(MainActivity.this, AppConfig.need_database_update) +
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
              Toast.makeText(MainActivity.this, AppConfig.getTextString(MainActivity.this,
                  AppConfig.database_already_update), Toast.LENGTH_LONG).show();
          }
        } else {
          snackBar(jObj.getString("Message"));
        }
      } catch (Exception e) {
        e.printStackTrace();
        snackBar(AppConfig.getTextString(MainActivity.this, AppConfig.went_wrong));
      }
    }

    @Override
    public void Failure(int stauscode, String errorResponse) {
      snackBar(errorResponse);
    }
  };

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
      Toast.makeText(MainActivity.this, AppConfig.getTextString(MainActivity.this,
          AppConfig.database_success), Toast.LENGTH_LONG).show();
      Intent intent = new Intent(MainActivity.this, HomeActivity.class);
      startActivity(intent);
    }

  }

 /*   @Override
    public void onBackPressed() {
        ActivityManager mngr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);

        if (isTaskRoot()) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            //// There are more activities in stack
        }

        super.onBackPressed();
    }*/
}
