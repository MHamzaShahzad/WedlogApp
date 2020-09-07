package com.example.wedlogapp.adapters.event_orders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wedlogapp.R;
import com.example.wedlogapp.models.EventOrder;

import java.util.List;

public class EventOrdersAdapter extends RecyclerView.Adapter<EventOrdersAdapterHolder> {

    private List<EventOrder> eventOrderList;

    public EventOrdersAdapter(List<EventOrder> eventOrderList) {
        this.eventOrderList = eventOrderList;
    }

    @NonNull
    @Override
    public EventOrdersAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_event_order_adapter, null);
        return new EventOrdersAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventOrdersAdapterHolder holder, int position) {
        EventOrder eventOrder = eventOrderList.get(holder.getAdapterPosition());

    }

    @Override
    public int getItemCount() {
        return eventOrderList.size();
    }
}
