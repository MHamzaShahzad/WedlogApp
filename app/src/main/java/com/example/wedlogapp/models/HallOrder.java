package com.example.wedlogapp.models;

public class HallOrder {

    public String date, appoint_date, hall_name, hall_slug, seating_plan, custom_note;

    public HallOrder() {
    }

    public HallOrder(String date, String appoint_date, String hall_name, String hall_slug, String seating_plan, String custom_note) {
        this.date = date;
        this.appoint_date = appoint_date;
        this.hall_name = hall_name;
        this.hall_slug = hall_slug;
        this.seating_plan = seating_plan;
        this.custom_note = custom_note;
    }

    public String getDate() {
        return date;
    }

    public String getAppoint_date() {
        return appoint_date;
    }

    public String getHall_name() {
        return hall_name;
    }

    public String getHall_slug() {
        return hall_slug;
    }

    public String getSeating_plan() {
        return seating_plan;
    }

    public String getCustom_note() {
        return custom_note;
    }
}
