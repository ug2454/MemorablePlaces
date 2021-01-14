package com.example.memorableplaces;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static ArrayList<String> addPlace = new ArrayList<>();
    static ArrayList<LatLng> locations = new ArrayList<>();
    LocationManager locationManager;
    LocationListener locationListener;
    static ArrayAdapter arrayAdapter;

    double latitude;
    double longitude;
    ListView listView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);
        addPlace = new ArrayList<>();
        addPlace.add("Add a Place");
        locations.add(new LatLng(0, 0));

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, addPlace);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener((adapterView, view, i, l) -> {

            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            System.out.println("ITEM" + i);
            intent.putExtra("placeLocation", i);


            startActivity(intent);
        });
    }


}