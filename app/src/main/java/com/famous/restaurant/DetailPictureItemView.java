package com.famous.restaurant;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

/**
 * Created by BOBAE on 2017-12-08.
 */

public class DetailPictureItemView extends LinearLayout{
    ImageView imageView;
    public DetailPictureItemView(Context context) {
        super(context);
        init(context);
    }
    public DetailPictureItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.detail_picture_item, this, true);

        imageView = (ImageView)findViewById(R.id.image);

    }
    /*이미지 set*/
    void setImage(String url) {
        Glide.with(getContext()).load(url).into(imageView);
    }
}
