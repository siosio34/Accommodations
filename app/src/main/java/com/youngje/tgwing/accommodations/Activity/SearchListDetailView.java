package com.youngje.tgwing.accommodations.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.youngje.tgwing.accommodations.R;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by JiwoonWon on 2016. 10. 22..
 */

public class SearchListDetailView extends AppCompatActivity{

    private final static String TAG = "SearchListDetailView";

    private SearchListReviewViewAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_detail);

        reviewListView();
    }

    public void reviewListView() {

        ListView listview;

        mAdapter = new SearchListReviewViewAdapter();
        listview = (ListView) findViewById(R.id.list_detail_view_listview);
        listview.setAdapter(mAdapter);

        //MainUI 데이터 설정
        mAdapter.addMainUI();
//        ContextCompat.getDrawable(this,R.drawable.test_img_palace), "경복궁", "고궁", "1200"+"m", "서울시 광화문쪽", "1688-8282"

        //List 데이터 설정
        mAdapter.addItem(ContextCompat.getDrawable(this,R.drawable.face1), 4,ContextCompat.getDrawable(this,R.drawable.googlelogo)
                ,"Nick","2016.10.23",ContextCompat.getDrawable(this,R.drawable.test_img_palace),"경복궁 정말 좋은 것 같아효", 2000);
        mAdapter.addItem(ContextCompat.getDrawable(this,R.drawable.face1), 5,ContextCompat.getDrawable(this,R.drawable.googlelogo)
                ,"Jason","2016.10.21",ContextCompat.getDrawable(this,R.drawable.test_img_palace),"여친이 생기면 꼭 다시 와야겠어요!", 1949);
        mAdapter.addItem(ContextCompat.getDrawable(this,R.drawable.face1), 3,ContextCompat.getDrawable(this,R.drawable.googlelogo)
                ,"Mark","2016.10.20",ContextCompat.getDrawable(this,R.drawable.test_img_palace),"전 별로였는데..?", 392);

        Log.d(TAG,"I've passed here!");

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            }
        });
    }


    /////////////////////////  Adapter
    public class SearchListReviewViewAdapter extends BaseAdapter {

        private static final int TYPE_MAIN_UI = 0;
        private static final int TYPE_LIST = 1;
        private List<SearchListReviewViewItem> reviewViewItemList = new ArrayList<SearchListReviewViewItem>();
        private List<SearchListMainItem> reviewMainUIList = new ArrayList<SearchListMainItem>();
        private LayoutInflater inflater;

        public SearchListReviewViewAdapter() {
            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return reviewViewItemList.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            ViewHolderMainUI viewHolderMainUI;
            int type = getItemViewType(position);

            if (type == TYPE_MAIN_UI) {
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.listview_detail_main_ui, parent, false);
                    viewHolderMainUI = new ViewHolderMainUI(convertView);
                    convertView.setTag(viewHolderMainUI);
                } else {
                    viewHolderMainUI = (ViewHolderMainUI) convertView.getTag();
                }
                SearchListMainItem listMainItem = getMainUI(position);

                convertView.setClickable(false);
                convertView.setFocusable(false);
                viewHolderMainUI.btnExit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
                viewHolderMainUI.btnWriteReview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getApplicationContext(), WriteReviewActivity.class));
                        Log.d(TAG, "moveTo : WriteReviewActivity.class");
                    }
                });
                // TODO: 데이터가 안들어감!!!!!! 수정필요 (10.31)
