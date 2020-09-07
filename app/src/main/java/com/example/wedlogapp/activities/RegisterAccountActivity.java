package com.example.wedlogapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wedlogapp.Common;
import com.example.wedlogapp.Constants;
import com.example.wedlogapp.R;
import com.example.wedlogapp.SessionManagement;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegisterAccountActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = RegisterAccountActivity.class.getName();
    private Context mContext;

    TextInputEditText textInputUserName, textInputEmail, textInputPassword, textInputConfirmPassword;
    CheckBox termsAndConditions;
    Button btnRegister;
    TextView btnAlreadyHaveAccount;

    private SessionManagement sessionManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);

        mContext = this;
        sessionManagement = new SessionManagement(mContext);

        textInputUserName = findViewById(R.id.textInputUserName);
        textInputEmail = findViewById(R.id.textInputEmail);
        textInputPassword = findViewById(R.id.textInputPassword);
        textInputConfirmPassword = findViewById(R.id.textInputConfirmPassword);
        termsAndConditions = findViewById(R.id.termsAndConditions);
        btnRegister = findViewById(R.id.btnRegister);
        btnAlreadyHaveAccount = findViewById(R.id.btnAlreadyHaveAccount);

        btnRegister.setOnClickListener(this);
        btnAlreadyHaveAccount.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == btnRegister.getId()) {

            if (TextUtils.isEmpty(textInputUserName.getText()))
                textInputUserName.setError("Field is required.");
            else if (TextUtils.isEmpty(textInputEmail.getText()))
                textInputEmail.setError("Field is required.");
            else if (TextUtils.isEmpty(textInputPassword.getText()))
                textInputPassword.setError("Field is required.");
            else if (TextUtils.isEmpty(textInputConfirmPassword.getText()))
                textInputConfirmPassword.setError("Field is required.");
            else if (TextUtils.isEmpty(textInputPassword.getText()))
                textInputPassword.setError("Field is required.");
            /*else if (!TextUtils.isEmpty(textInputEmail.getText()) && !Patterns.EMAIL_ADDRESS.matcher(textInputEmail.getText()).matches())
                textInputEmail.setError("Invalid email address.");
            else if (!TextUtils.isEmpty(textInputPassword.getText()) && !isValidPassword(textInputPassword.getText().toString()))
                textInputPassword.setError("Invalid Password");
            else if (!TextUtils.isEmpty(textInputPassword.getText()) && !TextUtils.isEmpty(textInputConfirmPassword.getText()) && isValidPassword(textInputPassword.getText().toString()) && !textInputPassword.getText().equals(textInputConfirmPassword.getText()))
                textInputConfirmPassword.setError("Password don't matches");*/
            else if (!TextUtils.isEmpty(textInputPassword.getText()) && !TextUtils.isEmpty(textInputConfirmPassword.getText()) && !textInputPassword.getText().toString().equals(textInputConfirmPassword.getText().toString()))
                textInputConfirmPassword.setError("Password don't matches");
            else
                registerNewAccount();

            registerNewAccount();
        }
        if (view.getId() == btnAlreadyHaveAccount.getId())
            moveToLogin();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveToLogin();
    }

    private void registerNewAccount() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest registerRequest = new StringRequest(Request.Method.POST, Constants.SERVER_API_URL + "accounts/register/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse-registerNewAccount: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    sessionManagement.setUserName(jsonObject.getString("username"));
                    sessionManagement.setToken(jsonObject.getString("token"));
                    Toast.makeText(mContext, jsonObject.getString("success_msg"), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterAccountActivity.this, HomeActivity.class));
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Common.volleyErrorFormatter(mContext, error);
            }
        }) {
            /*@Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", textInputEmail.getText().toString());
                params.put("password", textInputPassword.getText().toString());
                params.put("password2", textInputConfirmPassword.getText().toString());
                params.put("email", textInputPassword.getText().toString());
                return params;
            }*/

            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject requestBodyObject = new JSONObject();
                try {
                    requestBodyObject.put("username", textInputUserName.getText().toString());
                    requestBodyObject.put("email", textInputEmail.getText().toString());
                    requestBodyObject.put("password", textInputPassword.getText().toString());
                    requestBodyObject.put("password2", textInputConfirmPassword.getText().toString());
                    Log.i(TAG, "getBody: " + requestBodyObject);
                    return requestBodyObject.toString().getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return super.getBody();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        // Add the request to the RequestQueue.
        queue.add(registerRequest);
    }

    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    private void moveToLogin() {
        startActivity(new Intent(RegisterAccountActivity.this, LoginActivity.class));
        finish();
    }
}