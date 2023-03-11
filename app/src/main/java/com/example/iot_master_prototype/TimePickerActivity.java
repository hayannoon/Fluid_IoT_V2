package com.example.iot_master_prototype;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TimePicker;

import androidx.annotation.Nullable;

public class TimePickerActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timepicker);

        TimePicker startTimePicker = findViewById(R.id.start_time_picker);
        TimePicker endTimePicker = findViewById(R.id.end_time_picker);

        startTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                // Do something when start time is changed
            }
        });

        endTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                // Do something when end time is changed
            }
        });



    }
}
