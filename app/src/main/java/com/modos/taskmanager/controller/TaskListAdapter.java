package com.modos.taskmanager.controller;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import com.modos.taskmanager.R;
import com.modos.taskmanager.model.Task;


import java.util.ArrayList;
import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.MyViewHolder>
                implements Filterable{

    private List<Task> taskList;
    private List<Task> taskListFiltered;
    private TasksAdapterListener listener;

    public interface TasksAdapterListener {
        void onContactSelected(Task task);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    taskListFiltered = taskList;
                } else {
                    List<Task> filteredList = new ArrayList<>();
                    for (Task row : taskList) {

                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase()) || row.getDescription().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    taskListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = taskListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                taskListFiltered = (ArrayList<Task>  ) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public  class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title, description, year, month, day, hour, minute;

        public MyViewHolder(View view) {
            super(view);
             title =  view.findViewById(R.id.task_title_item);
            description = view.findViewById(R.id.task_description_item);
           year =  view.findViewById(R.id.task_year_item);
            month = view.findViewById(R.id.task_month_item);
             day = view.findViewById(R.id.task_day_item);
             hour = view.findViewById(R.id.task_hour_item);
            minute = view.findViewById(R.id.task_minute_item);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onContactSelected(taskListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

    public TaskListAdapter(List<Task> taskList, TasksAdapterListener listener)
    {
        this.taskList = taskList;
        this.taskListFiltered = taskList;
        this.listener = listener;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tasks_list_layout, parent, false);

        return new MyViewHolder(itemView);    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Task task = taskListFiltered.get(position);

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
        return taskListFiltered.size();
    }

    }
