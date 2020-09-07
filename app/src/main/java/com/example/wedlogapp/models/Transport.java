package com.example.wedlogapp.models;

public class Transport {

    private String name, location, description, slug;

    public Transport() {
    }

    public Transport(String name, String location, String description, String slug) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getSlug() {
        return slug;
    }
}
