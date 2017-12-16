package com.famous.restaurant;

/**
 * Created by Jin on 2017-12-07.
 */

public class RegisteredReviewItem {
    private String registeredReview;
    private String reviewKey;

    public RegisteredReviewItem(String registeredReview, String reviewKey) {
        this.registeredReview = registeredReview;
        this.reviewKey = reviewKey;
    }

    public String getReviewKey() {
        return reviewKey;
    }

    public void setReviewKey(String reviewKey) {
        this.reviewKey = reviewKey;
    }

    public String getRegisteredReview() {
        return registeredReview;
    }

    public void setRegisteredReview(String registeredReview) {
        this.registeredReview = registeredReview;
    }
}
