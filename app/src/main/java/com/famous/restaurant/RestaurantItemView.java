package com.famous.restaurant;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by Jin on 2017-11-20.
 */

public class RestaurantItemView extends LinearLayout {
    TextView tv_storeName;
    TextView tv_phoneNumber;
    TextView tv_realReviewCnt;
    RatingBar rb_starScore;
    ImageView iv_storePreview;

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

        tv_storeName=(TextView)findViewById(R.id.tv_storeName);
        tv_phoneNumber=(TextView)findViewById(R.id.tv_phoneNumber);
        tv_realReviewCnt=(TextView)findViewById(R.id.tv_realReviewCnt);
        rb_starScore=(RatingBar)findViewById(R.id.rb_starScore);
        iv_storePreview=(ImageView)findViewById(R.id.iv_storePreview);
    }

    public void setStoreName(String storeName) {
        tv_storeName.setText(storeName);
    }

    public void setPhoneNumber(String phoneNumber) {
        tv_phoneNumber.setText("연락처 : "+phoneNumber);
    }

    public void setRealReviewCnt(int realReviewCnt) {
        tv_realReviewCnt.setText("리얼후기 : "+realReviewCnt+" 개");
    }

    public void setStarScore(float starScore) {
        rb_starScore.setRating(starScore);
    }

    public void setImageURL(Context context, String imageURL) {
        Glide.with(context).load(imageURL).into(iv_storePreview);
    }
}
