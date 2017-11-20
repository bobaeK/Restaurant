package com.famous.restaurant;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by Jin on 2017-11-20.
 */

public class RestaurantItemView extends LinearLayout {
    TextView txt_storeName;
    TextView txt_phoneNumber;
    TextView txt_realReviewCnt;
    RatingBar rb_starScore;
    ImageView img_storeSample;

    public RestaurantItemView(Context context) {
        super(context);
        init(context);
    }

    public RestaurantItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.restaurant_item, this, true);
    }

    public void setStoreName(String storeName) {
        txt_storeName.setText(storeName);
    }

    public void setPhoneNumber(String phoneNumber) {
        txt_phoneNumber.setText("연락처 : "+phoneNumber);
    }

    public void setRealReviewCnt(int realReviewCnt) {
        txt_realReviewCnt.setText("리얼후기 : "+realReviewCnt);
    }

    public void setStarScore(int starScore) {
        rb_starScore.setRating(starScore);
    }

    public void setResId(int resId) {
       img_storeSample.setImageResource(resId);
    }
}
