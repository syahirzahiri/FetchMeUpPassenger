package com.urbantechies.fetch_me_up_passenger;


import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.urbantechies.fetch_me_up_passenger.model.User;
import com.urbantechies.fetch_me_up_passenger.model.UserLocation;
import com.urbantechies.fetch_me_up_passenger.passengers.JoinRideFragment;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class RunJoinRide extends Fragment {

    private String TAG = "RunCustomRide";

    private FirebaseFirestore mDb;
    private ListenerRegistration mUserListEventListener;
    private ArrayList<User> mUserList = new ArrayList<>();
    private ArrayList<UserLocation> mUserLocations = new ArrayList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDb = FirebaseFirestore.getInstance();
        addAllUser();

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                inflateRideFragment();
            }

        };

        handler.postDelayed(runnable, 2000);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_run_join_ride, container, false);
    }

    public static RunJoinRide newInstance() {
        return new RunJoinRide();
    }


    private void addAllUser() {
        getAllOnlineUsers();
        getUserPassengerLocation();
    }

    private void getAllOnlineUsers() {

        CollectionReference usersRef = mDb.collection(getString(R.string.collection_user_online));

        mUserListEventListener = usersRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e(TAG, "onEvent: Listen failed.", e);
                    return;
                }

                if (queryDocumentSnapshots != null) {

                    // Clear the list and add all the users again
                    mUserList.clear();
                    mUserList = new ArrayList<>();

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        User user = doc.toObject(User.class);
                        mUserList.add(user);
                        getUserLocation(user);
                    }

                    Log.d(TAG, "onEvent: user list size: " + mUserList.size());
                }
            }
        });
    }

    private void getUserLocation(User user) {
        DocumentReference locationRef = mDb.collection(getString(R.string.collection_user_locations))
                .document(user.getUser_id());

        locationRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().toObject(UserLocation.class) != null) {
                        mUserLocations.add(task.getResult().toObject(UserLocation.class));
                    }
                }
            }
        });
    }

    private void getUserPassengerLocation() {
        DocumentReference locationRef = mDb.collection(getString(R.string.collection_user_locations))
                .document(FirebaseAuth.getInstance().getUid());

        locationRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().toObject(UserLocation.class) != null) {
                        mUserLocations.add(task.getResult().toObject(UserLocation.class));
                    }
                }
            }
        });

    }


    private void inflateRideFragment() {

        JoinRideFragment fragment = JoinRideFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(getString(R.string.intent_user_list), mUserList);
        bundle.putParcelableArrayList(getString(R.string.intent_user_locations), mUserLocations);
        fragment.setArguments(bundle);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

}
