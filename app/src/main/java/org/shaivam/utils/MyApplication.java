package org.shaivam.utils;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.shaivam.R;
import org.shaivam.controllers.GsonVolley;
import org.shaivam.controllers.JsonHelper;
import org.shaivam.database.DatabaseHelper;
import org.shaivam.database.Repo;

import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class MyApplication extends MultiDexApplication {
    private static Context sContext;
    private static MyApplication mInstance;
    public static Gson gson;
    public static PrefManager prefManager;
    public static Repo repo;
    public static GsonVolley gsonVolley = new GsonVolley();
    public static JsonHelper jsonHelper = new JsonHelper();

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        sContext = this;
        Fabric.with(this, new Crashlytics());

        prefManager = new PrefManager(this);
        prefManager.save(PrefManager.PACKAGE_NAME, this.getPackageName());

        if(!prefManager.getBoolean(PrefManager.IS_FIRST_TIME_DATABASE_VERSION)) {
            MyApplication.prefManager.saveBoolean(PrefManager.IS_FIRST_TIME_DATABASE_VERSION, true);
            MyApplication.prefManager.saveInt(PrefManager.DATABASE_VERSION, AppConfig.DATABASE_VERSION);
        }
        String isoCode = MyApplication.prefManager.getValue(PrefManager.LANGUAGE_ISO_CODE);
        if (isoCode.equalsIgnoreCase(""))
            prefManager.save(PrefManager.LANGUAGE_ISO_CODE, AppConfig.LANG_TN);
        repo = new Repo(this);


        GsonBuilder gsonBuilder = new GsonBuilder().serializeNulls();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

        AppConfig.customeLanguage(this);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Lato-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
    }

    public static Context getContext() {
        return sContext;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    public static void clearCache() {
    }

}
