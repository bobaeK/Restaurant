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
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class DetailActivity extends AppCompatActivity {
    public final static int MY_PERMISSIONS = 1;
    private ArrayList<String> images = new ArrayList<String>();
    private RestaurantVO restaurantVO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DetailMapFragment detailMapFragment;
        setContentView(R.layout.activity_detail);
        //음식점 정보 받아오기
        Intent intent = getIntent();
        restaurantVO = intent.getParcelableExtra("SELECTED_ITEM");
        Log.i("RESTAURANTVO(BB)", restaurantVO.toString());

        //이 조건문 왜 쓰는지 모름,,
        if (savedInstanceState == null) {
            detailMapFragment = new DetailMapFragment(DetailMapFragment.Check.DetailActivity,
                                                        getApplicationContext(),
                                                        (float)restaurantVO.getLatitude(),
                                                        (float)restaurantVO.getLongitude(),
                                                        restaurantVO.getName());
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.map_fragment, detailMapFragment, "detail_map")
                    .commit();
        }
        //이미지 동적으로 넣기
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.image_layout);
        StringTokenizer token = new StringTokenizer(restaurantVO.getImageURL(),"$");
        //LayoutParams 셋팅-왜 LinearLayout의 LayoutParmas?
        final int width = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, getResources().getDisplayMetrics());
        final int height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, getResources().getDisplayMetrics());
        final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(width, height);
        layoutParams.rightMargin = margin;
        while(token.hasMoreTokens()) {
            String url = token.nextToken();
            Log.i("image url(BB)", url);
            images.add(url);
        }
        for(String url : images) {
            ImageView imageView = new ImageView(this);
            //안드로이드 이미지 크기설정
            imageView.setLayoutParams(layoutParams);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //안드로이드 이미지 이벤트
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DetailActivity.this, ImageActivity.class);
                    intent.putStringArrayListExtra("images", images);
                    startActivity(intent);
                }
            });
            //이미지 넣기
            Glide.with(this).load(url).into(imageView);
            linearLayout.addView(imageView);
        }

        //음식점정보
        TextView phoneText = (TextView)findViewById(R.id.phone_text);
        TextView locationText = (TextView)findViewById(R.id.location_text);
        TextView timeText = (TextView)findViewById(R.id.time_text);
        TextView menuText = (TextView)findViewById(R.id.menu_text);

        phoneText.setText(restaurantVO.getPhone());
        locationText.setText(restaurantVO.getAddress());
        timeText.setText(restaurantVO.getBusinessHours());
        StringBuffer buffer = new StringBuffer("메뉴정보\n");
        token = new StringTokenizer(restaurantVO.getMenuList(),"$");
        while(token.hasMoreTokens()){
            buffer.append(token.nextToken()+" ");
            buffer.append(token.nextToken()+"\n");
        }
        menuText.setText(buffer);
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

    //전화걸기
    public void onPhoneCallClicked(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+restaurantVO.getPhone()));
        startActivity(intent);
    }
    //전체후기보기
    public void onTotalReviewClicked(View view) {
        Intent intent = new Intent(this, TotalReviewActivity.class);
        //전체후기 볼 음식점 아이디 전달
        intent.putExtra("restaurant_name", 1);
        startActivity(intent);
    }

    //인증하기
    public void onAddPlaceClicked(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)||ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                new AlertDialog.Builder(this).setTitle("Request Permission Rationale")
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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 10000, gpsInfo);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 100000, gpsInfo);

        boolean isGPSEnabled = locationManager.isProviderEnabled("gps");
        boolean isNetworkEnabled = locationManager.isProviderEnabled("network");

        Location location = null;
        if(isGPSEnabled)
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        else if(isNetworkEnabled)
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        else{
            Toast.makeText(getApplicationContext(), "위치정보를 가져올 수 없습니다. 네트워크상태를 확인해 주세요!", Toast.LENGTH_SHORT);
            return;
        }
        Log.i("MyGPSInfo", "gps_enable : "+isGPSEnabled+" network_enable : "+isNetworkEnabled);
        Log.i("MyGPSInfo", "longitude : " + location.getLongitude() + " latitude : " + location.getLatitude());
    }
    //후기등록
    public void onAddReviewClicked(View view){

    }
    //뒤로가기
    public void onBackButtonClicked(View view){
        finish();
    }
}