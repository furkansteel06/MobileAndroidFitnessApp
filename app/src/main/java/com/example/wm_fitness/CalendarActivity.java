package com.example.wm_fitness;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.HashSet;

public class CalendarActivity extends AppCompatActivity {

    MaterialCalendarView materialCalendarView;
    HashSet<CalendarDay> list = new HashSet<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        bindViews();
        HashSet<CalendarDay> convertedList = new HashSet<>();


    }

    public void bindViews(){
        materialCalendarView = (MaterialCalendarView)findViewById(R.id.calendar);
    }
}