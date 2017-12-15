package com.famous.restaurant;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;

/**
 * Created by BOBAE on 2017-12-10.
 */

public class GPSInfo implements LocationListener{
    private static GPSInfo gpsInfo = null;
    private Context context;
    private double longitude;
    private double latitude;

    private GPSInfo(){}
    public static GPSInfo getInstance(@NonNull Context context){
        if(gpsInfo == null){
            gpsInfo = new GPSInfo();
        }
        gpsInfo.context = context;
        return gpsInfo;
    }
    @Override
    public void onLocationChanged(Location location) {
        LocationManager locationManager = (LocationManager)context.getSystemService(Context. LOCATION_SERVICE);

        // 경도, 위도 가져오기
        longitude = location.getLongitude();
        latitude = location.getLatitude();

        // Stop the update to prevent changing the location.
        //locationManager.removeUpdates( this );
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
    @Override
    public void onProviderEnabled(String provider) {

    }
    @Override
    public void onProviderDisabled(String provider) {
    }
    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
