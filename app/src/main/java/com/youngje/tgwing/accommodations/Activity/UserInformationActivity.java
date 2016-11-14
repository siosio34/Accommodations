package com.youngje.tgwing.accommodations.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.youngje.tgwing.accommodations.R;
import com.youngje.tgwing.accommodations.User;

/**
 * Created by JiwoonWon on 2016. 10. 31..
 */

public class UserInformationActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private ImageView buttonBack;
    private TextView userEmail;
    private ImageView userThumbnail;
    private TextView userName;
    private String languageChoice;
    private EditText userMessage;
    private ImageView buttonSave;

    private LinearLayout flag_US;
    private LinearLayout flag_Japan;
    private LinearLayout flag_Chinese;
    private LinearLayout flag_Korea;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        final User currrentUser = User.getMyInstance();

        buttonBack = (ImageView) findViewById(R.id.info_backbutton);

        userEmail = (TextView) findViewById(R.id.info_user_email);
        userEmail.setText(currrentUser.getUserEmail());

        userThumbnail = (ImageView) findViewById(R.id.info_user_thumbnail);
        Picasso.with(getApplicationContext()).load(currrentUser.getImageUri()).into(userThumbnail);

        userName = (TextView) findViewById(R.id.info_user_name);
        userName.setText(currrentUser.getUserName());

        userMessage = (EditText) findViewById(R.id.info_user_message);
        userMessage.setText(currrentUser.getUserDescription());

        buttonSave = (ImageView) findViewById(R.id.info_button_start);

        flag_US = (LinearLayout) findViewById(R.id.info_click_us);
        flag_Chinese = (LinearLayout) findViewById(R.id.info_click_china);
        flag_Japan = (LinearLayout) findViewById(R.id.info_click_japan);
        flag_Korea = (LinearLayout) findViewById(R.id.info_click_ko);

        FrameLayout clickEnglish = (FrameLayout) findViewById(R.id.info_language_english);
        FrameLayout clickChinese = (FrameLayout) findViewById(R.id.info_language_chinese);
        FrameLayout clickJapan = (FrameLayout) findViewById(R.id.info_language_japanese);
        FrameLayout clickKorean = (FrameLayout) findViewById(R.id.info_language_korean);

        clickEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag_US.setVisibility(view.VISIBLE);
                flag_Japan.setVisibility(view.INVISIBLE);
                flag_Chinese.setVisibility(view.INVISIBLE);
                flag_Korea.setVisibility(view.INVISIBLE);
                languageChoice = "en";
            }
        });
        clickChinese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag_US.setVisibility(view.INVISIBLE);
                flag_Japan.setVisibility(view.INVISIBLE);
                flag_Chinese.setVisibility(view.VISIBLE);
                flag_Korea.setVisibility(view.INVISIBLE);
                languageChoice = "zh-CN";
            }
        });
        clickJapan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag_US.setVisibility(view.INVISIBLE);
                flag_Japan.setVisibility(view.VISIBLE);
                flag_Chinese.setVisibility(view.INVISIBLE);
                flag_Korea.setVisibility(view.INVISIBLE);
                languageChoice = "ja";
            }
        });
        clickKorean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag_US.setVisibility(view.INVISIBLE);
                flag_Japan.setVisibility(view.INVISIBLE);
                flag_Chinese.setVisibility(view.INVISIBLE);
                flag_Korea.setVisibility(view.VISIBLE);
                languageChoice = "ko";
            }
        });

        // user정보 불러오기

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 저장 기능 코드
                /*
                DB에 넘겨야 할 데이터 :
                languageChoice, userMessage
                 */
                currrentUser.setUserName(userName.getText().toString());
                currrentUser.setCountry(languageChoice);
                currrentUser.setUserDescription(userMessage.getText().toString());
                User.setMyInstance(currrentUser);
                databaseReference.child("users").child(User.getMyInstance().getUserId()).setValue(currrentUser);
                finish();
            }
        });
    }
}
