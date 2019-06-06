package org.shaivam.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;

import org.shaivam.BuildConfig;
import org.shaivam.R;
import org.shaivam.activities.MyFavouritesActivity;
import org.shaivam.activities.RadioActivity;
import org.shaivam.activities.SixTimePoojaDetailActivity;
import org.shaivam.activities.WebViewActivity;
import org.shaivam.model.Thirumurai_songs;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AppConfig {

    public static String URL_HOME_WEB = "https://shaivam.org";

    public static String URL_BASE = BuildConfig.SERVER_URL;

    public static String URL_EVENT_IMAGE = "https://shaivam.org/images/uploads/events/";

    public static final String URL_MAP = "map";
    public static final String URL_EVENT = "events";
    public static final String URL_GET_EVENT = "getevents";
    public static final String URL_CALENDAR = "calendar";
    public static final String URL_RADIO_CURRENT = "radiocurrent";
    public static final String URL_RADIO_CURRENT_NEW = "radiocurrentnew";
    public static final String URL_RADIO_URL = "radiourl";
    public static final String URL_RADIO = "radio";
    public static final String URL_RADIO_SCHEDULE = "schedule";
    public static final String URL_QUIZ = "quiz";
    public static final String URL_SYNCHRONIZE = "synchronize";
    public static final String URL_ODUVAR = "oduvar";
    public static final String URL_VERSION = "version";

    public static final String APP_VERSION = "8";
    public static final int DATABASE_VERSION = 12;

    public static final int SMALL_SONG_FONT_SIZE = 18;
    public static final int MEDIUM_SONG_FONT_SIZE = 20;
    public static final int LARGE_SONG_FONT_SIZE = 22;
    public static final int EXTRA_LARGE_SONG_FONT_SIZE = 24;

    public static final String URL_RADIO_TANTRIC = "https://tantric-yoga.com/mobile/schedule";


    public static final String KEY_AMPLITUDE = "b525dc19af12c4fee3a0154efc6648f1";

    public static final String ORG_EMAIL_ID = "mobile@shaivam.org";
    public static final String ORG_EMAIL_SUBJECT = "Add Map";
    public static final String ORG_EMAIL_INCORRECT_SUBJECT = "Incorrect Location in Map";

    public static final String ORG_EMAIL_SUBJECT_CONTACT = "Reg: Contact Shaivam.org";


    public static final String FONT_KAVIVANAR = "fonts/Lato-Regular.ttf";
//    public static final String FONT_KAVIVANAR = "fonts/Kavivanar-Regular.ttf";


    public static final String URL_ADD_EVENT = "https://shaivam.org/eventpage.php";
    public static final String URL_SITE_SEARCH = "https://shaivam.org/mobile-search.php";

    public static final String Radio_Shaivalahari = "Radio Shaivalahari";
    public static final String Radio_Thiruneriya_Thamizhosai = "Radio Thiruneriya Thamizhosai";

    public static final String Url_Radio_Shaivalahari = "https://50.22.212.204:8194/;stream/1";
    public static final String Url_Radio_Thiruneriya_Thamizhosai = "https://50.22.212.203:2199/stream.php?port=8007";

    public static final String Six_Pooja_6_Am_Url = "https://www.shaivam.org/gallery/audio/app-6kaala-puja-reminder/1-6am-kad-rudraya&porri-en.mp3";
    public static final String Six_Pooja_8_Am_Url = "https://www.shaivam.org/gallery/audio/app-6kaala-puja-reminder/2-8am-tryambakam&unakkup-paniseyya.mp3";
    public static final String Six_Pooja_12_Pm_Url = "https://www.shaivam.org/gallery/audio/app-6kaala-puja-reminder/3-12pm-namah-somayacha&puzhuvayp-pirakkinum.mp3";
    public static final String Six_Pooja_6_Pm_Url = "https://www.shaivam.org/gallery/audio/app-6kaala-puja-reminder/4-6pm-avo-rajanam&idarinum-thalarinum.mp3";
    public static final String Six_Pooja_8_Pm_Url = "https://www.shaivam.org/gallery/audio/app-6kaala-puja-reminder/5-8pm-namah-sayam-namah&kuraivila-niraive.mp3";
    public static final String Six_Pooja_9_Pm_Url = "https://www.shaivam.org/gallery/audio/app-6kaala-puja-reminder/6-9pm-svasti-prajapya&vazhga-andhanar.mp3";

    public static final int Six_Pooja_6_Am_hours = 6;
    public static final int Six_Pooja_8_Am_hours = 8;
    public static final int Six_Pooja_12_Pm_hours = 12;
    public static final int Six_Pooja_6_Pm_hours = 18;
    public static final int Six_Pooja_8_Pm_hours = 20;
    public static final int Six_Pooja_9_Pm_hours = 21;

    //color
    public static String colorPrimary = "colorPrimary";

    //dynamic string
    public static String exit_app = "exit_app";
    public static String OK = "OK";
    public static String Cancel = "Cancel";
    public static String go = "go";

    public static String search_hint_thirumurai = "search_hint_thirumurai";
    public static String search_hint = "search_hint";

    public static String thiru_head = "thiru_head";
    public static String temple = "temple";
    public static String search = "search";
    public static String calander = "calander";
    public static String nayanmargal = "nayanmargal";
    public static String others = "others";
    public static String quiz = "quiz";

    public static String database_success = "database_success";
    public static String database_already_update = "database_already_update";

    public static String thevaram = "thevaram";
    public static String thiruvasagam = "thiruvasagam";
    public static String thirukovaiyar = "thirukovaiyar";
    public static String thiruvisaipa = "thiruvisaipa";
    public static String thirumanthiram = "thirumanthiram";
    public static String prabandham = "prabandham";
    public static String periyapuranam = "periyapuranam";

    public static String Calendar_Gurupujai = "Gurupujai";
    public static String Calendar_Shivaratri = "Shivaratri";
    public static String Calendar_Theipirai = "தேய்பிறை";
    public static String Calendar_Pradhosham = "Pradhosham";
    public static String Calendar_Amavasai = "Amavasai";
    public static String Calendar_Pournami = "Pournami";

    public static String Calendar_Natarajar = "Natarajar";
    public static String Calendar_Thirunadanam = "Thirunadanam";

    public static String thalam = "thalam";
    public static String nadu = "nadu";
    public static String pann = "pann";
    public static String aruliyavar = "aruliyavar";
    public static String paadal = "paadal";
    public static String paadal_varigal = "paadal_varigal";

    public static String connection_message = "connection_message";
    public static String connection_message_audio = "connection_message_audio";
    public static String went_wrong = "went_wrong";

    public static String wrong = "wrong";
    public static String correct = "correct";

    public static String cannot_open_file = "cannot_open_file";
    public static String upload_failed = "upload_failed";
    public static String alarm_received = "alarm_received";
    public static String select_tamil = "select_tamil";
    public static String select_english = "select_english";
    public static String select_answer = "select_answer";
    public static String device_alert = "device_alert";
    public static String cannot_fetch_loc = "cannot_fetch_loc";
    public static String error_no_music = "error_no_music";
    public static String no_thirumurai = "no_thirumurai";
    public static String choose_language = "choose_language";
    public static String app_Language = "app_Language";
    public static String need_database_update = "need_database_update";

    public static String from_date_mandatory = "from_date_mandatory";
    public static String to_date_mandatory = "to_date_mandatory";
    public static String to_date_greater_alert = "to_date_greater_alert";


    //image loader through volley
    public static ImageLoader mImageLoader;

    public static ProgressDialog progressDialog;
    public static ArrayList<ProgressDialog> sProgStack = new ArrayList<ProgressDialog>();

    public static String CHANNEL_ID = "my_channel_01";

    public static final String LANG_EN = "en";
    public static final String LANG_TN = "tn";


    public static String DD = "";
    public static String MM = "";
    public static String YYYY = "";
    public static String DATESET = "";
    public static int MIN_YEAR = 0;


    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static int NOTIFICATION_ID = 100;
    public static int NOTIFICATION_ID_BIG_IMAGE = 300;


    public static List<LatLng> routeLatLng = new ArrayList<LatLng>();

    public static void customeLanguage(Context context) {
        String isoCode = MyApplication.prefManager.getValue(PrefManager.LANGUAGE_ISO_CODE);
        if (!isoCode.equalsIgnoreCase("")) {
            Locale locale = new Locale(isoCode);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        }
    }

    public static String getTextString(Context mContext, String textKey) {
        String dynamicString = "";
        String packageName = MyApplication.prefManager.getValue(PrefManager.PACKAGE_NAME);
        int identifier;
        if (packageName == null || packageName.isEmpty())
            identifier = mContext.getResources().getIdentifier(textKey, "string", mContext.getPackageName());
        else
            identifier = mContext.getResources().getIdentifier(textKey, "string", packageName);
        if (identifier != 0) {
            dynamicString = mContext.getResources().getString(identifier);
        }
        String isoCode = MyApplication.prefManager.getValue(PrefManager.LANGUAGE_ISO_CODE);
     /*   if (isoCode.equalsIgnoreCase("tn")) {
            return TamilUtil.convertToTamil(TamilUtil.TAM, dynamicString);
        } else*/
        return dynamicString;
    }

    public static void setBackgroundColor(View view, Context mContext, String color) {
        String packageName = MyApplication.prefManager.getValue(PrefManager.PACKAGE_NAME);
        int identifier = mContext.getResources().getIdentifier(color, "color", packageName);
        if (identifier != 0) {
            view.setBackgroundColor(mContext.getResources().getColor(identifier));
        }
    }

    public static void setBackgroundResourceFromMipmap(ImageView view, Context mContext, String resource) {
        String packageName = MyApplication.prefManager.getValue(PrefManager.PACKAGE_NAME);
        int identifier = mContext.getResources().getIdentifier(resource, "mipmap", packageName);
        if (identifier != 0) {
            view.setImageResource(identifier);
        }
    }

    public static void setBackgroundResourceFromDrawable(View view, Context mContext, String color) {
        String packageName = MyApplication.prefManager.getValue(PrefManager.PACKAGE_NAME);
        int identifier = mContext.getResources().getIdentifier(color, "drawable", packageName);
        if (identifier != 0) {
            view.setBackgroundResource(identifier);
        }
    }

    public static void setBackgroundResourceFromUrl(NetworkImageView view, Context mContext, String url_string) {
        String packageName = MyApplication.prefManager.getValue(PrefManager.PACKAGE_NAME);
        try {
            URL url = new URL(url_string);
            mImageLoader = CustomVolleyRequestQueue.getInstance(mContext)
                    .getImageLoader();
            mImageLoader.get(url.toString(), ImageLoader.getImageListener(view,
                    R.mipmap.ic_launcher, android.R.drawable
                            .ic_dialog_alert));
            view.setImageUrl(url.toString(), mImageLoader);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static void createSnackBarView(Context context, Snackbar snackbar) {
        final View snackBarView = snackbar.getView();
        setBackgroundColor(snackBarView, context, colorPrimary);
        //To hide soft keyboard When snackbar displays
        final InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        snackBarView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            /**
             * This method is used to hide soft keyboard When snackbar displays
             * @return Nothing.
             */
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                snackBarView.getWindowVisibleDisplayFrame(r);
                int screenHeight = snackBarView.getRootView().getHeight();
                // r.bottom is the position above soft keypad or device button.
                // if keypad is shown, the r.bottom is smaller than that before.
                int keypadHeight = screenHeight - r.bottom;
                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                    // keyboard is opened
                    im.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                } else {
                    // keyboard is closed
                }
            }
        });
        TextView tv = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.parseColor("#FFFFFF"));
        snackbar.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void showProgDialiog(Context context) {
        progressDialog = new ProgressDialog(context);
        int size = sProgStack.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                ProgressDialog dia = sProgStack.get(i);
                if (dia != null && dia.isShowing()) {
                    try {
                        dia.dismiss();
                    } catch (Exception e) {
                    }
                }
            }
            sProgStack.clear();
        }
        if (progressDialog != null && !progressDialog.isShowing() && !((Activity) context).isDestroyed()) {
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_bar);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            progressDialog.setCancelable(false);
            sProgStack.add(progressDialog);
        }
    }

    public static void hideProgDialog() {

        int size = sProgStack.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                ProgressDialog dia = sProgStack.get(i);
                if (dia != null && dia.isShowing()) {
                    try {
                        dia.dismiss();
                    } catch (Exception e) {
                    }
                }
            }
            sProgStack.clear();
        }
    }

    public static void onApplicationExit(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(getTextString(context, exit_app));
        AlertDialog alertDialog = builder.create();
        builder.setPositiveButton(getTextString(context, OK),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            ((Activity) context).finishAffinity();
                        } else
                            ((Activity) context).finish();
                    }
                });
        builder.setNegativeButton(getTextString(context, Cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }

    /*public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }*/
    public static boolean isConnectedToInternet(Context context) {

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static void createNotificationChannel(Context context) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

// The id of the channel.
        String id = "my_channel_01";

// The user-visible name of the channel.
        CharSequence name = context.getResources().getString(R.string.app_name);

// The user-visible description of the channel.
        String description = context.getResources().getString(R.string.app_name);

        int importance = NotificationManager.IMPORTANCE_LOW;

        NotificationChannel mChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(id, name, importance);
            // Configure the notification channel.
            mChannel.setDescription(description);

            mChannel.enableLights(true);
// Sets the notification light color for notifications posted to this
// channel, if the device supports this feature.
            mChannel.setLightColor(Color.RED);

            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            mNotificationManager.createNotificationChannel(mChannel);
        }
    }

    public static Notification createNotificationPooja(Context context, String requestcode, String title, String message) {
        Intent resultIntent;

        resultIntent = new Intent(context, SixTimePoojaDetailActivity.class);
        resultIntent.putExtra("requestcode", requestcode);

        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        final PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );
        createNotificationChannel(context);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

