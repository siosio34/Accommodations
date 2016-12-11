package com.youngje.tgwing.accommodations.ARAccomdation;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.VideoView;


import com.squareup.picasso.Picasso;
import com.youngje.tgwing.accommodations.R;
import com.youngje.tgwing.accommodations.User;

import com.youngje.tgwing.accommodations.ARAccomdation.mixare.ARMarker;
import com.youngje.tgwing.accommodations.ARAccomdation.mixare.DocumentARMarker;

public class UserProfileActivity extends Activity {
    private ImageView profileImage;
    private TextView profileName;
    private User user = null;
    private int shadowSize = 10;
    private double popupWindowRatio = 0.8;
    private int userProfilePadding = 16;
    private int profileSize = 100;
    private int nameSize = 16;
    private int nameRightMargin = 200;
    private int nameBottomMargin = 10;
    private int introSize = 12;
    private int followerCircleSize = 80;
    private int followingCircleSize = 80;
    private int userProfileTopMarginBottom = 10;
    private int myIntroLeftMargin = 10;
    private int followingTextSize = 20;
    private int followerTextSize = 20;
    private int commentListLayoutPadding = 16;
    //comment
    private int commentLayoutPadding = 5;
    private int commentCircleSize = 80;
    private int commentBorder = 2;
    private int commentNameSize = 16;
    private int commentSize = 16;
    private int commentPadding = 20;
    private int commentNameMarginLeft = 10;
    private int commentNameMarginTop = 20;
    private int commentIconMarginRight = 10;
    private int commentIconMarginTop = 10;
    private int iconTextSize = 10;
    private int commentViewLeftMargin = 10;
    private int commentPicSize = 750;
    private int videoSize = 750;

