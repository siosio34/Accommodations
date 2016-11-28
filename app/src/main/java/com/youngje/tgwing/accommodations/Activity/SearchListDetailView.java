package com.youngje.tgwing.accommodations.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.youngje.tgwing.accommodations.DaumMarker;
import com.youngje.tgwing.accommodations.Marker;
import com.youngje.tgwing.accommodations.R;
import com.youngje.tgwing.accommodations.Review;

import org.w3c.dom.Text;


public class SearchListDetailView extends AppCompatActivity {


    private TextView markerTitle;
    private TextView markerCategory;
    private TextView markerDistance;
    private TextView markerLocation;
    private TextView markerPhone;

    private TextView markerExitButtonText;
    private FloatingActionButton writeReviewButton;

    private ListView reviewListView;

    private DaumMarker selectMarker;
    private String selectmarkerId;

    private SearchListDetailViewListAdapter mAdapter;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_detail);
        selectMarker = (DaumMarker)Marker.getSelectedMarker();
        selectmarkerId = selectMarker.getId();

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference("review");

        loadMainBar();
        loadReviewList();

    }

    public void loadMainBar() {

        markerTitle = (TextView) findViewById(R.id.list_detail_main_title);
        markerTitle.setText(selectMarker.getTitle());
        markerCategory = (TextView) findViewById(R.id.list_detail_main_category);
        markerCategory.setText(selectMarker.getCategory());
        markerDistance = (TextView) findViewById(R.id.list_detail_main_distance);
        markerDistance.setText(String.valueOf(selectMarker.getDistance()) + " m" );
        markerLocation = (TextView) findViewById(R.id.list_detail_main_location);
        markerLocation.setText(selectMarker.getNewAddress());
        markerPhone = (TextView) findViewById(R.id.list_detail_main_phone);
        markerPhone.setText(selectMarker.getPhoneNum());

        writeReviewButton = (FloatingActionButton) findViewById(R.id.list_detail_view_add_review);
        writeReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), WriteReviewActivity.class));
            }
        });

    }

    public void loadReviewList() {

        mAdapter = new SearchListDetailViewListAdapter();
        reviewListView = (ListView) findViewById(R.id.review_list_item);
        reviewListView.setAdapter(mAdapter);

        myRef.child(selectmarkerId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot reviewDataSnapshot : dataSnapshot.getChildren())
                {
                    Log.i("dddd","Ddddd");
                    Review getReview = reviewDataSnapshot.getValue(Review.class);
                    mAdapter.addItem(getReview);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }









}