// Sets an ID for the notification, so it can be updated.
        int notifyID = 1;

// The id of the channel.
        int color = ContextCompat.getColor(context, R.color.colorPrimaryDark);

        // Create a notification and set the notification channel.
        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder builder;
            builder = new Notification.Builder(context);

            notification = builder.setColor(color)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setContentIntent(resultPendingIntent)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setChannelId(CHANNEL_ID)
                    .setAutoCancel(true)
                    .setOngoing(false)
                    .build();
        } else {
            Notification.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                builder = new Notification.Builder(context).setColor(color);
            else
                builder = new Notification.Builder(context);

            notification = builder.setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(resultPendingIntent)
                    .setAutoCancel(true)
                    .setOngoing(false)
                    .build();
        }

// Issue the notification.
        mNotificationManager.notify(notifyID, notification);
        return notification;
    }

    public static Notification createRadioNotification(Context context, String title, String message) {
        Intent resultIntent;

        resultIntent = new Intent(context, RadioActivity.class);

        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        final PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );
        createNotificationChannel(context);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

// Sets an ID for the notification, so it can be updated.
        int notifyID = 1;

// The id of the channel.
        int color = ContextCompat.getColor(context, R.color.colorPrimaryDark);

        // Create a notification and set the notification channel.
        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder builder;
            builder = new Notification.Builder(context);

            notification = builder.setColor(color)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setContentIntent(resultPendingIntent)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setChannelId(CHANNEL_ID)
                    .setAutoCancel(true)
                    .setOngoing(false)
                    .build();
        } else {
            Notification.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                builder = new Notification.Builder(context).setColor(color);
            else
                builder = new Notification.Builder(context);

            notification = builder.setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(resultPendingIntent)
                    .setAutoCancel(true)
                    .setOngoing(false)
                    .build();
        }

