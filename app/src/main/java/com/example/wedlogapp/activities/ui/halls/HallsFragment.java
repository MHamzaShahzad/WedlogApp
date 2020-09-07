package com.example.wedlogapp.activities.ui.halls;

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
import com.example.wedlogapp.adapters.halls.HallsAdapter;
import com.example.wedlogapp.fragments.CreateHallFragment;
import com.example.wedlogapp.models.Hall;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HallsFragment extends Fragment {

    private static final String TAG = HallsFragment.class.getName();
    private Context mContext;
    private HallsViewModel hallsViewModel;
    private List<Hall> hallList;
    private HallsAdapter hallsAdapter;

    private RecyclerView recyclerView;
    private FloatingActionButton fabCreateHall;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        hallsViewModel =
                ViewModelProviders.of(this).get(HallsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_halls, container, false);

        mContext = getContext();
        hallList = new ArrayList<>();
        hallsAdapter = new HallsAdapter(hallList, mContext);

        fabCreateHall = root.findViewById(R.id.fab);
        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(hallsAdapter);

        loadHallsData();
        setFabCreateHall();
        /*hallsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });*/
        return root;
    }

    private void loadHallsData() {
        RequestQueue queue = Volley.newRequestQueue(mContext);
        StringRequest loginRequest = new StringRequest(Request.Method.GET, Constants.SERVER_API_URL + "halls/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse-loadHallsData: " + response);
                hallList.clear();
                // update list from responded data
                try {
                    JSONArray responseArray = new JSONArray(response);
                    for (int i = 0; i < responseArray.length(); i++){
                        JSONObject responseObject = responseArray.getJSONObject(i);
                        hallList.add(
                                new Hall(
                                        responseObject.getString("name"),
                                        responseObject.getString("location"),
                                        responseObject.getString("hall_uri"),
                                        responseObject.getString("header_img"),
                                        responseObject.getJSONObject("rates")
                                )
                        );
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hallsAdapter.notifyDataSetChanged();
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
        };
        // Add the request to the RequestQueue.
        queue.add(loginRequest);
    }

    private void setFabCreateHall() {
        fabCreateHall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateHallFragment createHallFragment = new CreateHallFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("create_hall");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                createHallFragment.show(ft, "create_hall");
            }
        });
    }
}