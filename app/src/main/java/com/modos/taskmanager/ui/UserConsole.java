package com.modos.taskmanager.ui;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.modos.taskmanager.R;
import com.modos.taskmanager.controller.SwipeToDelete;
import com.modos.taskmanager.controller.TaskListAdapter;
import com.modos.taskmanager.model.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserConsole extends AppCompatActivity implements TaskListAdapter.TasksAdapterListener{

    private FloatingActionButton floatingActionButton;

    private static RecyclerView recyclerView;
    private SearchView searchView;

    static Context c;

    static TextView empty;

    @Override
    public Context getApplicationContext() {
       return c =  super.getApplicationContext();
    }

    public static List<Task> taskList;
    public static TaskListAdapter adapter;

    private static final String KEY_USERNAME_OR_EMAIL = "usernameOrEmail";
    private static final String KEY_NEW_PASSWORD = "newPassword";
    private static final String KEY_TITLE = "title";
    private static final String DELETE_ACCOUNT_URL = "http://192.168.1.5/TaskManager/deleteUser.php";
    private static final String CHANGE_PASSWORD_URL = "http://192.168.1.5/TaskManager/changePassword.php";
    private static final String TASKS_URL = "http://192.168.1.5/TaskManager/showTasks.php";
    private static final String DELETE_TASK_URL = "http://192.168.1.5/TaskManager/deleteTaskFromList.php";
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE= "message";

    private void logout(){
        finish();
    }

    private void deleteAccount(){
        final JSONObject request = new JSONObject();

        try {
            request.put(KEY_USERNAME_OR_EMAIL , MainActivity.usernameOrEmail);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, DELETE_ACCOUNT_URL, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("developer", response.toString());

                try {
                    if (response.getInt(KEY_STATUS) ==  0){
                       finish();
                    }else{
                    }
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

    @Override
    public void onBackPressed() {
        super.onResume();
        if (searchView != null){
            if (!searchView.isIconified()) {
                searchView.setIconified(true);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.log_out_item:
                logout();
                break;
            case R.id.delete_item:
                deleteAccount();
                break;
            case R.id.change_password:
                changePassword();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                adapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        taskList.clear();
        showTasks();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_console);

        recyclerView = findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDelete(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        taskList = new ArrayList<>();
        adapter = new TaskListAdapter(taskList, UserConsole.this);

        empty = findViewById(R.id.empty);


        floatingActionButton = findViewById(R.id.floating_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserConsole.this, CreateTask.class));
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
    private void changePassword(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.change_password_layout);
        dialog.setTitle("Change Password");

        final TextView inputNewPassword = dialog.findViewById(R.id.input_new_password);

        Button submit = dialog.findViewById(R.id.submit_new_password);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final JSONObject request = new JSONObject();

                try {
                    request.put(KEY_USERNAME_OR_EMAIL , MainActivity.usernameOrEmail);
                    request.put(KEY_NEW_PASSWORD , inputNewPassword.getText().toString().trim());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, CHANGE_PASSWORD_URL, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("developer", response.toString());

                        try {
                            if (response.getInt(KEY_STATUS) ==  0){
                                startActivity(new Intent(UserConsole.this, MainActivity.class));
                                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                            }
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

                RequestQueue requestQueue =  Volley.newRequestQueue(UserConsole.this);
                requestQueue.add(jsonObjectRequest);

            }
        });

        dialog.show();
    }

    private void showTasks(){

        StringRequest stringrequest = new StringRequest(Request.Method.POST, TASKS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jarray = new JSONArray(response);
                            for (int i = 0; i < jarray.length(); i++) {
                                JSONObject taskObject = jarray.getJSONObject(i);

                                if (taskObject.getString("username").equals(MainActivity.usernameOrEmail)
                                        || taskObject.getString("email").equals(MainActivity.usernameOrEmail)){
                                    Task task = new Task(taskObject.getString("title"), taskObject.getString("description"),
                                                 taskObject.getInt("year"), taskObject.getInt("month"), taskObject.getInt("day"),
                                            taskObject.getInt("hour"), taskObject.getInt("minute"));

                                    taskList.add(task);
                                }

                            }
                            adapter.notifyDataSetChanged();

                            recyclerView.setAdapter(adapter);

                            if (taskList.isEmpty()){
                                recyclerView.setVisibility(View.GONE);
                                empty.setVisibility(View.VISIBLE);
                            }else{
                                recyclerView.setVisibility(View.VISIBLE);
                                empty.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringrequest);
    }

    public static void deleteItem(int position) {
        String title = taskList.get(position).getTitle();

        UserConsole.taskList.remove(position);

        UserConsole.adapter.notifyDataSetChanged();

        final JSONObject request = new JSONObject();

        try {
            request.put(KEY_USERNAME_OR_EMAIL, MainActivity.usernameOrEmail);
            request.put(KEY_TITLE, title);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, DELETE_TASK_URL, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("developer", error.getMessage());
            }
        });

        RequestQueue requestQueue =  Volley.newRequestQueue(c);
        requestQueue.add(jsonObjectRequest);

        if (taskList.isEmpty()){
            recyclerView.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        }else{
            recyclerView.setVisibility(View.VISIBLE);
            empty.setVisibility(View.GONE);
        }


    }

    @Override
    public void onContactSelected(Task task)
    {
        Toast.makeText(this, task.getDescription(), Toast.LENGTH_SHORT).show();
    }
}

