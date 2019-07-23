package com.urbantechies.fetch_me_up_passenger;

import android.app.Application;

import com.urbantechies.fetch_me_up_passenger.model.Driver;


public class DriverClient extends Application {

    private Driver driver = null;

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

}
