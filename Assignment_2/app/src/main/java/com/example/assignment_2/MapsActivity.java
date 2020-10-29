package com.example.assignment_2;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.assignment_2.basepedo.ui.Activity;
import com.example.assignment_2.friendlist.FriendsListActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationServices;
//import com.google.android.libraries.maps.CameraUpdateFactory;
//import com.google.android.libraries.maps.GoogleMap;
//import com.google.android.libraries.maps.OnMapReadyCallback;
//import com.google.android.libraries.maps.SupportMapFragment;
//import com.google.android.libraries.maps.model.LatLng;
//import com.google.android.libraries.maps.model.Marker;
//import com.google.android.libraries.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.location.Address;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener, LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private Location mLastLocation;
    private String username, email, password;
    private DatabaseReference databaseRef;
    private Button btn_main_friend;
    private Button btn_main_person;
    //for test
    private Button btn_main_map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseRef = FirebaseDatabase.getInstance().getReference();
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        username = getIntent().getStringExtra("username");

        setContentView(R.layout.activity_maps_test);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        //button friends
        btn_main_friend = findViewById(R.id.main_btn_friends);
        btn_main_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFriendsActivity();
            }
        });

        //button presonal
        btn_main_person = (Button) findViewById(R.id.main_btn_person);
        btn_main_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, PersonalActivity.class);
                startActivity(intent);
            }
        });
        //button map for test friend list
        btn_main_map = (Button) findViewById(R.id.main_btn_map);
        btn_main_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, FriendsListActivity.class);
                startActivity(intent);
            }
        });

    }

    private void openFriendsActivity() {
        Intent intent = new Intent(this, Friends.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 2
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 3
        if( mGoogleApiClient != null && mGoogleApiClient.isConnected() ) {
            mGoogleApiClient.disconnect();
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMarkerClickListener(this);

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        setUpMap();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    private void setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {android.Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        mMap.setMyLocationEnabled(true);

        LocationAvailability locationAvailability =
                LocationServices.FusedLocationApi.getLocationAvailability(mGoogleApiClient);
        if (null != locationAvailability && locationAvailability.isLocationAvailable()) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                LatLng currentLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation
                        .getLongitude());
                //add pin at user's location
<<<<<<< Updated upstream
                //placeMarkerOnMap(currentLocation);
                databaseRef.child("coordinates").child("user3").setValue(currentLocation);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12));
=======
                //给朋友定大头针
//                placeMarkerOnMap(currentLocation);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16));
>>>>>>> Stashed changes

                System.out.println("------------------------------");
                System.out.println(getAddress(currentLocation));
//                MarkerOptions markerOptions = new MarkerOptions().position(currentLocation).title("I am here!");
//                mMap.addMarker(markerOptions);
            }
        }
    }

<<<<<<< Updated upstream

=======
    protected void placeMarkerOnMap(LatLng location){
        MarkerOptions markerOptions = new MarkerOptions().position(location);
        String titleStr = getAddress(location);  // add these two lines
        markerOptions.title(titleStr);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource
                (getResources(), R.drawable.pig)));

        mMap.addMarker(markerOptions);
    }

    private String getAddress( LatLng latLng ) {
        // 1
        Geocoder geocoder = new Geocoder( this );
        String addressText = "";
        List<Address> addresses = null;
        Address address = null;
        try {
            // 2
            addresses = geocoder.getFromLocation( latLng.latitude, latLng.longitude, 1 );
            // 3
            if (null != addresses && !addresses.isEmpty()) {
                address = addresses.get(0);
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    addressText += (i == 0)?address.getAddressLine(i):("\n" + address.getAddressLine(i));
                }
            }
        } catch (IOException e ) {
        }
        return addressText;
    }
>>>>>>> Stashed changes

}