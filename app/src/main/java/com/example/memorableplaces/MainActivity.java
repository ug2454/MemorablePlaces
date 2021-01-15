package com.example.memorableplaces;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
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
        addPlace = new ArrayList<>();
SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.memorableplaces",Context.MODE_PRIVATE);
        ArrayList<String> latitudes= new ArrayList<>();
        ArrayList<String> longitudes = new ArrayList<>();



        try{
            System.out.println((ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("places",ObjectSerializer.serialize(new ArrayList<String>()))));
          addPlace= (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("places",ObjectSerializer.serialize(new ArrayList<String>())));
          latitudes = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("latitudes",ObjectSerializer.serialize(new ArrayList<String>())));
          longitudes=  (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("longitudes",ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (Exception e) {
            e.printStackTrace();
        }



        if(addPlace.size()>0 && latitudes.size()>0 && longitudes.size()>0){
            System.out.println("IN IF");
            if(addPlace.size()==latitudes.size() && addPlace.size()==longitudes.size()){
                for(int i=0;i<addPlace.size();i++){
                    LatLng latLng = new LatLng(Double.parseDouble(latitudes.get(i)),Double.parseDouble(longitudes.get(i)));
                    locations.add(latLng);
                }
            }
        }
        else{
            System.out.println("IN ELSE");
            addPlace.add("Add a Place");
            locations.add(new LatLng(0, 0));

        }


        listView = findViewById(R.id.listView);


        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, addPlace);
        listView.setAdapter(arrayAdapter);


        listView.setOnItemClickListener((adapterView, view, i, l) -> {

            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            System.out.println("ITEM" + i);
            intent.putExtra("placeLocation", i);


            startActivity(intent);
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
              addPlace.remove(i);
              locations.remove(i);
              arrayAdapter.notifyDataSetChanged();
              return false;
            }
        });
    }


}