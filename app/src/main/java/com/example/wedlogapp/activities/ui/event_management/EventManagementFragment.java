package com.example.wedlogapp.activities.ui.event_management;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
import com.example.wedlogapp.fragments.CreateEventVendorFragment;
import com.example.wedlogapp.fragments.CreateHallFragment;
import com.example.wedlogapp.models.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EventManagementFragment extends Fragment {

    private static final String TAG = EventManagementFragment.class.getName();
    private Context mContext;
    private EventManagementViewModel eventManagementViewModel;
    private List<Event> eventList;
    private EventsAdapter eventsAdapter;

    private FloatingActionButton fabCreateHall;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        eventManagementViewModel =
                ViewModelProviders.of(this).get(EventManagementViewModel.class);
        View root = inflater.inflate(R.layout.fragment_event_management, container, false);

        mContext = getContext();
        eventList = new ArrayList<>();
        eventsAdapter = new EventsAdapter(eventList);

        fabCreateHall = root.findViewById(R.id.fab);
        final RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(eventsAdapter);

        loadEventsData();
        setFabCreateEventVendor();

        /*eventManagementViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });*/
        return root;
    }

    private void loadEventsData() {
        RequestQueue queue = Volley.newRequestQueue(mContext);
        StringRequest loginRequest = new StringRequest(Request.Method.GET, Constants.SERVER_API_URL + "events/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse-loadEventsData: " + response);
                eventList.clear();
                // update list with responded data
                try {
                    JSONArray responseArray = new JSONArray(response);
                    for (int i = 0; i < responseArray.length(); i++){
                        JSONObject responseObject = responseArray.getJSONObject(i);
                        eventList.add(
                                new Event(
                                        responseObject.getString("name"),
                                        responseObject.getString("location"),
                                        responseObject.getString("image")
                                )
                        );
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                eventsAdapter.notifyDataSetChanged();
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

    private void setFabCreateEventVendor() {
        fabCreateHall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateEventVendorFragment createEventVendorFragment = new CreateEventVendorFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("create_event_vendor");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                createEventVendorFragment.show(ft, "create_event_vendor");
            }
        });
    }
}