package com.example.wedlogapp.adapters.event_management;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wedlogapp.R;
import com.example.wedlogapp.models.Event;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapterHolder> {

    private List<Event> eventList;

    public EventsAdapter(List<Event> eventList) {
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventsAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_events_adapter, null);
        return new EventsAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventsAdapterHolder holder, int position) {
        Event event = eventList.get(holder.getAdapterPosition());
        holder.textPlaceEventName.setText(event.getName());
        holder.textPlaceEventLocation.setText(event.getLocation());
        Picasso.get().load(event.getImage()).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).centerInside().fit().into(holder.eventImageHeader);
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
}
