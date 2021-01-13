package com.example.memorableplaces;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> addPlace;
    ArrayList<LatLng> locations;
    LocationManager locationManager;
    LocationListener locationListener;

    double latitude;
    double longitude;
    ListView listView;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);
        addPlace = new ArrayList<>();
        addPlace.add("Add a Place");
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                Log.i("Location", String.valueOf(location.getLatitude()));
                Log.i("Location", String.valueOf(location.getLongitude()));
                latitude = location.getLatitude();
                longitude = location.getLongitude();


            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {

            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
        };
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


            if (lastKnownLocation != null) {
                longitude = lastKnownLocation.getLongitude();
                latitude = lastKnownLocation.getLatitude();
                System.out.println("LAST KNOWN LOCATION" + latitude + "" + longitude);
            }


        }


        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, addPlace);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener((adapterView, view, i, l) -> {

            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            intent.putExtra("latitude", latitude);
            intent.putExtra("longitude", longitude);
            startActivityForResult(intent, 1);

        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode== Activity.RESULT_OK) {
            if (requestCode ==1) {
                if (data != null)
                addPlace.add(data.getStringExtra("address"));
                ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, addPlace);
                listView.setAdapter(arrayAdapter);


            }
        }
    }
}