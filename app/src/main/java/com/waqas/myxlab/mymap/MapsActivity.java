package com.waqas.myxlab.mymap;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    int count = 0;

    Location loc1 ;
    Location loc2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        loc1 = new Location("");
        loc2 = new Location("");
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
        //change the map types
        //map type hybrid
        //mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        //map type satellite
        //mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        //map type hybrid
        //mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        //map type hybrid
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        LatLng khartoum = new LatLng(15, 32);
        mMap.addMarker(new MarkerOptions().position(khartoum).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(khartoum));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                String address = getAddressFromLocation(latLng.latitude, latLng.longitude);
                mMap.addMarker(new MarkerOptions().position(latLng).
                        icon(BitmapDescriptorFactory.
                                defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).
                        title(address));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                Toast.makeText(MapsActivity.this, "MapClicked at " + address, Toast.LENGTH_SHORT).show();

                count++;



                if (count == 1) {


                    loc1.setLatitude(latLng.latitude);
                    loc1.setLongitude(latLng.longitude);
                } else if (count == 2) {

                    loc2.setLatitude(latLng.latitude);
                    loc2.setLongitude(latLng.longitude);

                    float dis = loc1.distanceTo(loc2);
                    float disKM = dis/1000;

                    Toast.makeText(MapsActivity.this, disKM +"KM", Toast.LENGTH_SHORT).show();
                    count = 0;
                }

                Log.e("count",""+count);
            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                mMap.addMarker(new MarkerOptions().position(latLng).
                        icon(BitmapDescriptorFactory.
                                fromResource(R.mipmap.ic_launcher)).title("Marker"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        });


    }


    private String getAddressFromLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
        String address = "";

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address fetchedAddress = addresses.get(0);
                StringBuilder strAddress = new StringBuilder();
                for (int i = 0; i < fetchedAddress.getMaxAddressLineIndex(); i++) {
                    strAddress.append(fetchedAddress.getAddressLine(i)).append(" ");
                }
                address = strAddress.toString();
                Log.e("Address", strAddress.toString());
                //txtLocationAddress.setText(strAddress.toString());
            } else {
                Log.e("Address", "Searching");

                //txtLocationAddress.setText("Searching Current Address");
            }

        } catch (IOException e) {
            e.printStackTrace();
            //printToast("Could not get address..!");
        }

        return address;
    }

}
