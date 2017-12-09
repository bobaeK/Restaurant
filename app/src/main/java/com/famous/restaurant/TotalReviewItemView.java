package com.famous.restaurant;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by BOBAE on 2017-12-08.
 */

public class TotalReviewItemView extends LinearLayout{
    TextView id;
    TextView date;
    TextView realReview;
    TextView rating;
    RatingBar ratingBar;
    TextView review;

    public TotalReviewItemView(Context context) {
        super(context);
    }

    public TotalReviewItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_total_review, this, true);

        id = (TextView)findViewById(R.id.id);
        date = (TextView)findViewById(R.id.date);
        realReview = (TextView)findViewById(R.id.real_review);
        rating = (TextView)findViewById(R.id.rating);
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
    public void setReview(String review){ this.review.setText(review); }
}