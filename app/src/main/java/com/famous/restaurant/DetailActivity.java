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
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class DetailActivity extends AppCompatActivity {
    public final static int MY_PERMISSIONS = 1;
    private ArrayList<String> images = new ArrayList<String>();
    private RestaurantVO restaurantVO;
    DatabaseReference authDatabase;
    DatabaseReference mDatabase;
    DatabaseReference reviewDatabase;

    //음식점정보
    TextView nameText;
    TextView phoneText;
    TextView locationText;
    TextView timeText;
    TextView menuText;
    //지도
    DetailMapFragment detailMapFragment;
    //후기 3개
    ListView listView;
    TotalReviewAdapter adapter;
    Query reviewQuary;
    List<ReviewVO> reviewList = new ArrayList<ReviewVO>();

    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //DetailMapFragment detailMapFragment;
        setContentView(R.layout.activity_detail);

        //음식점 정보 받아오기
        Intent intent = DetailActivity.this.getIntent();
        name = intent.getStringExtra("SELECTED_ITEM");
        /*Intent intent = getIntent();
        final String name = intent.getParcelableExtra("SELECTED_ITEM");*/

        authDatabase = FirebaseDatabase.getInstance().getReference("authentication");
        mDatabase = FirebaseDatabase.getInstance().getReference("restaurants");
        reviewDatabase = FirebaseDatabase.getInstance().getReference("reviews");
        reviewQuary = reviewDatabase.orderByChild("restaurant").equalTo(name).limitToFirst(3);

        nameText = (TextView)findViewById(R.id.name_text);
        phoneText = (TextView)findViewById(R.id.phone_text);
        locationText = (TextView)findViewById(R.id.location_text);
        timeText = (TextView)findViewById(R.id.time_text);
        menuText = (TextView)findViewById(R.id.menu_text);

        listView = (ListView)findViewById(R.id.total_review_list);
        adapter = new TotalReviewAdapter(getApplicationContext());

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
                Log.i("SELECTED_ITME", name);
                while(child.hasNext()){
                    DataSnapshot detailData = child.next();
                    Log.i("data", detailData.getKey());
                    if(detailData.getKey().equals(name)){
                        restaurantVO = detailData.getValue(RestaurantVO.class);
                        Log.i("correct data",restaurantVO.toString());
                        //datasetting
                        detailMapFragment = new DetailMapFragment(DetailMapFragment.Check.DetailActivity,
                                getApplicationContext(),
                                (float)restaurantVO.getLatitude(),
                                (float)restaurantVO.getLongitude(),
                                restaurantVO.getName());
                        DetailActivity.this.getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.map_fragment, detailMapFragment, "detail_map")
                                .commit();
                        //이미지 동적으로 넣기
                        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.image_layout);
                        StringTokenizer token = new StringTokenizer(restaurantVO.getImageURL(),"$");
                        //LayoutParams 셋팅-왜 LinearLayout의 LayoutParmas?
                        int width = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, getResources().getDisplayMetrics());
                        int height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, getResources().getDisplayMetrics());
                        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
                        LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(width, height);
                        layoutParams.rightMargin = margin;
                        while(token.hasMoreTokens()) {
                            String url = token.nextToken();
                            Log.i("image url(BB)", url);
                            images.add(url);
                        }
                        for(String url : images) {
                            final String choice = url;
                            ImageView imageView = new ImageView(DetailActivity.this);

                            //안드로이드 이미지 크기설정
                            imageView.setLayoutParams(layoutParams);
                            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            //안드로이드 이미지 이벤트
                            imageView.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(DetailActivity.this, ImageActivity.class);
                                    intent.putStringArrayListExtra("images", images);
                                    intent.putExtra("choice_image", choice);
                                    startActivity(intent);
                                }
                            });
                            //이미지 넣기
                            Glide.with(DetailActivity.this).load(url).into(imageView);
                            linearLayout.addView(imageView);
                        }
                        nameText.setText(restaurantVO.getName());
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
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //후기 3개
        reviewQuary.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
                Log.i("SELECTED_ITME", name);
                while(child.hasNext()){
                    DataSnapshot reviewData = child.next();
                    ReviewVO review = reviewData.getValue(ReviewVO.class);
                    Log.i("review data",review.toString());
                    review.setKey(reviewData.getKey());
                    reviewList.add(review);
                }
                DatabaseReference dataR1;
                for(final ReviewVO review:reviewList){
                    if(review.getImageCnt() > 0){
                        dataR1 = reviewDatabase.child(review.getKey()).child("imageUrl");
                        dataR1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                Iterator<DataSnapshot> list_it = dataSnapshot.getChildren().iterator();
                                ArrayList<String> image_url_list = new ArrayList<String>();
                                while (list_it.hasNext()) {
                                    DataSnapshot dss = list_it.next();
                                    image_url_list.add(dss.getValue(String.class));
//                                    Toast.makeText(getApplicationContext(),dss.getKey(),Toast.LENGTH_SHORT).show();
                                }
                                review.setImageUri(image_url_list);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        adapter.addItems(reviewList);
        listView.setAdapter(adapter);
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
        intent.putExtra("restaurant_name", restaurantVO.getName());
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

        AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
        double gapLatitude = Math.abs(location.getLatitude() - restaurantVO.getLatitude());
        double gapLongitude = Math.abs(location.getLongitude() - restaurantVO.getLongitude());
        Log.d("gapLatitude", Double.toString(gapLatitude));
        Log.d("gapLongitude", Double.toString(gapLongitude));
        if (gapLatitude < 0.0005 && gapLongitude < 0.005) {
            builder.setTitle("인증 완료");
            builder.setMessage(restaurantVO.getName() + "이(가) 가본 음식점으로 인증되었습니다!" +
                    " 후기를 작성하시겠습니까");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(), "확인", Toast.LENGTH_LONG).show();
                }
            });
            builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new AlertDialog.Builder(DetailActivity.this)
                            .setTitle("인증완료")
                            .setMessage("본인이 인증한 목록을 보고 싶다면 '마이페이지>인증목록'에서 확인하실 수 있습니다.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    authDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Calendar now = Calendar.getInstance();
                                            String nowDate = now.get(Calendar.YEAR) + "/" +
                                                    (now.get(Calendar.MONTH) + 1) + "/" + now.get(Calendar.DATE);

                                            AuthenticationVO auth = new AuthenticationVO();
                                            auth.setMem_id(SaveSharedPreference.getUserName(DetailActivity.this));
                                            auth.setRestaurant(restaurantVO.getName());
                                            auth.setReview_id("none");
                                            auth.setDate(nowDate);

                                            authDatabase.child(authDatabase.push().getKey()).setValue(auth);
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            });
                }
            });
        } else {
            builder.setTitle("인증 실패");
            builder.setMessage(restaurantVO.getName() + " 의 위치와 현재 위치가 일치하지 않습니다!");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
        }

        builder.show();
    }
    //후기등록
    public void onAddReviewClicked(View view){
        Intent intent = new Intent();
        intent.putExtra("restaurant_name", restaurantVO.getName());
        startActivity(intent);
    }
    //뒤로가기
    public void onBackButtonClicked(View view){
        finish();
    }
}