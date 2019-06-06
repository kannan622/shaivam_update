package org.shaivam.adapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import org.shaivam.R;
import org.shaivam.activities.RadioDetailActivity;
import org.shaivam.broadcastReceivers.RadioAlarmReceiver;
import org.shaivam.model.Radio;
import org.shaivam.playback.PlaybackInfoListener;
import org.shaivam.utils.AppConfig;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class RadioDetailAdapter extends RecyclerView.Adapter<RadioDetailAdapter.ViewHolder> {
    private List<Radio> mDataset;
    private Context context;
    private LayoutInflater inflater;
    private static int currentPosition = -1;
    public static int currentRadioPosition = -1;

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView radio_text, language, timimg, recent_presenter_title, recent_presenter_value,
                type_title, type_value;
        ImageView reminder;
        Switch reminderSwitch;

        public ViewHolder(View v) {
            super(v);

            radio_text = v.findViewById(R.id.radio_text);
            language = v.findViewById(R.id.language);
            timimg = v.findViewById(R.id.timimg);
            recent_presenter_title = v.findViewById(R.id.recent_presenter_title);
            recent_presenter_value = v.findViewById(R.id.recent_presenter_value);
            type_title = v.findViewById(R.id.type_title);
            type_value = v.findViewById(R.id.type_value);
            reminder = v.findViewById(R.id.reminder);
            reminderSwitch = v.findViewById(R.id.reminderSwitch);


        }
    }

    public RadioDetailAdapter(Context context, List<Radio> radioList) {
        this.context = context;
        mDataset = radioList;
        inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        currentPosition = -1;
    }

    @Override
    public RadioDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View rootView = inflater.inflate(R.layout.adapter_radio_detail, parent, false);
        return new RadioDetailAdapter.ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final RadioDetailAdapter.ViewHolder holder, final int position) {
        if (mDataset.size() > 0) {
            final Radio radio = mDataset.get(position);
            Typeface tf = Typeface.createFromAsset(context.getAssets(), AppConfig.FONT_KAVIVANAR);
            holder.radio_text.setTypeface(tf);
            holder.language.setTypeface(tf);
            holder.timimg.setTypeface(tf);
            holder.recent_presenter_title.setTypeface(tf);
            holder.recent_presenter_value.setTypeface(tf);
            holder.type_title.setTypeface(tf);
            holder.type_value.setTypeface(tf);

            holder.radio_text.setText(radio.getProgram().trim());
            holder.language.setText(radio.getLanguage().trim());
            holder.timimg.setText(radio.getTimestart().trim());
            holder.recent_presenter_value.setText(radio.getArtist().trim());
            holder.type_value.setText(radio.getCategory().trim());

            holder.reminderSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    // TODO Auto-generated method stub

                    if (isChecked) {
                        createAlarm(radio, position);
                    } else {
                        cancelAlarm(radio, position);
                    }
                }
            });
        }
    }

    void createAlarm(Radio radio, int currentPosition) {

        Calendar calNow1 = Calendar.getInstance();
        Calendar cal = (Calendar) calNow1.clone();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
        try {
            cal.setTime(sdf.parse(radio.getRadioDate() + " " + radio.getTimestart()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (cal.compareTo(calNow1) <= 0) {
            // Today Set time passed, count to tomorrow
            cal.add(Calendar.DATE, 0);
        } else {
            setAlarm(radio, cal, currentPosition);
        }
    }

    private void cancelAlarm(Radio radio, int currentPosition) {
        Intent intent = new Intent(((RadioDetailActivity) context).getBaseContext(), RadioAlarmReceiver.class);
        intent.putExtra("program", radio.getProgram());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(((RadioDetailActivity) context).getBaseContext(), currentPosition, intent, 0);
        AlarmManager alarmManager = (AlarmManager) ((RadioDetailActivity) context).getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    private void setAlarm(Radio radio, Calendar targetCal, int requestcode) {
        Intent intent = new Intent(((RadioDetailActivity) context).getBaseContext(), RadioAlarmReceiver.class);
        intent.putExtra("program", radio.getProgram());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                ((RadioDetailActivity) context).getBaseContext(), requestcode, intent, 0);
        AlarmManager alarmManager = (AlarmManager) ((RadioDetailActivity) context).getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(),
                pendingIntent);
    }

    public int getItemCount() {

        if (mDataset != null)
            return mDataset.size();
        else
            return 0;
    }
}