    public void initialize() {

        user = User.getMyInstance();
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        shadowSize = size.x/128;
        userProfilePadding = size.x/80;
        profileSize = (int)(size.x/9.6);
        //nameSize = size.x/80;
        nameRightMargin = (int)(size.x/6.4);
        nameBottomMargin = size.x/128;
        //introSize = size.x/106;
        followerCircleSize = size.x/16;
        followingCircleSize = size.x/16;
        userProfileTopMarginBottom = size.x/128;
        myIntroLeftMargin = size.x/128;
        //followingTextSize = size.x/128;
        //followerTextSize = size.x/128;
        commentListLayoutPadding = size.x/80;
        commentLayoutPadding = size.x/256;
        commentCircleSize = size.x/16;
        //commentNameSize = size.x/80;
        //commentSize = size.x/80;
        commentPadding = size.x/64;
        commentNameMarginLeft = size.x/128;
        commentNameMarginTop = size.x/64;
        commentIconMarginRight = size.x/128;
        commentIconMarginTop = size.x/128;
        iconTextSize = size.x/256;
        commentViewLeftMargin = size.x/64;
        commentPicSize = (int)(size.x/3.2);
        videoSize = (int)(size.x/2.4);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        RelativeLayout topLayout = new RelativeLayout(this);
        setContentView(topLayout);
        initialize();
        Bitmap testIcon = BitmapFactory.decodeResource(getResources(), R.drawable.profile);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        FrameLayout.LayoutParams topLayoutParams = new FrameLayout.LayoutParams(size.x, size.y);
        topLayout.setLayoutParams(topLayoutParams);

        FrameLayout shadow = new FrameLayout(this);
        shadow.setBackgroundResource(R.drawable.shadow);
        RelativeLayout.LayoutParams shadowParams = new RelativeLayout.LayoutParams((int)(size.x*popupWindowRatio)+shadowSize, (int)(size.y*popupWindowRatio)+shadowSize);
        shadowParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        shadow.setLayoutParams(shadowParams);
        topLayout.addView(shadow);

        LinearLayout userProfileLayout = new LinearLayout(this);
        userProfileLayout.setOrientation(LinearLayout.VERTICAL);
        userProfileLayout.setBackgroundResource(R.drawable.toplayer);
        userProfileLayout.setPadding(userProfilePadding, userProfilePadding, userProfilePadding, userProfilePadding);
        RelativeLayout.LayoutParams userProfileParams = new RelativeLayout.LayoutParams((int)(size.x*popupWindowRatio), (int)(size.y*popupWindowRatio));
        userProfileParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        userProfileLayout.setLayoutParams(userProfileParams);
        topLayout.addView(userProfileLayout);

        RelativeLayout userProfileTopLayout = new RelativeLayout(this);
        userProfileTopLayout.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams userProfileTopParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        userProfileTopParams.setMargins(0, 0, 0, userProfileTopMarginBottom);
        userProfileTopLayout.setLayoutParams(userProfileTopParams);
        userProfileLayout.addView(userProfileTopLayout);

        LinearLayout profileWithIntro = new LinearLayout(this);
        profileWithIntro.setOrientation(LinearLayout.HORIZONTAL);
        profileWithIntro.setGravity(Gravity.CENTER_VERTICAL);
        RelativeLayout.LayoutParams profileWithIntroParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        profileWithIntroParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        profileWithIntro.setLayoutParams(profileWithIntroParams);
        userProfileTopLayout.addView(profileWithIntro);

        ImageView profile = new ImageView(this);
        //profile.setImageDrawable(new RoundedAvatarDrawable(profileBitmap, circleSize, circleSize));
        LinearLayout.LayoutParams profileParams = new LinearLayout.LayoutParams(profileSize, profileSize);
        profileParams.gravity = Gravity.CENTER_VERTICAL;
        profile.setLayoutParams(profileParams);
        Picasso.with(this).load(user.getImageUri()).into(profile);
        profileWithIntro.addView(profile);

        LinearLayout myIntroLayout = new LinearLayout(this);
        myIntroLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams myIntroParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        myIntroParams.setMargins(myIntroLeftMargin, 0, 0, 0);
        myIntroLayout.setLayoutParams(myIntroParams);
        profileWithIntro.addView(myIntroLayout);

        TextView nameView = new TextView(this);
        nameView.setText(user.getUserName());
        nameView.setTextColor(Color.parseColor("#000000"));
        nameView.setTypeface(Typeface.DEFAULT_BOLD);
        nameView.setTextSize(nameSize);
        LinearLayout.LayoutParams nameViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        nameViewParams.setMargins(0, 0, nameRightMargin, nameBottomMargin);
        nameView.setLayoutParams(nameViewParams);
        myIntroLayout.addView(nameView);

        TextView introView = new TextView(this);
        introView.setText(user.getUserName());
        introView.setTextColor(Color.parseColor("#40000000"));
        introView.setTextSize(introSize);
        //introView.setMaxWidth(nameView.getWidth());
        LinearLayout.LayoutParams introViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        introView.setLayoutParams(introViewParams);
        myIntroLayout.addView(introView);

        FrameLayout followLayout = new FrameLayout(this);
        RelativeLayout.LayoutParams followParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        followParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        followParams.addRule(RelativeLayout.CENTER_VERTICAL);
        followLayout.setLayoutParams(followParams);
        userProfileTopLayout.addView(followLayout);


        TextView followerText = new TextView(this);
        followerText.setText("팔로워");
        followerText.setTextSize(10);
        followerText.setTextColor(Color.parseColor("#40000000"));
        FrameLayout.LayoutParams followerTextParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        followerTextParams.gravity = Gravity.TOP | Gravity.RIGHT;
        followerText.setLayoutParams(followerTextParams);
        followLayout.addView(followerText);

        TextView followingText = new TextView(this);
        followingText.setText("팔로잉");
        followingText.setTextSize(10);
        followingText.setTextColor(Color.parseColor("#40000000"));
        FrameLayout.LayoutParams followingTextParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        followingTextParams.gravity = Gravity.BOTTOM | Gravity.LEFT;
        followingText.setLayoutParams(followingTextParams);
        followLayout.addView(followingText);

        ScrollView documentListScroll = new ScrollView(this);
        documentListScroll.setBackgroundColor(Color.parseColor("#E6E6E6"));
        LinearLayout.LayoutParams documentListScrollParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        documentListScroll.setLayoutParams(documentListScrollParams);
        userProfileLayout.addView(documentListScroll);

        LinearLayout documentListLayout = new LinearLayout(this);
        documentListLayout.setPadding(commentListLayoutPadding, commentListLayoutPadding, commentListLayoutPadding, commentListLayoutPadding);
        LinearLayout.LayoutParams documentListLayoutParams =  new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        documentListLayout.setOrientation(LinearLayout.VERTICAL);
        documentListLayout.setLayoutParams(documentListLayoutParams);
        documentListScroll.addView(documentListLayout);

        // TODO: 2016. 12. 11. 클래스 비교해서 예외처리해야됨. 
        
        //// TODO: 2016. 12. 11. 같이 있으면 터지네 하아... 

        for(int i = 0; i< ARMarker.markersList.size() ; i++) {

            DocumentARMarker documentMarker = (DocumentARMarker)ARMarker.markersList.get(i);
            Log.i("정보정보",i +":" + documentMarker.getUid());
            if(user.getUserId().equals(documentMarker.getUid())) {
               documentListLayout.addView(makeDocumentLayout(documentMarker));
            }
        }

    }

