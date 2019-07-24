package com.urbantechies.fetch_me_up_passenger.passengers;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.urbantechies.fetch_me_up_passenger.PassengerClient;
import com.urbantechies.fetch_me_up_passenger.R;
import com.urbantechies.fetch_me_up_passenger.model.Driver;
import com.urbantechies.fetch_me_up_passenger.model.DriverLocation;
import com.urbantechies.fetch_me_up_passenger.model.Passenger;
import com.urbantechies.fetch_me_up_passenger.model.PassengerLocation;

import java.util.ArrayList;

import static com.urbantechies.fetch_me_up_passenger.Constants.ERROR_DIALOG_REQUEST;
import static com.urbantechies.fetch_me_up_passenger.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.urbantechies.fetch_me_up_passenger.Constants.PERMISSIONS_REQUEST_ENABLE_GPS;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private boolean mLocationPermissionGranted = false;
    private FusedLocationProviderClient mFusedLocationClient;
    private FirebaseFirestore mDb;
    private PassengerLocation mPassengerLocation;
    private PassengerLocation mPassengerLocation2;

    private ListenerRegistration mDriverListEventListener;
    private ArrayList<Driver> mDriverList = new ArrayList<>();
    private ArrayList<DriverLocation> mDriverLocations = new ArrayList<>();
    private ArrayList<Passenger> mPassengerList = new ArrayList<>();
    private ArrayList<PassengerLocation> mPassengerLocationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mDb = FirebaseFirestore.getInstance();

        getAllOnlineDrivers();
        getPassengerLocation();
       // inflateUserListFragment();

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        if (savedInstanceState == null) {
            //  getSupportFragmentManager().beginTransaction().replace(R.id.fragment,
            //           new MainMapFragment()).commit();
           // bottomNavigationView.setSelectedItemId(R.id.nav_home);
            bottomNavigationView.setSelectedItemId(R.id.nav_request);
           // inflateUserListFragment();
        }

    }

    // ********************************************************************************************************************************

    private void getPassengerLocation() {
        DocumentReference locationRef = mDb.collection(getString(R.string.collection_passenger_locations))
                .document(FirebaseAuth.getInstance().getUid());

        locationRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().toObject(PassengerLocation.class) != null) {
                        mPassengerLocationList.add(task.getResult().toObject(PassengerLocation.class));
                    }

                }
            }
        });

    }


    // ********************************************************************************************************************************


    private void getDriverLocation(Driver driver) {
        DocumentReference locationRef = mDb.collection(getString(R.string.collection_driver_locations))
                .document(driver.getUser_id());

        locationRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().toObject(DriverLocation.class) != null) {
                        mDriverLocations.add(task.getResult().toObject(DriverLocation.class));
                    }

                    for (DriverLocation driverLocation : mDriverLocations) {
                        Log.d(TAG, "getDriverLocation: user location: inside ");
                        Log.d(TAG, "getDriverLocation: user location: " + driverLocation.getDriver().getUsername());
                        Log.d(TAG, "getDriverLocation: user latitude: " + driverLocation.getGeo_point().getLongitude() + ", " + driverLocation.getGeo_point().getLatitude());
                    }


                    //   inflateUserListFragment();

                }
            }
        });

    }

    private void getAllOnlineDrivers() {

        CollectionReference usersRef = mDb.collection(getString(R.string.collection_driver_online));

        mDriverListEventListener = usersRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e(TAG, "onEvent: Listen failed.", e);
                    return;
                }

                if (queryDocumentSnapshots != null) {

                    // Clear the list and add all the users again
                    mDriverList.clear();
                    mDriverList = new ArrayList<>();

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Driver driver = doc.toObject(Driver.class);
                        mDriverList.add(driver);
                        getDriverLocation(driver);
                    }

                    Log.d(TAG, "onEvent: user list size: " + mDriverList.size());
                }
            }
        });
    }

    private void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void inflateUserListFragment() {
        hideSoftKeyboard();

        MainMapFragment fragment = MainMapFragment.newInstance();
        Bundle bundle = new Bundle();

        bundle.putParcelableArrayList(getString(R.string.intent_user_locations), mDriverLocations);
        bundle.putParcelableArrayList(getString(R.string.intent_passenger_location), mPassengerLocationList);
        fragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //   transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up);
        transaction.replace(R.id.fragment, fragment);
        // transaction.addToBackStack(getString(R.string.intent_user_locations));
        transaction.commit();
    }


    private void getUserDetails() {
        if (mPassengerLocation == null) {
            mPassengerLocation = new PassengerLocation();

            DocumentReference userRef = mDb.collection(getString(R.string.collection_passengers))
                    .document(FirebaseAuth.getInstance().getUid());

            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: successfly get the user details");

                        Passenger passenger = task.getResult().toObject(Passenger.class);
                        mPassengerLocation.setPassenger(passenger);
                        ((PassengerClient) getApplicationContext()).setPassenger(passenger);
                        getLastKnownLocation();
                    }
                }
            });
        } else {
            getLastKnownLocation();
        }
    }


    private void saveUserLocation() {

        if (mPassengerLocation != null) {
            DocumentReference locationRef = mDb.
                    collection(getString(R.string.collection_passenger_locations))
                    .document(FirebaseAuth.getInstance().getUid());

            locationRef.set(mPassengerLocation).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "saveUserLocation: \ninserted user location into database." +
                                "\n latitude: " + mPassengerLocation.getGeo_point().getLatitude() +
                                "\n longitude: " + mPassengerLocation.getGeo_point().getLongitude());
                    }
                }
            });
        }
    }


    private void getLastKnownLocation() {
        Log.d(TAG, "getLastKnownLocation: called.");

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    Location location = task.getResult();
                    GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                    Log.d(TAG, "onComplete: latitude: " + geoPoint.getLatitude());


                    mPassengerLocation.setGeo_point(geoPoint);
                    mPassengerLocation.setTimestamp(null);
                    saveUserLocation();
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (checkMapServices()) {
            if (mLocationPermissionGranted) {
                //getChatrooms();
                getAllOnlineDrivers();
                getPassengerLocation();
               // inflateUserListFragment();
                getUserDetails();
            } else {
                getLocationPermission();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sign_out: {
                signOut();
                return true;
            }
            case R.id.action_profile: {
                //    startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }

    }


    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, loginpassenger.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    private boolean checkMapServices() {
        if (isServicesOK()) {
            if (isMapsEnabled()) {
                return true;
            }
        }
        return false;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This application requires GPS to work properly, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean isMapsEnabled() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
            return false;
        }
        return true;
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            getUserDetails();
            getAllOnlineDrivers();
            getPassengerLocation();
          //  inflateUserListFragment();
            //getChatrooms(); maybe can be used to load where are other users
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    public boolean isServicesOK() {
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(HomePage.this);

        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(HomePage.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: called.");
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ENABLE_GPS: {
                if (mLocationPermissionGranted) {
                    //getChatrooms();
                    getUserDetails();
                    getAllOnlineDrivers();
                    getPassengerLocation();
                  //  inflateUserListFragment();
                } else {
                    getLocationPermission();
                }
            }
        }

    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()) {
                        case R.id.nav_home:
                            // selectedFragment = new MainMapFragment();
                            inflateUserListFragment();
                            return true;
                        // break;
                        case R.id.nav_request:
                            selectedFragment = new PassengerRequestFragment();
                            break;
                        case R.id.nav_custom_ride:
                            selectedFragment = new CustomRideFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment,
                            selectedFragment).commit();

                    return true;
                }
            };


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_inbox:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment,
                        new InboxFragment()).commit();
                break;
            case R.id.nav_trip:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment,
                        new MyTripFragment()).commit();
                break;
            case R.id.nav_wallet:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment,
                        new WalletFragment()).commit();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }
}
