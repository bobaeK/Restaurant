package com.famous.restaurant;

/**
 * Created by Jin on 2017-11-20.
 */

public class RestaurantItem {
    private String storeName;
    private String phoneNumber;
    private int realReviewCnt;
    private float starScore;
    private String imageURL;

    public RestaurantItem(String storeName, String phoneNumber, int realReviewCnt, float starScore, String imageURL) {
        this.storeName = storeName;
        this.phoneNumber = phoneNumber;
        this.realReviewCnt = realReviewCnt;
        this.starScore = starScore;
        this.imageURL = imageURL;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getRealReviewCnt() {
        return realReviewCnt;
    }

    public void setRealReviewCnt(int realReviewCnt) {
        this.realReviewCnt = realReviewCnt;
    }

    public float getStarScore() {
        return starScore;
    }

    public void setStarScore(float starScore) {
        this.starScore = starScore;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(int resId) {
        this.imageURL = imageURL;
    }
}
