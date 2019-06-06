package org.shaivam.utils;

import android.content.Context;

import org.shaivam.activities.EventsActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonValidation{

    boolean status = true;
    String msg = "";

    public boolean dateValidation(Context ctx, String from_date, String to_date) {
        if (checkEmpty(from_date)) {
            msg = AppConfig.getTextString(ctx, AppConfig.from_date_mandatory);
            status = false;
        }else if (checkEmpty(to_date)) {
            msg = AppConfig.getTextString(ctx, AppConfig.to_date_mandatory);
            status = false;
        } else if (!todateIsGreater(from_date,to_date)) {
            msg = AppConfig.getTextString(ctx, AppConfig.to_date_greater_alert);
            status = false;
        } else {
            status = true;
        }
        if (status) {
            return status;
        } else {
            ((EventsActivity)ctx).snackBar( msg);
            return status;
        }
    }

    private boolean checkEmpty(String field_value) {
        return field_value.trim().isEmpty();
    }

    private boolean todateIsGreater(String fromDate, String toDate) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = formatter.parse(fromDate);
            Date date2 = formatter.parse(toDate);
            if (date1.compareTo(date2) <= 0) {
                return true;
            }else
                return false;

        } catch (ParseException e1) {
            e1.printStackTrace();
            return false;
        }
    }
}
