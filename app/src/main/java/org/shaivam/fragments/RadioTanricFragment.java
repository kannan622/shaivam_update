package org.shaivam.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;
import org.shaivam.R;
import org.shaivam.activities.RadioDetailActivity;
import org.shaivam.adapter.RadioDetailAdapter;
import org.shaivam.interfaces.VolleyCallback;
import org.shaivam.model.Radio;
import org.shaivam.utils.AppConfig;
import org.shaivam.utils.MyApplication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.shaivam.activities.RadioDetailActivity.selected_songs;

public class RadioTanricFragment extends Fragment {
    private RadioDetailAdapter adapter;
    public RecyclerView recycle_radio_list;
    String title = "";
    int position = 0;

    public RadioTanricFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_radio_tanric, container, false);
        try {


            Bundle bundle = this.getArguments();
            if (bundle != null) {
                title = bundle.getString("title");
                position = bundle.getInt("position");
            }
            RadioDetailActivity.radios.get(position).clear();
            recycle_radio_list = view.findViewById(R.id.recycle_radio_list);
            adapter = new RadioDetailAdapter(getContext(), RadioDetailActivity.radios.get(position));
            recycle_radio_list.setLayoutManager(new LinearLayoutManager(getContext()));
            recycle_radio_list.setItemAnimator(null);
            recycle_radio_list.getRecycledViewPool().clear();
            recycle_radio_list.setAdapter(adapter);
            adapter.notifyDataSetChanged();


            if (AppConfig.isConnectedToInternet(MyApplication.getInstance())) {
                AppConfig.showProgDialiog(getContext());
                MyApplication.gsonVolley.Request(MyApplication.getInstance(), AppConfig.URL_BASE, AppConfig.URL_RADIO_SCHEDULE,
                        MyApplication.jsonHelper.createRadioListJson(selected_songs.getAuthor_name(), title),
                        radioListCallBack, com.android.volley.Request.Method.POST);
            } else {
                ((RadioDetailActivity) getContext()).snackBar(AppConfig.getTextString(getContext(), AppConfig.connection_message));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return view;
    }


    VolleyCallback radioListCallBack = new VolleyCallback() {
        @Override
        public void Success(int stauscode, String response) {
            try {
                JSONObject jObj = new JSONObject(response);
                if (jObj.has("Status") && jObj.getString("Status").equalsIgnoreCase("true")) {
                    RadioDetailActivity.radios.get(position).clear();
                    adapter.notifyDataSetChanged();

                    List<Radio> data = Arrays.asList(MyApplication.gson.fromJson(jObj.getJSONArray("data").toString(), Radio[].class));
                    RadioDetailActivity.radios.get(position).addAll(data);
//                    adapter.notifyDataSetChanged();
                    adapter.notifyItemRangeInserted(0,  RadioDetailActivity.radios.get(position).size());// notify adapter of new data


                } else {
                    ((RadioDetailActivity) getContext()).snackBar(jObj.getString("Message"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                ((RadioDetailActivity) getContext()).snackBar(AppConfig.getTextString(getContext(), AppConfig.went_wrong));
            }
        }

        @Override
        public void Failure(int stauscode, String errorResponse) {
            ((RadioDetailActivity) getContext()).snackBar(errorResponse);
        }
    };

}
