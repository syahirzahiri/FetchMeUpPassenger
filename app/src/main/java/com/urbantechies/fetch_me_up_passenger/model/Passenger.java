package com.urbantechies.fetch_me_up_passenger.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Passenger implements Parcelable {

    private String first_name;
    private String last_name;
    private String email;
    private String user_id;
    private String username;
    private String phone_no;
    private String matric_id;

    public Passenger(String first_name, String last_name, String email, String user_id, String username, String phone_no, String matric_id) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.user_id = user_id;
        this.username = username;
        this.phone_no = phone_no;
        this.matric_id = matric_id;
    }

    public Passenger() {

    }

    protected Passenger(Parcel in) {
        first_name = in.readString();
        last_name = in.readString();
        email = in.readString();
        user_id = in.readString();
        username = in.readString();
        phone_no = in.readString();
        matric_id = in.readString();
    }

    public static final Creator<Passenger> CREATOR = new Creator<Passenger>() {
        @Override
        public Passenger createFromParcel(Parcel in) {
            return new Passenger(in);
        }

        @Override
        public Passenger[] newArray(int size) {
            return new Passenger[size];
        }
    };

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getMatric_id() {
        return matric_id;
    }

    public void setMatric_id(String matric_id) {
        this.matric_id = matric_id;
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", user_id='" + user_id + '\'' +
                ", username='" + username + '\'' +
                ", phone_no='" + phone_no + '\'' +
                ", matric_id='" + matric_id + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(first_name);
        dest.writeString(last_name);
        dest.writeString(email);
        dest.writeString(user_id);
        dest.writeString(username);
        dest.writeString(phone_no);
        dest.writeString(matric_id);
    }
}

