package com.example.wedlogapp.activities.ui.profile;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.wedlogapp.adapters.halls.HallsAdapter;
import com.example.wedlogapp.models.Hall;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ProfileFragment extends Fragment {

    private static final String TAG = ProfileFragment.class.getName();
    private Context mContext;
    private ProfileViewModel profileViewModel;

    private ImageView profile_photo;
    private TextInputEditText textInputUserName, textInputEmail, textInputPhoneNumber, textInputAddress;
    private Button btnUpdateAccount;

    private SessionManagement sessionManagement;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();
        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        sessionManagement = new SessionManagement(mContext);
        initLayoutWidgets(root);
        loadProfileData(sessionManagement.getUserName());
        /*profileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });*/
        return root;
    }

    private void initLayoutWidgets(View view) {
        profile_photo = view.findViewById(R.id.profile_photo);
        textInputUserName = view.findViewById(R.id.textInputUserName);
        textInputEmail = view.findViewById(R.id.textInputEmail);
        textInputPhoneNumber = view.findViewById(R.id.textInputPhoneNumber);
        textInputAddress = view.findViewById(R.id.textInputAddress);
        btnUpdateAccount = view.findViewById(R.id.btnUpdateAccount);
    }

    private void setBtnUpdateAccount() {
        btnUpdateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("username", textInputUserName.getText().toString());
                    jsonObject.put("email", textInputEmail.getText().toString());
                    JSONObject jsonObjectProfile = new JSONObject();
                    jsonObject.put("profile", jsonObjectProfile.put("address", textInputAddress.getText().toString()));
                    jsonObject.put("profile", jsonObjectProfile.put("phone_num", textInputAddress.getText().toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                updateProfileData(sessionManagement.getUserName(), jsonObject);
            }
        });
    }

    private void loadProfileData(String username) {
        RequestQueue queue = Volley.newRequestQueue(mContext);
        StringRequest loginRequest = new StringRequest(Request.Method.GET, Constants.SERVER_API_URL + "accounts/profile/" + username + "/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse-loadProfileData: " + response);
                try {
                    JSONObject responseObject = new JSONObject(response);
                    textInputUserName.setText(responseObject.getString("username"));
                    textInputEmail.setText(responseObject.getString("email"));
                    textInputAddress.setText(responseObject.getJSONObject("profile").getString("address"));
                    textInputPhoneNumber.setText(responseObject.getJSONObject("profile").getString("phone_num"));
                    Picasso.get().load(responseObject.getJSONObject("profile").getString("profile_pic")).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).centerInside().fit().into(profile_photo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                setBtnUpdateAccount();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onErrorResponse: " + error);
                Common.volleyErrorFormatter(mContext, error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", Constants.API_PRE_TOKEN + sessionManagement.getToken());
                return map;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(loginRequest);
    }

    private void updateProfileData(String username, final JSONObject requestParams) {
        RequestQueue queue = Volley.newRequestQueue(mContext);
        StringRequest loginRequest = new StringRequest(Request.Method.PUT, Constants.SERVER_API_URL + "accounts/profile/" + username + "/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse-loadProfileData: " + response);
                Toast.makeText(mContext, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onErrorResponse: " + error);
                Common.volleyErrorFormatter(mContext, error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", Constants.API_PRE_TOKEN + sessionManagement.getToken());
                return map;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    Log.e(TAG, "getBody: " + requestParams);
                    return requestParams.toString().getBytes("UTF-8");
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