//                viewHolderMainUI.mainImageView.setImageDrawable(listMainItem.getPictureDrawable());
//                viewHolderMainUI.mainTitle.setText(listMainItem.getTitle());
//                viewHolderMainUI.mainCategory.setText(listMainItem.getCategory());
//                viewHolderMainUI.mainLocation.setText(listMainItem.getLocation());
//                viewHolderMainUI.mainContact.setText(listMainItem.getPhoneNumber());
//                viewHolderMainUI.mainDistance.setText(listMainItem.getDistance());
            } else {
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.listview_detail_item, parent, false);
                    holder = new ViewHolder(convertView);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                SearchListReviewViewItem listViewItem = getItem(position);

                holder.ratingBar.setOnRatingBarChangeListener(onRatingChangedListener(holder, position));
                holder.ratingBar.setTag(position);
                holder.ratingBar.setRating(listViewItem.getRatingStar());

                holder.profileView.setImageDrawable(listViewItem.getProfileDrawable());
                holder.ratingScore.setText(new Float(listViewItem.getRatingScore()).toString());
                holder.userName.setText(listViewItem.getUserName());
                holder.nationalityView.setImageDrawable(listViewItem.getNationality());
                holder.reviewPictureView.setImageDrawable(listViewItem.getReviewDrawable());
                holder.dateView.setText(listViewItem.getDate());
                holder.reviewTextView.setText(listViewItem.getUserReview());
            }

            return convertView;
        }

        private RatingBar.OnRatingBarChangeListener onRatingChangedListener(final ViewHolder holder,
                                                                            final int position) {
            return new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                    SearchListReviewViewItem item = getItem(position);
                    item.setRatingStar(v);
                    Log.i("Adapter", "star: " + v);
                }
            };
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public SearchListReviewViewItem getItem(int position) {
            return reviewViewItemList.get(position);
        }

        public SearchListMainItem getMainUI(int position) {
            return reviewMainUIList.get(position);
        }

        @Override
        public int getItemViewType (int position) {
            if(position == 0) {
                return TYPE_MAIN_UI;
            } else {
                return TYPE_LIST;
            }
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        public void addItem(Drawable profileDrawable, float ratingScore, Drawable nationality, String userName, String date
                ,Drawable reviewDrawable, String userReview, int likeNum) {
            SearchListReviewViewItem item = new SearchListReviewViewItem();

            item.setProfileDrawable(profileDrawable);
            item.setRatingScore(ratingScore);
            item.setRatingStar(ratingScore);
            item.setNationality(nationality);
            item.setUserName(userName);
            item.setDate(date);
            item.setReviewDrawable(reviewDrawable);
            item.setUserReview(userReview);
//            item.setLikeNum(likeNum);
            reviewViewItemList.add(item);

        }

        // TODO: 데이터가 자꾸만 안들어감..! 수정필요 (10.31)
        public void addMainUI(){
//            Drawable pictureDrawable, String title, String category, String distance,
//                    String location, String phoneNumber

            SearchListMainItem item = new SearchListMainItem();
//            item.setPictureDrawable(pictureDrawable);
//            item.setTitle(title);
//            item.setCategory(category);
//            item.setDistance(distance);
//            item.setLocation(location);
//            item.setPhoneNumber(phoneNumber);

            reviewMainUIList.add(item);

            SearchListReviewViewItem addItemNum = new SearchListReviewViewItem();
            reviewViewItemList.add(addItemNum);
        }

        private class ViewHolderMainUI {
            private ImageView mainImageView;
            private TextView mainTitle;
            private TextView mainDistance;
            private TextView mainCategory;
            private TextView mainLocation;
            private TextView mainContact;
            private TextView btnExit;
            private ImageView btnWriteReview;
            public View view;

            public ViewHolderMainUI(View view) {
                mainImageView = (ImageView) view.findViewById(R.id.list_detail_main_image);
                mainTitle = (TextView) view.findViewById(R.id.list_detail_main_title);
                mainDistance = (TextView) view.findViewById(R.id.list_detail_main_distance);
                mainCategory = (TextView) view.findViewById(R.id.list_detail_main_category);
                mainLocation = (TextView) view.findViewById(R.id.list_detail_main_location);
                mainContact = (TextView) view.findViewById(R.id.list_detail_main_phone);
                btnExit = (TextView) view.findViewById(R.id.list_detail_main_exit);
                btnWriteReview = (ImageView) view.findViewById(R.id.list_detail_view_add_review);
                this.view = view;
            }
        }

        private class ViewHolder {
            private final ImageView profileView;
            private final RatingBar ratingBar;
            private final TextView ratingScore;
            private final TextView userName;
            private final ImageView nationalityView;
            private final ImageView reviewPictureView;
            private final TextView dateView;
            private final TextView reviewTextView;
            private final TextView likeNumView;
            public final View view;

            public ViewHolder(View view) {
                profileView = (ImageView) view.findViewById(R.id.listview_detail_item_profile);
                ratingBar = (RatingBar) view.findViewById(R.id.listview_detail_item_ratingbar);
                ratingScore = (TextView) view.findViewById(R.id.listview_detail_item_score);
                userName = (TextView) view.findViewById(R.id.listview_detail_item_username);
                nationalityView = (ImageView) view.findViewById(R.id.listview_detail_item_nationality);
                reviewPictureView = (ImageView) view.findViewById(R.id.listview_detail_item_img);
                dateView = (TextView) view.findViewById(R.id.listview_detail_item_date);
                reviewTextView = (TextView) view.findViewById(R.id.listview_detail_item_review);
                likeNumView = (TextView) view.findViewById(R.id.listview_detail_item_liked);
                this.view = view;
            }
        }

    }

    ////////////////////  Model
    public class SearchListReviewViewItem {
        private Drawable profileDrawable;
        private Drawable nationality;
        private Drawable reviewDrawable;
        private String userName;
        private String userReview;
        private float ratingScore;
        private String date;
        private int likeNum;
        private float ratingStar;

        public Drawable getReviewDrawable() {
            return reviewDrawable;
        }
        public void setReviewDrawable(Drawable reviewDrawable) {
            this.reviewDrawable = reviewDrawable;
        }

        public String getDate() {
            return date;
        }
        public void setDate(String date) {
            this.date = date;
        }

        public float getRatingStar() {
            return ratingStar;
        }
        public void setRatingStar(float ratingStar) {
            this.ratingStar = ratingStar;
        }

        public Drawable getProfileDrawable() {
            return profileDrawable;
        }
        public void setProfileDrawable(Drawable profileDrawable) {
            this.profileDrawable = profileDrawable;
        }

        public Drawable getNationality() {
            return nationality;
        }
        public void setNationality(Drawable nationality) {
            this.nationality = nationality;
        }

        public String getUserName() {
            return userName;
        }
        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserReview() {
            return userReview;
        }
        public void setUserReview(String userReview) {
            this.userReview = userReview;
        }

        public float getRatingScore() {
            return ratingScore;
        }
        public void setRatingScore(float ratingScore) {
            this.ratingScore = ratingScore;
        }

        public int getLikeNum() {
            return likeNum;
        }
        public void setLikeNum(int likeNum) {
            this.likeNum = likeNum;
        }
    }

    public class SearchListMainItem {
        private Drawable pictureDrawable;
        private String title;
        private String category;
        private String distance;
        private String location;
        private String phoneNumber;

        public Drawable getPictureDrawable() {
            return pictureDrawable;
        }
        public void setPictureDrawable(Drawable pictureDrawable) {
            this.pictureDrawable = pictureDrawable;
        }

        public String getTitle() {
            return title;
        }
        public void setTitle(String title) {
            this.title = title;
        }

        public String getCategory() {
            return category;
        }
        public void setCategory(String category) {
            this.category = category;
        }

        public String getDistance() {
            return distance;
        }
        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getLocation() {
            return location;
        }
        public void setLocation(String location) {
            this.location = location;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }
        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }
    }
}