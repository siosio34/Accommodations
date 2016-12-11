package com.youngje.tgwing.accommodations.ARAccomdation;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Shader;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.youngje.tgwing.accommodations.R;
import com.youngje.tgwing.accommodations.User;

import java.util.Date;
import java.util.List;

import com.youngje.tgwing.accommodations.ARAccomdation.mixare.DocumentARMarker;
import com.youngje.tgwing.accommodations.ARAccomdation.mixare.data.DataHandler;

/**
 * Created by joyeongje on 2016. 12. 10..
 */
public class ReadDocumentActivity extends Activity {
    private LinearLayout document = null;
    private double popupWindowRatio = 0.8;
    private int circleSize = 128;
    private int myProfileTextMarginLeft = 10;
    private int myProfileTextMarginTop = 10;
    private int myNameSize = 16;
    private int myIntroMarginBottom = 10;
    private int myIntroSize = 10;
    private int documentLeftScrollMarginTop = 10;

    private DatabaseReference mCommentsReference;

    //comment
    private int commentLayoutPadding = 16;
    private int commentCircleSize = 64;
    private int commentNameSize = 12;
    private int commentTimeSize = 12;
    private int commentSize = 16;
    private int commentPadding = 10;
    private int commentNameMarginLeft = 10;
    private int commentNameMarginTop = 10;
    private int commentTimeMarginRight = 10;
    private int commentTimeMarginTop = 10;

    DocumentARMarker documentMarker = null;
    EditText editText;
    User user = null;

    Point displaySize;
    LinearLayout commentLayout;
    public void initialize() {
        user = User.getMyInstance();
        documentMarker = DocumentARMarker.getInstance();
        Log.i("선택된 마커",documentMarker.getUid());

        editText = (EditText)findViewById(R.id.comment);

        Display display = getWindowManager().getDefaultDisplay();
        displaySize = new Point();
        display.getSize(displaySize);
        getWindow().getAttributes().width = displaySize.x;
        getWindow().getAttributes().height = displaySize.y;

        popupWindowRatio = 0.8;
        circleSize = displaySize.x/10;
        myProfileTextMarginLeft = displaySize.x/128;
        myProfileTextMarginTop = displaySize.x/128;
        //myNameSize = displaySize.x/80;
        myIntroMarginBottom = displaySize.x/128;
        //myIntroSize = displaySize.x/128;
        documentLeftScrollMarginTop = displaySize.x/128;

        //comment
        commentLayoutPadding = displaySize.x/80;
        commentCircleSize = displaySize.x/20;
        //commentNameSize = 12;
        //commentTimeSize = 12;
        //commentSize = 16;
        commentPadding = displaySize.x/128;
        commentNameMarginLeft = displaySize.x/128;
        commentNameMarginTop = displaySize.x/128;
        commentTimeMarginRight = displaySize.x/128;
        commentTimeMarginTop = displaySize.x/128;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_read_document);
        RelativeLayout topLayer = (RelativeLayout)findViewById(R.id.readDocumentTopLayer);

        initialize();

