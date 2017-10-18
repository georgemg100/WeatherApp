package com.ibm.bluelist.weatherapp.Data.Model;

/**
 * Created by michaelgeorgescu on 10/15/17.
 */

public class City {
    private String id;
    private String name;
    private String country;

    public City(String id, String name, String country) {
        this.id = id;
        this.name = name;
        this.country = country;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}