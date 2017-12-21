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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static com.famous.restaurant.R.id.review;

public class TotalReviewActivity extends AppCompatActivity {
    DatabaseReference mDatabase;
    List<ReviewVO> reviewList = new ArrayList<ReviewVO>();
    String restaurant_name;
    TextView restaurantName;
    TextView reviewCount;
    ListView listView;
    TotalReviewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_review);
        //전체 후기 목록 보여주기
        restaurantName = (TextView)findViewById(R.id.restaurant_name);
        reviewCount = (TextView)findViewById(R.id.review_count);
        listView = (ListView)findViewById(R.id.total_review_list);

        //데이터 가져오기
        Intent intent = getIntent();
        restaurant_name=intent.getStringExtra("restaurant_name");
        restaurantName.setText(restaurant_name);

        //Toast.makeText(this, restaurant_name, Toast.LENGTH_SHORT).show();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabase = firebaseDatabase.getReference("reviews");
        Query reviewQuery = mDatabase.orderByChild("restaurant").equalTo(restaurant_name);

        /* 데이터 setting */
        reviewQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
                while (child.hasNext()) {
                    DataSnapshot reviewData = child.next();
                    final ReviewVO review = reviewData.getValue(ReviewVO.class);
                    review.setKey(reviewData.getKey());
                    if(review.getImageCnt()>0){
                        DatabaseReference dataR1 = mDatabase.child(review.getKey()).child("imageUrl");
                        dataR1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Iterator<DataSnapshot> list_it = dataSnapshot.getChildren().iterator();
                                ArrayList<String> image_url_list = new ArrayList<String>();
                                while (list_it.hasNext()) {
                                    DataSnapshot dss = list_it.next();
                                    image_url_list.add(dss.getValue(String.class));
        //                                  Toast.makeText(getApplicationContext(),dss.getKey(),Toast.LENGTH_SHORT).show();
                                }
                                review.setImageUri(image_url_list);
                                Log.i("iamgeUrl_1",image_url_list.toString());
                                Log.i("iamgeUrl_2",review.getImageUri().toString());
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }
                    reviewList.add(review);
                }
                reviewCount.setText("후기("+reviewList.size()+")");
                adapter = new TotalReviewAdapter();
                adapter.addItems(reviewList);
                listView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // reviewList 안에 데이터 들어있음.
        // 이미지는 list형식으로 reviewList의 imageUri 에 넣음.
        // 이미지 개수는 imageCnt
        //adapter.addItems(reviewList);
        //listView.setAdapter(adapter);

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
    //후기등록
    public void onAddReviewClicked(View view){
        Intent intent = new Intent(this,ReviewAddActivity.class);
        intent.putExtra("restaurant_name", restaurant_name);
        startActivity(intent);
    }
    //뒤로가기
    public void onBackButtonClicked(View view){
        finish();
    }

    private class TotalReviewAdapter extends BaseAdapter {
        private List<ReviewVO> items = new ArrayList<ReviewVO>();
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
        public TotalReviewAdapter(){

        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TotalReviewItemView view = (TotalReviewItemView)convertView;

            if(convertView == null)
                view = new TotalReviewItemView(TotalReviewActivity.this);
            ReviewVO item = items.get(position);
            view.setId(item.getUser_id());
            view.setDate(item.getDate());
            view.setRatingBar(item.getRating_star());
            view.setRealReview(item.isAuthentication());
            view.setReview(item.getReview_text());
            Log.i("image_adopter",item.getImageUri().toString());
            view.setImages(item.getImageUri());

            return view;
        }
        public void addItem(ReviewVO item){
            items.add(item);
        }
        public void addItems(List<ReviewVO> items){
            this.items = items;
        }
        public List<ReviewVO> getItems(){
            return this.items;
        }
    }
}
