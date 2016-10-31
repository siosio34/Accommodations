package com.youngje.tgwing.accommodations.Activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.youngje.tgwing.accommodations.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SEGU on 2016-10-31.
 */

public class MyReviewAdapter extends BaseAdapter {
    private Context context;
    private int layoutResourceId;
    private List<MyReviewItem> myReviewItemList;

    public MyReviewAdapter(Context context, int resource, ArrayList list) {
        this.context = context;
        this.layoutResourceId = resource;
        this.myReviewItemList = new ArrayList<MyReviewItem>();
    }
    public MyReviewAdapter(){

    }
    @Override
    public int getCount() {
        return myReviewItemList.size();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        View tempView = convertView;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            tempView = inflater.inflate(R.layout.gridview_item, parent, false);
            holder = new ViewHolder(tempView);
            tempView.setTag(holder);
        } else {
            holder = (ViewHolder) tempView.getTag();
        }

        holder.reviewTitleTextView.setText(getItem(position).getTitleStr());
        holder.reviewDateView.setText(getItem(position).getReviewDateStr());
        holder.reviewContentView.setText(getItem(position).getReviewContentStr());
        holder.numRatingStarView.setText(new Float(getItem(position).getNumOfStar()).toString());
        holder.reviewImageView.setImageBitmap(getItem(position).getReviewImage());

        holder.reviewRatingBar.setOnRatingBarChangeListener(onRatingChangedListener(holder, position));
        holder.reviewRatingBar.setTag(position);
        holder.reviewRatingBar.setRating(getItem(position).getRatingStar());

        return tempView;
    }

    @Override
    public MyReviewItem getItem(int position) {
        return myReviewItemList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    private RatingBar.OnRatingBarChangeListener onRatingChangedListener(final ViewHolder holder, final int position) {
        return new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                MyReviewItem item = getItem(position);
                item.setRatingStar(v);
            }
        };
    }
    public void addItem(String titleStr, String reviewDateStr, String reviewContentStr, float numOfStar, Bitmap reviewImage){
        MyReviewItem temp = new MyReviewItem(titleStr, numOfStar, reviewDateStr, reviewContentStr, numOfStar, reviewImage);
        myReviewItemList.add(temp);
    }
    private class ViewHolder {

        private RatingBar reviewRatingBar;
        private TextView reviewTitleTextView;
        private TextView reviewDateView;
        private TextView reviewContentView;
        private TextView numRatingStarView;
        private ImageView reviewImageView;


        public ViewHolder(View view) {
            reviewRatingBar = (RatingBar) view.findViewById(R.id.review_ratingbar);
            reviewTitleTextView = (TextView) view.findViewById(R.id.review_title);
            reviewDateView = (TextView) view.findViewById(R.id.review_date);
            reviewContentView = (TextView) view.findViewById(R.id.review_content);
            numRatingStarView = (TextView) view.findViewById(R.id.review_rating_score);
            reviewImageView = (ImageView) view.findViewById(R.id.review_image);

        }
    }
}


