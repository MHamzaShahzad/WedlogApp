package com.example.wedlogapp.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
import java.util.HashMap;
import java.util.Map;

public class OrderEventVendorFragment extends DialogFragment {

    private static final String TAG = OrderEventVendorFragment.class.getName();
    private Context mContext;
    private String slug;

    public OrderEventVendorFragment(String slug) {
        this.slug = slug;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getContext();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_event_vendor, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final TextInputEditText textInputBookingDate = view.findViewById(R.id.textInputBookingDate);
        final TextInputEditText textInputEventType = view.findViewById(R.id.textInputEventType);
        Button btnOrderEventVendor = view.findViewById(R.id.btnOrderEventVendor);

        btnOrderEventVendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderEventVendor(textInputBookingDate.getText().toString(), textInputEventType.getText().toString(), slug);
            }
        });
    }

    private void orderEventVendor(final String date, final String type, final String slug) {
        RequestQueue queue = Volley.newRequestQueue(mContext);
        StringRequest loginRequest = new StringRequest(Request.Method.POST, Constants.SERVER_HOST + slug, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse-createHall: " + response);
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
                map.put("Authorization", Constants.API_PRE_TOKEN + new SessionManagement(mContext).getToken());
                return map;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    Log.e(TAG, "getBody: " + date);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("date", date);
                    jsonObject.put("event_type", type);
                    return jsonObject.toString().getBytes("UTF-8");
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
        queue.add(loginRequest);
    }

}