// Issue the notification.
        mNotificationManager.notify(notifyID, notification);
        return notification;
    }

    public static boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static CharSequence menuIconWithText(Drawable r, String title) {

        r.setBounds(0, 0, r.getIntrinsicWidth(), r.getIntrinsicHeight());
        SpannableString sb = new SpannableString("    " + title);
        ImageSpan imageSpan = new ImageSpan(r, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return sb;
    }

    public static boolean isMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static Spanned buildSpanned(final String res) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                Html.fromHtml(res, Html.FROM_HTML_MODE_LEGACY) :
                Html.fromHtml(res);
    }

    public static void updateTextView(@NonNull final TextView textView, final String text) {
        textView.post(new Runnable() {
            @Override
            public void run() {
                textView.setText(text);
            }
        });
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != activity.getCurrentFocus())
            imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                    .getApplicationWindowToken(), 0);
    }

    public static void showSettingsDialog(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings(activity);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    public static void showFavoritesDialog(final Activity activity, final Thirumurai_songs thirumurai_songs,
                                           final Boolean isFavourite, final ImageView bookmark) {
        thirumurai_songs.setFavorites(isFavourite);
        MyApplication.repo.repoThirumurai_songs.update(thirumurai_songs);
        updateFavouritesPref();

        if (bookmark != null && thirumurai_songs.getFavorites()) {
            bookmark.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_bookmark_white));
            bookmark.setBackgroundResource(R.color.colorPrimary);
        } else if (bookmark != null) {
            bookmark.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_bookmark));
            bookmark.setBackgroundResource(R.color.bg_white);
        }
        if (isFavourite)
            Toast.makeText(activity, activity.getResources().getText(R.string.add_fav_message), Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(activity, activity.getResources().getText(R.string.remove_fav_message), Toast.LENGTH_SHORT).show();

        if (activity instanceof MyFavouritesActivity) {
            ((MyFavouritesActivity) activity).onResume();
        }

    }


    public static void addFavourites() {
        ArrayList<String> song_titles = new ArrayList<String>() {
        };
        song_titles.add("01.052 மறையுடையாய் தோலுடையாய்");
        song_titles.add("01.116 அவ்வினைக் கிவ்வினை");
        song_titles.add("01.128 ஓருரு வாயினை");
        song_titles.add("02.085 வேயுறு தோளிபங்கன்");
        song_titles.add("03.004 இடரினுந் தளரினும்");
        song_titles.add("03.024 மண்ணின்நல் லவண்ணம்");
        song_titles.add("03.049 காத லாகிக்");
        song_titles.add("03.054 வாழ்க அந்தணர் வானவர்");
        song_titles.add("06.094 இருநிலனாய்த் தீயாகி");
        song_titles.add("08.001 சிவபுராணம்");
        MyApplication.prefManager.saveListString(PrefManager.FAVOURITES_LIST, song_titles);


        updateFavouritesDatabase();

        MyApplication.prefManager.saveBoolean(PrefManager.FAVOURITES_PREF, true);
    }

    public static void updateFavouritesPref() {
        List<String> favorites = new ArrayList<>();
        for (Thirumurai_songs thirumurai_songs1 : MyApplication.repo.repoThirumurai_songs.getAllFavouritesThirumurai()) {
            favorites.add(thirumurai_songs1.getTitle());
        }
        MyApplication.prefManager.saveListString(PrefManager.FAVOURITES_LIST, favorites);
    }

    public static void updateFavouritesDatabase() {
        List<String> song_titles = MyApplication.prefManager.getListString(PrefManager.FAVOURITES_LIST);

        for (int i = 0; i < song_titles.size(); i++) {
            List<Thirumurai_songs> thirumurai_songs = MyApplication.repo.repoThirumurai_songs.getSongsByTitle(song_titles.get(i));
            if (thirumurai_songs != null && thirumurai_songs.size() > 0) {
                thirumurai_songs.get(0).setFavorites(true);
                MyApplication.repo.repoThirumurai_songs.update(thirumurai_songs.get(0));
            }
        }
    }

    // navigating user to app settings
    public static void openSettings(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivityForResult(intent, 101);
    }


    public static void customAlert(final Dialog dialog, Context context, String title, String message) {
        if (dialog != null) {
            dialog.setContentView(R.layout.dialog_alert);
            final TextView tilte_text = dialog.findViewById(R.id.tilte_text);
            final TextView message_text = dialog.findViewById(R.id.message_text);
            tilte_text.setText(title);
            message_text.setText(message);
            Button ok_button = (Button) dialog.findViewById(R.id.ok_button);
            ok_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });
            dialog.show();
        }
    }

    public static void customNotificationAlert(final Dialog dialog, final Activity context, String title, String message, String image_url, final String redirect_url) {
        if (dialog != null) {
            dialog.setContentView(R.layout.dialog_push_notification_alert);
            dialog.setCancelable(false);
            final TextView tilte_text = dialog.findViewById(R.id.tilte_text);
            final TextView message_text = dialog.findViewById(R.id.message_text);
            final ImageView notification_image = dialog.findViewById(R.id.notification_image);
            tilte_text.setText(title);
            message_text.setText(message);
            Glide.with(context).load(image_url).into(notification_image);
            Button ok_button = (Button) dialog.findViewById(R.id.ok_button);
            ok_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                    if (redirect_url != null && !redirect_url.equalsIgnoreCase("")) {
                        Intent resultIntent = new Intent(context, WebViewActivity.class);
                        resultIntent.putExtra("url", redirect_url); // check for image attachment
                        context.startActivity(resultIntent);
                    }
                }
            });
            dialog.show();
        }
    }


    public static void customAlertPlaystore(final Dialog dialog, final Context context, String title, String message) {
        if (dialog != null) {
            dialog.setContentView(R.layout.dialog_alert);
            dialog.setCancelable(true);
            final TextView tilte_text = dialog.findViewById(R.id.tilte_text);
            final TextView message_text = dialog.findViewById(R.id.message_text);
            tilte_text.setText(title);
            message_text.setText(message);
            Button ok_button = (Button) dialog.findViewById(R.id.ok_button);
            ok_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri uri = Uri.parse("market://details?id=" + context.getPackageName());

                    Log.d("URI", context.getPackageName().toString());
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    // To count with Play market backstack, After pressing back
                    // button,
                    // to taken back to our application, we need to add following
                    // flags to intent.
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY
                            | Intent.FLAG_ACTIVITY_NEW_DOCUMENT
                            | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    try {
                        context.startActivity(goToMarket);
                    } catch (ActivityNotFoundException e) {
                        Log.d("PACKAGE", context.getPackageName());
                        context.startActivity(new Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id="
                                        + context.getPackageName())));

                    }
                }
            });
            dialog.show();
        }
    }

    public static String capitalizeWord(String str) {
        String words[] = str.split("\\s");
        String capitalizeWord = "";
        for (String w : words) {
            String first = w.substring(0, 1);
            String afterfirst = w.substring(1);
            capitalizeWord += first.toUpperCase() + afterfirst + " ";
        }
        return capitalizeWord.trim();
    }
}
