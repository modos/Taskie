package com.modos.taskmanager.ui;

import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.modos.taskmanager.R;

import org.json.JSONException;
import org.json.JSONObject;

public class UserConsole extends AppCompatActivity {

    private FloatingActionButton floatingActionButton;

    private static final String KEY_USERNAME_OR_EMAIL = "usernameOrEmail";
    private static final String KEY_NEW_PASSWORD = "newPassword";
    private static final String DELETE_ACCOUNT_URL = "http://172.20.174.224/TaskManager/deleteUser.php";
    private static final String CHANGE_PASSWORD_URL = "http://172.20.174.224/TaskManager/changePassword.php";
    private static final String KEY_STATUS = "status";

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
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_console);


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
}
