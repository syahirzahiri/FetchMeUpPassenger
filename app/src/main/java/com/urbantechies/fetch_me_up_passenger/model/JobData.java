package com.urbantechies.fetch_me_up_passenger.model;

import androidx.annotation.NonNull;

import com.google.maps.model.LatLng;

public class JobData {

    private User passenger;
    private User driver;
    private String id;
    private String status;
    private com.google.maps.model.LatLng passenger_location;
    private com.google.maps.model.LatLng driver_location;
    private com.google.maps.model.LatLng destination_location;
    private String destination;
    private String fare;

    public JobData(User passenger, User driver, String id, String status, LatLng passenger_location, LatLng driver_location, LatLng destination_location, String destination, String fare) {
        this.passenger = passenger;
        this.driver = driver;
        this.id = id;
        this.status = status;
        this.passenger_location = passenger_location;
        this.driver_location = driver_location;
        this.destination_location = destination_location;
        this.destination = destination;
        this.fare = fare;
    }

    public JobData() {
    }

    public User getPassenger() {
        return passenger;
    }

    public void setPassenger(User passenger) {
        this.passenger = passenger;
    }

    public User getDriver() {
        return driver;
    }

    public void setDriver(User driver) {
        this.driver = driver;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LatLng getPassenger_location() {
        return passenger_location;
    }

    public void setPassenger_location(LatLng passenger_location) {
        this.passenger_location = passenger_location;
    }

    public LatLng getDriver_location() {
        return driver_location;
    }

    public void setDriver_location(LatLng driver_location) {
        this.driver_location = driver_location;
    }

    public LatLng getDestination_location() {
        return destination_location;
    }

    public void setDestination_location(LatLng destination_location) {
        this.destination_location = destination_location;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }

    @NonNull
    @Override
    public String toString() {
        return "JobData{" +
                "passenger='" + passenger + '\'' +
                ", driver='" + driver + '\'' +
                ", id='" + id + '\'' +
                ", status='" + status + '\'' +
                ", passenger_location='" + passenger_location + '\'' +
                ", driver_location='" + driver_location + '\'' +
                ", destination_location='" + destination_location + '\'' +
                ", destination='" + destination + '\'' +
                '}';
    }
}
