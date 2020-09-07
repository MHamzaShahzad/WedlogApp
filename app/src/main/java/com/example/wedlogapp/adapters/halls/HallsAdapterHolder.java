package com.example.wedlogapp.adapters.halls;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wedlogapp.R;

public class HallsAdapterHolder extends RecyclerView.ViewHolder {

    public Button btnBookHallDialog;
    public ImageView hallImageHeader;
    public TextView textPlaceHallName, textPlaceHallLocation, textRates;

    public HallsAdapterHolder(@NonNull View itemView) {
        super(itemView);
        btnBookHallDialog = itemView.findViewById(R.id.btnBookHallDialog);
        hallImageHeader = itemView.findViewById(R.id.hallImageHeader);
        textPlaceHallName = itemView.findViewById(R.id.textPlaceHallName);
        textPlaceHallLocation = itemView.findViewById(R.id.textPlaceHallLocation);
        textRates = itemView.findViewById(R.id.textRates);
    }
}
