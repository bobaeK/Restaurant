package com.famous.restaurant;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BOBAE on 2017-12-16.
 */

public class TotalReviewAdapter extends BaseAdapter {
    private List<ReviewVO> items = new ArrayList<ReviewVO>();
    Context context;
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public TotalReviewAdapter(){

    }
    public TotalReviewAdapter(Context context){
        this.context = context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TotalReviewItemView view = (TotalReviewItemView)convertView;

        if(convertView == null)
            view = new TotalReviewItemView(context.getApplicationContext());
        ReviewVO item = items.get(position);
        view.setId(item.getUser_id());
        view.setDate(item.getDate());
        view.setRatingBar(item.getRating_star());
        view.setRealReview(item.isAuthentication());
        view.setReview(item.getReview_text());
        int num=((TextView)view.findViewById(R.id.review)).getLineCount();
        Log.i("LineNum(BB)",String.valueOf(num));

        //TextView textView = (TextView)findViewById(R.id.review);

            /*if (ViewCompat.isLaidOut(view)) {
                Log.d("TEXTVIEW", "line count : " + textView.getLineCount());
            } else {
                final TextView postTextView = view.getReview();
                final TextView moreView = view.getMoreView();
                postTextView.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("TEXTVIEW", "line count : " + postTextView.getLineCount());
                    }
                });
            }*/


        return view;
    }
    public void addItem(ReviewVO item){
        items.add(item);
    }
    public void addItems(List<ReviewVO> items){
        this.items = items;
    }
    public List<ReviewVO> getItems(){
        return this.items;
    }
}
