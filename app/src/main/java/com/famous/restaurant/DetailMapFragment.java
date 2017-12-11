package com.famous.restaurant;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by BOBAE on 2017-12-08.
 */

public class DetailMapFragment extends Fragment implements OnMapReadyCallback{
    View view;
    MapView mapView;
    String restuarantName;
    float latitude;
    float logitude;
    GoogleMap googleMap;
    public DetailMapFragment() {
        this((float)37.50094, (float)126.95025, "지코바 치킨");
    }
    public DetailMapFragment( float latitude, float logitude, String restuarantName){
        this.latitude=latitude;
        this.logitude=logitude;
        this.restuarantName = restuarantName;
    }

    public String getRestuarantName() {
        return restuarantName;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLogitude() {
        return logitude;
    }

    public GoogleMap getGoogleMap() {
        return googleMap;
    }

    public void setRestuarantName(String restuarantName) {
        this.restuarantName = restuarantName;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setLogitude(float logitude) {
        this.logitude = logitude;
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.detail_map_fragment, container, false);
        mapView = (MapView)view.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Updates the location and zoom of the MapView
        LatLng latLng = new LatLng(latitude,logitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
        googleMap.animateCamera(cameraUpdate);
        googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(restuarantName));
        this.googleMap = googleMap;
    }

}