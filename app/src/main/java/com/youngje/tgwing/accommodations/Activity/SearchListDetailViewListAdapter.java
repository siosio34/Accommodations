package com.youngje.tgwing.accommodations.Activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;
import com.youngje.tgwing.accommodations.Data.DataFormat;
import com.youngje.tgwing.accommodations.Data.NavigationDataProcessor;
import com.youngje.tgwing.accommodations.Marker;
import com.youngje.tgwing.accommodations.Navi;
import com.youngje.tgwing.accommodations.NaviXY;
import com.youngje.tgwing.accommodations.R;
import com.youngje.tgwing.accommodations.Review;
import com.youngje.tgwing.accommodations.Util.HttpHandler;
import com.youngje.tgwing.accommodations.Util.LocationUtil;

import org.json.JSONException;
import org.json.JSONObject;

import static com.youngje.tgwing.accommodations.Marker.markerList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by aswoo on 2016-10-15.
 */

public class SearchListDetailViewListAdapter extends BaseAdapter {

    private ArrayList<Review> reviews = new ArrayList<Review>();
    Marker selectMarker;

    public SearchListDetailViewListAdapter() {
        selectMarker = Marker.getSelectedMarker();

    }

    @Override
    public int getCount() {
        return reviews.size();
    }

    @Override
    public Object getItem(int i) {
        return reviews.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final int pos = position;
        final Context context = viewGroup.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listview_detail_item, viewGroup, false);
        }

        ImageView profileView;
        TextView userName;
        ImageView nationalityView;
        ImageView reviewPictureView;
        VideoView reviewVideoView;
        TextView dateView;
        TextView reviewTextView;
        TextView likeNumView;
        RatingBar ratingBar;
        TextView ratingScore;

        profileView = (ImageView) view.findViewById(R.id.listview_detail_item_profile);
        ratingBar = (RatingBar) view.findViewById(R.id.listview_detail_item_ratingbar);
        ratingScore = (TextView) view.findViewById(R.id.listview_detail_item_score);
        userName = (TextView) view.findViewById(R.id.listview_detail_item_username);
        //nationalityView = (ImageView) view.findViewById(R.id.listview_detail_item_nationality);

        reviewPictureView = (ImageView) view.findViewById(R.id.listview_detail_item_img);
        reviewVideoView = (VideoView) view.findViewById(R.id.listview_dettail_item_video);

        dateView = (TextView) view.findViewById(R.id.listview_detail_item_date);
        reviewTextView = (TextView) view.findViewById(R.id.listview_detail_item_review);
        likeNumView = (TextView) view.findViewById(R.id.listview_detail_item_liked);

        Review item = reviews.get(pos);

        switch (item.getContentType()) {
            case 0:
                reviewPictureView.setVisibility(View.GONE);
                reviewVideoView.setVisibility(View.GONE);
                break;
            case 1:
                Picasso.with(view.getContext()).load(item.getreviewContentUrl()).into(reviewPictureView);
                reviewPictureView.setVisibility(View.VISIBLE);
                reviewVideoView.setVisibility(View.GONE);
                break;
            case 2:
                reviewVideoView.setVideoURI(Uri.parse(item.getreviewContentUrl()));
                reviewPictureView.setVisibility(View.GONE);
                reviewVideoView.setVisibility(View.VISIBLE);
                break;
        }

        userName.setText(item.getUserName());
        Picasso.with(context).load(item.getUserImageUrl()).into(profileView);
        //dateView.setText(item.getCreateDate().toString());
        reviewTextView.setText(item.getContent());

        return view;
    }


    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(Review newReview) {
        reviews.add(newReview);
    }


}
