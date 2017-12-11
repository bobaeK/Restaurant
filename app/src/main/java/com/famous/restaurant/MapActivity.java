package com.famous.restaurant;

import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity {

    private GoogleMap mMap;
    private String restaurantName;
    private float latitude;
    private float longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent intent = getIntent();
        restaurantName = intent.getStringExtra("restaurantName");
        latitude = intent.getFloatExtra("latitude", 0);
        longitude = intent.getFloatExtra("longitude", 0);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        DetailMapFragment detailMapFragment = null;
        if (savedInstanceState == null) {
            detailMapFragment = new DetailMapFragment(DetailMapFragment.Check.MapActivity, getApplicationContext(),latitude, longitude, restaurantName);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.map_fragment, detailMapFragment, "detail_map")
                    .commit();
        }
    }
    //뒤로가기
    public void onBackButtonClicked(View view){
        finish();
    }
}
