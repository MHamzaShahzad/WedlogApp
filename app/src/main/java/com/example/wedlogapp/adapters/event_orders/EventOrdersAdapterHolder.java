package com.example.wedlogapp.adapters.event_orders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wedlogapp.R;

public class EventOrdersAdapterHolder extends RecyclerView.ViewHolder {

    public TextView textPlaceName, textPlaceType, textPlaceCreate, textPlaceAppointDate;

    public EventOrdersAdapterHolder(@NonNull View itemView) {
        super(itemView);

        textPlaceName = itemView.findViewById(R.id.textPlaceName);
        textPlaceType = itemView.findViewById(R.id.textPlaceType);
        textPlaceCreate = itemView.findViewById(R.id.textPlaceCreate);
        textPlaceAppointDate = itemView.findViewById(R.id.textPlaceAppointDate);

    }
}
