package com.famous.restaurant;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Jin on 2017-12-07.
 */

public class RegisteredReviewItemView extends LinearLayout {
    TextView tv_registeredReview;

    public RegisteredReviewItemView(Context context) {
        super(context);
        init(context);
    }

    public RegisteredReviewItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.registered_review_item, this, true);

        tv_registeredReview=(TextView)findViewById(R.id.tv_registeredReview);
    }

    public void setRegisteredReview(String registeredReview) {
        tv_registeredReview.setText(registeredReview);
    }

}
