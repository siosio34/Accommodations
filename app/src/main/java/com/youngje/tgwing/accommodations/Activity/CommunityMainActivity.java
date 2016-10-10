package com.youngje.tgwing.accommodations.Activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.youngje.tgwing.accommodations.Chatroom;
import com.youngje.tgwing.accommodations.R;
import com.youngje.tgwing.accommodations.Util.RoundedAvatarDrawable;

import java.io.IOException;
import java.util.ArrayList;

public class CommunityMainActivity extends Activity implements View.OnClickListener {
    private ArrayList<Chatroom> chatroomList;
    private PopupWindow popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_main);

        FloatingActionButton createChatroomBtn = (FloatingActionButton) findViewById(R.id.community_main_createChatroom);
        createChatroomBtn.setOnClickListener(this);

        ViewPager chatroomListPagerAdapter = (ViewPager) findViewById(R.id.community_main_chatroom_viewpager);
        chatroomListPagerAdapter.setAdapter(new ChatroomListPagerAdapter());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.community_main_chatroom_body:
               // Intent intent = new Intent(CommunityMainActivity.this, CommunityChatroomActivity.class);
               // intent.putExtra("boardId", (int) v.getTag());
                /*
                    채팅룸 구별하기 위해 데이터 전달 필요
                 */
                //startActivity(intent);
                break;

            //클릭시 팝업 윈도우 생성
            case R.id.community_main_createChatroom:
                //팝업으로 띄울 커스텀뷰를 설정
                View popupView = getLayoutInflater().inflate(R.layout.layout_community_main_create_chatroom_popup, null);

                //팝업 객체 생성
                popup = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                //팝업 뷰 터치 가능하도록 설정
                popup.setTouchable(true);

                //popupwindow를 parent view 기준으로 띄움
                popup.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                //register onclick listener for create and cancel
                popupView.findViewById(R.id.community_main_chatroom_popup_create).setOnClickListener(this);
                popupView.findViewById(R.id.community_main_chatroom_popup_cancel).setOnClickListener(this);

                //make background dim
                WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                WindowManager.LayoutParams p = (WindowManager.LayoutParams) popupView.getLayoutParams();
                p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                p.dimAmount = 0.5f;
                wm.updateViewLayout(popupView, p);
                break;

            case R.id.community_main_chatroom_popup_create:
                try {
                    String chatroomTitle = ((EditText) findViewById(R.id.community_main_chatroom_popup_title)).getText().toString();
                    int limitNumber = Integer.parseInt(((EditText) findViewById(R.id.community_main_chatroom_popup_limitNumber)).getText().toString());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                /*
                    방생성에 필요한 데이터를 캐치해서 데이터베이스에 저장 필요
                 */


                break;

            case R.id.community_main_chatroom_popup_cancel:
                popup.dismiss();
                break;
        }
    }

    private class ChatroomListPagerAdapter extends PagerAdapter {

        public ChatroomListPagerAdapter() {
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v;

            switch (position) {
                case 0:
                    v = getChatroomList(0);
                    container.addView(v);
                    break;

                case 1:
                    v = getChatroomList(1);
                    container.addView(v);
                    break;

                default:
                    v = null;
            }

            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View pager, Object obj) {
            return pager == obj;
        }

        public ScrollView getChatroomList(int flag) {
            chatroomList = new ArrayList<>();
            ScrollView chatroomListScroll = new ScrollView(CommunityMainActivity.this);
            LinearLayout.LayoutParams chatroomListScrollParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            chatroomListScroll.setLayoutParams(chatroomListScrollParams);

            LinearLayout chatroomListLayout = new LinearLayout(CommunityMainActivity.this);
            LinearLayout.LayoutParams chatroomListLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            chatroomListLayout.setLayoutParams(chatroomListLayoutParams);
            chatroomListScroll.addView(chatroomListLayout);

            //get data from db
            if (flag == 0) {
                chatroomListScroll.setBackgroundColor(0x80000000);

                /*
                    내주위에 있는 채팅룸 리스트 가져오기 필요.
                 */

            } else if (flag == 1) {
                chatroomListScroll.setBackgroundColor(0x80FFFFFF);

                /*
                    내가 들어간 방 리스트 가져오기 필요
                 */

            }

            for (int i = 0; i < chatroomList.size(); i++) {
                Chatroom chatroom = chatroomList.get(i);
                //create chatroom
                LinearLayout chatroomView = (LinearLayout) LayoutInflater.from(CommunityMainActivity.this).inflate(R.layout.layout_community_main_chatroom, null);
                //방마다 구별하기 위해 채팅룸 id를 구별자로 추가.
                chatroomView.setTag(chatroom.getChatroomId());
                chatroomView.setOnClickListener(CommunityMainActivity.this);

                //set chatroom writer profile
                ImageView profilePicView = (ImageView) chatroomView.findViewById(R.id.community_main_chatroom_writerProfilePic);
                try {
                    Bitmap profilePic = Picasso.with(CommunityMainActivity.this).load(Uri.parse(chatroom.getChatroomWriterProfilePic())).get();
                    profilePicView.setImageDrawable(new RoundedAvatarDrawable(profilePic, profilePic.getWidth(), profilePic.getWidth()));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //set chatroom writer name
                TextView chatroomWriterName = (TextView) chatroomView.findViewById(R.id.community_main_chatroom_writerName);
                chatroomWriterName.setText(chatroom.getChatroomWriterName());

                //set chatroom writer nationality
                ImageView chatroomNationality = (ImageView) chatroomView.findViewById(R.id.community_main_chatroom_nationality);
                Picasso.with(CommunityMainActivity.this).load(Uri.parse(chatroom.getChatroomWriterNationality())).into(chatroomNationality);

                //set chatroom number
                TextView chatroomNumber = (TextView) chatroomView.findViewById(R.id.community_main_chatroom_number);
                chatroomNumber.setText(chatroom.getChatroomNumber() + "/" + chatroom.getChatroomMaxNumbr());

                //set chatroom date
                TextView chatroomDate = (TextView) chatroomView.findViewById(R.id.community_main_chatroom_date);
                chatroomDate.setText(chatroom.getChatroomDate().toString());

                //set chatroom title
                TextView chatroomTitle = (TextView) chatroomView.findViewById(R.id.community_main_chatroom_title);
                chatroomTitle.setText(chatroom.getChatroomTitle());

                //set chatroom
                chatroomListLayout.addView(chatroomView);
            }

            return chatroomListScroll;
        }
    }
}