    public RelativeLayout makeDocumentLayout(DocumentARMarker documentMarker) {
        RelativeLayout documentLayout = new RelativeLayout(this);
        documentLayout.setPadding(commentLayoutPadding, commentLayoutPadding, commentLayoutPadding, commentLayoutPadding);
        RelativeLayout.LayoutParams documentLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        documentLayout.setLayoutParams(documentLayoutParams);

        TextView nameView = new TextView(this);
        RelativeLayout.LayoutParams nameViewParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        nameViewParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        nameViewParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        nameViewParams.setMargins(commentCircleSize+commentNameMarginLeft, commentNameMarginTop, 0, 0);
        nameView.setLayoutParams(nameViewParams);
        nameView.setText(user.getUserName());
        nameView.setTextColor(0x40000000);
        nameView.setTypeface(Typeface.DEFAULT_BOLD);
        nameView.setTextSize(commentNameSize);
        documentLayout.addView(nameView);

        LinearLayout iconsLayout = new LinearLayout(this);
        iconsLayout.setOrientation(LinearLayout.HORIZONTAL);
        RelativeLayout.LayoutParams iconLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        iconLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        iconLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        iconLayoutParams.setMargins(0, commentIconMarginTop, commentIconMarginRight, 0);
        iconsLayout.setLayoutParams(iconLayoutParams);
        documentLayout.addView(iconsLayout);

        LinearLayout iconWithmeLayout = new LinearLayout(this);
        iconWithmeLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams iconWithmeLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        iconWithmeLayout.setLayoutParams(iconWithmeLayoutParams);
        iconWithmeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ImageView iconWithme = new ImageView(this);
        iconWithme.setBackgroundResource(R.drawable.icon_emotion_bewithme2);
        iconWithmeLayout.addView(iconWithme);

        TextView iconWithmeTextView = new TextView(this);
        LinearLayout.LayoutParams iconWithmeTextViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        iconWithmeTextView.setText(new Integer(documentMarker.getDocumentResponseWithme()).toString());
        iconWithmeTextView.setTextColor(Color.parseColor("#000000"));
        iconWithmeTextView.setTextSize(iconTextSize);
        iconWithmeTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        iconWithmeTextView.setLayoutParams(iconWithmeTextViewParams);
        iconWithmeLayout.addView(iconWithmeTextView);

        iconsLayout.addView(iconWithmeLayout);

        LinearLayout iconNotNiceLayout = new LinearLayout(this);
        iconNotNiceLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams iconNotNiceLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        iconNotNiceLayout.setLayoutParams(iconNotNiceLayoutParams);

        ImageView iconNotNice = new ImageView(this);
        iconNotNice.setBackgroundResource(R.drawable.icon_emotion_notnice2);
        iconNotNiceLayout.addView(iconNotNice);
        iconNotNiceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        TextView iconNotNiceTextView = new TextView(this);
        LinearLayout.LayoutParams iconNotNiceTextViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        iconNotNiceTextView.setText(new Integer(documentMarker.getDocumentResponseNotgood()).toString());
        iconNotNiceTextView.setTextColor(Color.parseColor("#000000"));
        iconNotNiceTextView.setTextSize(iconTextSize);
        iconNotNiceTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        iconNotNiceTextView.setLayoutParams(iconNotNiceTextViewParams);
        iconNotNiceLayout.addView(iconNotNiceTextView);

        iconsLayout.addView(iconNotNiceLayout);

        LinearLayout iconSeeyaLayout = new LinearLayout(this);
        iconSeeyaLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams iconSeeyaLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        iconSeeyaLayout.setLayoutParams(iconSeeyaLayoutParams);
        iconSeeyaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ImageView iconSeeya = new ImageView(this);
        iconSeeya.setBackgroundResource(R.drawable.icon_emotion_seeya2);
        iconSeeyaLayout.addView(iconSeeya);

        TextView iconSeeyaTextView = new TextView(this);
        LinearLayout.LayoutParams iconSeeyaTextViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        iconSeeyaTextView.setText(new Integer(documentMarker.getDocumentResponseSeeyou()).toString());
        iconSeeyaTextView.setTextColor(Color.parseColor("#000000"));
        iconSeeyaTextView.setTextSize(iconTextSize);
        iconSeeyaTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        iconSeeyaTextView.setLayoutParams(iconSeeyaTextViewParams);
        iconSeeyaLayout.addView(iconSeeyaTextView);

        iconsLayout.addView(iconSeeyaLayout);

        LinearLayout contentLayout = new LinearLayout(this);
        contentLayout.setPadding(commentPadding, commentPadding, commentPadding, commentPadding);
        contentLayout.setBackgroundColor(0xFFFFFFFF);
        contentLayout.setOrientation(LinearLayout.HORIZONTAL);
        RelativeLayout.LayoutParams contentLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        contentLayoutParams.setMargins(0, (int)(1.1*commentCircleSize), 0, 0);
        contentLayout.setLayoutParams(contentLayoutParams);
        documentLayout.addView(contentLayout);

        if(documentMarker.getDocumentType()==1) {
            Log.i("urlurl", documentMarker.getURL());
            ImageView imgView = new ImageView(this);
            LinearLayout.LayoutParams imgViewParams = new LinearLayout.LayoutParams(commentPicSize, commentPicSize);
            imgView.setLayoutParams(imgViewParams);
            imgViewParams.gravity = Gravity.CENTER_VERTICAL;
            Picasso.with(this).load(documentMarker.getURL()).into(imgView);
            contentLayout.addView(imgView);
        }
        else if(documentMarker.getDocumentType() == 2) {
            Log.i("urlurlvideo", documentMarker.getURL());
            final VideoView videoView = new VideoView(this);
            LinearLayout.LayoutParams imgViewParams = new LinearLayout.LayoutParams(videoSize, videoSize);
            videoView.setLayoutParams(imgViewParams);
            videoView.setVideoURI(Uri.parse(documentMarker.getURL()));
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    MediaController mc = new MediaController(UserProfileActivity.this);
                    videoView.setMediaController(mc);
                    mc.setAnchorView(videoView);
                }
            });
            contentLayout.addView(videoView);
        }

        TextView commentView = new TextView(this);
        LinearLayout.LayoutParams commentViewParams = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        commentView.setLayoutParams(commentViewParams);
        commentViewParams.setMargins(commentViewLeftMargin, 0, 0, 0);
        commentView.setText(documentMarker.getTitle());
        commentView.setTextSize(commentSize);
        commentView.setTextColor(Color.BLACK);
        commentView.setGravity(Gravity.LEFT | Gravity.TOP);
        contentLayout.addView(commentView);

        ImageView profile = new ImageView(this);
        RelativeLayout.LayoutParams profileParams = new RelativeLayout.LayoutParams(commentCircleSize, commentCircleSize);
        profileParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        profileParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        profile.setLayoutParams(profileParams);
        Picasso.with(this).load(user.getImageUri()).into(profile);
        documentLayout.addView(profile);

        return documentLayout;
    }

    public void profileLoad () {

        String profileURL = user.getImageUri();
        String userName = user.getUserName();

        Log.i("google get information",userName);

        Picasso.with(this).load(profileURL).into(profileImage);
        profileName.setText(userName);
    }
}