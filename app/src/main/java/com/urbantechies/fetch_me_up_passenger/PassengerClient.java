package com.urbantechies.fetch_me_up_passenger;

import android.app.Application;

import com.urbantechies.fetch_me_up_passenger.model.Passenger;


public class PassengerClient extends Application {

    private Passenger passenger = null;

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

}
