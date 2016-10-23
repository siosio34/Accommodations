package com.youngje.tgwing.accommodations.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.youngje.tgwing.accommodations.Chat;
import com.youngje.tgwing.accommodations.ChatManager;
import com.youngje.tgwing.accommodations.Chatroom;
import com.youngje.tgwing.accommodations.Data.DataFormat;
import com.youngje.tgwing.accommodations.R;
import com.youngje.tgwing.accommodations.Util.HttpHandler;
import com.youngje.tgwing.accommodations.Util.RoundedAvatarDrawable;
import com.youngje.tgwing.accommodations.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;


public class CommunityMainActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private PopupWindow popup;
    private View popupView;

    //community chatroom activity request code
    private final int COMMUNITY_CHATROOM_REQUEST_CODE = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_main);

        FloatingActionButton createChatroomBtn = (FloatingActionButton)findViewById(R.id.community_main_createChatroom);
        createChatroomBtn.setOnClickListener(this);

        ViewPager chatroomListPagerAdapter = (ViewPager)findViewById(R.id.community_main_chatroom_viewpager);
        chatroomListPagerAdapter.setAdapter(new ChatroomListPagerAdapter());
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.community_main_chatroom_body:
                enterRoom((String)v.getTag());
                break;

            //클릭시 팝업 윈도우 생성
            case R.id.community_main_createChatroom:
                popupView();
                break;

            case R.id.community_main_chatroom_popup_create:
                createRoom();
                break;

            case R.id.community_main_chatroom_popup_cancel:
                popup.dismiss();
                break;
        }
    }

    public void enterRoom(final String chatManagerId) {
        databaseReference.child("ChatManager").child(chatManagerId).child("chatroom").runTransaction(new Transaction.Handler() {
            int check=-1;
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Chatroom chatroom = mutableData.getValue(Chatroom.class);

                ArrayList<User> list = chatroom.getUserList();
                boolean isRoomMember = false;
                for(int i=0; i<list.size(); i++)
                    if(list.get(i).getUserId().equals(User.getMyInstance().getUserId())) {
                        isRoomMember = true;
                        break;
                    }

                if(isRoomMember || !chatroom.isEnd()) {
                    int maxNumber = chatroom.getChatroomMaxNumber();
                    int currentNumber = chatroom.getUserList().size();

                    if(isRoomMember)
                        check = 1;
                    else if (currentNumber < maxNumber) {
                        mutableData.child("userList").child(Integer.toString(currentNumber)).setValue(User.getMyInstance());
                        check = 1;
                    }
                    else
                        check = 2;
                }
                else if(chatroom.isEnd()) {
                    check = 0;
                }

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                if(databaseError == null) {
                    if(check == 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(CommunityMainActivity.this, "마감된 방입니다.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    else if(check == 2) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(CommunityMainActivity.this, "방이 꽉 찼습니다.", Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                    else if (check == 1) {
                        Intent intent = new Intent(CommunityMainActivity.this, CommunityChatroomActivity.class);
                        intent.putExtra("chatManagerId", chatManagerId);
                        startActivityForResult(intent, COMMUNITY_CHATROOM_REQUEST_CODE);
                    }
                    else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(CommunityMainActivity.this, "방 입장에 문제가 생겼습니다. 네트워크를 확인해주세요.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
                else {
                    /*
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CommunityMainActivity.this, "방 입장에 실패하였습니다. 네트워크를 확인해주세요.", Toast.LENGTH_LONG).show();
                        }
                    });
                    */
                    enterRoom(chatManagerId);
                }
                System.out.println(databaseError);
            }
        });
    }

    public void createRoom() {
        databaseReference.child("users").child(User.getMyInstance().getUserId()).child("chatRoomID").addListenerForSingleValueEvent(
                new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue(String.class).length() == 0) {
                    databaseReference.child("ChatManager").push().addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(final DataSnapshot dataSnapshot) {
                                    try {
                                        String chatroomTitle = ((EditText) popupView.findViewById(R.id.community_main_chatroom_popup_title)).getText().toString();
                                        int limitNumber = Integer.parseInt(((EditText) popupView.findViewById(R.id.community_main_chatroom_popup_limitNumber)).getText().toString());
                                        User user = User.getMyInstance();
                                        Chatroom chatroom = new Chatroom(chatroomTitle, user.getImageUri(), user.getUserName(), user.getCountry(), limitNumber, new Date(), false, new ArrayList<User>());
                                        final ChatManager chatManager = new ChatManager(chatroom, new HashMap<String, Chat>());
                                        dataSnapshot.getRef().setValue(chatManager, new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                //add room id to User object
                                                if(databaseError == null) {
                                                    databaseReference.getRoot().child("users").child(User.getMyInstance().getUserId()).child("chatRoomID").setValue(dataSnapshot.getKey(), new DatabaseReference.CompletionListener() {
                                                        @Override
                                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                            dataSnapshot.child("chatroom").child("userList").getRef().child("0").setValue(User.getMyInstance());
                                                            Intent intent = new Intent(CommunityMainActivity.this, CommunityChatroomActivity.class);
                                                            intent.putExtra("chatManagerId", dataSnapshot.getKey());
                                                            startActivityForResult(intent, COMMUNITY_CHATROOM_REQUEST_CODE);
                                                        }
                                                    });
                                                }
                                                else {
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText(CommunityMainActivity.this, "방 생성에 실패하였습니다. 네트워크를 확인해주세요.", Toast.LENGTH_LONG).show();
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    } catch(NullPointerException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(CommunityMainActivity.this, "방 생성에 실패하였습니다. 네트워크를 확인해주세요.", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }
                    );
                }
                else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CommunityMainActivity.this, "이미 만든 방이 존재합니다.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CommunityMainActivity.this, "방 생성에 실패하였습니다. 네트워크를 확인해주세요.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    public void popupView() {
        databaseReference.child("users").child(User.getMyInstance().getUserId()).child("chatRoomID").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue(String.class).length() == 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //팝업으로 띄울 커스텀뷰를 설정
                            popupView = getLayoutInflater().inflate(R.layout.layout_community_main_create_chatroom_popup, null);

                            //팝업 객체 생성
                            popup = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                            //팝업 뷰 터치 가능하도록 설정
                            popup.setTouchable(true);

                            //popupwindow를 parent view 기준으로 띄움
                            popup.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                            //register onclick listener for create and cancel
                            popupView.findViewById(R.id.community_main_chatroom_popup_create).setOnClickListener(CommunityMainActivity.this);
                            popupView.findViewById(R.id.community_main_chatroom_popup_cancel).setOnClickListener(CommunityMainActivity.this);

                            //make background dim
                            if(android.os.Build.VERSION.SDK_INT <= 22) {
                                WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                                WindowManager.LayoutParams p = (WindowManager.LayoutParams) popupView.getLayoutParams();
                                p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                                p.dimAmount = 0.5f;
                                wm.updateViewLayout(popupView, p);
                            }
                            else {
                                View parent = (View)popup.getContentView().getParent();
                                WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                                WindowManager.LayoutParams p = (WindowManager.LayoutParams)parent.getLayoutParams();
                                p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                                p.dimAmount = 0.5f;
                                wm.updateViewLayout(parent, p);
                            }
                        }
                    });
                }
                else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CommunityMainActivity.this, "이미 만든 방이 존재합니다.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case COMMUNITY_CHATROOM_REQUEST_CODE:
                if(resultCode == 9998)
                    Toast.makeText(this, "방이 존재하지 않습니다.", Toast.LENGTH_LONG).show();
                else if(resultCode == 9999)
                    Toast.makeText(this, "방 접속에 실패하였습니다.", Toast.LENGTH_LONG).show();
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

        public ListView getChatroomList(int flag) {
            ArrayList<Chatroom> chatroomList = new ArrayList<>();
            ChatRoomAdapter chatroomAdapter = new ChatRoomAdapter(CommunityMainActivity.this, R.layout.layout_community_main_chatroom, chatroomList);
            final ListView chatroomListView = new ListView(CommunityMainActivity.this);
            chatroomListView.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
            chatroomListView.setAdapter(chatroomAdapter);

            //get data from db
            if (flag == 0) {
                getAroundChatroomListAndDraw(chatroomAdapter);
            } else if (flag == 1) {
                getEnteredChatroomListAndDraw(chatroomAdapter);
            }

            return chatroomListView;
        }

        private void getAroundChatroomListAndDraw(final ChatRoomAdapter chatroomAdapter) {
            try {
                ArrayList<String> userIdList = getAroundUserList();
                for (int i = 0; i < userIdList.size(); i++) {
                    databaseReference.child("users").child(userIdList.get(i)).child("chatRoomID").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final String chatroomId = dataSnapshot.getValue(String.class);
                            System.out.println("chatroomId length is " + chatroomId.length());
                            if(chatroomId.length() != 0) {
                                databaseReference.child("ChatManager").child(chatroomId).child("chatroom").addListenerForSingleValueEvent(
                                        new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                chatroomAdapter.addWithChatManagerId(dataSnapshot.getValue(Chatroom.class), chatroomId);
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        }
                                );
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            } catch(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CommunityMainActivity.this, "문제가 생겼습니다. 다시 시도해 주세요", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }

        public ArrayList<String> getAroundUserList() throws ExecutionException, InterruptedException, JSONException {
            HttpHandler httpHandler = new HttpHandler();
            String requestUrl = DataFormat.createGetAroungRequestURL(User.getMyInstance().getLat(),User.getMyInstance().getLon());
            String result = httpHandler.execute(requestUrl).get();
            JSONObject root = new JSONObject(result);

            ArrayList<String> userIdList = new ArrayList<>();

            String jsonArr = "[";
            Iterator iterator = root.keys();
            while(iterator.hasNext()) {
                String key = (String)iterator.next();
                JSONObject data = root.getJSONObject(key);
                jsonArr+=data.toString();
                jsonArr+=",";
            }
            jsonArr = jsonArr.substring(0, jsonArr.length()-1)+"]";

            JSONArray jsonArray = new JSONArray(jsonArr);
            for(int i=0 ; i<jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                userIdList.add(jsonObject.getString("userId"));
            }

            Log.i("temptemp",userIdList.toString());
            return userIdList;
        }
        private void getEnteredChatroomListAndDraw(final ChatRoomAdapter chatroomAdapter) {
            databaseReference.child("users").child(User.getMyInstance().getUserId()).child("myChatroomList").addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};
                            ArrayList<String> chatroomList = dataSnapshot.getValue(t);
                            if (chatroomList != null) {
                                for(int i=0; i<chatroomList.size(); i++) {
                                    final String value = chatroomList.get(i);
                                    databaseReference.child("ChatManager").child(value).child("chatroom").addListenerForSingleValueEvent(
                                            new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    chatroomAdapter.addWithChatManagerId(dataSnapshot.getValue(Chatroom.class), value);
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                    //한개 문제생겼을 때에도 알려줘야하나?
                                                }
                                            }
                                    );
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(CommunityMainActivity.this, "내가 들어간 방 정보를 가져올 수 없습니다. 네트워크를 확인해주세요.", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
            );
        }

        private class ChatRoomAdapter extends ArrayAdapter<Chatroom> {
            private ArrayList<Chatroom> chatList;
            private ArrayList<String> chatManagerIdList;

            public ChatRoomAdapter(Context context, int chatLayoutId, ArrayList<Chatroom> chatList) {
                super(context, chatLayoutId, chatList);
                chatManagerIdList = new ArrayList<>();
                this.chatList = chatList;
            }

            public void addWithChatManagerId(Chatroom chatroom, String chatManagerId) {
                this.add(chatroom);
                chatManagerIdList.add(chatManagerId);
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = convertView;
                Chatroom chatroom = chatList.get(position);
                String chatManagerId = chatManagerIdList.get(position);

                if (v == null)
                    v = getLayoutInflater().inflate(R.layout.layout_community_main_chatroom, null);

                return drawChatroomToList(chatroom, v, chatManagerId);
            }

            private View drawChatroomToList(Chatroom chatroom, View chatroomView, String chatManagerId) {
                //방마다 구별하기 위해 채팅룸 id를 구별자로 추가.
                chatroomView.setTag(chatManagerId);
                chatroomView.setOnClickListener(CommunityMainActivity.this);

                //set chatroom writer profile
                final ImageView profilePicView = (ImageView)chatroomView.findViewById(R.id.community_main_chatroom_writerProfilePic);

                Picasso.with(CommunityMainActivity.this).load(chatroom.getChatroomWriterProfilePic()).into(new Target() {

                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        profilePicView.setImageDrawable(new RoundedAvatarDrawable(bitmap, bitmap.getWidth(), bitmap.getWidth()));
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });

                //set chatroom writer name
                TextView chatroomWriterName = (TextView)chatroomView.findViewById(R.id.community_main_chatroom_writerName);
                if(chatManagerId.equals(User.getMyInstance().getChatRoomID()))
                    chatroomWriterName.setText("나");
                else
                    chatroomWriterName.setText(chatroom.getChatroomWriterName());

                //set chatroom writer nationality
                ImageView chatroomNationality = (ImageView)chatroomView.findViewById(R.id.community_main_chatroom_nationality);
                //chatroom.getChatroomWriterNationality()
                chatroomNationality.setBackgroundResource(R.drawable.googlelogo);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(chatroomWriterName.getWidth(), chatroomWriterName.getHeight());
                chatroomNationality.setLayoutParams(params);
                //Picasso.with(CommunityMainActivity.this).load("googlelogo.png").into(chatroomNationality);

                //set chatroom number
                TextView chatroomNumber = (TextView)chatroomView.findViewById(R.id.community_main_chatroom_number);
                if(chatroom.getUserList() != null)
                    chatroomNumber.setText(chatroom.getUserList().size() + "/" + chatroom.getChatroomMaxNumber());
                else
                    chatroomNumber.setText(0 + "/" + chatroom.getChatroomMaxNumber());

                //set chatroom date
                TextView chatroomDate = (TextView)chatroomView.findViewById(R.id.community_main_chatroom_date);
                Date date = chatroom.getChatroomDate();
                SimpleDateFormat format;
                if(date.getHours() > 12)
                    format = new SimpleDateFormat("오후 hh:mm");
                else
                    format = new SimpleDateFormat("오전 hh:mm");
                chatroomDate.setText(format.format(date));

                //set chatroom title
                TextView chatroomTitle = (TextView)chatroomView.findViewById(R.id.community_main_chatroom_title);
                chatroomTitle.setText(chatroom.getChatroomTitle());

                if(chatroom.isEnd()) {
                    profilePicView.setAlpha(25);
                    chatroomWriterName.setAlpha((float)0.1);
                    chatroomNationality.setAlpha(25);
                    chatroomNumber.setAlpha((float)0.1);
                    chatroomDate.setAlpha((float)0.1);
                    chatroomTitle.setAlpha((float)0.1);
                }
                return chatroomView;
            }
        }
    }
}