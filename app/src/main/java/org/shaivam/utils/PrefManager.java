package org.shaivam.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrefManager {
    static SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "SHAIVAM_IOPEX";
    public static final String PACKAGE_NAME = "package_name";

    public static final String IS_FIRST_TIME_LAUNCH = "IS_FIRST_TIME_LAUNCH";

    public static final String LANGUAGE_ISO_CODE = "language_iso_code";
    public static final String LANGUAGE_SELECTED_PREF = "language_selected_pref";

    public static final String IS_FIRST_TIME_DATABASE_VERSION = "IS_FIRST_TIME_DATABASE_VERSION";

    public static final String DATABASE_VERSION = "DATABASE_VERSION";

    public static final String POOJA_PREF = "pooja_pref";

    public static final String POOJA_PREF_6_AM = "pooja_pref_6_am";
    public static final String POOJA_PREF_8_AM = "pooja_pref_8_am";
    public static final String POOJA_PREF_12_PM = "pooja_pref_12_pm";
    public static final String POOJA_PREF_6_PM = "pooja_pref_6_pm";
    public static final String POOJA_PREF_8_PM = "pooja_pref_8_pm";
    public static final String POOJA_PREF_9_PM = "pooja_pref_9_pm";

    public static final String FAVOURITES_PREF = "favourites_pref";
    public static final String FAVOURITES_LIST = "favourites_list";

    public static final String FIREBASE_PREF = "firebase_pref";

    public static final String SONG_FONT_SIZE = "song_Font_Size";

    public static final String APP_VERSION = "app_version";


    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    public void saveListString(String key, List<String> stringList) {
        checkForNullKey(key);
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = MyApplication.getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        String[] myStringList = stringList.toArray(new String[stringList.size()]);
        editor.putString(key, TextUtils.join("‚‗‚", myStringList)).apply();
    }

    public List<String> getListString(String key) {
        SharedPreferences settings;
        settings = MyApplication.getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return new ArrayList<>(Arrays.asList(TextUtils.split(settings.getString(key, ""), "‚‗‚")));
    }

    public void save(String Keyvalue, String text) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = MyApplication.getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putString(Keyvalue, text);
        editor.commit();
    }

    public String getValue(String Keyvalue) {
        SharedPreferences settings;
        String text;
        settings = MyApplication.getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        text = settings.getString(Keyvalue, "");
        return text;
    }

    public static void clearSharedPreference() {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = MyApplication.getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.clear();
        editor.commit();
    }

    public void removeValue(String Keyvalue) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = MyApplication.getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.remove(Keyvalue);
        editor.commit();
    }

    public void saveBoolean(String Keyvalue, boolean status)
    {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = MyApplication.getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = settings.edit(); //2
        editor.putBoolean(Keyvalue, status); //3
        editor.commit(); //4
    }

    public boolean getBoolean(String Keyvalue) {

        SharedPreferences settings;
        boolean text;
        settings = MyApplication.getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        text = settings.getBoolean(Keyvalue, false);
        return text;
    }

    public void saveInt(String Keyvalue, int lvalue){
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = MyApplication.getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putInt(Keyvalue, lvalue);
        editor.commit();
    }

    public int getInt(String Keyvalue , int defaultInt){
        SharedPreferences settings;
        int text;
        settings = MyApplication.getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        text = settings.getInt(Keyvalue, defaultInt);
        return text;
    }

    public void checkForNullKey(String key){
        if (key == null){
            throw new NullPointerException();
        }
    }
}
