package com.famous.restaurant;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
    enum Check {MapActivity, DetailActivity}
    private View view;
    private MapView mapView;
    private String restaurantName;
    private float latitude;
    private float longitude;
    private Context context;
    private Check check;
    public DetailMapFragment(Check check, Context context) {
        this(check, context,(float)37.50094, (float)126.95025, "지코바 치킨");
    }
    public DetailMapFragment(Check check, Context context, float latitude, float logitude, String restuarantName){
        this.check = check;
        this.context = context;
        this.latitude=latitude;
        this.longitude=logitude;
        this.restaurantName = restuarantName;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
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
        LatLng latLng = new LatLng(latitude,longitude);
        if(check == Check.DetailActivity) {
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15
            );
            googleMap.animateCamera(cameraUpdate);
            googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(restaurantName));
            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    Intent intent = new Intent(context, MapActivity.class);
                    intent.putExtra("latitude", latitude);
                    intent.putExtra("longitude", longitude);
                    intent.putExtra("restaurantName", restaurantName);
                    context.startActivity(intent);
                }
            });
        }else if(check == Check.MapActivity){
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
            googleMap.animateCamera(cameraUpdate);
            googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(restaurantName));
        }
    }

}