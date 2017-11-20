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
    ImageView img_storePreview;

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

        txt_storeName=(TextView)findViewById(R.id.txt_storeName);
        txt_phoneNumber=(TextView)findViewById(R.id.txt_phoneNumber);
        txt_realReviewCnt=(TextView)findViewById(R.id.txt_realReviewCnt);
        rb_starScore=(RatingBar)findViewById(R.id.rb_starScore);
        img_storePreview=(ImageView)findViewById(R.id.img_storePreview);
    }

    public void setStoreName(String storeName) {
        txt_storeName.setText(storeName);
    }

    public void setPhoneNumber(String phoneNumber) {
        txt_phoneNumber.setText("연락처 : "+phoneNumber);
    }

    public void setRealReviewCnt(int realReviewCnt) {
        txt_realReviewCnt.setText("리얼후기 : "+realReviewCnt+" 개");
    }

    public void setStarScore(float starScore) {
        rb_starScore.setRating(starScore);
    }

    public void setResId(int resId) {
       img_storePreview.setImageResource(resId);
    }
}
