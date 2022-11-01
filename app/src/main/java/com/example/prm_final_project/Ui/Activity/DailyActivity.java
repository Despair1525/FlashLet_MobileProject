package com.example.prm_final_project.Ui.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prm_final_project.Dao.UserDao;
import com.example.prm_final_project.Model.User;
import com.example.prm_final_project.R;
import com.example.prm_final_project.Util.Methods;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DailyActivity extends AppCompatActivity {
    CompactCalendarView compactCalendar;
    TextView month_year;
    ImageView backButton;
    TextView tvStreak,tvLong;

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);


        // Settings calender
        compactCalendar = (CompactCalendarView) findViewById(R.id.calendar_view);
        month_year = findViewById(R.id.tvMonthYear);
        backButton = findViewById(R.id.backButton);
        tvStreak =findViewById(R.id.tvStreakNum);
        tvLong = findViewById(R.id.tvLongestStreak);
        tvStreak.setText(UserDao.getCurrentUser().getCurrentStreak()+" Days");
        tvLong.setText(UserDao.getCurrentUser().getLongestStreak()+" Days");
        
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setMonth_year(compactCalendar.getFirstDayOfCurrentMonth());


        // Use constants provided by Java Calendar class
        compactCalendar.setFirstDayOfWeek(Calendar.MONDAY);
        compactCalendar.setContextClickable(false);
        compactCalendar.setShouldDrawDaysHeader(false);

        compactCalendar.shouldDrawIndicatorsBelowSelectedDays(false);
        compactCalendar.shouldScrollMonth(false);
        compactCalendar.shouldDrawIndicatorsBelowSelectedDays(false);
        compactCalendar.setEventIndicatorStyle(1);
        compactCalendar.setCurrentDayIndicatorStyle(2);


        initDaily();
//        enableDisableView(compactCalendar,false);
        // define a listener to receive callbacks when certain events happen.

    }
    public static void enableDisableView(View view, boolean enabled) {
        view.setEnabled(enabled);
        if ( view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup)view;

            for ( int idx = 0 ; idx < group.getChildCount() ; idx++ ) {
                enableDisableView(group.getChildAt(idx), enabled);
            }
        }
    }
    public void initDaily() {
        User user = UserDao.getCurrentUser();
        if (user != null) {
            for (String date : user.getDaily()) {
                try {
                    Event ev1 = null;
                    ev1 = new Event(Color.parseColor("#64A46C"), Methods.convertDateString(date));
                    compactCalendar.addEvent(ev1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void setMonth_year(Date date) {
        month_year.setText(Methods.convertMonthYear(date));
    }



}