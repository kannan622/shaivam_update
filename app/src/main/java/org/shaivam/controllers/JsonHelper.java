package org.shaivam.controllers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.shaivam.utils.MyApplication;
import org.shaivam.utils.PrefManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JsonHelper {

    Map<String, String> params = new HashMap<String, String>();

    public Map<String, String> createCalendarJson(String from_date, String to_date) {
        params.clear();
        params.put("fromDate", from_date);
        params.put("endDate", to_date);
        params.put("version", MyApplication.prefManager.getValue(PrefManager.APP_VERSION));
//        params.put("version","");
        return params;
    }

    public Map<String, String> createVersionJson() {
        params.clear();
        params.put("version", MyApplication.prefManager.getValue(PrefManager.APP_VERSION));
        return params;
    }

    public Map<String, String> createGetEventJson(String from_date) {
        params.clear();
        params.put("fromDate", from_date);
        return params;
    }

    public Map<String, String> createEventJson(String from_date, String to_date) {
        params.clear();
        params.put("fromDate", from_date);
        params.put("endDate", to_date);
        return params;
    }

    public Map<String, String> createCurrentRadioJson() {

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        params.clear();
        params.put("radiotime", sdf.format(new Date()));
        return params;
    }

    public Map<String, String> createCurrentRadioNewJson(String radiot_title) {

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

        params.clear();
        params.put("radiotime", sdf.format(new Date()));
        params.put("radio_date", sdf1.format(new Date()));
        params.put("title", radiot_title);
        return params;
    }
    public Map<String, String> createRadioListJson(String radiot_title, String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        params.clear();
//        sdf.format(new Date())
        params.put("radio_date", date);
//        params.put("radiotitles", radiot_title);
        params.put("title", radiot_title);
        return params;
    }

    public Map<String, String> createMapJson(String latitude, String longitude) {
        params.clear();
        params.put("latitude", latitude);
        params.put("longitude", longitude);
        params.put("radius", "10");
        return params;
    }

    public Map<String, String> createOduvarJson(String thirumuraiId, String pathikamNo) {
        params.clear();
        params.put("thirumuraiId", thirumuraiId);
        params.put("pathikamNo", pathikamNo);
        return params;
    }
}

