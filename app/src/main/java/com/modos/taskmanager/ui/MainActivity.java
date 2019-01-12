package com.modos.taskmanager.ui;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.modos.taskmanager.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    EditText inputUsernameOrEmail, inputPassword;
    Button buttonLogin, buttonRegister;
    Snackbar snackbar;

    private static final String KEY_USERNAME_OR_EMAIL = "usernameOrEmail";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_STATUS = "status";
    private static final String LOGIN_URL = "http://172.20.174.99/TaskManager/login.php";
    private static boolean loggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputUsernameOrEmail = (EditText) findViewById(R.id.mainInputUsernameOrEmail);
        inputPassword = (EditText) findViewById(R.id.registerInputPassword);
        buttonLogin = (Button) findViewById(R.id.mainButtonLogin);
        buttonRegister = (Button) findViewById(R.id.mainButtonRegister);

        inputUsernameOrEmail.addTextChangedListener(new MyTextWatcher(inputUsernameOrEmail));
        inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));

        buttonRegister.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mainButtonRegister:
                startActivity(new Intent(MainActivity.this, Register.class));
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                break;
            case R.id.mainButtonLogin:
                login();

                if (loggedIn){
                    startActivity(new Intent(MainActivity.this, UserConsole.class));
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                }

                break;
            default:
                break;
        }
    }

    private void login(){
        JSONObject request = new JSONObject();

        try {
            request.put(KEY_USERNAME_OR_EMAIL , inputUsernameOrEmail.getText().toString().trim());
            request.put(KEY_PASSWORD , inputPassword.getText().toString().trim());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, LOGIN_URL, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("developer", response.toString());

                try {
                    if (response.getInt(KEY_STATUS) == 0){
                        loggedIn = true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            buttonLogin.setEnabled(false);

                switch (view.getId()){
                    case R.id.mainInputUsernameOrEmail:
                        validateUsernameOrEmail();
                        break;
                    case R.id.registerInputPassword:
                        validatePassword();
                        break;
                    default:
                        break;
                }

                if (validateUsernameOrEmail() && validatePassword()){
                    buttonLogin.setEnabled(true);
                }
        }
    }

    private boolean validateUsernameOrEmail(){
        if (inputUsernameOrEmail.getText().toString().trim().isEmpty()){
            inputUsernameOrEmail.setError("enter username or email");
            return false;
        }

        return true;
    }

    private boolean validatePassword(){
        if (inputPassword.getText().toString().trim().isEmpty()){
            inputPassword.setError("enter password");
            return false;
        }

        return true;
    }

    private void showSnackbar(String string){
        snackbar.make(findViewById(android.R.id.content), string.toString(), Snackbar.LENGTH_SHORT)
                .setActionTextColor(getResources().getColor(R.color.colorPrimary)).show();
    }
}