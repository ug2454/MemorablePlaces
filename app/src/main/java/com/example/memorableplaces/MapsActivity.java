package com.example.memorableplaces;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    double lat;
    double longi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Intent intent = getIntent();
        lat = intent.getDoubleExtra("latitude", 0);

        longi = intent.getDoubleExtra("longitude", 0);
        Toast.makeText(this, "" + lat + ", " + longi, Toast.LENGTH_SHORT).show();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


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

        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng currentLocation = new LatLng(lat, longi);
        if (!checkReady()) {
            return;
        }
        mMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16));


        mMap.setOnMapLongClickListener(latLng -> {
            List<Address> addressList = null;
            LatLng newLocation = new LatLng(latLng.latitude, latLng.longitude);
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            String address = "";

            try {
                addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                Log.i("address", addressList.toString());

                if (addressList != null && addressList.size() > 0) {

                    Log.i("PlaceInfo", addressList.get(0).toString());
//                    if (addressList.get(0).getFeatureName() != null) {
//                        address += addressList.get(0).getFeatureName() + ", ";
//                    }
//                    if (addressList.get(0).getLocality() != null) {
//                        address += addressList.get(0).getLocality() + ", ";
//                    }
//                    if (addressList.get(0).getAdminArea() != null) {
//                        address += addressList.get(0).getAdminArea();
//                    }

                    address = addressList.get(0).getAddressLine(0);
                    Toast.makeText(this, "location saved", Toast.LENGTH_SHORT).show();
                    Log.i("Address", address);

                }
                else{
                    address= Time.getCurrentTimezone();
                    Toast.makeText(this, "location saved", Toast.LENGTH_SHORT).show();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i("new location", "" + latLng.latitude + "," + latLng.longitude);
            if (addressList != null && addressList.size() > 0) {
                mMap.addMarker(new MarkerOptions().position(newLocation).title(addressList.get(0).getFeatureName() + ", " + addressList.get(0).getLocality()));

            } else {
                mMap.addMarker(new MarkerOptions().position(newLocation).title("Location Unavailable"));
            }
            Intent intent=new Intent();
            intent.putExtra("address",address);
            setResult(RESULT_OK,intent);




        });

       /*
         TODO: get address of current location on the tooltip marker
          implement touch and hold to place the marker and get the address of that place
        */


    }

    private boolean checkReady() {
        if (mMap == null) {
            Toast.makeText(this, R.string.map_not_ready, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}