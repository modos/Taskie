package com.modos.taskmanager.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.modos.taskmanager.R;
import com.modos.taskmanager.model.Task;
import com.modos.taskmanager.ui.MainActivity;
import com.modos.taskmanager.ui.UserConsole;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.MyViewHolder>{

    private static List<Task> taskList;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, description, year, month, day, hour, minute;

        public MyViewHolder(View view) {
            super(view);
             title =  view.findViewById(R.id.task_title_item);
            description = view.findViewById(R.id.task_description_item);
           year =  view.findViewById(R.id.task_year_item);
            month = view.findViewById(R.id.task_month_item);
             day = view.findViewById(R.id.task_day_item);
             hour = view.findViewById(R.id.task_hour_item);
            minute = view.findViewById(R.id.task_minute_item);
        }
    }

    public TaskListAdapter(List<Task> taskList) {
        this.taskList = taskList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tasks_list_layout, parent, false);

        return new MyViewHolder(itemView);    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Task task = taskList.get(position);

        holder.title.setText(task.getTitle());
        holder.description.setText(task.getDescription());
        holder.year.setText(Integer.toString(task.getYear()));
        holder.month.setText(Integer.toString(task.getMonth()));
        holder.day.setText(Integer.toString(task.getDay()));
        holder.hour.setText(Integer.toString(task.getHour()));
        holder.minute.setText(Integer.toString(task.getMinute()));
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    }
