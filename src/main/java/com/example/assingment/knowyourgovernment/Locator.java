package com.example.assingment.knowyourgovernment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import static android.content.Context.LOCATION_SERVICE;

public class Locator
{
    private LocationManager locationManager;
    private LocationListener locationListener;
    MainActivity mainActivity;
    public Locator(MainActivity mainActivity)
    {
        this.mainActivity=mainActivity;
        setUpLocationManager();
    }

    private boolean checkPermission()
    {
        if (ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(mainActivity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);
            return false;
        }
        return true;
    }

    public void setUpLocationManager()
    {
        if (!checkPermission())
            return;
        // Get the system's Location Manager
        if(locationManager==null) {
            locationManager = (LocationManager) mainActivity.getSystemService(LOCATION_SERVICE);


                locationListener = new LocationListener() {
                    public void onLocationChanged(Location location) {
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
                locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 100000, 0, locationListener );

        }
        if (locationManager != null)
        {
            Location loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(loc!=null)
            {
                mainActivity.setLocation(loc.getLatitude(),loc.getLongitude());
                return;
            }
            loc = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            if(loc!=null)
            {
                mainActivity.setLocation(loc.getLatitude(),loc.getLongitude());
                return;
            }
            loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(loc!=null)
            {
                mainActivity.setLocation(loc.getLatitude(),loc.getLongitude());
                return;
            }
            mainActivity.locationView.setText("set location manually");
            return;
        }
    }

    public void destroy()
    {
        locationManager.removeUpdates(locationListener);
        locationManager = null;
    }

}
