package com.famous.restaurant;

import java.util.ArrayList;

/**
 * Created by BOBAE on 2017-12-08.
 */

public class TotalReviewItem {
    String reviewId;
    String id;
    String date;
    boolean realReview;
    float ratingBar;
    String review;
    ArrayList<String> imageUrl;

    public TotalReviewItem(String reviewId, String id, String date,
                           boolean realReview,
                           float ratingBar,
                           String review,
                           ArrayList<String> imageUrl) {
        this.reviewId = reviewId;
        this.id = id;
        this.date = date;
        this.realReview = realReview;
        this.ratingBar = ratingBar;
        this.review = review;
        this.imageUrl = imageUrl;
    }

    public String getReviewId() {
        return reviewId;
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

    public ArrayList<String> getImageUrl() {
        return imageUrl;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
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

    public void setImageUrl(ArrayList<String> imageUrl) {
        this.imageUrl = imageUrl;
    }
}