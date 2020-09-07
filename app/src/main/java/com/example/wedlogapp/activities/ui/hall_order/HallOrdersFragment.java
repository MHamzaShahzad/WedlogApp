package com.example.wedlogapp.activities.ui.hall_order;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.example.wedlogapp.adapters.hall_orders.HallOrdersAdapter;
import com.example.wedlogapp.adapters.halls.HallsAdapter;
import com.example.wedlogapp.models.Hall;
import com.example.wedlogapp.models.HallOrder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HallOrdersFragment extends Fragment {

    private static final String TAG = HallOrdersFragment.class.getName();
    private Context mContext;
    private HallOrdersViewModel hallOrdersViewModel;
    private List<HallOrder> hallOrderList;
    private HallOrdersAdapter hallOrdersAdapter;

    private String API;

    public HallOrdersFragment(String api) {
        API = api;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        hallOrdersViewModel =
                ViewModelProviders.of(this).get(HallOrdersViewModel.class);
        View root = inflater.inflate(R.layout.fragment_hall_orders, container, false);

        mContext = getContext();
        hallOrderList = new ArrayList<>();
        hallOrdersAdapter = new HallOrdersAdapter(hallOrderList);

        final RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(hallOrdersAdapter);


        loadHallOrdersData();
        /*hallOrdersViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });*/
        return root;
    }

    private void loadHallOrdersData() {
        RequestQueue queue = Volley.newRequestQueue(mContext);
        StringRequest loginRequest = new StringRequest(Request.Method.GET, Constants.SERVER_API_URL + API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse-loadHallOrdersData: " + response);
                hallOrderList.clear();
                // update list from responded data
                try {
                    JSONArray responseArray = new JSONArray(response);
                    for (int i = 0; i < responseArray.length(); i++) {
                        JSONObject responseObject = responseArray.getJSONObject(i);
                        hallOrderList.add(
                                new HallOrder(
                                        responseObject.getString("date"),
                                        responseObject.getString("appoint_date"),
                                        responseObject.getJSONObject("hall").getString("name"),
                                        responseObject.getJSONObject("hall").getString("slug"),
                                        responseObject.getString("seating_plan"),
                                        responseObject.getString("custom_note")
                                )
                        );
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hallOrdersAdapter.notifyDataSetChanged();
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
        };
        // Add the request to the RequestQueue.
        queue.add(loginRequest);
    }
}