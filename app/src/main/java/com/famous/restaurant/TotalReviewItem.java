package com.famous.restaurant;

/**
 * Created by BOBAE on 2017-12-08.
 */

public class TotalReviewItem {
    String id;
    String date;
    boolean realReview;
    float ratingBar;
    String review;

    public TotalReviewItem(String id, String date,
                           boolean realReview,
                           float ratingBar,
                           String review) {
        this.id = id;
        this.date = date;
        this.realReview = realReview;
        this.ratingBar = ratingBar;
        this.review = review;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public boolean isRealReview() {
        return realReview;
    }

    public float getRatingBar() {
        return ratingBar;
    }

    public String getReview() {
        return review;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setRealReview(boolean realReview) {
        this.realReview = realReview;
    }

    public void setRatingBar(float ratingBar) {
        this.ratingBar = ratingBar;
    }

    public void setReview(String review) {
        this.review = review;
    }
}