package com.urbantechies.fetch_me_up_passenger;

import android.app.Application;

import com.urbantechies.fetch_me_up_passenger.model.User;


public class UserClient extends Application {

    private User user = null;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
