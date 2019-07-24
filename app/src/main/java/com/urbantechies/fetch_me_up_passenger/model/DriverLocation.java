package com.urbantechies.fetch_me_up_passenger.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class DriverLocation implements Parcelable {

    private Driver driver;
    private GeoPoint geo_point;
    private @ServerTimestamp
    Date timestamp;

    public DriverLocation(Driver driver, GeoPoint geo_point, Date timestamp) {
        this.driver = driver;
        this.geo_point = geo_point;
        this.timestamp = timestamp;
    }

    public DriverLocation() {

    }

    protected DriverLocation(Parcel in) {
        driver = in.readParcelable(Driver.class.getClassLoader());
    }

    public static final Creator<DriverLocation> CREATOR = new Creator<DriverLocation>() {
        @Override
        public DriverLocation createFromParcel(Parcel in) {
            return new DriverLocation(in);
        }

        @Override
        public DriverLocation[] newArray(int size) {
            return new DriverLocation[size];
        }
    };

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
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
                "driver=" + driver +
                ", geo_point=" + geo_point +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(driver, i);
    }
}
