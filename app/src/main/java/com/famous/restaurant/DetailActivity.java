package com.famous.restaurant;

import android.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

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

    }
    public void onTotalReviewClicked(View view){

    }
}
