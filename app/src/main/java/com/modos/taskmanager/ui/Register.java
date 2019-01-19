package com.modos.taskmanager.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.support.design.widget.Snackbar;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity{

    EditText inputUsername, inputPassword, inputConfirmPassword, inputEmail;
    Button buttonSubmit;
    RadioGroup radioGroup;
    RadioButton goldRadio, silverRadio, bronzeRadio;
    Snackbar snackbar;

    String role = "";

    private static final String KEY_USERNAME = "username";
    private static final String KEY_Password= "password";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_ROLE = "role";
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";

    private static final String REGISTER_URL = "http://172.20.179.65/TaskManager/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputUsername = findViewById(R.id.registerInputUsername);
        inputPassword = findViewById(R.id.registerInputPassword);
        inputConfirmPassword = findViewById(R.id.registerInputConfirmPassword);
        inputEmail = findViewById(R.id.registerInputEmail);
        buttonSubmit = findViewById(R.id.registerButtonSubmit);
        radioGroup = findViewById(R.id.radioGroup);
        goldRadio = findViewById(R.id.goldRadio);
        silverRadio = findViewById(R.id.silverRadio);
        bronzeRadio = findViewById(R.id.bronzeRadio);

        inputUsername.addTextChangedListener(new MyTextWatcher(inputUsername));
        inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));
        inputConfirmPassword.addTextChangedListener(new MyTextWatcher(inputConfirmPassword));
        inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ischecked = true;

                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.goldRadio:
                        role = "gold";
                        break;
                    case R.id.silverRadio:
                        role = "silver";
                        break;
                    case R.id.bronzeRadio:
                        role = "bronze";
                        break;
                    default:
                        ischecked = false;
                        Toast.makeText(Register.this, "choose a role" , Toast.LENGTH_SHORT).show();
                        break;
                }


                    register();

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    private void register(){

        JSONObject request = new JSONObject();

        try{
            request.put(KEY_USERNAME, inputUsername.getText().toString().trim());
            request.put(KEY_Password, inputPassword.getText().toString().trim());
            request.put(KEY_EMAIL, inputEmail.getText().toString().trim());
            request.put(KEY_ROLE, role);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, REGISTER_URL, request, new Response.Listener<JSONObject>() {
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

    private class MyTextWatcher implements TextWatcher{

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
                buttonSubmit.setEnabled(false);

                switch (view.getId()){
                    case R.id.registerInputUsername:
                        validateUsername();
                        break;
                    case R.id.registerInputPassword:
                        validatePassword();
                        break;
                    case R.id.registerInputConfirmPassword:
                        validateConfirmPassword();
                        break;
                    case R.id.registerInputEmail:
                        validateEmail();
                        break;
                    default:
                        break;
                }

                if (validateUsername() && validatePassword() && validateConfirmPassword()
                        && validateEmail()){
                    buttonSubmit.setEnabled(true);
                }
        }
    }

    private boolean validateUsername(){
        String username = inputUsername.getText().toString().trim();

        Pattern pattern = Pattern.compile("[a-zA-Z0-9]*");
        Matcher matcher = pattern.matcher(username);

        if (username.isEmpty()){
            inputUsername.setError("enter username");
            return false;
        }else if (username.length() < 8){
            inputUsername.setError("must be more than 7 characters");
            return false;
        }else if(Character.isDigit(username.charAt(0))){
            inputUsername.setError("first character must letter");
            return false;
        }else if (!matcher.matches()){
            inputUsername.setError("must not contains special characters");
            return false;
        }
        return true;
    }

    private boolean validatePassword(){
        String password = inputPassword.getText().toString().trim();

        boolean findDigit = Pattern.compile( "[0-9]" ).matcher(password).find();
        boolean findUpper = Pattern.compile( "[A-Z]" ).matcher(password).find();

        if (password.isEmpty()){
            inputPassword.setError("enter password");
            return false;
        }else if (!findDigit){
            inputPassword.setError("must contain at least 1 number");
            return false;
        }else if (!findUpper){
            inputPassword.setError("must contain at least 1 upper character");
            return false;
        }else if (password.length() < 8){
            inputPassword.setError("must be more than 7 character");
            return false;
        }

        return true;
    }

    private boolean validateConfirmPassword(){

        if (!inputConfirmPassword.getText().toString().trim().equals(
                inputPassword.getText().toString().trim()
        )){
            inputConfirmPassword.setError("must match with password");
            return false;
        }

        return true;
    }

    private boolean validateEmail(){
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty()){
            inputEmail.setError("enter email");
            return false;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches() || !email.contains(".com")){
            inputEmail.setError("format is not correct");
            return false;
        }

        return true;
    }

    private void showSnackbar(String string){
        snackbar.make(findViewById(android.R.id.content), string.toString(), Snackbar.LENGTH_SHORT)
                .setActionTextColor(getResources().getColor(R.color.colorPrimary)).show();
    }

}
