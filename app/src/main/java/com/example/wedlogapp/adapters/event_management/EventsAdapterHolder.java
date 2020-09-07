package com.example.wedlogapp.adapters.event_management;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wedlogapp.R;

public class EventsAdapterHolder extends RecyclerView.ViewHolder {

    public ImageView eventImageHeader;
    public TextView textPlaceEventName, textPlaceEventLocation;

    public EventsAdapterHolder(@NonNull View itemView) {
        super(itemView);

        eventImageHeader = itemView.findViewById(R.id.eventImageHeader);
        textPlaceEventName = itemView.findViewById(R.id.textPlaceEventName);
        textPlaceEventLocation = itemView.findViewById(R.id.textPlaceEventLocation);

    }
}
