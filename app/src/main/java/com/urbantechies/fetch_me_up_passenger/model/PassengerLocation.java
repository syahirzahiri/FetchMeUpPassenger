package com.urbantechies.fetch_me_up_passenger.model;

import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class PassengerLocation {

    private Passenger passenger;
    private GeoPoint geo_point;
    private @ServerTimestamp
    Date timestamp;

    public PassengerLocation(Passenger passenger, GeoPoint geo_point, Date timestamp) {
        this.passenger = passenger;
        this.geo_point = geo_point;
        this.timestamp = timestamp;
    }

    public PassengerLocation() {

    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public GeoPoint getGeo_point() {
        return geo_point;
    }

    public void setGeo_point(GeoPoint geo_point) {
        this.geo_point = geo_point;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "UserLocation{" +
                "passenger=" + passenger +
                ", geo_point=" + geo_point +
                ", timestamp=" + timestamp +
                '}';
    }

}
