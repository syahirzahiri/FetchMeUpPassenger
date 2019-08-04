package com.urbantechies.fetch_me_up_passenger.passengers;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.urbantechies.fetch_me_up_passenger.R;
import com.urbantechies.fetch_me_up_passenger.RunJoinRide;
import com.urbantechies.fetch_me_up_passenger.model.RideData;
import com.urbantechies.fetch_me_up_passenger.model.User;
import com.urbantechies.fetch_me_up_passenger.model.UserLocation;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class JoinRideFragment extends Fragment implements View.OnClickListener {

    private String TAG = "CustomRideFragment";

    private Button btnAddRide;
    private FirebaseFirestore mDb;
    private ListenerRegistration mRideAvailableEventListener;
    private User user;
    private ArrayList<UserLocation> mUserLocations = new ArrayList<>();
    private LinearLayout linearLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

            final ArrayList<UserLocation> locations = getArguments().getParcelableArrayList(getString(R.string.intent_user_locations));
            mUserLocations.clear();
            mUserLocations.addAll(locations);
        }


        for (UserLocation userLocation : mUserLocations) {
            if (userLocation.getUser().getUser_id().equals(FirebaseAuth.getInstance().getUid())) {
                user = userLocation.getUser();
            }
        }

        mDb = FirebaseFirestore.getInstance();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_join_ride, container, false);
//        btnAddRide = view.findViewById(R.id.addcustombtn);
//        btnAddRide.setOnClickListener(this);
        linearLayout =  view.findViewById(R.id.ride_list_layout);
        linearLayout.removeAllViews();
        getAllJobUser(view);

        return view;
    }

    public static JoinRideFragment newInstance() {
        return new JoinRideFragment();
    }

    private void inflateRunJoinRideFragment() {

        RunJoinRide fragment = RunJoinRide.newInstance();
        //Bundle bundle = new Bundle();
       // bundle.putParcelableArrayList(getString(R.string.intent_user_locations), mUserLocations);
        //fragment.setArguments(bundle);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    private void getAllJobUser(View view) {

        CollectionReference usersRef = mDb.collection(getString(R.string.collection_ride_available));

        mRideAvailableEventListener = usersRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e(TAG, "onEvent: Listen failed.", e);
                    return;
                }

                if (queryDocumentSnapshots != null) {
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        RideData rideData = doc.toObject(RideData.class);

                        if (rideData.getStatus().equals("Available")) {
                            generateRideList(rideData,view);
                        }
                    }
                }
            }
        });
    }


    private void generateRideList(RideData rideData, View view) {

        LinearLayout mainLayout = view.findViewById(R.id.ride_list_layout);

        LayoutInflater inflater = getLayoutInflater();
        View myLayout = inflater.inflate(R.layout.ridelistcard, mainLayout, false);

        Button joinbtn = myLayout.findViewById(R.id.joinRide);
        joinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUserToRide(rideData);
            }
        });

        for(User ridedatauser: rideData.getPassenger()){
            if(ridedatauser.getUser_id().equals(user.getUser_id())){
                joinbtn.setEnabled(false);
                joinbtn.setText("Joined!");
                joinbtn.setBackgroundColor(getResources().getColor(R.color.darkGrey));
            }
        }

        TextView driverName = myLayout.findViewById(R.id.driver_text_ride);
        driverName.setText("Driver: " + rideData.getDriver().getFirst_name());

        TextView id = myLayout.findViewById(R.id.id_text_ride);
        id.setText("ID: " + rideData.getId());

        TextView destination = myLayout.findViewById(R.id.destination_text_ride);
        destination.setText("Destination: " + rideData.getDestination());

        TextView pickup = myLayout.findViewById(R.id.pickup_text_ride);
        pickup.setText("Pick Up: " + rideData.getPickup());

        TextView passenger = myLayout.findViewById(R.id.passenger_text_ride);
        passenger.setText("Passenger: " + rideData.getPassenger().size() + "/" + rideData.getMaxpassenger());

        TextView datetime = myLayout.findViewById(R.id.datetime_text_ride);
        datetime.setText("DateTime: " + rideData.getDate() + " " + rideData.getTime());

        TextView fare = myLayout.findViewById(R.id.fare_text_ride);
        fare.setText("Fare: " + rideData.getFare());

        TextView status = myLayout.findViewById(R.id.status_text_ride);
        status.setText("Status: " + rideData.getStatus());

        mainLayout.addView(myLayout);

    }


    private void addUserToRide(RideData rideData){

        ArrayList<User> mRidePassengerList = new ArrayList<>();
        String status = "Available";

        mRidePassengerList.add(user);

        if(rideData.getPassenger().size() > 0){
            for(User user: rideData.getPassenger()){
                mRidePassengerList.add(user);
            }
        }

        if(mRidePassengerList.size() >= Integer.parseInt(rideData.getMaxpassenger())){
            status = "Full";
        }

        Log.d(TAG, "Ride list size: " + mRidePassengerList.size());

       // String id = mDb.collection(getString(R.string.collection_ride_available)).document().getId();
        RideData tempRideData = new RideData(rideData.getDriver(),mRidePassengerList, rideData.getId(), status, rideData.getDestination(),
                rideData.getPickup(), rideData.getDate(), rideData.getTime(),
                rideData.getMaxpassenger(), rideData.getFare());


        DocumentReference endRef = mDb.collection(getString(R.string.collection_ride_available))
                .document(rideData.getId());

        endRef.set(tempRideData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        inflateRunJoinRideFragment();
                        Log.d(TAG, "onComplete: Update to database ride available!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.addcustombtn: {
//               // inflateAddRideFragment();
//                break;
//            }
        }
    }

}
