package com.famous.restaurant;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView list_store=(ListView)findViewById(R.id.list_store);

        final RestaurantAdapter adapter=new RestaurantAdapter();

        adapter.addItem(new RestaurantItem("샤론스톤", "010-0000-0000", 1, (float)3.5, R.drawable.charlostone));
        list_store.setAdapter(adapter);

        list_store.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                RestaurantItem item=(RestaurantItem)adapter.getItem(position);
                Toast.makeText(getApplicationContext(), "선택 : "+item.getStoreName(), Toast.LENGTH_LONG).show();
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
