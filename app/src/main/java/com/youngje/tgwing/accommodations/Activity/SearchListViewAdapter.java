package com.youngje.tgwing.accommodations.Activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.youngje.tgwing.accommodations.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aswoo on 2016-10-15.
 */

public class SearchListViewAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private List<SearchListViewItem> listViewItemList = new ArrayList<SearchListViewItem>();

    // ListViewAdapter의 생성자
    public SearchListViewAdapter() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.ratingBar.setOnRatingBarChangeListener(onRatingChangedListener(holder, position));
        holder.ratingBar.setTag(position);
        holder.ratingBar.setRating(getItem(position).getRatingStar());

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득


        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        SearchListViewItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        holder.titleTextView.setText(getItem(position).getTitle());
        holder.categoryView.setText(getItem(position).getCategory());
        holder.ReviewNumView.setText(getItem(position).getReviewNum());
        holder.distanceView.setText(getItem(position).getDistance());
        holder.ratingStarView.setText(new Float(getItem(position).getNumOfStar()).toString());

        return convertView;
    }
    private RatingBar.OnRatingBarChangeListener onRatingChangedListener(final ViewHolder holder, final int position) {
        return new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                SearchListViewItem item = getItem(position);
                item.setRatingStar(v);
                Log.i("Adapter", "star: " + v);
            }
        };
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public SearchListViewItem getItem(int position) {
        return listViewItemList.get(position);
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String distance, String title, String category,float ratingbar, String ReviewNum) {
        SearchListViewItem item = new SearchListViewItem();

        item.setDistance(distance);
        item.setTitle(title);
        item.setCateGory(category);
        item.setReviewNum(ReviewNum);
        item.setRatingStar(ratingbar);
        item.setNumOfStar(ratingbar);
        listViewItemList.add(item);
    }

    private class ViewHolder {

        private TextView distanceView;
        private RatingBar ratingBar;
        private TextView titleTextView;
        private TextView categoryView;
        private TextView ReviewNumView;
        private TextView ratingStarView;


        public ViewHolder(View view) {
            ratingBar = (RatingBar) view.findViewById(R.id.listview_rating_bar);
            titleTextView = (TextView) view.findViewById(R.id.listview_title);
            categoryView = (TextView) view.findViewById(R.id.listview_category);
            ReviewNumView = (TextView) view.findViewById(R.id.listview_rating_num);
            distanceView = (TextView) view.findViewById(R.id.listview_distance);
            ratingStarView = (TextView) view.findViewById(R.id.listview_rating_score);

        }
    }
}
