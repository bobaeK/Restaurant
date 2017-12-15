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
    final List<ReviewVO> reviewList = new ArrayList<ReviewVO>();
    String restaurant_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_review);
        //전체 후기 목록 보여주기
        final ListView listView = (ListView)findViewById(R.id.total_review_list);
        final TotalReviewAdapter adapter = new TotalReviewAdapter();
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
        Toast.makeText(this, "review_count"+reviewList.size(), Toast.LENGTH_SHORT).show();
        //testData
        ReviewVO r1 = new ReviewVO();
        r1.setDate(new Date().toString());
        r1.setAuthentication(true);
        r1.setImageCnt(0);
        r1.setImageUri(new ArrayList<String>());
        r1.setRating_star(5);
        r1.setReview_text("맛있다");
        r1.setUser_id("hello");
        r1.setRestaurant("The Kone");
        ReviewVO r2 = new ReviewVO();
        r2.setDate(new Date().toString());
        r2.setAuthentication(true);
        r2.setImageCnt(0);
        r2.setImageUri(new ArrayList<String>());
        r2.setRating_star(5);
        r2.setReview_text("맛있다");
        r2.setUser_id("hello");
        r2.setRestaurant("The Kone");
        ReviewVO r3 = new ReviewVO();
        r3.setDate(new Date().toString());
        r3.setAuthentication(false);
        r3.setImageCnt(0);
        r3.setImageUri(new ArrayList<String>());
        r3.setRating_star(5);
        r3.setReview_text("맛있다");
        r3.setUser_id("hello");
        r3.setRestaurant("The Kone");
        reviewList.add(r1);
        reviewList.add(r2);
        reviewList.add(r3);

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
                    adapter.items = new ArrayList<ReviewVO>();
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
                adapter.notifyDataSetChanged();
            }
        });
        realSwitch.setChecked(false);

    }
    public void onBackButtonClicked(View view){
        finish();
    }
    private class TotalReviewAdapter extends BaseAdapter{
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

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TotalReviewItemView view = (TotalReviewItemView)convertView;

            if(convertView == null)
                view = new TotalReviewItemView(getApplicationContext());
            ReviewVO item = items.get(position);
            view.setId(item.getUser_id());
            view.setDate(item.getDate());
            view.setRatingBar(item.getRating_star());
            view.setRealReview(item.isAuthentication());
            view.setReview(item.getReview_text());
            int num=((TextView)view.findViewById(R.id.review)).getLineCount();
            Log.i("LineNum(BB)",String.valueOf(num));

            //TextView textView = (TextView)findViewById(R.id.review);

            /*if (ViewCompat.isLaidOut(view)) {
                Log.d("TEXTVIEW", "line count : " + textView.getLineCount());
            } else {
                final TextView postTextView = view.getReview();
                final TextView moreView = view.getMoreView();
                postTextView.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("TEXTVIEW", "line count : " + postTextView.getLineCount());
                    }
                });
            }*/


            return view;
        }
        void addItem(ReviewVO item){
            items.add(item);
        }
        void addItems(List<ReviewVO> items){
            this.items = items;
        }
    }
}
