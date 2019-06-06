package org.shaivam.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import org.shaivam.R;
import org.shaivam.broadcastReceivers.AlarmReceiver;
import org.shaivam.utils.AppConfig;
import org.shaivam.utils.LogUtils;
import org.shaivam.utils.MyApplication;
import org.shaivam.utils.PrefManager;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SixTimePoojaActivity extends MainActivity {
  private Toolbar toolbar;

  @BindView(R.id.switcher)
  Switch switcher;

  @BindView(R.id.ushat_switcher)
  Switch ushat_switcher;
  @BindView(R.id.kaala_switcher)
  Switch kaala_switcher;
  @BindView(R.id.uchi_switcher)
  Switch uchi_switcher;
  @BindView(R.id.saya_switcher)
  Switch saya_switcher;
  @BindView(R.id.iranda_switcher)
  Switch iranda_switcher;
  @BindView(R.id.ardha_switcher)
  Switch ardha_switcher;

  @BindView(R.id.six_pooja_main)
  CoordinatorLayout six_pooja_main;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_six_time_pooja);
    ButterKnife.bind(this);
    LogUtils.amplitudeLog(this, "Puja Reminder");

    toolbar = findViewById(R.id.toolbar);
    TextView textCustomTitle = (TextView) findViewById(R.id.custom_title);
    Typeface customFont = Typeface.createFromAsset(this.getAssets(), AppConfig.FONT_KAVIVANAR);
    textCustomTitle.setTypeface(customFont);
    setSupportActionBar(toolbar);
    getSupportActionBar().setHomeButtonEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    if (MyApplication.prefManager.getBoolean(PrefManager.POOJA_PREF)) {
      switcher.setChecked(true);
      switcher.setText(getText(R.string.on));
      if (MyApplication.prefManager.getBoolean(PrefManager.POOJA_PREF_6_AM)) {
        ushat_switcher.setChecked(true);
        ushat_switcher.setText(getText(R.string.on));
      } else {
        ushat_switcher.setChecked(false);
        ushat_switcher.setText(getText(R.string.off));
      }
      if (MyApplication.prefManager.getBoolean(PrefManager.POOJA_PREF_8_AM)) {
        kaala_switcher.setChecked(true);
        kaala_switcher.setText(getText(R.string.on));
      } else {
        kaala_switcher.setChecked(false);
        kaala_switcher.setText(getText(R.string.off));
      }
      if (MyApplication.prefManager.getBoolean(PrefManager.POOJA_PREF_12_PM)) {
        uchi_switcher.setChecked(true);
        uchi_switcher.setText(getText(R.string.on));
      } else {
        uchi_switcher.setChecked(false);
        uchi_switcher.setText(getText(R.string.off));
      }
      if (MyApplication.prefManager.getBoolean(PrefManager.POOJA_PREF_6_PM)) {
        saya_switcher.setChecked(true);
        saya_switcher.setText(getText(R.string.on));
      } else {
        saya_switcher.setChecked(false);
        saya_switcher.setText(getText(R.string.off));
      }
      if (MyApplication.prefManager.getBoolean(PrefManager.POOJA_PREF_8_PM)) {
        iranda_switcher.setChecked(true);
        iranda_switcher.setText(getText(R.string.on));
      } else {
        iranda_switcher.setChecked(false);
        iranda_switcher.setText(getText(R.string.off));
      }
      if (MyApplication.prefManager.getBoolean(PrefManager.POOJA_PREF_9_PM)) {
        ardha_switcher.setChecked(true);
        ardha_switcher.setText(getText(R.string.on));
      } else {
        ardha_switcher.setChecked(false);
        ardha_switcher.setText(getText(R.string.off));
      }
    } else {
      switcher.setChecked(false);
      switcher.setText(getText(R.string.off));

      ushat_switcher.setChecked(false);
      kaala_switcher.setChecked(false);
      uchi_switcher.setChecked(false);
      saya_switcher.setChecked(false);
      iranda_switcher.setChecked(false);
      ardha_switcher.setChecked(false);

      ushat_switcher.setText(getText(R.string.off));
      kaala_switcher.setText(getText(R.string.off));
      uchi_switcher.setText(getText(R.string.off));
      saya_switcher.setText(getText(R.string.off));
      iranda_switcher.setText(getText(R.string.off));
      ardha_switcher.setText(getText(R.string.off));
    }

    switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

      @Override
      public void onCheckedChanged(CompoundButton buttonView,
                                   boolean isChecked) {
        // TODO Auto-generated method stub
        if (isChecked) {
          if (!ushat_switcher.isChecked() && !kaala_switcher.isChecked() && !uchi_switcher.isChecked() &&
              !saya_switcher.isChecked() && !iranda_switcher.isChecked() && !ardha_switcher.isChecked()) {

            switcher.setText(getText(R.string.on));

            MyApplication.prefManager.saveBoolean(PrefManager.POOJA_PREF, true);

            MyApplication.prefManager.saveBoolean(PrefManager.POOJA_PREF_6_AM, true);
            MyApplication.prefManager.saveBoolean(PrefManager.POOJA_PREF_8_AM, true);
            MyApplication.prefManager.saveBoolean(PrefManager.POOJA_PREF_12_PM, true);
            MyApplication.prefManager.saveBoolean(PrefManager.POOJA_PREF_6_PM, true);
            MyApplication.prefManager.saveBoolean(PrefManager.POOJA_PREF_8_PM, true);
            MyApplication.prefManager.saveBoolean(PrefManager.POOJA_PREF_9_PM, true);

            ushat_switcher.setChecked(true);
            kaala_switcher.setChecked(true);
            uchi_switcher.setChecked(true);
            saya_switcher.setChecked(true);
            iranda_switcher.setChecked(true);
            ardha_switcher.setChecked(true);

            ushat_switcher.setText(getText(R.string.on));
            kaala_switcher.setText(getText(R.string.on));
            uchi_switcher.setText(getText(R.string.on));
            saya_switcher.setText(getText(R.string.on));
            iranda_switcher.setText(getText(R.string.on));
            ardha_switcher.setText(getText(R.string.on));

            setAllAlarm();
          }
        } else {
          switcher.setText(getText(R.string.off));

          MyApplication.prefManager.saveBoolean(PrefManager.POOJA_PREF, false);

          MyApplication.prefManager.saveBoolean(PrefManager.POOJA_PREF_6_AM, false);
          MyApplication.prefManager.saveBoolean(PrefManager.POOJA_PREF_8_AM, false);
          MyApplication.prefManager.saveBoolean(PrefManager.POOJA_PREF_12_PM, false);
          MyApplication.prefManager.saveBoolean(PrefManager.POOJA_PREF_6_PM, false);
          MyApplication.prefManager.saveBoolean(PrefManager.POOJA_PREF_8_PM, false);
          MyApplication.prefManager.saveBoolean(PrefManager.POOJA_PREF_9_PM, false);

          ushat_switcher.setChecked(false);
          kaala_switcher.setChecked(false);
          uchi_switcher.setChecked(false);
          saya_switcher.setChecked(false);
          iranda_switcher.setChecked(false);
          ardha_switcher.setChecked(false);


          ushat_switcher.setText(getText(R.string.off));
          kaala_switcher.setText(getText(R.string.off));
          uchi_switcher.setText(getText(R.string.off));
          saya_switcher.setText(getText(R.string.off));
          iranda_switcher.setText(getText(R.string.off));
          ardha_switcher.setText(getText(R.string.off));

          cancelAllAlarm();

        }
      }
    });

    ushat_switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

      @Override
      public void onCheckedChanged(CompoundButton buttonView,
                                   boolean isChecked) {
        // TODO Auto-generated method stub

        if (isChecked) {
          if (!MyApplication.prefManager.getBoolean(PrefManager.POOJA_PREF)) {
            switcher.setChecked(true);
            switcher.setText(getText(R.string.on));
            MyApplication.prefManager.saveBoolean(PrefManager.POOJA_PREF, true);
          }
          ushat_switcher.setText(getText(R.string.on));
          MyApplication.prefManager.saveBoolean(PrefManager.POOJA_PREF_6_AM, true);
          createAlarm(AppConfig.Six_Pooja_6_Am_hours);
        } else {
          ushat_switcher.setText(getText(R.string.off));
          MyApplication.prefManager.saveBoolean(PrefManager.POOJA_PREF_6_AM, false);
          cancelAlarm(AppConfig.Six_Pooja_6_Am_hours);
          if (!ushat_switcher.isChecked() && !kaala_switcher.isChecked() && !uchi_switcher.isChecked() &&
              !saya_switcher.isChecked() && !iranda_switcher.isChecked() && !ardha_switcher.isChecked()) {
            switcher.setChecked(false);
            switcher.setText(getText(R.string.off));
            MyApplication.prefManager.saveBoolean(PrefManager.POOJA_PREF, false);

          }
        }
      }
    });
    kaala_switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

      @Override
      public void onCheckedChanged(CompoundButton buttonView,
                                   boolean isChecked) {
        // TODO Auto-generated method stub

        if (isChecked) {
          if (!MyApplication.prefManager.getBoolean(PrefManager.POOJA_PREF)) {
            switcher.setChecked(true);
            switcher.setText(getText(R.string.on));
            MyApplication.prefManager.saveBoolean(PrefManager.POOJA_PREF, true);
          }
          kaala_switcher.setText(getText(R.string.on));

          MyApplication.prefManager.saveBoolean(PrefManager.POOJA_PREF_8_AM, true);
          createAlarm(AppConfig.Six_Pooja_8_Am_hours);
        } else {
          kaala_switcher.setText(getText(R.string.off));
          MyApplication.prefManager.saveBoolean(PrefManager.POOJA_PREF_8_AM, false);
          cancelAlarm(AppConfig.Six_Pooja_8_Am_hours);
          if (!ushat_switcher.isChecked() && !kaala_switcher.isChecked() && !uchi_switcher.isChecked() &&
              !saya_switcher.isChecked() && !iranda_switcher.isChecked() && !ardha_switcher.isChecked()) {
            switcher.setChecked(false);
            switcher.setText(getText(R.string.off));
            MyApplication.prefManager.saveBoolean(PrefManager.POOJA_PREF, false);

          }
        }
      }
    });

    uchi_switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

      @Override
      public void onCheckedChanged(CompoundButton buttonView,
                                   boolean isChecked) {
        // TODO Auto-generated method stub

        if (isChecked) {
          if (!MyApplication.prefManager.getBoolean(PrefManager.POOJA_PREF)) {
            switcher.setChecked(true);
            switcher.setText(getText(R.string.on));
            MyApplication.prefManager.saveBoolean(PrefManager.POOJA_PREF, true);
          }
          uchi_switcher.setText(getText(R.string.on));

          MyApplication.prefManager.saveBoolean(PrefManager.POOJA_PREF_12_PM, true);
          createAlarm(AppConfig.Six_Pooja_12_Pm_hours);
        } else {
          uchi_switcher.setText(getText(R.string.off));
          MyApplication.prefManager.saveBoolean(PrefManager.POOJA_PREF_12_PM, false);
          cancelAlarm(AppConfig.Six_Pooja_12_Pm_hours);
          if (!ushat_switcher.isChecked() && !kaala_switcher.isChecked() && !uchi_switcher.isChecked() &&
              !saya_switcher.isChecked() && !iranda_switcher.isChecked() && !ardha_switcher.isChecked()) {
            switcher.setChecked(false);
            switcher.setText(getText(R.string.off));
            MyApplication.prefManager.saveBoolean(PrefManager.POOJA_PREF, false);

          }
        }
      }
    });

    saya_switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

      @Override
      public void onCheckedChanged(CompoundButton buttonView,
                                   boolean isChecked) {
        // TODO Auto-generated method stub

        if (isChecked) {
          if (!MyApplication.prefManager.getBoolean(PrefManager.POOJA_PREF)) {
            switcher.setChecked(true);
            switcher.setText(getText(R.string.on));
            MyApplication.prefManager.saveBoolean(PrefManager.POOJA_PREF, true);
          }
          saya_switcher.setText(getText(R.string.on));

          MyApplication.prefManager.saveBoolean(PrefManager.POOJA_PREF_6_PM, true);
          createAlarm(AppConfig.Six_Pooja_6_Pm_hours);
        } else {
          saya_switcher.setText(getText(R.string.off));
          MyApplication.prefManager.saveBoolean(PrefManager.POOJA_PREF_6_PM, false);
          cancelAlarm(AppConfig.Six_Pooja_6_Pm_hours);
          if (!ushat_switcher.isChecked() && !kaala_switcher.isChecked() && !uchi_switcher.isChecked() &&
              !saya_switcher.isChecked() && !iranda_switcher.isChecked() && !ardha_switcher.isChecked()) {
            switcher.setChecked(false);
            switcher.setText(getText(R.string.off));
            MyApplication.prefManager.saveBoolean(PrefManager.POOJA_PREF, false);

          }
        }
      }
    });

    iranda_switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

      @Override
      public void onCheckedChanged(CompoundButton buttonView,
                                   boolean isChecked) {
        // TODO Auto-generated method stub

        if (isChecked) {
          if (!MyApplication.prefManager.getBoolean(PrefManager.POOJA_PREF)) {
            switcher.setChecked(true);
            switcher.setText(getText(R.string.on));
            MyApplication.prefManager.saveBoolean(PrefManager.POOJA_PREF, true);
          }
          iranda_switcher.setText(getText(R.string.on));

          MyApplication.prefManager.saveBoolean(PrefManager.POOJA_PREF_8_PM, true);
          createAlarm(AppConfig.Six_Pooja_8_Pm_hours);
        } else {
          iranda_switcher.setText(getText(R.string.off));
          MyApplication.prefManager.saveBoolean(PrefManager.POOJA_PREF_8_PM, false);
          cancelAlarm(AppConfig.Six_Pooja_8_Pm_hours);
          if (!ushat_switcher.isChecked() && !kaala_switcher.isChecked() && !uchi_switcher.isChecked() &&
              !saya_switcher.isChecked() && !iranda_switcher.isChecked() && !ardha_switcher.isChecked()) {
            switcher.setChecked(false);
            switcher.setText(getText(R.string.off));
            MyApplication.prefManager.saveBoolean(PrefManager.POOJA_PREF, false);

          }
        }
      }
    });

    ardha_switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

      @Override
      public void onCheckedChanged(CompoundButton buttonView,
                                   boolean isChecked) {
        // TODO Auto-generated method stub

        if (isChecked) {
          if (!MyApplication.prefManager.getBoolean(PrefManager.POOJA_PREF)) {
            switcher.setChecked(true);
            switcher.setText(getText(R.string.on));
            MyApplication.prefManager.saveBoolean(PrefManager.POOJA_PREF, true);
          }
          ardha_switcher.setText(getText(R.string.on));

          MyApplication.prefManager.saveBoolean(PrefManager.POOJA_PREF_9_PM, true);
          createAlarm(AppConfig.Six_Pooja_9_Pm_hours);
        } else {
          ardha_switcher.setText(getText(R.string.off));
          MyApplication.prefManager.saveBoolean(PrefManager.POOJA_PREF_9_PM, false);
          cancelAlarm(AppConfig.Six_Pooja_9_Pm_hours);
          if (!ushat_switcher.isChecked() && !kaala_switcher.isChecked() && !uchi_switcher.isChecked() &&
              !saya_switcher.isChecked() && !iranda_switcher.isChecked() && !ardha_switcher.isChecked()) {
            switcher.setChecked(false);
            switcher.setText(getText(R.string.off));
            MyApplication.prefManager.saveBoolean(PrefManager.POOJA_PREF, false);

          }
        }
      }
    });
    if (!ushat_switcher.isChecked() && !kaala_switcher.isChecked() && !uchi_switcher.isChecked() &&
        !saya_switcher.isChecked() && !iranda_switcher.isChecked() && !ardha_switcher.isChecked()) {
      switcher.setChecked(false);
      switcher.setText(getText(R.string.off));
      MyApplication.prefManager.saveBoolean(PrefManager.POOJA_PREF, false);

    }

  }

  @Override
  protected void onResume() {
    super.onResume();
    AppConfig.customeLanguage(this);
  }

  void createAlarm(int hours) {

    Calendar calNow1 = Calendar.getInstance();
    Calendar cal1 = (Calendar) calNow1.clone();

    cal1.set(Calendar.HOUR_OF_DAY, hours);
    cal1.set(Calendar.MINUTE, 0);
    cal1.set(Calendar.SECOND, 0);

    if (cal1.compareTo(calNow1) <= 0) {
      // Today Set time passed, count to tomorrow
      cal1.add(Calendar.DAY_OF_MONTH, 1);
    }
    setAlarm(cal1, hours);
  }

  void setAllAlarm() {
    createAlarm(AppConfig.Six_Pooja_6_Am_hours);
    createAlarm(AppConfig.Six_Pooja_8_Am_hours);
    createAlarm(AppConfig.Six_Pooja_12_Pm_hours);
    createAlarm(AppConfig.Six_Pooja_6_Pm_hours);
    createAlarm(AppConfig.Six_Pooja_8_Pm_hours);
    createAlarm(AppConfig.Six_Pooja_9_Pm_hours);

  }

  void cancelAllAlarm() {

    cancelAlarm(AppConfig.Six_Pooja_6_Am_hours);
    cancelAlarm(AppConfig.Six_Pooja_8_Am_hours);
    cancelAlarm(AppConfig.Six_Pooja_12_Pm_hours);
    cancelAlarm(AppConfig.Six_Pooja_6_Pm_hours);
    cancelAlarm(AppConfig.Six_Pooja_8_Pm_hours);
    cancelAlarm(AppConfig.Six_Pooja_9_Pm_hours);
  }

  private void cancelAlarm(int requestcode) {
    Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
    intent.putExtra("requestcode", String.valueOf(requestcode));
    PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), requestcode, intent, 0);
    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    alarmManager.cancel(pendingIntent);
  }

  private void setAlarm(Calendar targetCal, int requestcode) {
    Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
    intent.putExtra("requestcode", String.valueOf(requestcode));
    PendingIntent pendingIntent = PendingIntent.getBroadcast(
        getBaseContext(), requestcode, intent, 0);
    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        alarmManager.setRepeating(AlarmManager.RTC, targetCal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent); //Repeat every 24 hours
    if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
      alarmManager.set(AlarmManager.RTC, targetCal.getTimeInMillis(), pendingIntent);
    } else {
      alarmManager.setExact(AlarmManager.RTC, targetCal.getTimeInMillis(), pendingIntent);
    }
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
  }

}
