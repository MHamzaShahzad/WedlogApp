package com.example.wedlogapp.adapters.transportation;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wedlogapp.R;

public class TransportationsAdapterHolder extends RecyclerView.ViewHolder {
    public TextView textPlaceTransportName, textPlaceTransportLocation, textPlaceTransportDescription;

    public TransportationsAdapterHolder(@NonNull View itemView) {
        super(itemView);
        textPlaceTransportName = itemView.findViewById(R.id.textPlaceTransportName);
        textPlaceTransportLocation = itemView.findViewById(R.id.textPlaceTransportLocation);
        textPlaceTransportDescription = itemView.findViewById(R.id.textPlaceTransportDescription);
    }
}
