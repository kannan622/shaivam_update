package org.shaivam.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.amplitude.api.Amplitude;

import org.shaivam.BuildConfig;
import org.shaivam.activities.HomeActivity;

public class LogUtils {
    public static void LOGD(final String tag, String message) {
        if (BuildConfig.DEBUG && message != null) {
            Log.d("L_" + tag, message);
        }
    }

    public static void LOGV(final String tag, String message) {
        if (BuildConfig.DEBUG && message != null) {
            Log.v(tag, message);
        }
    }

    public static void LOGI(final String tag, String message) {
        if (BuildConfig.DEBUG && message != null) {
            Log.i(tag, message);
        }
    }

    public static void LOGW(final String tag, String message) {
        if (BuildConfig.DEBUG && message != null) {
            Log.w(tag, message);
        }
    }

    public static void LOGE(final String tag, String message) {
        if (BuildConfig.DEBUG && message != null) {
            Log.e(tag, message);
        }
    }

    public static void amplitudeLog(Activity activity, String message) {
        if (!BuildConfig.DEBUG && message != null) {
            Amplitude.getInstance().initialize(activity, AppConfig.KEY_AMPLITUDE)
                    .enableForegroundTracking((activity.getApplication()));
            Amplitude.getInstance().logEvent("V3_1_" + message);
        }
    }

}