package org.shaivam.utils;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import org.shaivam.activities.EventsActivity;
import org.shaivam.fragments.CalendarFragment;
import org.shaivam.fragments.EventsFragment;

import java.util.Calendar;
import java.util.Locale;

public class DatePickerDialogTheme4 extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance(Locale.ENGLISH);;
        int year = calendar.get(Calendar.YEAR) - AppConfig.MIN_YEAR;
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),
                AlertDialog.THEME_HOLO_LIGHT, this, year, month, day);

//        datepickerdialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        /*  if (AppConfig.BACK_FRAGMENT == PivotType.LMS) {
            datepickerdialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        } else {
            datepickerdialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        }*/

        return datepickerdialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        AppConfig.DD = String.valueOf(day);
        AppConfig.MM = String.valueOf(month + 1);
        AppConfig.YYYY = String.valueOf(year);

        AppConfig.DATESET = year + "-" + (month + 1) + "-" + day;

        if (EventsActivity.textViewDatePicker.equalsIgnoreCase("from_text_events"))
            EventsFragment.from_text.setText(AppConfig.DATESET);
        else if (EventsActivity.textViewDatePicker.equalsIgnoreCase("to_text_events"))
            EventsFragment.to_text.setText(AppConfig.DATESET);
            else if (EventsActivity.textViewDatePicker.equalsIgnoreCase("from_text_calendars"))
            CalendarFragment.from_text.setText(AppConfig.DATESET);
        else if (EventsActivity.textViewDatePicker.equalsIgnoreCase("to_text_calendars"))
            CalendarFragment.to_text.setText(AppConfig.DATESET);

        view.clearFocus();
      /*  if (AppConfig.BACK_FRAGMENT == PivotType.LMS) {
           textViewDatePicker.setText(AppConfig.DATESET);
            view.clearFocus();
        }*/
    }

}
