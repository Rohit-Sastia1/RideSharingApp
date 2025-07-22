package com.example.ridesharingapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.firebase.database.*;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private DatabaseReference rideRequestRef, driversRef, assignRef;
    private String currentUserId = "user123"; // In real apps, get from FirebaseAuth

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        rideRequestRef = FirebaseDatabase.getInstance().getReference("rideRequests");
        driversRef = FirebaseDatabase.getInstance().getReference("drivers");
        assignRef = FirebaseDatabase.getInstance().getReference("rideAssignments").child(currentUserId);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) mapFragment.getMapAsync(this);

        Button requestRideBtn = findViewById(R.id.requestRideBtn);
        requestRideBtn.setOnClickListener(v -> requestRide());

        listenForAssignedDriver();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            return;
        }
        mMap.setMyLocationEnabled(true);

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
            }
        });

        listenForDrivers();
    }

    private void listenForDrivers() {
        driversRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mMap.clear();
                for (DataSnapshot driverSnap : snapshot.getChildren()) {
                    Double lat = driverSnap.child("lat").getValue(Double.class);
                    Double lng = driverSnap.child("lng").getValue(Double.class);
                    String name = driverSnap.child("name").getValue(String.class);

                    if (lat != null && lng != null) {
                        LatLng driverLoc = new LatLng(lat, lng);
                        mMap.addMarker(new MarkerOptions()
                                .position(driverLoc)
                                .title("Driver: " + name)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void requestRide() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                Map<String, Object> ride = new HashMap<>();
                ride.put("lat", location.getLatitude());
                ride.put("lng", location.getLongitude());
                ride.put("status", "requested");
                ride.put("userId", currentUserId);
                ride.put("timestamp", System.currentTimeMillis());

                rideRequestRef.push().setValue(ride).addOnSuccessListener(aVoid -> {
                    Toast.makeText(MainActivity.this, "Ride Requested", Toast.LENGTH_SHORT).show();
                    // Simulate assigning driver
                    Map<String, Object> assign = new HashMap<>();
                    assign.put("driverId", "driver123");
                    assign.put("status", "assigned");
                    assignRef.setValue(assign);
                });
            }
        });
    }

    private void listenForAssignedDriver() {
        assignRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String driverId = snapshot.child("driverId").getValue(String.class);
                    if (driverId != null) fetchDriverDetails(driverId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void fetchDriverDetails(String driverId) {
        DatabaseReference driverRef = driversRef.child(driverId);
        driverRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);
                String vehicle = snapshot.child("vehicle").getValue(String.class);

                Toast.makeText(MainActivity.this,
                        "Assigned Driver:\n" + name + "\nVehicle: " + vehicle,
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}
