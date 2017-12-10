package com.famous.restaurant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    Integer[] foodImageIds={ 0, R.drawable.category, R.drawable.category0, R.drawable.category1, R.drawable.category2,
            R.drawable.category3, R.drawable.category4, R.drawable.category5 , R.drawable.category, 0};

    DatabaseReference mDatabase;
    ProgressDialog progressDialog;

    List<RestaurantVO> restaurantList=new ArrayList<>();
    List<RestaurantVO> filteredList=new ArrayList<>();
    List<RestaurantItem> itemList=new ArrayList<>();

    RestaurantAdapter adapter=new RestaurantAdapter();
    ListView list_store;
    TextView tv_category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Handler handler= new Handler();
        final FrameLayout fl_category=(FrameLayout)findViewById(R.id.fl_category);
        final ImageButton ib_myPage=(ImageButton)findViewById(R.id.ib_myPage);
        final ScrollView sv_main=(ScrollView)findViewById(R.id.sv_main);
        list_store=(ListView)findViewById(R.id.lv_store);
        tv_category=(TextView)findViewById(R.id.tv_category);

        mDatabase = FirebaseDatabase.getInstance().getReference("restaurants");
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("데이터 불러오는 중...");
        progressDialog.show();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    RestaurantVO restaurant = postSnapshot.getValue(RestaurantVO.class);
                    restaurantList.add(restaurant);
                    filteredList.add(restaurant);
                }
                setItemList();
                for(RestaurantItem rItem : itemList) {
                    adapter.addItem(rItem);
                }
                list_store.setAdapter(adapter);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });

        ib_myPage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent ev) {
                switch(ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ib_myPage.setAlpha((float)0.1);
                        break;
                    case MotionEvent.ACTION_UP:
                        ib_myPage.setAlpha((float)1.0);
                        Intent intent=new Intent(getApplicationContext(), MypageActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        }) ;

        fl_category.setOnTouchListener(new View.OnTouchListener() {
            Animation categoryLeftAnim;
            Animation categoryCenterAnim;
            Animation categoryRightAnim;

            ImageView iv_back=(ImageView)findViewById(R.id.iv_back);
            ImageView iv_current=(ImageView)findViewById(R.id.iv_current);
            ImageView iv_next=(ImageView)findViewById(R.id.iv_next);

            float initX;
            int imageIdx=1;
            int backImageId=foodImageIds[0];
            int currentImageId=foodImageIds[1];
            int nextImageId=foodImageIds[2];

            @Override
            public boolean onTouch(View v, MotionEvent ev){
                switch(ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        sv_main.requestDisallowInterceptTouchEvent(true);
                        initX=ev.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        if(initX-ev.getX()>50) {
                            categoryLeftAnim=AnimationUtils.loadAnimation(
                                    getApplicationContext(), R.anim.category_left);
                            categoryCenterAnim=AnimationUtils.loadAnimation(
                                    getApplicationContext(), R.anim.category_current_back);
                            categoryRightAnim=AnimationUtils.loadAnimation(
                                    getApplicationContext(), R.anim.category_next_current);
                            iv_back.setAlpha((float)1.0);
                            iv_next.setAlpha((float)1.0);
                            iv_back.startAnimation(categoryLeftAnim);
                            iv_current.startAnimation(categoryCenterAnim);
                            iv_next.startAnimation(categoryRightAnim);

                            imageIdx++;
                            if(imageIdx>8)
                                imageIdx--;

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    backImageId=foodImageIds[imageIdx-1];
                                    currentImageId=foodImageIds[imageIdx];
                                    nextImageId=foodImageIds[imageIdx+1];

                                    iv_back.setImageResource(backImageId);
                                    iv_back.invalidate();
                                    iv_current.setImageResource(currentImageId);
                                    iv_current.invalidate();
                                    iv_next.setImageResource(nextImageId);
                                    iv_next.invalidate();

                                    checkCurrentImageId(currentImageId);
                                }
                            }, 299);


                        } else if(ev.getX()-initX>50) {
                            categoryLeftAnim=AnimationUtils.loadAnimation(
                                    getApplicationContext(), R.anim.category_back_current);
                            categoryCenterAnim=AnimationUtils.loadAnimation(
                                    getApplicationContext(), R.anim.category_current_next);
                            categoryRightAnim=AnimationUtils.loadAnimation(
                                    getApplicationContext(), R.anim.category_right);
                            iv_back.setAlpha((float)1.0);
                            iv_next.setAlpha((float)1.0);
                            iv_back.startAnimation(categoryLeftAnim);
                            iv_current.startAnimation(categoryCenterAnim);
                            iv_next.startAnimation(categoryRightAnim);

                            imageIdx--;
                            if(imageIdx<1)
                                imageIdx++;

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    nextImageId=foodImageIds[imageIdx+1];
                                    currentImageId=foodImageIds[imageIdx];
                                    backImageId=foodImageIds[imageIdx-1];

                                    iv_back.setImageResource(backImageId);
                                    iv_back.invalidate();
                                    iv_current.setImageResource(currentImageId);
                                    iv_current.invalidate();
                                    iv_next.setImageResource(nextImageId);
                                    iv_next.invalidate();

                                    checkCurrentImageId(currentImageId);
                                }
                            }, 299);

                        }
                        break;
                }
                iv_back.setAlpha((float)0.2);
                iv_next.setAlpha((float)0.2);
                return true;
            }
        });

        list_store.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                RestaurantItem item=(RestaurantItem)adapter.getItem(position);
                Toast.makeText(getApplicationContext(), "선택 : "+item.getStoreName(), Toast.LENGTH_LONG).show();
            }
        });

        list_store.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                sv_main.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

    private class RestaurantAdapter extends BaseAdapter {
        private ArrayList<RestaurantItem> items=new ArrayList<RestaurantItem>();

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
            RestaurantItemView view =(RestaurantItemView) convertView;

            if(convertView==null)
                view=new RestaurantItemView(getApplicationContext());

            RestaurantItem item=items.get(position);
            view.setStoreName(item.getStoreName());
            view.setPhoneNumber(item.getPhoneNumber());
            view.setRealReviewCnt(item.getRealReviewCnt());
            view.setStarScore(item.getStarScore());
            view.setImageURL(getApplicationContext(), item.getImageURL());

            return view;
        }

        void addItem(RestaurantItem item) { items.add(item); }

        void clearItem() { items.clear(); }
    }


    private void setItemList() {
        String sName;
        String pNumber;
        int rCnt=0;
        float sScore=0;
        String prevURL;

        for(RestaurantVO rVO : filteredList) {
            sName=rVO.getName();
            pNumber=rVO.getPhone();
            StringTokenizer setImgURL=new StringTokenizer(rVO.getImageURL(), "$");
            prevURL=setImgURL.nextToken();

            itemList.add(new RestaurantItem(sName, pNumber, rCnt, sScore, prevURL));
        }

    }

    private void filterList(String category) {
        filteredList.clear();
        adapter.clearItem();
        itemList.clear();

        for(int i=0; i<restaurantList.size(); i++) {
            if(category.equals("total")) {
                filteredList.add(restaurantList.get(i));
            }
            else if (restaurantList.get(i).getCategory().equals(category)) {
                filteredList.add(restaurantList.get(i));
            }
        }

        setItemList();
        for(RestaurantItem rItem : itemList) {
            adapter.addItem(rItem);
        }
        adapter.notifyDataSetChanged();
        list_store.setAdapter(adapter);
    }

    public void checkCurrentImageId(int currentImageId) {
        switch(currentImageId) {
            case R.drawable.category:
                filterList("total");
                tv_category.setText("맛집보장");
                break;
            case R.drawable.category0:
                filterList("korea");
                tv_category.setText("한식");
                break;
            case R.drawable.category1:
                filterList("japan");
                tv_category.setText("일식");
                break;
            case R.drawable.category2:
                filterList("china");
                tv_category.setText("중식");
                break;
            case R.drawable.category3:
                filterList("western");
                tv_category.setText("양식");
                break;
            case R.drawable.category4:
                filterList("chicken");
                tv_category.setText("치킨");
                break;
            case R.drawable.category5:
                filterList("school");
                tv_category.setText("분식");
                break;
        }
    }
}
