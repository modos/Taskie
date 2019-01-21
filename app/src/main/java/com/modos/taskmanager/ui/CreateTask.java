package com.modos.taskmanager.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.modos.taskmanager.R;
import com.modos.taskmanager.controller.TaskListAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class CreateTask extends AppCompatActivity implements View.OnClickListener{

    EditText title, description;
    Button setDateButton, setTimeButton, submit;
    Snackbar snackbar;

    private int mYear, mMonth, mDay, mHour, mMinute;

    private static final String KEY_USERNAME_OR_EMAIL = "usernameOrEmail";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_YEAR = "year";
    private static final String KEY_MONTH = "month";
    private static final String KEY_DAY = "day";
    private static final String KEY_HOUR = "hour";
    private static final String KEY_MINUTE = "minute";
    private static final String KEY_CREATE_TASK_URL = "http://192.168.1.5/TaskManager/createTask.php";
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";

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
        submit.setOnClickListener(this);


    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

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
            case R.id.buttonSubmitTask:
                submit();
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
                        mYear = year;mMonth = (monthOfYear + 1);mDay = dayOfMonth;

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
                        mMinute = minute;mHour = hourOfDay;
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private void submit(){
        if (title.getText().toString().trim().isEmpty()){
            title.setError("set a title ");
        }else{
            JSONObject request = new JSONObject();

            try {
                request.put(KEY_USERNAME_OR_EMAIL , MainActivity.usernameOrEmail);
                request.put(KEY_TITLE , title.getText().toString().trim());
                request.put(KEY_DESCRIPTION , description.getText().toString().trim());
                request.put(KEY_YEAR , mYear);
                request.put(KEY_MONTH , mMonth);
                request.put(KEY_DAY , mDay);
                request.put(KEY_HOUR , mHour);
                request.put(KEY_MINUTE , mMinute);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, KEY_CREATE_TASK_URL, request, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("developer", response.toString());

                    try {
                        showSnackbar(response.getString(KEY_MESSAGE));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();

                }
            });

            RequestQueue requestQueue =  Volley.newRequestQueue(this);
            requestQueue.add(jsonObjectRequest);
        }
    }

    private void showSnackbar(String string){
        snackbar.make(findViewById(android.R.id.content), string.toString(), Snackbar.LENGTH_SHORT)
                .setActionTextColor(getResources().getColor(R.color.colorPrimary)).show();
    }
}
