package com.example.wedlogapp.activities.ui.transportation;

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
import com.example.wedlogapp.adapters.transportation.TransportationsAdapter;
import com.example.wedlogapp.models.Transport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TransportationFragment extends Fragment {

    private static final String TAG = TransportationFragment.class.getName();
    private Context mContext;
    private TransportationViewModel transportationViewModel;
    private TransportationsAdapter transportationsAdapter;
    private List<Transport> transportList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_transportation, container, false);

        transportationViewModel =
                ViewModelProviders.of(this).get(TransportationViewModel.class);

        mContext = getContext();
        transportList = new ArrayList<>();
        transportationsAdapter = new TransportationsAdapter(transportList);

        final RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(transportationsAdapter);

        loadTransportationData();
        /*transportationViewModel.getData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });*/

        return root;
    }

    private void loadTransportationData() {
        RequestQueue queue = Volley.newRequestQueue(mContext);
        StringRequest loginRequest = new StringRequest(Request.Method.GET, Constants.SERVER_API_URL + "transportations/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse-loadTransportationData: " + response);
                transportList.clear();
                // update list here with responded data
                try {
                    JSONArray responseArray = new JSONArray(response);
                    for (int i = 0; i < responseArray.length(); i++){
                        JSONObject responseObject = responseArray.getJSONObject(i);
                        transportList.add(
                                new Transport(
                                     responseObject.getString("name"),
                                     responseObject.getString("location"),
                                     responseObject.getString("descripation"),
                                     responseObject.getString("slug")
                                )
                        );
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                transportationsAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onErrorResponse: " + error);
                Common.volleyErrorFormatter(mContext, error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", Constants.API_PRE_TOKEN + new SessionManagement(mContext).getToken());
                return map;
            }
        };;
        // Add the request to the RequestQueue.
        queue.add(loginRequest);
    }

}