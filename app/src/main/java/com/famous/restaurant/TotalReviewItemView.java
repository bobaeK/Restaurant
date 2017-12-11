package com.famous.restaurant;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by BOBAE on 2017-12-08.
 */

public class TotalReviewItemView extends LinearLayout{
    private TextView id;
    private TextView date;
    private TextView realReview;
    private RatingBar ratingBar;
    private TextView review;

    public TotalReviewItemView(Context context) {
        super(context);
        init(context);
    }

    public TotalReviewItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.total_review_item, this, true);

        id = (TextView)findViewById(R.id.id);
        date = (TextView)findViewById(R.id.date);
        realReview = (TextView)findViewById(R.id.real_review);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        review = (TextView)findViewById(R.id.review);


    }
    public void setId(String id){ this.id.setText(id); }
    public void setDate(String date){ this.date.setText(date); }
    public void setRealReview(boolean realReview){
        if(realReview){
            this.realReview.setVisibility(VISIBLE);
        }else{
            this.realReview.setVisibility(INVISIBLE);
        }
    }
    public void setRatingBar(float rating){ this.ratingBar.setRating(rating); }
    public void setReview(String review){
        this.review.setText(review);
    }
}