        document = (LinearLayout)findViewById(R.id.documentTopLayer);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int)(displaySize.x*popupWindowRatio), (int)(displaySize.y*popupWindowRatio));
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        document.setLayoutParams(params);

        ImageView myLocation = new ImageView(this);
        Picasso.with(this).load(user.getImageUri()).into(myLocation);
        RelativeLayout.LayoutParams myLocationParams = new RelativeLayout.LayoutParams(circleSize, circleSize);
        myLocationParams.setMargins((int)((displaySize.x*(1-popupWindowRatio)))/2-circleSize/2, (int)(displaySize.y*(1-popupWindowRatio))/2-circleSize/2, 0, 0);
        myLocation.setLayoutParams(myLocationParams);
        topLayer.addView(myLocation);

        ImageView mark = new ImageView(this);
        Picasso.with(this).load(user.getImageUri()).into(mark);
        RelativeLayout.LayoutParams markParams = new RelativeLayout.LayoutParams(circleSize, circleSize);
        markParams.setMargins((int)(displaySize.x*(1-popupWindowRatio))/2-circleSize/2, (int)(displaySize.y*(1-popupWindowRatio))/2-circleSize/2, 0, 0);
        mark.setLayoutParams(markParams);
        topLayer.addView(mark);

        ImageView myPic = new ImageView(this);
        Picasso.with(this).load(user.getImageUri()).into(myPic);
        RelativeLayout.LayoutParams myPicParams = new RelativeLayout.LayoutParams(circleSize, circleSize);
        myPicParams.setMargins((int)(displaySize.x*(1-popupWindowRatio))/2, (int)(displaySize.y*(1-popupWindowRatio))/2, 0, 0);
        myPic.setLayoutParams(myPicParams);
        topLayer.addView(myPic);

        LinearLayout myProfileText = (LinearLayout)findViewById(R.id.myProfileText);
        LinearLayout.LayoutParams myProfileTextParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        myProfileTextParams.setMargins(circleSize+myProfileTextMarginLeft, myProfileTextMarginTop, 0, 0);
        myProfileText.setLayoutParams(myProfileTextParams);

        TextView myIntro = new TextView(this);
        LinearLayout.LayoutParams myIntroParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        myIntroParams.setMargins(0, 0, 0, myIntroMarginBottom);
        myIntro.setLayoutParams(myIntroParams);
        myIntro.setText(user.getUserName());
        myIntro.setTextColor(0x40000000);
        myIntro.setTextSize(myIntroSize);
        myProfileText.addView(myIntro);

        TextView myName = new TextView(this);
        LinearLayout.LayoutParams myNameParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        myName.setLayoutParams(myNameParams);
        myName.setText(user.getUserName());
        myName.setTypeface(Typeface.DEFAULT_BOLD);
        myName.setTextColor(Color.BLACK);
        myName.setTextSize(myNameSize);
        myProfileText.addView(myName);

        ScrollView documentLeftScroll = (ScrollView)findViewById(R.id.documentLeftScroll);
        LinearLayout.LayoutParams scrollParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        scrollParams.setMargins(0, circleSize/2+documentLeftScrollMarginTop, 0, 0);
        documentLeftScroll.setLayoutParams(scrollParams);

        LinearLayout documentLeft = (LinearLayout)findViewById(R.id.documentLeft);
        if(documentMarker.getDocumentType() == 1) {
            //picture
            ImageView documentPic = new ImageView(this);
            Picasso.with(this).load(documentMarker.getURL()).into(documentPic);
            //documentPic.setBackground(new Drawable(bitmap));
            documentLeft.addView(documentPic);
        }

        else if(documentMarker.getDocumentType() == 2) {

         //   Display display = getWindowManager().getDefaultDisplay();
         //   Point size = new Point();
         //   display.getSize(size);
          //  int videoSize = size.x * 750;
//
//
          //  final VideoView videoView = new VideoView(this);
          //  LinearLayout.LayoutParams imgViewParams = new LinearLayout.LayoutParams(videoSize, videoSize);
          //  videoView.setLayoutParams(imgViewParams);
          //  videoView.setVideoURI(Uri.parse(documentMarker.getURL()));
          //  videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
          //      @Override
          //      public void onPrepared(MediaPlayer mp) {
          //          MediaController mc = new MediaController(ReadDocumentActivity.this);
          //          videoView.setMediaController(mc);
          //          mc.setAnchorView(videoView);
          //      }
          //  });

         //  documentLeft.addView(videoView);
        }

        TextView documentText = new TextView(this);
        documentText.setText(documentMarker.getTitle());
        documentText.setGravity(Gravity.TOP | Gravity.LEFT);
        documentText.setTextColor(Color.BLACK);
        documentLeft.addView(documentText);
        commentLayout = (LinearLayout)findViewById(R.id.commentLayout);

        TextView commentNumTextView = (TextView)findViewById(R.id.commentNum);
        commentNumTextView.setText(documentMarker.getDocumentCommentNum()+"");

        TextView withMeTextView = (TextView)findViewById(R.id.withMeNum);
        withMeTextView.setText(documentMarker.getDocumentResponseWithme()+"");

        TextView notNiceTextView = (TextView)findViewById(R.id.notNiceNum);
        notNiceTextView.setText(documentMarker.getDocumentResponseNotgood()+"");

        TextView seeyaTextView = (TextView)findViewById(R.id.seeyaNum);
        seeyaTextView.setText(documentMarker.getDocumentResponseSeeyou()+"");

        Button writeCommentBtn = (Button)findViewById(R.id.writeCommentBtn);
        writeCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date currentDate =new Date();
                String inputText = null;
                inputText = editText.getText().toString();
                if(inputText != null) {

                    Comment comment = new Comment(0, documentMarker.getId(),user.getUserId(),user.getUserName(), user.getImageUri(), inputText, currentDate, 0);
                    Log.i("코멘트",user.getUserName());
                    Log.i("이름",user.getUserId());
                    documentMarker.getCommentList().add(comment);
                    postComment();
                    commentLayout.addView(createCommentLayout(user.getImageUri(), user.getUserName(), DataHandler.getDateString(currentDate), inputText));
                }
                else {
                    Toast.makeText(getApplicationContext(),"입력 내용 없음",Toast.LENGTH_LONG).show();
                }
            }
        });

        List<Comment> comments = documentMarker.getCommentList();
        for(int i=0; i<comments.size(); i++) {
            System.out.println("urllll: "+comments.get(i).getUserImageUrl());
            commentLayout.addView(createCommentLayout(comments.get(i).getUserImageUrl(), comments.get(i).getUserName(), DataHandler.getDateString(comments.get(i).getCreateDate()), comments.get(i).getContent()));
        }
    }


    public void postComment() {

        FirebaseDatabase.getInstance().getReference().child("posts")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            Document temp = child.getValue(Document.class);
                            if(temp.getDocumentId() == documentMarker.getId()) {
                                temp.setCommentList(documentMarker.getCommentList());
                                child.getRef().setValue(temp);
                            }
                            else continue;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public RelativeLayout createCommentLayout(String profileUri, String name, String time, String comment) {
        RelativeLayout commentLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        commentLayout.setPadding(commentLayoutPadding, commentLayoutPadding, commentLayoutPadding, commentLayoutPadding);
        commentLayout.setLayoutParams(params);

        TextView nameView = new TextView(this);
        RelativeLayout.LayoutParams nameViewParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        nameViewParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        nameViewParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        nameViewParams.setMargins(commentCircleSize+commentNameMarginLeft, commentNameMarginTop, 0, 0);
        nameView.setLayoutParams(nameViewParams);
        nameView.setText(name);
        nameView.setTextColor(0x40000000);
        nameView.setTypeface(Typeface.DEFAULT_BOLD);
        nameView.setTextSize(commentNameSize);
        commentLayout.addView(nameView);

        TextView timeView = new TextView(this);
        RelativeLayout.LayoutParams timeViewParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        timeViewParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        timeViewParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        timeViewParams.setMargins(0, commentTimeMarginTop, commentTimeMarginRight, 0);
        timeView.setLayoutParams(timeViewParams);
        timeView.setText(time);
        timeView.setTextColor(0x40000000);
        timeView.setTypeface(Typeface.DEFAULT_BOLD);
        timeView.setTextSize(commentTimeSize);
        commentLayout.addView(timeView);

        TextView commentView = new TextView(this);
        RelativeLayout.LayoutParams commentViewParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        commentViewParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        commentViewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        commentViewParams.setMargins(0, 9*commentCircleSize/10, 0, 0);
        commentView.setLayoutParams(commentViewParams);
        commentView.setPadding(commentPadding, commentPadding, commentPadding, commentPadding);
        commentView.setText(comment);
        commentView.setTextSize(commentSize);
        commentView.setTextColor(Color.BLACK);
        commentView.setGravity(Gravity.LEFT | Gravity.TOP);
        commentView.setBackgroundColor(0xFFFFFFFF);
        commentLayout.addView(commentView);

        ImageView profile = new ImageView(this);
        RelativeLayout.LayoutParams profileParams = new RelativeLayout.LayoutParams(commentCircleSize, commentCircleSize);
        profileParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        profileParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        Picasso.with(this).load(profileUri).into(profile);
        profile.setLayoutParams(profileParams);
        commentLayout.addView(profile);

        return commentLayout;
    }

    public Bitmap getCircularBitmapWithWhiteBorder(Bitmap bitmap, int b_width, int b_height, int borderWidth) {
        if (bitmap == null || bitmap.isRecycled()) {
            return null;
        }

        final int width =  b_width+ borderWidth;
        final int height = b_height + borderWidth;

        Bitmap canvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);

        Canvas canvas = new Canvas(canvasBitmap);
        float radius = width > height ? ((float) height) / 2f : ((float) width) / 2f;
        canvas.drawCircle(width / 2, height / 2, radius, paint);
        paint.setShader(null);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(borderWidth);
        canvas.drawCircle(width / 2, height / 2, radius - borderWidth / 2, paint);
        return canvasBitmap;
    }
}