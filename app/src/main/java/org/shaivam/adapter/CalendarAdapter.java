package org.shaivam.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.shaivam.R;
import org.shaivam.activities.WebViewActivity;
import org.shaivam.model.CalendarModel;
import org.shaivam.utils.AppConfig;

import java.util.Calendar;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {
    private List<CalendarModel> mDataset;
    private Context context;
    private LayoutInflater inflater;
    private static int currentPosition = -1;

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView calendar_date, calendar_month, calendar_text, calendar_week, wrong_version_text;
        LinearLayout calendar_linear, carcd_calendar, date_linear, calendar_main_linear;
        ImageView calendar_image, calendar_arrow;

        public ViewHolder(View v) {
            super(v);

            calendar_date = v.findViewById(R.id.calendar_date);
            calendar_month = v.findViewById(R.id.calendar_month);
            calendar_text = v.findViewById(R.id.calendar_text);
            calendar_week = v.findViewById(R.id.calendar_week);
            calendar_linear = v.findViewById(R.id.calendar_linear);
            calendar_image = v.findViewById(R.id.calendar_image);
            carcd_calendar = v.findViewById(R.id.carcd_calendar);
            calendar_arrow = v.findViewById(R.id.calendar_arrow);
            date_linear = v.findViewById(R.id.date_linear);
            wrong_version_text = v.findViewById(R.id.wrong_version_text);
            calendar_main_linear = v.findViewById(R.id.calendar_main_linear);
        }
    }

    public CalendarAdapter(Context context, List<CalendarModel> calendarModelList) {
        this.context = context;
        mDataset = calendarModelList;
        inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        currentPosition = -1;
    }

    @Override
    public CalendarAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        View rootView = inflater.inflate(R.layout.adapter_calendar, parent, false);
        return new CalendarAdapter.ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final CalendarAdapter.ViewHolder holder, final int position) {
        if (mDataset.size() > 0) {
            final CalendarModel calendarModel = mDataset.get(position);
            Typeface tf = Typeface.createFromAsset(context.getAssets(), AppConfig.FONT_KAVIVANAR);

            holder.calendar_text.setTypeface(tf);

            String[] dateParts = calendarModel.getCalendarFromdate().split("-");
            String day = dateParts[2];
            String month = dateParts[1];
            String year = dateParts[0];

            holder.calendar_date.setText(day.trim());
/*
            SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
            int monthnum=Integer.parseInt(month);
            cal.set(Calendar.MONTH,monthnum);
            String month_name = month_date.format(cal.getTime());
            Calendar cal=Calendar.getInstance();*/
            if (!calendarModel.getCalendarFromdate().equalsIgnoreCase("0-0-0")) {
                holder.calendar_month.setText(theMonth(Integer.parseInt(month) - 1));
                holder.wrong_version_text.setVisibility(View.GONE);
                holder.calendar_main_linear.setVisibility(View.VISIBLE);
            }else {
                holder.wrong_version_text.setVisibility(View.VISIBLE);
                holder.calendar_main_linear.setVisibility(View.GONE);
                holder.wrong_version_text.setText(calendarModel.getCalendarTitle().trim());
            }
            holder.calendar_text.setText(calendarModel.getCalendarTitle().trim());

            if (calendarModel.getCalendarTitle().contains(AppConfig.Calendar_Gurupujai)) {
                holder.calendar_image.setImageResource(R.drawable.ic_pooja);
            } else if (calendarModel.getCalendarTitle().contains(AppConfig.Calendar_Theipirai)) {
                holder.calendar_image.setImageResource(R.drawable.ic_theipirai);
            } else if (calendarModel.getCalendarTitle().contains(AppConfig.Calendar_Pradhosham)) {
                holder.calendar_image.setImageResource(R.drawable.ic_pradhosam);
            } else if (calendarModel.getCalendarTitle().contains(AppConfig.Calendar_Amavasai)) {
                holder.calendar_image.setImageResource(R.drawable.ic_ammavasai);
            } else if (calendarModel.getCalendarTitle().contains(AppConfig.Calendar_Pournami)) {
                holder.calendar_image.setImageResource(R.drawable.ic_poornami);
            } else if (calendarModel.getCalendarTitle().contains(AppConfig.Calendar_Natarajar) ||
                    calendarModel.getCalendarTitle().contains(AppConfig.Calendar_Thirunadanam) ||
                    calendarModel.getCalendarTitle().contains(AppConfig.Calendar_Shivaratri)) {
                holder.calendar_image.setImageResource(R.drawable.ic_natarajar);
            } else {
                holder.calendar_image.setImageResource(R.drawable.ic_kayathri);
            }


            holder.calendar_week.setText(getNameOfDay(Integer.parseInt(year), (Integer.parseInt(month) - 1), Integer.parseInt(day)).trim());
            if (calendarModel.getCalendarUrl() == null || calendarModel.getCalendarUrl().equalsIgnoreCase(""))
                holder.calendar_arrow.setVisibility(View.GONE);
            else
                holder.calendar_arrow.setVisibility(View.VISIBLE);

            if (position == currentPosition) {
                holder.calendar_linear.setBackgroundResource(R.drawable.background_tirumurai);
            } else {
                holder.calendar_linear.setBackgroundResource(R.drawable.background_events);
            }
            holder.carcd_calendar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currentPosition >= 0) {
                        int prev = currentPosition;
                        notifyItemChanged(prev);
                    }
                    if (currentPosition == holder.getPosition()) {
                        currentPosition = holder.getPosition();
                        notifyItemChanged(currentPosition);

                        if (!calendarModel.getCalendarUrl().equalsIgnoreCase(""))
                            try {
                                Intent intent = new Intent(context, WebViewActivity.class);
                                intent.putExtra("url", calendarModel.getCalendarUrl());
                                context.startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                    } else {
                        currentPosition = holder.getPosition();
                        notifyItemChanged(currentPosition);
                        if (!calendarModel.getCalendarUrl().equalsIgnoreCase(""))
                            try {
                                Intent intent = new Intent(context, WebViewActivity.class);
                                intent.putExtra("url", calendarModel.getCalendarUrl());
                                context.startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                    }
                }
            });
        }

    }

    public static String theMonth(int month) {
        String[] monthNames = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL",
                "AUG", "SEP", "OCT", "NOV", "DEC"};
        return monthNames[month];
    }

    public String getNameOfDay(int year,int month, int dayOfYear) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfYear);
        String days[] = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

        int dayIndex = calendar.get(Calendar.DAY_OF_WEEK);

        return days[dayIndex - 1];
    }

    public int getItemCount() {

        if (mDataset != null)
            return mDataset.size();
        else
            return 0;
    }
}