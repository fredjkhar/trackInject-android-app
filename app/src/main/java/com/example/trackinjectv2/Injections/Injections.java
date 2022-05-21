package com.example.trackinjectv2.Injections;

import java.io.Serializable;

public class Injections implements Serializable{

    private String id;
    private String locationName;
    private int injectionLocationNumber;


    public Injections(String id, String name, int injectionLocation) {
        this.id = id;
        this.locationName = name;
        this.injectionLocationNumber = injectionLocation;
    }

    public Injections() {
    }

    public String getId() { return id; }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public int getInjectionLocationNumber() {
        return injectionLocationNumber;
    }

    public void setInjectionLocationNumber(int injectionLocationNumber) {
        this.injectionLocationNumber = injectionLocationNumber;
    }
}
