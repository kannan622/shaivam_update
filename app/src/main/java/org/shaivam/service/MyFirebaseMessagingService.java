package org.shaivam.service;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;
import org.shaivam.R;
import org.shaivam.activities.HomeActivity;
import org.shaivam.activities.WebViewActivity;
import org.shaivam.utils.AppConfig;
import org.shaivam.utils.LogUtils;
import org.shaivam.utils.NotificationUtils;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        LogUtils.LOGE(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            LogUtils.LOGE(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            LogUtils.LOGE(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                LogUtils.LOGE(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(AppConfig.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            pushNotification.putExtra("title", getApplicationContext().getResources().getString(R.string.app_name));

            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json) {
        LogUtils.LOGE(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
            String message = data.getString("message");
            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = data.getString("image");
            String timestamp = data.getString("timestamp");
            String categoryurl ="";
            if(data.has("categoryurl"))
           categoryurl = data.getString("categoryurl");

            JSONObject payload = data.getJSONObject("payload");

            LogUtils.LOGE(TAG, "title: " + title);
            LogUtils.LOGE(TAG, "message: " + message);
            LogUtils.LOGE(TAG, "isBackground: " + isBackground);
            LogUtils.LOGE(TAG, "payload: " + payload.toString());
            LogUtils.LOGE(TAG, "imageUrl: " + imageUrl);
            LogUtils.LOGE(TAG, "timestamp: " + timestamp);
            LogUtils.LOGE(TAG, "categoryurl: " + categoryurl);


            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(AppConfig.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                pushNotification.putExtra("title", title);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
                Intent resultIntent;

                if(!categoryurl.equalsIgnoreCase("")){
                     resultIntent = new Intent(getApplicationContext(), WebViewActivity.class);
                    resultIntent.putExtra("title", title); // check for image attachment
                    resultIntent.putExtra("message", message); // check for image attachment
                    resultIntent.putExtra("imageUrl", message); // check for image attachment
                    resultIntent.putExtra("url", categoryurl); // check for image attachment
                }else {
                    resultIntent = new Intent(getApplicationContext(), HomeActivity.class);
                    resultIntent.putExtra("title", title); // check for image attachment
                    resultIntent.putExtra("message", message); // check for image attachment
                    resultIntent.putExtra("imageUrl", message); // check for image attachment
                }
                if (imageUrl != null && !TextUtils.isEmpty(imageUrl) &&  imageUrl.length() >0) {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                } else {
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                }
            } else {
                // app is in background, show the notification in notification tray
                Intent resultIntent;

                if(!categoryurl.equalsIgnoreCase("")){
                    resultIntent = new Intent(getApplicationContext(), WebViewActivity.class);
                    resultIntent.putExtra("title", title); // check for image attachment
                    resultIntent.putExtra("message", message); // check for image attachment
                    resultIntent.putExtra("imageUrl", message); // check for image attachment
                    resultIntent.putExtra("url", categoryurl); // check for image attachment
                }else {
                    resultIntent = new Intent(getApplicationContext(), HomeActivity.class);
                    resultIntent.putExtra("title", title); // check for image attachment
                    resultIntent.putExtra("message", message); // check for image attachment
                    resultIntent.putExtra("imageUrl", message); // check for image attachment
                }

                // check for image attachment
                if (imageUrl != null && !TextUtils.isEmpty(imageUrl) &&  imageUrl.length() >0 ) {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                } else {
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                }
            }
        } catch (JSONException e) {
            LogUtils.LOGE(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            LogUtils.LOGE(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }
}