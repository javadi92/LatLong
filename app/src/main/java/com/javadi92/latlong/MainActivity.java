package com.javadi92.latlong;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private LocationManager locationManager;
    Button button,buttonOpenMap;
    TextView textView;
    AlertDialog dialog;
    static double longitude;
    static double latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        textView = findViewById(R.id.textView);

        buttonOpenMap=findViewById(R.id.button_open_map);

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int checkPermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
                if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 20);
                } else {
                    Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
                    onLocationChanged(location);
                }
            }
        });

        buttonOpenMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latitude, longitude);
                String uri="https://maps.google.com?q="+latitude+","+longitude;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });


    }

    @Override
    public void onLocationChanged(Location location) {
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        textView.setText("Longitude: " + longitude + "\n" + "Latitude: " + latitude);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 20) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
                    onLocationChanged(location);
                }
            }
            else if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)){
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("نیاز به دسترسی به موقعیت مکانی است");
                builder.setNegativeButton("خب", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.cancel();
                    }
                });
                dialog=builder.create();
                dialog.show();
            }
        }
    }
}
