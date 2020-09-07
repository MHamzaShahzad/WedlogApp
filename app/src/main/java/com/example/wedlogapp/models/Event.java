package com.example.wedlogapp.models;

public class Event {

    String name, location, image;

    public Event() {
    }

    public Event(String name, String location, String image) {
        this.name = name;
        this.location = location;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getImage() {
        return image;
    }

}
