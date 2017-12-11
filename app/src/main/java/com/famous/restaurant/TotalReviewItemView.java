package com.famous.restaurant;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by BOBAE on 2017-12-08.
 */

public class TotalReviewItemView extends LinearLayout{
    private TextView id;
    private TextView date;
    private TextView realReview;
    private RatingBar ratingBar;
    private TextView review;
    private ArrayList<ImageView> images;
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
        images = new ArrayList<ImageView>();
        images.add((ImageView)findViewById(R.id.image1));
        images.add((ImageView)findViewById(R.id.image2));
        images.add((ImageView)findViewById(R.id.image3));
        images.add((ImageView)findViewById(R.id.image4));


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
    public void setImages(ArrayList<String> url){
        for(int i = 0; i < url.size(); ++i){
            ImageView imageView = images.get(i);
            imageView.setVisibility(VISIBLE);
            Glide.with(getContext()).load(url.get(i)).into(imageView);
            images.set(i, imageView);
        }
    }
}