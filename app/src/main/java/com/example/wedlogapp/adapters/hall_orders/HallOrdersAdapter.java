package com.example.wedlogapp.adapters.hall_orders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wedlogapp.R;
import com.example.wedlogapp.models.HallOrder;

import java.util.List;

public class HallOrdersAdapter extends RecyclerView.Adapter<HallOrdersAdapterHolder> {

    private List<HallOrder> hallOrderList;

    public HallOrdersAdapter(List<HallOrder> hallOrderList) {
        this.hallOrderList = hallOrderList;
    }

    @NonNull
    @Override
    public HallOrdersAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_hall_order_adapter, null);
        return new HallOrdersAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HallOrdersAdapterHolder holder, int position) {
        HallOrder hallOrder = hallOrderList.get(holder.getAdapterPosition());
    }

    @Override
    public int getItemCount() {
        return hallOrderList.size();
    }
}
