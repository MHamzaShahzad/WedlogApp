package com.example.wedlogapp.adapters.halls;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
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
import com.example.wedlogapp.fragments.CreateHallFragment;
import com.example.wedlogapp.fragments.HallBookFragment;
import com.example.wedlogapp.models.Hall;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HallsAdapter extends RecyclerView.Adapter<HallsAdapterHolder> {

    private static final String TAG = HallsAdapter.class.getName();
    private List<Hall> hallList;
    private Context mContext;

    public HallsAdapter(List<Hall> hallList, Context mContext) {
        this.hallList = hallList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public HallsAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_halls_adapter, null);
        return new HallsAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HallsAdapterHolder holder, int position) {
        Hall hall = hallList.get(holder.getAdapterPosition());
        holder.textPlaceHallName.setText(hall.getName());
        holder.textPlaceHallLocation.setText(hall.getLocation());
        holder.textRates.setText(getRatesString(hall.getRates()));
        Picasso.get().load(hall.getHeader_img()).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).centerInside().fit().into(holder.hallImageHeader);
        holder.btnBookHallDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HallBookFragment hallBookFragment = new HallBookFragment(hallList.get(holder.getAdapterPosition()).getHall_uri());
                FragmentTransaction ft = ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction();
                Fragment prev =  ((FragmentActivity) mContext).getSupportFragmentManager().findFragmentByTag("book_hall");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                hallBookFragment.show(ft, "book_hall");
            }
        });
    }

    @Override
    public int getItemCount() {
        return hallList.size();
    }

    private String getRatesString(JSONObject ratesObject) {
        StringBuilder ratesString = new StringBuilder();
        Iterator<String> ratesList = ratesObject.keys();

        while (ratesList.hasNext()) {
            String key = ratesList.next();
            Log.i(TAG, "getRatesString: " + key);
            // Do something with the value
            try {
                ratesString.append(key).append("  :  ").append(ratesObject.getString(key)).append("\n");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return ratesString.toString();
    }
}
