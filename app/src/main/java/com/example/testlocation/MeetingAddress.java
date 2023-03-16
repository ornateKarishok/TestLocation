package com.example.testlocation;

import android.location.Address;

public class MeetingAddress {
    int id;
    String address;
    Address addressCoordinates;
    float distance;

    public MeetingAddress(int id, String address) {
        this.id = id;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public Address getAddressCoordinates() {
        return addressCoordinates;
    }

    public void setAddressCoordinates(Address addressCoordinates) {
        this.addressCoordinates = addressCoordinates;
    }
}
