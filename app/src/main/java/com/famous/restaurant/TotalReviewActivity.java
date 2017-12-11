package com.famous.restaurant;

import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class TotalReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_review);
        //전체 후기 목록 보여주기
        ListView listView = (ListView)findViewById(R.id.total_review_list);
        final TotalReviewAdapter adapter = new TotalReviewAdapter();
        //데이터 가져오기
        adapter.addItem(new TotalReviewItem("1","gudwls12", "2017.09.09", true, (float)4.5, "매우 맛있어서 또 가고싶다", new ArrayList<String>()));
        adapter.addItem(new TotalReviewItem("2","dlsdud2", "2017.09.10", false, (float)5.0, "매우 맛있어서 또 가고싶다", new ArrayList<String>()));
        adapter.addItem(new TotalReviewItem("3","qhqokim", "2017.09.11", false, (float)4.0, "매우 맛있어서 도 가고싶다 " +
                                                                                            "매우 맛있어서 또 가고싶다 " +
                                                                                            "매우 맛있어서 또 가고싶다 " +
                                                                                            "매우 맛있어서 또 가고싶다 " +
                                                                                            "매우 맛있어서 또 가고싶다 " +
                                                                                            "매우 맛있어서 또 가고싶다 " +
                                                                                            "매우 맛있어서 또 가고싶다 " +
                                                                                            "매우 맛있어서 도 가고싶다 " +
                                                                                            "매우 맛있어서 도 가고싶다 " +
                                                                                            "매우 맛있어서 도 가고싶다 " +
                                                                                            "매우 맛있어서 도 가고싶다 " +
                                                                                            "매우 맛있어서 도 가고싶다 " +
                                                                                            "매우 맛있어서 도 가고싶다", new ArrayList<String>()));

        listView.setAdapter(adapter);

        //리얼후기 보기 스위치
        Switch realSwitch = (Switch)findViewById(R.id.real_switch);
        realSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i("MYSWITCH_STATE:", "state : "+isChecked);
                adapter.items.clear();
                if(isChecked){
                    //리얼후기만 보기
                }else{
                    //전체후기 보기
                }
            }
        });
    }
    public void onBackButtonClicked(View view){
        finish();
    }
    private class TotalReviewAdapter extends BaseAdapter{
        private ArrayList<TotalReviewItem> items = new ArrayList<TotalReviewItem>();

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
            TotalReviewItem item = items.get(position);
            view.setId(item.getId());
            view.setDate(item.getDate());
            view.setRatingBar(item.getRatingBar());
            view.setRealReview(item.isRealReview());
            view.setReview(item.getReview());
            int num=((TextView)view.findViewById(R.id.review)).getLineCount();
            Log.i("LineNum(BB)",String.valueOf(num));

            TextView textView = (TextView)findViewById(R.id.review);

            if (ViewCompat.isLaidOut(view)) {
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
            }


            return view;
        }
        void addItem(TotalReviewItem item){ items.add(item); }
    }
}
