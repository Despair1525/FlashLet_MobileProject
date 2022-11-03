package com.example.prm_final_project.Ui.Fragment;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.example.prm_final_project.R;
import com.example.prm_final_project.Services.ReminderBroadcast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

public class NotificationSettingFragment extends PreferenceFragmentCompat {
    private static Preference editTimeChosen;
    private SwitchPreference switchButton;
    private EditTextPreference message;
    private int hour, minute;

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.setting_screen, rootKey);
        init();

        createNotificationChannel();
        onClickSwitchNotification();
        onClickTimeSetting();
        onMessageChange();
    }

    private final static String MY_PREFERENCE = "preference_notification";
    private final static String TIME_CHOSEN = "time_chosen";
    private static final String NOTIFICATION_MESSAGE = "message";
    private final static String NOTIFICATION_CHANNEL= "NC011";



    private void onClickTimeSetting(){
        editTimeChosen.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        hour = selectedHour;
                        minute = selectedMinute;
                        String newValue = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
                        editTimeChosen.setSummary(newValue);
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(
                                MY_PREFERENCE, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(TIME_CHOSEN, newValue);
                        editor.commit();
                        cancelAlarmNotification();
                        callAlarm();
                    }
                };

                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        getActivity(), onTimeSetListener, hour, minute, true );
                timePickerDialog.setTitle("Set time to remind!");
                timePickerDialog.show();
                return false;
            }
        });
    }

    private void onClickSwitchNotification(){
        switchButton.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
                Boolean isChecked = (Boolean) newValue;
                if(isChecked){
                    Toast.makeText(getActivity(), "Reminder set!", Toast.LENGTH_SHORT).show();
                    callAlarm();
                } else {
                    cancelAlarmNotification();
                }
                return true;
            }
        });
    }

    private void onMessageChange(){
        message.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
                String newMessage = newValue.toString();
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MY_PREFERENCE, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(NOTIFICATION_MESSAGE, newMessage);
                editor.commit();

                return true;
            }
        });
    }

    public void callAlarm(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MY_PREFERENCE, Context.MODE_PRIVATE);
        String[] time = sharedPreferences.getString(TIME_CHOSEN, "20:00").split(":");

        hour = Integer.parseInt(time[0]);
        minute = Integer.parseInt(time[1]);
        setAlarmNotification(hour, minute);
    }
    final int flag =  Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE : PendingIntent.FLAG_UPDATE_CURRENT;


    private void setAlarmNotification(int hour, int minute){
//        String message = getDataFromFile();
        Intent intent = new Intent(requireContext(), ReminderBroadcast.class);
//        intent.putExtra("random_message", message);
//        Log.i("random_message", message);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                requireContext(), 2, intent, flag);

        AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        Log.i("time_setup ", "" + hour + ":" + minute);

        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void cancelAlarmNotification(){
        Intent intent = new Intent(requireContext(), ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                requireContext(), 2, intent, flag);

        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(
                    NOTIFICATION_CHANNEL, "Demo Channel", importance);
            channel.enableVibration(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            channel.setVibrationPattern(new long[] { 1000, 1000, 1000, 1000, 1000 });

            NotificationManager notificationManager =
                    (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private String getDataFromFile(){
        StringBuffer buffer = new StringBuffer();
        Scanner scanner = new Scanner(getResources().openRawResource(R.raw.sample_messages));

        ArrayList<String> array = new ArrayList<>();
        while(scanner.hasNext()){
            String line = scanner.nextLine();
            array.add(line);
        }
        scanner.close();

        Random rand = new Random();

        int randomIndex = rand.nextInt(array.size());
        return array.get(randomIndex);
    }

    public void init(){
        editTimeChosen = findPreference("btn_choose_time");
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MY_PREFERENCE, Context.MODE_PRIVATE);
        String value = sharedPreferences.getString(TIME_CHOSEN, "20:00");
        editTimeChosen.setSummary(value);

        String[] time = value.split(":");
        hour = Integer.parseInt(time[0]);
        minute = Integer.parseInt(time[1]);

        switchButton = findPreference("noti_switch_preference");

        message = findPreference("notification_message");
        String originMessage = sharedPreferences.getString(NOTIFICATION_MESSAGE, null);
        message.setText(originMessage);
    }
}
