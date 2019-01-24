package com.modos.taskmanager.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
    Button buttonLogin;
    TextView textRegister;
    Snackbar snackbar;

    // for save login and logout state
    SharedPreferences log;
    SharedPreferences.Editor editor;

    @Override
    protected void onResume() {
        super.onResume();
        inputUsernameOrEmail.setText("");
        inputPassword.setText("");

        if(log.getBoolean("isLogged", false)){
            startActivity(new Intent(MainActivity.this, UserConsole.class));
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }
    }

    private static final String KEY_USERNAME_OR_EMAIL = "usernameOrEmail";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_STATUS = "status";
    private static final String LOGIN_URL = "http://192.168.1.5/TaskManager/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        log = getSharedPreferences("Log", Context.MODE_PRIVATE);
        editor = log.edit();

        inputUsernameOrEmail = findViewById(R.id.mainInputUsernameOrEmail);
        inputPassword = findViewById(R.id.registerInputPassword);
        buttonLogin = findViewById(R.id.mainButtonLogin);
        textRegister = findViewById(R.id.register_text);

        inputUsernameOrEmail.addTextChangedListener(new MyTextWatcher(inputUsernameOrEmail));
        inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));

        textRegister.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_text:
                startActivity(new Intent(MainActivity.this, Register.class));
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                break;
            case R.id.mainButtonLogin:
                login();
                break;
            default:
                break;
        }
    }

    private void login(){
        final JSONObject request = new JSONObject();

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
                    if (response.getInt(KEY_STATUS) ==  0){
                        startActivity(new Intent(MainActivity.this, UserConsole.class));
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

                        editor.putBoolean("isLogged", true);
                        editor.putString("usernameOrEmail", inputUsernameOrEmail.getText().toString().trim());
                        editor.commit();

                    }else{
                        showSnackbar(response.getString(KEY_MESSAGE));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                    showSnackbar("some error happened");
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
            buttonLogin.setBackground(getDrawable(R.drawable.disabled_button));

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
                    buttonLogin.setBackground(getDrawable(R.drawable.background_button));
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
        snackbar.make(findViewById(android.R.id.content), string, Snackbar.LENGTH_SHORT)
                .setActionTextColor(getResources().getColor(R.color.colorPrimary)).show();
    }
}
