package org.shaivam.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;
import org.shaivam.R;
import org.shaivam.activities.EventsActivity;
import org.shaivam.adapter.CalendarAdapter;
import org.shaivam.interfaces.VolleyCallback;
import org.shaivam.model.CalendarModel;
import org.shaivam.utils.AppConfig;
import org.shaivam.utils.CommonValidation;
import org.shaivam.utils.DatePickerDialogTheme4;
import org.shaivam.utils.MyApplication;
import org.shaivam.utils.SnappingLinearLayoutManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static org.shaivam.activities.EventsActivity.textViewDatePicker;

public class CalendarFragment extends Fragment {
    private static CalendarAdapter calendarAdapter;
    public RecyclerView recycle_calendar;
    List<CalendarModel> calendarModelList = new ArrayList<CalendarModel>();

    public static TextView from_text;
    public static TextView to_text;

    public CalendarFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        ButterKnife.bind(this, view);
        calendarModelList.clear();
        AppConfig.DATESET =  "";
        textViewDatePicker = "";

        try {
            from_text = view.findViewById(R.id.from_text);
            to_text = view.findViewById(R.id.to_text);
            recycle_calendar = view.findViewById(R.id.recycle_calendar);
            calendarAdapter = new CalendarAdapter(getContext(), calendarModelList);
            recycle_calendar.setLayoutManager(new SnappingLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            recycle_calendar.setAdapter(calendarAdapter);
            recycle_calendar.setNestedScrollingEnabled(false);

            final Calendar mcurrentDate = Calendar.getInstance();
            final int date = mcurrentDate.get(Calendar.DAY_OF_MONTH);
            final int month = mcurrentDate.get(Calendar.MONTH+1);
            final int year = mcurrentDate.get(Calendar.YEAR);
            Date from_date = mcurrentDate.getTime();

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, 180);
            Date to_date = calendar.getTime();

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String dateOutput = format.format(to_date);
            String currentOutput = format.format(from_date);

            from_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    textViewDatePicker = "from_text_calendars";
                    DialogFragment dialogfragment = new DatePickerDialogTheme4();
                    dialogfragment.show(getFragmentManager(), "Theme 4");
                }
            });
            to_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    textViewDatePicker = "to_text_calendars";
                    DialogFragment dialogfragment = new DatePickerDialogTheme4();
                    dialogfragment.show(getFragmentManager(), "Theme 4");
                }
            });

            if (AppConfig.isConnectedToInternet(MyApplication.getInstance())) {
                AppConfig.showProgDialiog(getContext());
                MyApplication.gsonVolley.Request(MyApplication.getInstance(), AppConfig.URL_BASE, AppConfig.URL_CALENDAR,
                        MyApplication.jsonHelper.createCalendarJson(currentOutput, dateOutput),
                        calendarCallBack, com.android.volley.Request.Method.POST);
            } else
                ((EventsActivity)getContext()).snackBar(AppConfig.getTextString(getContext(), AppConfig.connection_message));

            if (textViewDatePicker.equalsIgnoreCase("from_text_calendars")) {
                if (!AppConfig.DATESET.equals("")) {
                    from_text.setText(AppConfig.DATESET);
                } else
                    from_text.setText(year + "-" + month + "-" + date);
                AppConfig.DATESET = from_text.getText().toString();
            } else if (textViewDatePicker.equalsIgnoreCase("to_text_calendars")) {
                if (!AppConfig.DATESET.equals("")) {
                    to_text.setText(AppConfig.DATESET);
                } else
                    to_text.setText(year + "-" + month + "-" + date);
                AppConfig.DATESET = to_text.getText().toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    @OnClick(R.id.search_go_btn)
    public void onGoButtonClick(View view) {
        CommonValidation validation = new CommonValidation();
        boolean status = validation.dateValidation(getContext(), from_text.getText().toString(), to_text.getText().toString());
        if (status) {
            if (AppConfig.isConnectedToInternet(MyApplication.getInstance())) {
                AppConfig.showProgDialiog(getContext());
                MyApplication.gsonVolley.Request(MyApplication.getInstance(), AppConfig.URL_BASE, AppConfig.URL_CALENDAR,
                        MyApplication.jsonHelper.createCalendarJson(from_text.getText().toString(), to_text.getText().toString()),
                        calendarCallBack, com.android.volley.Request.Method.POST);
            } else
                ((EventsActivity)getContext()).snackBar(AppConfig.getTextString(getContext(),AppConfig.connection_message));
        } }


    VolleyCallback calendarCallBack = new VolleyCallback() {
        @Override
        public void Success(int stauscode, String response) {
            try {
                JSONObject jObj = new JSONObject(response);
                if (jObj.has("Status") && jObj.getString("Status").equalsIgnoreCase("true")) {
                    calendarModelList.clear();
                    List<CalendarModel> data = Arrays.asList(MyApplication.gson.fromJson(jObj.getJSONArray("data").toString(), CalendarModel[].class));
                    calendarModelList.addAll(data);
                    calendarAdapter.notifyDataSetChanged();
                } else
                    ((EventsActivity)getContext()).snackBar(jObj.getString("Message"));
            } catch (Exception e) {
                e.printStackTrace();
                ((EventsActivity)getContext()).snackBar(AppConfig.getTextString(getContext(),AppConfig.went_wrong));
            }
        }

        @Override
        public void Failure(int stauscode, String errorResponse) {
            ((EventsActivity)getContext()).snackBar(errorResponse);
        }
    };
}