package com.example.dck.hikerwatch2;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    TextView latitudeTextView;
    TextView longitudeTextView;
    TextView accuracyTextView;
    TextView addressTextView;
    TextView altitudeTextView;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }

        }
    }

    public void update(Location location) {

        latitudeTextView.setText("LATITUDE : " + Double.toString(location.getLatitude()));
        longitudeTextView.setText("LONGITUDE : " + Double.toString(location.getLongitude()));
        accuracyTextView.setText("ACCURACY : " + Double.toString(location.getAccuracy()));
        altitudeTextView.setText("ALTITUDE : " + Double.toString(location.getAltitude()));


        longitudeTextView.setVisibility(View.VISIBLE);
        latitudeTextView.setVisibility(View.VISIBLE);
        accuracyTextView.setVisibility(View.VISIBLE);


        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());


        try {
            List<Address> Address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            String address = "ADDERESS : ";

            if (Address != null && Address.size() > 0) {
                if (Address.get(0).getThoroughfare() != null) {
                    address += Address.get(0).getThoroughfare() + " ";
                }
                if (Address.get(0).getLocality() != null) {
                    address += Address.get(0).getLocality() + " ";

                }
                if (Address.get(0).getPostalCode() != null) {
                    address += Address.get(0).getPostalCode() + " ";


                }
                if (Address.get(0).getAdminArea() != null) {
                    address += Address.get(0).getAdminArea();
                }
                addressTextView = (TextView) findViewById(R.id.addressTextView);

                addressTextView.setText(address);
                addressTextView.setVisibility(View.VISIBLE);


            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latitudeTextView = (TextView) findViewById(R.id.latitudeTextView);
        longitudeTextView = (TextView) findViewById(R.id.longitudeTextView);
        accuracyTextView = (TextView) findViewById(R.id.accuracyTextView);
        altitudeTextView = (TextView) findViewById(R.id.altitudeTextView);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                update(location);

            }


            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (Build.VERSION.SDK_INT < 23) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        }else{
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (location!=null){
                    update(location);

                }

            }


        }



    }
}
