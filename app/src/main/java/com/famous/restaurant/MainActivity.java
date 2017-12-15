package com.famous.restaurant;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

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

    DatabaseReference restaurantDatabase;
    DatabaseReference reviewDatabase;
    DatabaseReference authDatabase;

    ProgressDialog progressDialog;

    List<RestaurantVO> restaurantList=new ArrayList<>();
    List<RestaurantVO> filteredList=new ArrayList<>();
    List<RestaurantVO> searchList=new ArrayList<>();
    List<RestaurantItem> itemList=new ArrayList<>();

    List<ReviewVO> reviewList=new ArrayList<>();
    List<AuthenticationVO> authenticationList=new ArrayList<>();

    RestaurantAdapter adapter=new RestaurantAdapter();
    ListView list_store;
    TextView tv_category;
    EditText et_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Handler handler= new Handler();
        final InputMethodManager imm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        final FrameLayout fl_category=(FrameLayout)findViewById(R.id.fl_category);
        final ImageButton ib_myPage=(ImageButton)findViewById(R.id.ib_myPage);
        final ScrollView sv_main=(ScrollView)findViewById(R.id.sv_main);
        final ImageButton ib_search=(ImageButton)findViewById(R.id.ib_search);
        final TextView tv_title=(TextView)findViewById(R.id.tv_title);
        final String userName;
        list_store=(ListView)findViewById(R.id.lv_store);
        tv_category=(TextView)findViewById(R.id.tv_category);
        et_search=(EditText)findViewById(R.id.et_search);

        restaurantDatabase = FirebaseDatabase.getInstance().getReference("restaurants");
        reviewDatabase = FirebaseDatabase.getInstance().getReference("reviews");
        authDatabase = FirebaseDatabase.getInstance().getReference("authentication");

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("데이터 불러오는 중...");
        progressDialog.show();

        userName=SaveSharedPreference.getUserName(MainActivity.this);
        tv_title.setText(userName+" 님의 맛집보장");

        restaurantDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    RestaurantVO restaurant = postSnapshot.getValue(RestaurantVO.class);
                    restaurantList.add(restaurant);
                    filteredList.add(restaurant);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });

        reviewDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    ReviewVO review = postSnapshot.getValue(ReviewVO.class);
                    reviewList.add(review);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });

        authDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    AuthenticationVO authentication= postSnapshot.getValue(AuthenticationVO.class);
                    authenticationList.add(authentication);
                }
                setItemList(filteredList);
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

        et_search.setOnKeyListener(new View.OnKeyListener() {
           @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
               if(keyCode==event.KEYCODE_ENTER)
                   return true;
               return false;
           }
        });

        ib_search.setOnTouchListener(new View.OnTouchListener() {
           @Override
            public boolean onTouch(View v, MotionEvent ev) {
               switch(ev.getAction()) {
                   case MotionEvent.ACTION_DOWN:
                       ib_search.setAlpha((float)0.1);
                       break;
                   case MotionEvent.ACTION_UP:
                       ib_search.setAlpha((float)1.0);

                       searchList.clear();
                       adapter.clearItem();
                       itemList.clear();

                       String searchWord=et_search.getText().toString();
                       for(int i=0; i<filteredList.size(); i++) {
                           if(filteredList.get(i).getName().indexOf(searchWord)>=0)
                               searchList.add(filteredList.get(i));
                       }
                       setItemList(searchList);
                       for(RestaurantItem rItem : itemList) {
                           adapter.addItem(rItem);
                       }
                       adapter.notifyDataSetChanged();
                       list_store.setAdapter(adapter);
                       imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
                       break;
               }
               return true;
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
                        imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
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
                Intent intent=new Intent(getApplicationContext(), DetailActivity.class);
                RestaurantItem item=(RestaurantItem)adapter.getItem(position);
                RestaurantVO sRestaurantVO=null;
                for(int i=0; i<filteredList.size(); i++) {
                    if(item.getStoreName().equals(filteredList.get(i).getName()))
                        sRestaurantVO=filteredList.get(i);
                }
                intent.putExtra("SELECTED_ITEM", sRestaurantVO.getName());
                startActivity(intent);
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


    private void setItemList(List<RestaurantVO> tempList) {
        String sName;
        String pNumber;
        int authCnt;
        int rCnt;
        float starAvg;
        String prevURL;

        for(RestaurantVO rVO : tempList) {
            rCnt=0;
            starAvg=0;
            authCnt=0;
            for(ReviewVO review : reviewList) {
                if(rVO.getName().equals(review.getRestaurant())) {
                    rCnt++;
                    starAvg=review.getRating_star();
                }
            }

            for(AuthenticationVO auth : authenticationList) {
                if(rVO.getName().equals(auth.getRestaurant())) {
                    authCnt++;
                }
            }
            if(rCnt!=0)
                starAvg/=rCnt;
            sName=rVO.getName();
            pNumber=rVO.getPhone();
            StringTokenizer setImgURL=new StringTokenizer(rVO.getImageURL(), "$");
            prevURL=setImgURL.nextToken();

            itemList.add(new RestaurantItem(sName, pNumber, authCnt, starAvg, prevURL));
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

        setItemList(filteredList);
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
