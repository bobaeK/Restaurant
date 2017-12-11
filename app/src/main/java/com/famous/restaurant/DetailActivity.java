package com.famous.restaurant;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    public final static int MY_PERMISSIONS = 1;
    private Location lastKnownLocation = null;
    private ArrayList<String> images = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent=getIntent();
        RestaurantVO sRestaurantVO=intent.getParcelableExtra("SELECTED_ITEM");
        Toast.makeText(getApplicationContext(), sRestaurantVO.getName(), Toast.LENGTH_SHORT).show();

        //이 조건문 왜 쓰는지 모름,,
        if (savedInstanceState == null) {
            DetailMapFragment detailMapFragment = new DetailMapFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.map_fragment, detailMapFragment, "detail_map")
                    .commit();
        }
        //이미지 ListView


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "GPS 권한을 사용자가 승인함.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "GPS 권한 거부됨.", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    //지도확대
    public void onMapClicked() {

    }

    //전화걸기
    public void onPhoneCallClicked(View view) {
        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:010-1000-1000"));
        startActivity(myIntent);
    }

    //사진확대
    public void onImageClicked(View view) {
        Intent intent = new Intent(this, ImageActivity.class);
        intent.putStringArrayListExtra("images", images);
        startActivity(intent);
    }

    //전체후기보기
    public void onTotalReviewClicked(View view) {
        Intent intent = new Intent(this, TotalReviewActivity.class);
        //전체후기 볼 음식점 아이디 전달
        intent.putExtra("restuarant_name", 1);
        startActivity(intent);
    }

    //인증하기
    public void onAddPlaceClicked(View view) {


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)||ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                new AlertDialog.Builder(this).setTitle("Request Persmission Rationale")
                                            .setMessage("인증을 위해서는 gps 허용을 설정해야 합니다.")
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    ActivityCompat.requestPermissions(DetailActivity.this,
                                                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}
                                                            ,MY_PERMISSIONS);
                                                }
                                            });
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS);
            }
            //return;
        }
        // Update location to get.
        GPSInfo gpsInfo = GPSInfo.getInstance(this);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.removeUpdates(gpsInfo);    // Stop the update if it is in progress.
        locationManager.requestLocationUpdates("gps", 0, 0, gpsInfo);
        locationManager.requestLocationUpdates("network", 0, 0, gpsInfo);
        boolean isGPSEnabled = locationManager.isProviderEnabled("gps");
        boolean isNetworkEnabled = locationManager.isProviderEnabled("network");
        Log.i("MyGPSInfo", "gps_enable : "+isGPSEnabled+" network_enable : "+isNetworkEnabled);
        Log.i("MyGPSInfo", "longitude : " + gpsInfo.getLongitude() + " latitude : " + gpsInfo.getLatitude());

    }
    //후기등록
    public void onAddReviewClicked(View view){

    }
    //뒤로가기
    public void onBackButtonClicked(View view){
        finish();
    }
    //상세보기화면 이미지 리스트뷰 어답터
    private class DetailImagedapter extends BaseAdapter {
        private ArrayList<DetailImageItem> items = new ArrayList<DetailImageItem>();

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            DetailPictureItemView view = (DetailPictureItemView) convertView;

            if(convertView == null)
                view = new DetailPictureItemView(getApplicationContext());
            DetailImageItem item = items.get(position);
            view.setImage(item.getImage());
            return view;
        }
        void addItem(DetailImageItem item){ items.add(item); }
    }
}
