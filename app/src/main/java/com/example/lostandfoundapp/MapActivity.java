package com.example.lostandfoundapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    EditText editRadius;
    Button btnApplyRadius;
    DatabaseHelper databaseHelper;
    GoogleMap googleMap;

    double userLat = -37.8136;
    double userLng = 144.9631;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        editRadius = findViewById(R.id.editRadius);
        btnApplyRadius = findViewById(R.id.btnApplyRadius);
        databaseHelper = new DatabaseHelper(this);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        btnApplyRadius.setOnClickListener(v -> {
            String radiusText = editRadius.getText().toString().trim();

            if (radiusText.isEmpty()) {
                Toast.makeText(this, "Please enter radius", Toast.LENGTH_SHORT).show();
                return;
            }

            double radiusKm = Double.parseDouble(radiusText);
            showItemsWithinRadius(radiusKm);
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        LatLng melbourne = new LatLng(userLat, userLng);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(melbourne, 10));

        showAllItemsOnMap();
    }

    private void showAllItemsOnMap() {
        if (googleMap == null) return;

        googleMap.clear();

        ArrayList<Advert> adverts = databaseHelper.getAllAdverts();

        for (Advert advert : adverts) {
            LatLng itemLocation = new LatLng(advert.getLatitude(), advert.getLongitude());

            googleMap.addMarker(new MarkerOptions()
                    .position(itemLocation)
                    .title(advert.getType() + ": " + advert.getCategory())
                    .snippet(advert.getDescription()));
        }

        Toast.makeText(this, "Showing all saved items", Toast.LENGTH_SHORT).show();
    }

    private void showItemsWithinRadius(double radiusKm) {
        if (googleMap == null) return;

        googleMap.clear();

        ArrayList<Advert> adverts = databaseHelper.getAllAdverts();

        int count = 0;

        for (Advert advert : adverts) {
            double distance = calculateDistance(
                    userLat,
                    userLng,
                    advert.getLatitude(),
                    advert.getLongitude()
            );

            if (distance <= radiusKm) {
                LatLng itemLocation = new LatLng(advert.getLatitude(), advert.getLongitude());

                googleMap.addMarker(new MarkerOptions()
                        .position(itemLocation)
                        .title(advert.getType() + ": " + advert.getCategory())
                        .snippet(advert.getDescription() + " (" + String.format("%.2f", distance) + " km away)"));

                count++;
            }
        }

        Toast.makeText(this, "Found " + count + " item(s) within " + radiusKm + " km", Toast.LENGTH_SHORT).show();
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 6371;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a =
                Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                        Math.cos(Math.toRadians(lat1)) *
                                Math.cos(Math.toRadians(lat2)) *
                                Math.sin(dLon / 2) *
                                Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return earthRadius * c;
    }
}