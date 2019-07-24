package com.urbantechies.fetch_me_up_passenger.passengers;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.auth.FirebaseAuth;
import com.urbantechies.fetch_me_up_passenger.R;
import com.urbantechies.fetch_me_up_passenger.model.DriverLocation;
import com.urbantechies.fetch_me_up_passenger.model.PassengerLocation;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainMapFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "MainMapFragment";

    private MapView mMapView;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private ArrayList<DriverLocation> mDriverLocations = new ArrayList<>();
    private ArrayList<PassengerLocation> mPassengerLocations = new ArrayList<>();
    private GoogleMap mGoogleMap;
    private LatLngBounds mMapBoundary;
    private PassengerLocation mPassengerPosition;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            final ArrayList<DriverLocation> locations = getArguments().getParcelableArrayList(getString(R.string.intent_user_locations));
            mDriverLocations.addAll(locations);

            final ArrayList<PassengerLocation> passengerLocation = getArguments().getParcelableArrayList(getString(R.string.intent_passenger_location));
            mPassengerLocations.addAll(passengerLocation);

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_map, container, false);
        mMapView = view.findViewById(R.id.navigation_map);

        setPassengerPosition();

            initGoogleMap(savedInstanceState);


        return view;
    }

    private void setCameraView() {

        double bottomBoundary = mPassengerPosition.getGeo_point().getLatitude() - .1;
        double leftBoundary = mPassengerPosition.getGeo_point().getLongitude() - .1;
        double topBoundary = mPassengerPosition.getGeo_point().getLatitude() + .1;
        double rightBoundary = mPassengerPosition.getGeo_point().getLongitude() + .1;

//        double bottomBoundary = 4.3877663 - .1;
//        double leftBoundary = 100.9639676 - .1;
//        double topBoundary = 4.3877663 + .1;
//        double rightBoundary =100.9639676 + .1;

        mMapBoundary = new LatLngBounds(
                new LatLng(bottomBoundary, leftBoundary),
                new LatLng(topBoundary, rightBoundary)
        );

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(mMapBoundary, 0));
    }

    private void setPassengerPosition() {
        for (PassengerLocation passengerLocation : mPassengerLocations) {
            if (passengerLocation.getPassenger().getUser_id().equals(FirebaseAuth.getInstance().getUid())) {
                mPassengerPosition = passengerLocation;
            }
        }

    }


    public static MainMapFragment newInstance() {
        return new MainMapFragment();
    }


    private void initGoogleMap(Bundle savedInstanceState) {
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        setPassengerPosition();

        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        // map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
        mGoogleMap = map;
        setCameraView();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


}
