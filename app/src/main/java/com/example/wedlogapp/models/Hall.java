package com.example.wedlogapp.models;

import org.json.JSONObject;

public class Hall {

    String name, location, hall_uri, header_img;
    JSONObject rates;

    public Hall() {
    }

    public Hall(String name, String location, String hall_uri, String header_img, JSONObject rates) {
        this.name = name;
        this.location = location;
        this.hall_uri = hall_uri;
        this.header_img = header_img;
        this.rates = rates;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getHall_uri() {
        return hall_uri;
    }

    public String getHeader_img() {
        return header_img;
    }

    public JSONObject getRates() {
        return rates;
    }
}
