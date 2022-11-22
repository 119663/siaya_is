package com.evelyne.labs.trialapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.evelyne.labs.trialapp.model.BookNow;
import com.evelyne.labs.trialapp.model.GpsUtils;
import com.evelyne.labs.trialapp.model.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.evelyne.labs.trialapp.databinding.ActivityMapsBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    //private ActivityMapsBinding binding;
    private LatLng currentLocation;
    private BottomSheetDialog dialog;
    private Context mContext;
    private Circle mCircle;
    private FusedLocationProviderClient mFusedLocationClient;

    private double wayLatitude = 0.0, wayLongitude = 0.0;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private boolean isGPS = false;
    private String currentAddress;
    Marker mMarker;
    public static final int LOCATION_REQUEST = 1000;
    public static final int GPS_REQUEST = 1001;
    SharedPreferences sharedPreferences;
    public final String SHARED_PREFS = "shared_prefs";
    public final String USER_LOC = "user_loc";
    String userloc;
    private FirebaseAuth mAuth;
    // Database Reference for Firebase.
    private DatabaseReference databaseReference;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //ViewGroup root = (ViewGroup) root.inflate(R.layout.activity_maps,null);
        //ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_booking,null);
        // binding = ActivityMapsBinding.inflate(getLayoutInflater());
        // setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
       // SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                //.findFragmentById(R.id.map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mapFragment.getMapAsync(this);

        mContext = this;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000); // 10 seconds
        locationRequest.setFastestInterval(5 * 1000); // 5 seconds

        new GpsUtils(this).turnGPSOn(isGPSEnable -> {
            // turn on GPS
            isGPS = isGPSEnable;
        });

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        wayLatitude = location.getLatitude();
                        wayLongitude = location.getLongitude();
                        //Toast.makeText(mContext, String.format(Locale.US, "%s - %s", wayLatitude, wayLongitude), Toast.LENGTH_SHORT).show();
                        getLocation();

                        if (mFusedLocationClient != null) {
                            mFusedLocationClient.removeLocationUpdates(locationCallback);
                        }
                    }
                }
            }
        };

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //mMap = googleMap;
        this.mMap = googleMap;
        mMap.setOnMapLoadedCallback(this);
        mMap.setOnMarkerClickListener(this);
      //  mMap.setOnMapLoadedCallback((GoogleMap.OnMapLoadedCallback) this);
       // mMap.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        //styling
       // MapStyleOptions mapStyleOptions = MapStyleOptions.loadRawResourceStyle(this, R.raw.style);
        //mMap.setMapStyle(mapStyleOptions);


    }

    private void drawMarkerWithCircle(LatLng position){
        double radiusInMeters = 300.0;
        int strokeColor = 0xff008577; // outline
        int shadeColor = 0x44008577; // opaque fill

        CircleOptions circleOptions = new CircleOptions().center(position).radius(radiusInMeters).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(8);
        mCircle = mMap.addCircle(circleOptions);

        MarkerOptions markerOptions = new MarkerOptions().position(position);
        mMarker = mMap.addMarker(markerOptions);
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_REQUEST);

        } else {

            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    wayLatitude = location.getLatitude();
                    wayLongitude = location.getLongitude();
                    Toast.makeText(mContext, String.format(Locale.US, "%s - %s", wayLatitude, wayLongitude), Toast.LENGTH_SHORT).show();
                    currentLocation = new LatLng(wayLatitude, wayLongitude);
                    mMap.addMarker(new MarkerOptions().position(currentLocation).title("Current location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17));
                    drawMarkerWithCircle(currentLocation);
                    getCityName();

                    databaseReference.child("Users").child(mAuth.getCurrentUser().getUid()).child("Cart").child("Location").setValue(currentLocation);
                } else {
                    mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                }
            });

        }
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    mFusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                        if (location != null) {
                            wayLatitude = location.getLatitude();
                            wayLongitude = location.getLongitude();
                            Toast.makeText(mContext, String.format(Locale.US, "%s - %s", wayLatitude, wayLongitude), Toast.LENGTH_SHORT).show();
                            getLocation();
                        } else {
                            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                        }
                    });

                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GPS_REQUEST) {
                isGPS = true; // flag maintain before get location
            }
        }
    }


    public void showBottomSheetDialogAdress(String city) {
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_location, null);
        Button btnValidateAddress = view.findViewById(R.id.btn_validate);
        btnValidateAddress.setOnClickListener(view1 -> {
            Intent startloc = new Intent(MapsActivity.this, BookNow.class);
            startActivity(startloc);
            finish();
        });
        //storing data in shared prefs
        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        EditText editCity = view.findViewById(R.id.edit_city);
        editCity.setText(city);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        String userloc = editCity.getText().toString().trim();
        //putting values
        editor.putString(USER_LOC, editCity.getText().toString());
        editor.apply();

        BookNow appUser = new BookNow(userloc);
        dialog = new BottomSheetDialog(this, R.style.MapsDialog);
        dialog.setContentView(view);
        dialog.show();
        dialog.setCancelable(false);

    }


    private void getCityName(){
        Geocoder gcd = new Geocoder(mContext, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(wayLatitude, wayLongitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(addresses != null && !addresses.isEmpty()){

            addresses.size();
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String street = addresses.get(0).getSubLocality();
            //String state = addresses.get(0).getAdminArea();
            //String country = addresses.get(0).getCountryName();
            //String postalCode = addresses.get(0).getPostalCode();
            //String knownName = addresses.get(0).getFeatureName();
            currentAddress = String.format(Locale.US, "%s - %s - %s", city, street, address);
            new Handler().postDelayed(() -> showBottomSheetDialogAdress(currentAddress), 3000);

        }else{
            new Handler().postDelayed(() -> showBottomSheetDialogAdress("Location not found"), 3000);
        }
    }

    @Override
    public void onMapLoaded() {
        if (mMap != null) {
            getLocation();
        }
    }
    @Override
    public boolean onMarkerClick(Marker marker) {
        if(!dialog.isShowing()){
            showBottomSheetDialogAdress(currentAddress);
            return true;
        }else{
            return false;
        }
        // Add a marker in Sydney and move the camera
       // LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
       // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}