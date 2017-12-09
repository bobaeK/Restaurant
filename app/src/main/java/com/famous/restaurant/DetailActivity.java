package com.famous.restaurant;


import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        //이 조건문 왜 쓰는지 모름,,
        if (savedInstanceState == null) {
            DetailMapFragment detailMapFragment = new DetailMapFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.map_fragment, detailMapFragment, "detail_map")
                    .commit();
        }
        //이미지 ListView


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //지도확대
    public void onMapClicked(){

    }
    //전화걸기
    public void onPhoneCallClicked(View view){
        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:010-1000-1000"));
        startActivity(myIntent);
    }
    //사진확대
    public void onImageClicked(View view){

    }
    //전체후기보기
    public void onTotalReviewClicked(View view){
        Intent intent = new Intent(this, TotalReviewActivity.class);
        startActivity(intent);
    }
    //인증하기
    public void onAddPlaceClicked(View view){

    }
    //후기등록
    public void onAddReviewClicked(View view){

    }
    //뒤로가기
    public void onBackButtonClicked(View view){
        finish();
    }
    //상세보기화면 이미지 리스트뷰 어답터
    private class DetailImagedapter extends BaseAdapter {
        private ArrayList<DetailImageItem> items = new ArrayList<DetailImageItem>();

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
            DetailPictureItemView view = (DetailPictureItemView) convertView;

            if(convertView == null)
                view = new DetailPictureItemView(getApplicationContext());
            DetailImageItem item = items.get(position);
            view.setImage(item.getImage());
            return view;
        }
        void addItem(DetailImageItem item){ items.add(item); }
    }
}
