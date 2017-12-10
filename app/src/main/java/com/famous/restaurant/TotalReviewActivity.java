package com.famous.restaurant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

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

        listView.setAdapter(adapter);

        //리얼후기 보기 스위치

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

            return view;
        }
        void addItem(TotalReviewItem item){ items.add(item); }
    }
}
