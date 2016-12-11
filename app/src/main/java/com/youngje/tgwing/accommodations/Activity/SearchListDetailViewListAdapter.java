package com.youngje.tgwing.accommodations.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sprylab.android.widget.TextureVideoView;
import com.squareup.picasso.Picasso;
import com.youngje.tgwing.accommodations.Marker;
import com.youngje.tgwing.accommodations.R;
import com.youngje.tgwing.accommodations.Review;
import com.youngje.tgwing.accommodations.Translate;
import com.youngje.tgwing.accommodations.User;
import com.youngje.tgwing.accommodations.Util.HttpHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

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

    private TextureVideoView reviewVideoView;

    private FirebaseStorage storage;
    private StorageReference storageRef;
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final int pos = position;
        final Context context = viewGroup.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listview_detail_item, viewGroup, false);
        }

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://tourseoul-451de.appspot.com");

        ImageView profileView;
        final TextView userName;
        
        ImageView reviewPictureView;

        TextView reviewTextView;

        // TODO: 2016. 12. 2.  아래것들 처리해야됨 
        // 국적
        ImageView nationalityView; 
        
        // 좋아요 갯수
        ImageView likeImage;
        TextView likeNumView;
        
        // 작성일
        TextView dateView;
        
        // 별점
        RatingBar ratingBar;
        TextView ratingScore;

        profileView = (ImageView) view.findViewById(R.id.listview_detail_item_profile);
        
        // 래이팅바 ~
        ratingBar = (RatingBar) view.findViewById(R.id.listview_detail_item_ratingbar);
        ratingScore = (TextView) view.findViewById(R.id.listview_detail_item_score);
        
        userName = (TextView) view.findViewById(R.id.listview_detail_item_username);
        //nationalityView = (ImageView) view.findViewById(R.id.listview_detail_item_nationality);

        reviewPictureView = (ImageView) view.findViewById(R.id.listview_detail_item_img);
        reviewVideoView = (TextureVideoView) view.findViewById(R.id.listview_detail_item_video);

        dateView = (TextView) view.findViewById(R.id.listview_detail_item_date);
        reviewTextView = (TextView) view.findViewById(R.id.listview_detail_item_review);
        
        likeNumView = (TextView) view.findViewById(R.id.listview_detail_item_liked);
        likeImage = (ImageView) view.findViewById(R.id.liked_image_button);

        final Review item = reviews.get(pos);
        TextView localeText = (TextView)view.findViewById(R.id.listview_detail_item_userLocale);
        if(item.getWriteUserLocale() != null) {
            localeText.setText(item.getWriteUserLocale());
        } else { localeText.setText("ko"); }

        Button translateButton = (Button)view.findViewById(R.id.listview_detail_item_translateButton);
        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Review item = reviews.get(pos);

                String apiKey = "AIzaSyAqRgOzgNxcKZfJK_Pcc0k0yvZ7FkztxrA";
                //String curUserLocale = User.getMyInstance().getCountry();
                String curUserLocale = User.getMyInstance().getCountry();
                String reviewContent = item.getContent();
                String reviewLocale = "ko";

                Translate translate = new Translate(apiKey,reviewLocale,curUserLocale);
                try {
                    String translatehttpUrl = translate.genRequest(reviewContent);

                    Log.i("translatehttpUrl",translatehttpUrl);
                    String transtedString = new HttpHandler().execute(translatehttpUrl).get();
                    String parsedString = "";
                    try{
                        parsedString = new JSONObject(transtedString).getJSONObject("data").getJSONArray("translations").getJSONObject(0).getString("translatedText");
                    }catch(NullPointerException e){
                        e.printStackTrace();
                    }finally {
                        Toast.makeText(context,parsedString,Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //RelativeLayout relativeLayoutTemp = (RelativeLayout)view.findViewById(R.id.reviewContentLayout);

        switch (item.getContentType()) {
            case 0:
                reviewTextView.setText(item.getContent());
                reviewPictureView.setVisibility(View.GONE);
                reviewVideoView.setVisibility(View.GONE);
                break;
            case 1:
                reviewTextView.setText(item.getContent());
                Picasso.with(view.getContext()).load(item.getreviewContentUrl()).into(reviewPictureView);
                reviewPictureView.setVisibility(View.VISIBLE);
                reviewVideoView.setVisibility(View.GONE);
                break;
            case 2:
                reviewTextView.setText(item.getContent()+"[동영상]");
                reviewTextView.setMovementMethod(LinkMovementMethod.getInstance());
                reviewTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final SpannableString site = new SpannableString(
                                item.getreviewContentUrl());

                        Linkify.addLinks(site, Linkify.ALL);

                       final AlertDialog dialog = new AlertDialog.Builder(
                               context)
                               .setMessage(site)
                               .setPositiveButton("OK",
                                       new DialogInterface.OnClickListener() {
                                           public void onClick(
                                                   DialogInterface dialog, int which) {
                                           }
                                       }).create();

                       dialog.show();

                       ((TextView) dialog.findViewById(android.R.id.message))
                               .setMovementMethod(LinkMovementMethod.getInstance());


                        //     WebView webview = new WebView(context);    // 웹 뷰
                 //     webview.getSettings().setJavaScriptEnabled(true);    // 자바스크립트 허용
                 //     webview.getSettings().setDomStorageEnabled(true);
                 //     // URL 을 연결하여 웹 뷰 클라이언트를 세팅
                 //     webview.setWebViewClient(new WebViewClient() {
                 //         public boolean shouldOverrideUrlLoading(WebView view, String url) {
                 //             view.loadUrl(url);
                 //             return true;
                 //         }

                 //     });

                 //     // 다이얼로그를 생성
                 //     Dialog d = new Dialog(context) {
                 //         public boolean onKeyDown(int keyCode, KeyEvent event) {
                 //             if (keyCode == KeyEvent.KEYCODE_BACK)
                 //                 this.dismiss();
                 //             return true;
                 //         }
                 //     };

                 //     // 웹 뷰를 다이얼로그 연결한다
                 //     d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                 //     d.getWindow().setGravity(Gravity.BOTTOM);
                 //     d.addContentView(webview, new FrameLayout.LayoutParams(
                 //             ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                 //             Gravity.BOTTOM));

                 //     d.show();    // 다이얼로그 출력

                 //     webview.loadUrl(item.getreviewContentUrl());    // 웹 뷰에 url 로드
                    }
                });

                // TODO: 2016. 12. 11. 유알엘만 띄우는 걸로하자.

               // reviewPictureView.setVisibility(View.GONE);
               // reviewVideoView.setVisibility(View.VISIBLE);
               // StorageReference temp = storageRef.child(item.getMarkerId()).child("1481112050481.mp4");
               // temp.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
               //     @Override
               //     public void onSuccess(Uri uri) {
               //         Log.i("tjrtptm","tjrtptm");
               //         reviewVideoView.setVideoURI(uri);
//
               //     }
               // });


              //  Log.i("비디오뷰뷰뷰",item.getreviewContentUrl());
//
              //  reviewPictureView.setVisibility(View.GONE);
              //  reviewVideoView.setVisibility(View.VISIBLE);
              //  reviewVideoView.bringToFront();
              //  reviewVideoView.setVideoPath(item.getreviewContentUrl());
              //  reviewVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
              //      @Override
              //      public void onPrepared(MediaPlayer mp) {
              //          reviewVideoView.start();
              //          reviewVideoView.bringToFront();
              //      }
              //  });


              //  Log.i("비디오뷰뷰뷰",item.getreviewContentUrl());
              //  final VideoView temp = new VideoView(context);
              //  temp.setVideoURI(Uri.parse(item.getreviewContentUrl()));
              //  temp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
              //      @Override
              //      public void onPrepared(MediaPlayer mp) {
              //          MediaController mc = new MediaController(context);
              //          temp.setMediaController(mc);
              //          mc.setAnchorView(temp);
              //          temp.start();
              //      }
              //  });
//
              //  relativeLayoutTemp.addView(temp);

                break;
        }

        userName.setText(item.getUserName());
        Picasso.with(context).load(item.getUserImageUrl()).into(profileView);
        //dateView.setText(item.getCreateDate().toString());
       // reviewTextView.setText(item.getContent());

        return view;
    }


    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(Review newReview) {
        reviews.add(newReview);
    }


}
