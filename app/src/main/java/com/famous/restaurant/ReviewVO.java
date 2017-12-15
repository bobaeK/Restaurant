package com.famous.restaurant;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by inyoungW on 2017-12-10.
 */

public class ReviewVO {
    private String user_id;
    private String restaurant;
    private int rating_star;
    private String review_text;
    private String date;
    private boolean authentication;

    private int imageCnt;
    private ArrayList<String> imageUri;

    private String key;

    public ReviewVO(){
        imageUri = new ArrayList<String>();
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public int getRating_star() {
        return rating_star;
    }

    public void setRating_star(int rating_star) {
        this.rating_star = rating_star;
    }

    public String getReview_text() {
        return review_text;
    }

    public void setReview_text(String review_text) {
        this.review_text = review_text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isAuthentication() {
        return authentication;
    }

    public void setAuthentication(boolean authentication) {
        this.authentication = authentication;
    }

    public int getImageCnt() {
        return imageCnt;
    }

    public void setImageCnt(int imageCnt) {
        this.imageCnt = imageCnt;
    }

    public ArrayList<String> getImageUri() {
        return imageUri;
    }

    public void setImageUri(ArrayList<String> imageUri) {
        this.imageUri = imageUri;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "ReviewVO{" +
                "user_id='" + user_id + '\'' +
                ", restaurant='" + restaurant + '\'' +
                ", rating_star=" + rating_star +
                ", review_text='" + review_text + '\'' +
                ", date=" + date +
                ", authentication=" + authentication +
                ", imageCnt=" + imageCnt +
                ", imageUri=" + imageUri +
                ", key='" + key + '\'' +
                '}';
    }
}
