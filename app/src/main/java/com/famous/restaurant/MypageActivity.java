package com.famous.restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.R.layout.simple_list_item_1;

public class MypageActivity extends AppCompatActivity {
    final int MAX_LIST_NUM=6;
    String[] certifiedStoreList=new String[MAX_LIST_NUM];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        final RegisteredReviewAdapter ReviewAdapter=new RegisteredReviewAdapter();
        final ImageButton ib_back=(ImageButton)findViewById(R.id.ib_back);
        final ListView lv_reviewList=(ListView)findViewById(R.id.lv_reviewList);
        final ListView lv_certifiedList=(ListView)findViewById(R.id.lv_certifiedList);
        final ScrollView sv_myPage=(ScrollView)findViewById(R.id.sv_myPage);
        final ArrayAdapter<String> certifiedAdapter = new ArrayAdapter(this, simple_list_item_1, certifiedStoreList);


        ib_back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent ev) {
                switch(ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ib_back.setAlpha((float)0.1);
                        break;
                    case MotionEvent.ACTION_UP:
                        ib_back.setAlpha((float)1.0);
                        Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        break;
                }
                return true;
            }
        }) ;
        certifiedStoreList[0]="마루스시 "+"2017/11/02";
        certifiedStoreList[1]="마루스시 "+"2017/11/02";
        certifiedStoreList[2]="마루스시 "+"2017/11/02";
        certifiedStoreList[3]="마루스시 "+"2017/11/02";
        certifiedStoreList[4]="마루스시 "+"2017/11/02";
        certifiedStoreList[5]="마루스시 "+"2017/11/02";
        lv_certifiedList.setAdapter(certifiedAdapter);

        ReviewAdapter.addItem(new RegisteredReviewItem("마루스시 "+"2017/11/05 "+"사장님도 친절하시고 가게 분위기가 너무 좋았어요!"));
        ReviewAdapter.addItem(new RegisteredReviewItem("마루스시 "+"2017/11/05 "+"사장님도 친절하시고 가게 분위기가 너무 좋았어요!"));
        ReviewAdapter.addItem(new RegisteredReviewItem("마루스시 "+"2017/11/05 "+"사장님도 친절하시고 가게 분위기가 너무 좋았어요!"));
        ReviewAdapter.addItem(new RegisteredReviewItem("마루스시 "+"2017/11/05 "+"사장님도 친절하시고 가게 분위기가 너무 좋았어요!"));
        ReviewAdapter.addItem(new RegisteredReviewItem("마루스시 "+"2017/11/05 "+"사장님도 친절하시고 가게 분위기가 너무 좋았어요!"));
        ReviewAdapter.addItem(new RegisteredReviewItem("마루스시 "+"2017/11/05 "+"사장님도 친절하시고 가게 분위기가 너무 좋았어요!"));
        lv_reviewList.setAdapter(ReviewAdapter);

        lv_reviewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                RegisteredReviewItem item=(RegisteredReviewItem)ReviewAdapter.getItem(position);
                Toast.makeText(getApplicationContext(), "선택 : "+item.getRegisteredReview(), Toast.LENGTH_LONG).show();
            }
        });

        lv_reviewList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                sv_myPage.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        lv_certifiedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item=certifiedAdapter.getItem(position);
                Toast.makeText(getApplicationContext(), "선택 : "+item, Toast.LENGTH_LONG).show();
            }
        });

        lv_certifiedList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                sv_myPage.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

    }

    private class RegisteredReviewAdapter extends BaseAdapter {
        private ArrayList<RegisteredReviewItem> items=new ArrayList<RegisteredReviewItem>();

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
            RegisteredReviewItemView view =(RegisteredReviewItemView) convertView;

            if(convertView==null)
                view=new RegisteredReviewItemView(getApplicationContext());

            RegisteredReviewItem item=items.get(position);
            view.setRegisteredReview(item.getRegisteredReview());

            return view;
        }

        void addItem(RegisteredReviewItem item) { items.add(item); }
    }

}
