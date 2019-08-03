package com.urbantechies.fetch_me_up_passenger.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.urbantechies.fetch_me_up_passenger.R;
import com.urbantechies.fetch_me_up_passenger.passengers.loginpassenger;

public class startup extends AppCompatActivity {

    private String TAG = "startup";
    private Button passenger_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup);

        passenger_btn = findViewById(R.id.passengerbtn);

      //  int avatar = R.drawable.ic_person_black_24dp;
       // Log.d(TAG, Integer.toString(avatar));

        passenger_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toLogin = new Intent(startup.this, loginpassenger.class);
                startActivity(toLogin);
            }
        });

    }
}
