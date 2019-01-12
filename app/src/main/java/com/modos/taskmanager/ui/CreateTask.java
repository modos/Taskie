package com.modos.taskmanager.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.modos.taskmanager.R;

import java.util.Calendar;

public class CreateTask extends AppCompatActivity implements View.OnClickListener{

    EditText title, description;
    Button setDateButton, setTimeButton, submit;

    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        title = findViewById(R.id.title_task);
        description = findViewById(R.id.description_task);
        setDateButton = findViewById(R.id.btnDatePicker);
        setTimeButton = findViewById(R.id.btnTimePicker);
        submit = findViewById(R.id.buttonSubmitTask);

        setDateButton.setOnClickListener(this);
        setTimeButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnDatePicker:
                setDateButtonListener();
                break;
            case R.id.btnTimePicker:
                setTimeButtonListener();
                break;
        }
    }

    private void setDateButtonListener(){
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        Toast.makeText(CreateTask.this, dayOfMonth + "-" + (monthOfYear + 1) + "-" + year, Toast.LENGTH_SHORT).show();

                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.show();
    }

    private void setTimeButtonListener(){
            Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                       Toast.makeText(CreateTask.this, hourOfDay + ":" + minute, Toast.LENGTH_SHORT).show();
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }
}
