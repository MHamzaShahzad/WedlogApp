package com.example.wedlogapp.activities.ui.event_order;

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
import com.example.wedlogapp.adapters.event_management.EventsAdapter;
import com.example.wedlogapp.adapters.event_orders.EventOrdersAdapter;
import com.example.wedlogapp.models.Event;
import com.example.wedlogapp.models.EventOrder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EventOrdersFragment extends Fragment {

    private static final String TAG = EventOrdersFragment.class.getName();
    private Context mContext;
    private EventOrdersViewModel eventOrdersViewModel;
    private List<EventOrder> eventOrderList;
    private EventOrdersAdapter eventOrdersAdapter;

    private String API;

    public EventOrdersFragment(String api) {
        API = api;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        eventOrdersViewModel =
                ViewModelProviders.of(this).get(EventOrdersViewModel.class);
        View root = inflater.inflate(R.layout.fragment_event_orders, container, false);

        mContext = getContext();
        eventOrderList = new ArrayList<>();
        eventOrdersAdapter = new EventOrdersAdapter(eventOrderList);

        final RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(eventOrdersAdapter);

        loadEventOrdersData();

        /*eventOrdersViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });*/
        return root;
    }

    private void loadEventOrdersData() {
        RequestQueue queue = Volley.newRequestQueue(mContext);
        StringRequest loginRequest = new StringRequest(Request.Method.GET, Constants.SERVER_API_URL + API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse-loadEventOrdersData: " + response);
                eventOrderList.clear();
                // update list with responded data
                try {
                    JSONArray responseArray = new JSONArray(response);
                    for (int i = 0; i < responseArray.length(); i++) {
                        JSONObject responseObject = responseArray.getJSONObject(i);
                        eventOrderList.add(
                                new EventOrder(
                                        responseObject.getJSONObject("event").getString("name"),
                                        responseObject.getJSONObject("event").getString("slug"),
                                        responseObject.getString("type"),
                                        responseObject.getString("created"),
                                        responseObject.getString("oppoint_date")
                                )
                        );
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                eventOrdersAdapter.notifyDataSetChanged();
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