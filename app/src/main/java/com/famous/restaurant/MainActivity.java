package com.famous.restaurant;

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
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Integer[] foodImageIds={ 0, R.drawable.category0, R.drawable.category1, R.drawable.category2,
            R.drawable.category3, R.drawable.category4, R.drawable.category5 ,0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Handler handler= new Handler();

        final RestaurantAdapter adapter=new RestaurantAdapter();
        final FrameLayout layout_category=(FrameLayout)findViewById(R.id.fl_category);
        final ImageButton ib_myPage=(ImageButton)findViewById(R.id.ib_myPage);
        final ListView list_store=(ListView)findViewById(R.id.lv_store);
        final ScrollView sv_main=(ScrollView)findViewById(R.id.sv_main);

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

        layout_category.setOnTouchListener(new View.OnTouchListener() {
            Animation categoryLeftAnim;
            Animation categoryCenterAnim;
            Animation categoryRightAnim;

            ImageView iv_back=(ImageView)findViewById(R.id.iv_back);
            ImageView iv_current=(ImageView)findViewById(R.id.iv_current);
            ImageView iv_next=(ImageView)findViewById(R.id.iv_next);

            float initX;
            int imageIdx=0;
            int backImageId=0;
            int currentImageId=0;
            int nextImageId=foodImageIds[0];

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
                            if(imageIdx>6)
                                imageIdx--;

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    backImageId=foodImageIds[imageIdx-1];
                                    currentImageId=foodImageIds[imageIdx];
                                    nextImageId=foodImageIds[imageIdx+1];

                                    iv_back.setImageResource(backImageId);
                                    iv_current.setImageResource(currentImageId);
                                    iv_next.setImageResource(nextImageId);

                                    iv_back.invalidate();
                                    iv_current.invalidate();
                                    iv_next.invalidate();
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
                                    iv_current.setImageResource(currentImageId);
                                    iv_next.setImageResource(nextImageId);

                                    iv_back.invalidate();
                                    iv_current.invalidate();
                                    iv_next.invalidate();
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

        adapter.addItem(new RestaurantItem("샤론스톤", "010-0000-0000", 1, (float)3.5, R.drawable.charlostone));
        adapter.addItem(new RestaurantItem("샤론스톤", "010-0000-0000", 1, (float)3.5, R.drawable.charlostone));
        adapter.addItem(new RestaurantItem("샤론스톤", "010-0000-0000", 1, (float)3.5, R.drawable.charlostone));
        adapter.addItem(new RestaurantItem("샤론스톤", "010-0000-0000", 1, (float)3.5, R.drawable.charlostone));
        adapter.addItem(new RestaurantItem("샤론스톤", "010-0000-0000", 1, (float)3.5, R.drawable.charlostone));

        list_store.setAdapter(adapter);

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
            view.setResId(item.getResId());

            return view;
        }

        void addItem(RestaurantItem item) { items.add(item); }
    }
}
