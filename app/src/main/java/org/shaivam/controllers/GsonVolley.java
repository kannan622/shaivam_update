package org.shaivam.controllers;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.shaivam.interfaces.VolleyCallback;
import org.shaivam.utils.AppConfig;
import org.shaivam.utils.LogUtils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class GsonVolley {
    private int mStatusCode = 0;

    public void Request(final Context mContext, final String base_url, final String url,
                        final Map<String, String> jsonServerRequest, final VolleyCallback callBack, int method) {
        LogUtils.LOGD("K_C_URL", url);
        if (jsonServerRequest != null)
            LogUtils.LOGD("K_C_INITIAL_REQUEST", jsonServerRequest.toString());

        StringRequest request = new StringRequest(method, base_url + url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        AppConfig.hideProgDialog();
                        LogUtils.LOGD("K_C_RESPONSE", response.toString());
                        LogUtils.LOGD("statusCode", String.valueOf(mStatusCode));

                        callBack.Success(mStatusCode, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AppConfig.hideProgDialog();

                        String message = error.toString();
                        if (message.equalsIgnoreCase("com.android.volley.AuthFailureError")) {
                            callBack.Failure(mStatusCode, "Authentication Failed");
                        } else if (error instanceof NoConnectionError) {
                            callBack.Failure(mStatusCode, "No Internet Connection");
                        } else if (message.equalsIgnoreCase("com.android.volley.TimeoutError")) {
                            callBack.Failure(mStatusCode, "Something went wrong! Kindly try again later.");
                        } else {
                            final String body;
                            if (error.networkResponse != null && error.networkResponse.data != null) {
                                try {
                                    body = new String(error.networkResponse.data, "UTF-8");
                                    JSONObject jObj = new JSONObject(body);
                                    message = jObj.getString("Message");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            LogUtils.LOGD("K_C_VolleyError", message);
                            LogUtils.LOGD("statusCode", String.valueOf(mStatusCode));
                            callBack.Failure(mStatusCode, message);
                        }
                    }
                }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                if (response != null) {
                    mStatusCode = response.statusCode;
                }
                return super.parseNetworkResponse(response);
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                if (jsonServerRequest != null)
                    return new JSONObject(jsonServerRequest).toString().getBytes();
                else
                    return null;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                try {
                    headers.put("Accept", "application/json");
                    headers.put("Content-Type", "application/x-www-form-urlencoded");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setShouldCache(true);
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(request);
    }
}