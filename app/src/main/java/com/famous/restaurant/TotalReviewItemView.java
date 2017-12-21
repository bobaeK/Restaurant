package com.famous.restaurant;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static com.famous.restaurant.R.id.more_view;

/**
 * Created by BOBAE on 2017-12-08.
 */

public class TotalReviewItemView extends LinearLayout{
    private TextView id;
    private TextView date;
    private TextView realReview;
    private RatingBar ratingBar;
    private TextView review;
    private TextView moreView;
    private ArrayList<ImageView> images;
    private int lineCount;
    Context context;
    private TextView less_view;

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
        this.context = context;
        id = (TextView)findViewById(R.id.id);
        date = (TextView)findViewById(R.id.date);
        realReview = (TextView)findViewById(R.id.real_review);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        review = (TextView)findViewById(R.id.review);
        moreView = (TextView)findViewById(R.id.more_view);
        images = new ArrayList<ImageView>();
        images.add((ImageView)findViewById(R.id.image1));
        images.add((ImageView)findViewById(R.id.image2));
        images.add((ImageView)findViewById(R.id.image3));
        images.add((ImageView)findViewById(R.id.image4));

        moreView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                moreView.setVisibility(TextView.GONE);
                review.setLines(lineCount);
                less_view.setVisibility(TextView.VISIBLE);
            }
        });

        less_view = (TextView)findViewById(R.id.less_view);
        less_view.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                less_view.setVisibility(TextView.GONE);
                moreView.setVisibility(TextView.VISIBLE);
                review.setLines(3);
            }
        });


    }
    public TextView getMoreView(){
        return moreView;
    }
    public TextView getReview() {
        return review;
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
    public void setReview(String review_){
        this.review.setText(review_);
        review.post(new Runnable() {
            @Override
            public void run() {
                lineCount = review.getLineCount();
                Log.i("LineNum(BB)",String.valueOf(lineCount));
                if(lineCount > 3) {
                    moreView.setVisibility(TextView.VISIBLE);
                    review.setLines(3);
                }

            }
        });

    }
    public void setImages(final ArrayList<String> url){
        for(int i = 0; i < url.size(); ++i){
            Log.i("image_url",url.get(i));
            ImageView imageView = images.get(i);
            imageView.setVisibility(VISIBLE);
            Glide.with(context).load(url.get(i)).into(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ImageActivity.class);
                    intent.putStringArrayListExtra("images", url);
                    context.startActivity(intent);
                }
            });
            images.set(i, imageView);
        }
    }
}