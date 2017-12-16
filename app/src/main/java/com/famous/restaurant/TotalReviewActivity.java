package com.famous.restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static com.famous.restaurant.R.id.review;

public class TotalReviewActivity extends AppCompatActivity {
    StorageReference storageReference;
    DatabaseReference mDatabase;
    List<ReviewVO> reviewList = new ArrayList<ReviewVO>();
    String restaurant_name;

    TextView reviewCount;
    ListView listView;
    TotalReviewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_review);
        //전체 후기 목록 보여주기
        reviewCount = (TextView)findViewById(R.id.review_count);
        listView = (ListView)findViewById(R.id.total_review_list);
        adapter = new TotalReviewAdapter(getApplicationContext());
        //데이터 가져오기
        Intent intent = getIntent();
        restaurant_name=intent.getStringExtra("restaurant_name");


        //Toast.makeText(this, restaurant_name, Toast.LENGTH_SHORT).show();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabase = firebaseDatabase.getReference("reviews");
        storageReference = FirebaseStorage.getInstance().getReference();
        /* 데이터 setting */
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
                while (child.hasNext()) {
                    DataSnapshot reviewData = child.next();
                    ReviewVO review = reviewData.getValue(ReviewVO.class);
                    if(review.getRestaurant().equals(restaurant_name)){
                        review.setKey(reviewData.getKey());
                        reviewList.add(review);
//                        Toast.makeText(getApplicationContext(),review.getKey(),Toast.LENGTH_SHORT).show();
                    }
                }
                reviewCount.setText("후기("+reviewList.size()+")");
                DatabaseReference dataR1;
                for(final ReviewVO review : reviewList){
                    if(review.getImageCnt()>0){
                        //Toast.makeText(getApplicationContext(),"tf",Toast.LENGTH_SHORT).show();
                        dataR1 = mDatabase.child(review.getKey()).child("imageUrl");
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

        // reviewList 안에 데이터 들어있음.
        // 이미지는 list형식으로 reviewList의 imageUri 에 넣음.
        // 이미지 개수는 imageCnt
        //Toast.makeText(this, "review_count"+reviewList.size(), Toast.LENGTH_SHORT).show();
        adapter.addItems(reviewList);
        listView.setAdapter(adapter);

        //리얼후기 보기 스위치
        Switch realSwitch = (Switch)findViewById(R.id.real_switch);
        realSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i("MYSWITCH_STATE:", "state : "+isChecked);
                //Toastx.makeText(TotalReviewActivity.this, "check"+isChecked, Toast.LENGTH_SHORT).show();
                if(isChecked){
                    //리얼후기만 보기
                    adapter.addItems(new ArrayList<ReviewVO>());
                    for(ReviewVO r:reviewList){
                        if(r.isAuthentication()){
                            adapter.addItem(r);
                        }
                    }
                }else{
                    //전체후기 보기
                    Log.i("reviewList", reviewList.toString());
                    adapter.addItems(reviewList);
                }
                reviewCount.setText("후기("+adapter.getCount()+")");
                adapter.notifyDataSetChanged();
            }
        });
    }
    public void onBackButtonClicked(View view){
        finish();
    }
}
