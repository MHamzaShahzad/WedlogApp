package com.example.wedlogapp.adapters.transportation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wedlogapp.R;
import com.example.wedlogapp.models.Transport;

import java.util.List;

public class TransportationsAdapter extends RecyclerView.Adapter<TransportationsAdapterHolder> {

    private List<Transport> transportList;

    public TransportationsAdapter(List<Transport> transportList) {
        this.transportList = transportList;
    }

    @NonNull
    @Override
    public TransportationsAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_transportations_adapter, null);
        return new TransportationsAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransportationsAdapterHolder holder, int position) {
        Transport transport = transportList.get(holder.getAdapterPosition());
        holder.textPlaceTransportName.setText(transport.getName());
        holder.textPlaceTransportLocation.setText(transport.getLocation());
        holder.textPlaceTransportDescription.setText(transport.getDescription());
    }

    @Override
    public int getItemCount() {
        return transportList.size();
    }
}
