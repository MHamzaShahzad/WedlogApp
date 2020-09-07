package com.example.wedlogapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wedlogapp.Common;
import com.example.wedlogapp.Constants;
import com.example.wedlogapp.R;
import com.example.wedlogapp.SessionManagement;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = LoginActivity.class.getName();
    private Context mContext;

    TextInputEditText textInputEmail;
    TextInputEditText textInputPassword;
    Button btnLogin;
    TextView linkRegister;

    SessionManagement sessionManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = this;
        sessionManagement = new SessionManagement(mContext);

        if (sessionManagement.getToken() != null) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }
        textInputEmail = findViewById(R.id.textInputEmail);
        textInputPassword = findViewById(R.id.textInputPassword);
        btnLogin = findViewById(R.id.btnLogin);
        linkRegister = findViewById(R.id.linkRegister);

        btnLogin.setOnClickListener(this);
        linkRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == btnLogin.getId()) {
            if (TextUtils.isEmpty(textInputEmail.getText()))
                textInputEmail.setError("Field is required.");
            else if (TextUtils.isEmpty(textInputPassword.getText()))
                textInputPassword.setError("Field is required.");
            else if (!TextUtils.isEmpty(textInputEmail.getText()) && !Patterns.EMAIL_ADDRESS.matcher(textInputEmail.getText()).matches())
                textInputEmail.setError("Invalid email address.");
            else {
                JSONObject params = new JSONObject();
                try {
                    params.put("email", textInputEmail.getText().toString());
                    params.put("password", textInputPassword.getText().toString());
                    loginIntoAccount(params);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        if (view.getId() == linkRegister.getId()) {
            startActivity(new Intent(LoginActivity.this, RegisterAccountActivity.class));
            finish();
        }
    }

    private void loginIntoAccount(final JSONObject params) {
        RequestQueue queue = Volley.newRequestQueue(mContext);
        StringRequest loginRequest = new StringRequest(Request.Method.POST, Constants.SERVER_API_URL + "accounts/api-token-auth/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse-loginIntoAccount: " + response);
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    sessionManagement.setToken(jsonResponse.getString("token"));
                    sessionManagement.setUserName(jsonResponse.getString("user"));
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onErrorResponse: " + error);
                Common.volleyErrorFormatter(mContext, error);
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                Log.i(TAG, "getBody: " + params.toString().getBytes());
                try {
                    return params.toString().getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
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
        queue.add(loginRequest);
    }
}