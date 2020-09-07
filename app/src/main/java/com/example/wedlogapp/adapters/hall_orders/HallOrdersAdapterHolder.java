package com.example.wedlogapp.adapters.hall_orders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wedlogapp.R;

public class HallOrdersAdapterHolder extends RecyclerView.ViewHolder {

    public TextView textPlaceName, textPlaceAppointDate, textPlaceSeatingPlan, textPlaceCustomNote;

    public HallOrdersAdapterHolder(@NonNull View itemView) {
        super(itemView);

        textPlaceName = itemView.findViewById(R.id.textPlaceName);
        textPlaceAppointDate = itemView.findViewById(R.id.textPlaceAppointDate);
        textPlaceSeatingPlan = itemView.findViewById(R.id.textPlaceSeatingPlan);
        textPlaceCustomNote = itemView.findViewById(R.id.textPlaceCustomNote);
    }
}
