package com.example.wedlogapp.models;

public class EventOrder {

    public String event_name, event_slug, type, create, appoint_date;

    public EventOrder() {
    }

    public EventOrder(String event_name, String event_slug, String type, String create, String appoint_date) {
        this.event_name = event_name;
        this.event_slug = event_slug;
        this.type = type;
        this.create = create;
        this.appoint_date = appoint_date;
    }

    public String getEvent_name() {
        return event_name;
    }

    public String getEvent_slug() {
        return event_slug;
    }

    public String getType() {
        return type;
    }

    public String getCreate() {
        return create;
    }

    public String getAppoint_date() {
        return appoint_date;
    }
}
