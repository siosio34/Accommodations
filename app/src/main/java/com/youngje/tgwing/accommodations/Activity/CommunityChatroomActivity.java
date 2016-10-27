package com.youngje.tgwing.accommodations.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.youngje.tgwing.accommodations.Chat;
import com.youngje.tgwing.accommodations.Chatroom;
import com.youngje.tgwing.accommodations.R;
import com.youngje.tgwing.accommodations.Util.RoundedAvatarDrawable;
import com.youngje.tgwing.accommodations.User;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mansu on 2016-10-08.
 */
public class CommunityChatroomActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private Chatroom chatroom;
    private String chatManagerId;
    private ArrayList<Chat> chatList;
    private ChatAdapter chatAdapter;
    private User user;
    private DrawerLayout dlDrawer;
    private ActionBarDrawerToggle dtToggle;

    private ArrayList<User> userList;
    private UserListAdapter userListAdapter;
    private int userNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_chatroom);

        //firebase에서 채팅룸의 데이터를 가져오기 위한 구별자.
        chatManagerId = getIntent().getStringExtra("chatManagerId");
        setChattingKeyboard();
        getChatroomInfo();
        setNavigationBar();
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.community_chatroom_sendBtn:
                EditText chatContent = (EditText)findViewById(R.id.community_chatroom_content);
                String content = chatContent.getText().toString();
                Chat chat = new Chat(user.getUserId(), user.getImageUri(), user.getUserName(), user.getCountry(), new Date(), content);
                chatContent.setText("");
                databaseReference.child("ChatManager").child(chatManagerId).child("chatList").push().setValue(chat);
                break;
            case R.id.community_chatroom_exit:
                System.out.println("exit!!");
                databaseReference.child("users").child(User.getMyInstance().getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};
                        ArrayList<String> list = dataSnapshot.child("myChatroomList").getValue(t);
                        for(int i=0; i < list.size(); i++){
                            String chatroomId = list.get(i);
                            if(chatroomId.compareTo(chatManagerId) == 0) {
                                list.remove(i);
                                break;
                            }
                            //something here
                        }
                        dataSnapshot.child("myChatroomList").getRef().setValue(list);
                        User.getMyInstance().setMyChatRoomList(list);
                        databaseReference.child("currentUser").child(User.getMyInstance().getUserId()).setValue(User.getMyInstance());

                        if(dataSnapshot.child("chatRoomID").getValue(String.class).equals(chatManagerId)) {
                            dataSnapshot.child("chatRoomID").getRef().setValue("", new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference dr) {
                                    if (databaseError == null) {
                                        databaseReference.child("ChatManager").child(chatManagerId).setValue(null, new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(DatabaseError databaseError, DatabaseReference dr) {
                                                if (databaseError == null) {
                                                    User.getMyInstance().setChatRoomID("");
                                                    databaseReference.child("currentUser").child(User.getMyInstance().getUserId()).setValue(User.getMyInstance());
                                                    finish();
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                        else
                            finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(CommunityChatroomActivity.this, "나가기에 실패하셨습니다. 네트워크를 확인해주세요.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                break;
            case R.id.community_chatroom_end:
                databaseReference.child("users").child(User.getMyInstance().getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child("chatRoomID").getValue(String.class).equals(chatManagerId)) {
                            databaseReference.child("ChatManager").child(chatManagerId).child("chatroom").child("end").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    boolean isEnd = dataSnapshot.getValue(boolean.class);
                                    if(isEnd == true) {
                                        dataSnapshot.getRef().setValue(false);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(CommunityChatroomActivity.this, "마감해제하였습니다.", Toast.LENGTH_LONG).show();
                                            }
                                        });

                                    }
                                    else {
                                        dataSnapshot.getRef().setValue(true);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(CommunityChatroomActivity.this, "마감하였습니다.", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(CommunityChatroomActivity.this, "마감하기에 실패하셨습니다. 네트워크를 확인해주세요.", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(CommunityChatroomActivity.this, "마감하기에 실패하셨습니다. 네트워크를 확인해주세요.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                break;
        }
    }

    private void setChattingKeyboard() {
        findViewById(R.id.community_chatroom_content_box).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                findViewById(R.id.community_chatroom_content).requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(findViewById(R.id.community_chatroom_content), InputMethodManager.SHOW_IMPLICIT);
                return false;
            }
        });
        findViewById(R.id.community_chatroom_content).setFocusableInTouchMode(true);
    }

    private void setNavigationBar() {
        dlDrawer = (DrawerLayout)findViewById(R.id.community_chatroom_drawer);
        dlDrawer.findViewById(R.id.community_chatroom_exit).setOnClickListener(this);
        dlDrawer.findViewById(R.id.community_chatroom_end).setOnClickListener(this);

        dtToggle = new ActionBarDrawerToggle(this, dlDrawer, R.string.open_drawer, R.string.close_drawer){

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        dlDrawer.setDrawerListener(dtToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        dtToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(dtToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        dtToggle.onConfigurationChanged(newConfig);
    }

    //채팅룸의 정보를 가지고온다.
    public void getChatroomInfo() {
        System.out.println(chatManagerId);
        databaseReference.child("ChatManager").child(chatManagerId).child("chatroom").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.

                        chatroom = dataSnapshot.getValue(Chatroom.class);
                        setTitle(chatroom.getChatroomTitle());

                        if (chatroom == null) {
                            //방이 삭제됨
                            setResult(9998);
                            finish();
                        } else
                            addChatroomInfoToMyChatroomList();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        //네트워크 에러
                        setResult(9999);
                        finish();
                    }
                }
        );
    }

    public void addChatroomInfoToMyChatroomList() {
        databaseReference.child("users").child(User.getMyInstance().getUserId()).child("myChatroomList").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean isExists = false;
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            if(chatManagerId.compareTo((String)postSnapshot.getValue()) == 0)
                                isExists = true;
                        }

                        if(isExists) {
                            createChatAdapter();
                            registerFirebaseChatListener();
                            createUserListAdapter();
                            registerFirebaseUserListListener();
                            setSendBtnEvent();
                        }
                        else {
                            databaseReference.child("users").child(User.getMyInstance().getUserId()).child("myChatroomList").runTransaction(new Transaction.Handler() {
                                @Override
                                public Transaction.Result doTransaction(MutableData mutableData) {
                                    int count = (int)mutableData.getChildrenCount();
                                    mutableData.child(Integer.toString(count)).setValue(chatManagerId);
                                    return Transaction.success(mutableData);
                                }

                                @Override
                                public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                                    if(databaseError == null) {
                                        createChatAdapter();
                                        setSendBtnEvent();
                                        registerFirebaseChatListener();
                                    }
                                    else {
                                        //네트워크 에러
                                        setResult(1);
                                        finish();
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }

    //채팅을 실시간으로 갱신하기위한 adapter 생성
    public void createChatAdapter() {
        chatList = new ArrayList<>();
        ListView chatListView = (ListView)findViewById(R.id.community_chatroom_chatList);
        chatListView.setDivider(null);
        chatAdapter = new ChatAdapter(this, R.layout.layout_community_chatroom_chat, chatList);
        chatListView.setAdapter(chatAdapter);
    }

    //버튼을 누를 시 채팅 데이터를 파이어베이스로 보냄.
    public void setSendBtnEvent() {
        user = User.getMyInstance();
        Button sendBtn = (Button)findViewById(R.id.community_chatroom_sendBtn);
        sendBtn.setOnClickListener(this);
    }

    //파이어베이스에 채팅값이 갱신될 경우 실시간으로 가져옴.
    public void registerFirebaseChatListener() {
        databaseReference.child("ChatManager").child(chatManagerId).child("chatList").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Chat chatData = dataSnapshot.getValue(Chat.class);  // chatData를 가져오고
                chatAdapter.add(chatData);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //유저리스트를 실시간으로 갱신하기위한 adapter 생성
    public void createUserListAdapter() {
        userList = new ArrayList<>();
        ListView chatListView = (ListView)findViewById(R.id.community_chatroom_userList);
        chatListView.setDivider(null);
        userListAdapter = new UserListAdapter(this, R.layout.layout_community_chatroom_userlist, userList);
        chatListView.setAdapter(userListAdapter);
    }

    //파이어베이스에 유저가 갱신될 경우 실시간으로 가져옴.
    public void registerFirebaseUserListListener() {
        databaseReference.child("ChatManager").child(chatManagerId).child("chatroom").child("userList").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);  // chatData를 가져오고
                userListAdapter.add(user);
                userNumber++;
                ((TextView)findViewById(R.id.community_chatroom_number)).setText(userNumber+"명");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);  // chatData를 삭제
                userListAdapter.remove(user);
                userNumber--;
                ((TextView)findViewById(R.id.community_chatroom_number)).setText(userNumber+"명");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    //채팅을 실시간으로 그려주기 위한 custom adapter
    private class ChatAdapter extends ArrayAdapter<Chat> {
        private ArrayList<Chat> chatList;

        public ChatAdapter(Context context, int chatLayoutId, ArrayList<Chat> chatList) {
            super(context, chatLayoutId, chatList);
            this.chatList = chatList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;

            Chat chat = chatList.get(position);
            if(chat.getUserId().compareTo(User.getMyInstance().getUserId()) == 0) {
                v = getLayoutInflater().inflate(R.layout.layout_community_chatroom_mychat, null);
                //chat 내용 설정
                TextView contentView = (TextView)v.findViewById(R.id.community_chatroom_chat_content);
                contentView.setText(chat.getChatContent());

                //chat 시간 설정
                TextView dateView = (TextView)v.findViewById(R.id.community_chatroom_chat_date);
                Date date = chat.getChatDate();
                SimpleDateFormat format;
                if(date.getHours() > 12)
                    format = new SimpleDateFormat("오후 hh:mm");
                else
                    format = new SimpleDateFormat("오전 hh:mm");
                dateView.setText(format.format(date));
            }
            else {
                v = getLayoutInflater().inflate(R.layout.layout_community_chatroom_chat, null);
                //chat 작성자 프로파일 이미지 설정
                final ImageView profilePicView = (ImageView)v.findViewById(R.id.community_chatroom_chat_profilePic);
                Picasso.with(CommunityChatroomActivity.this).load(Uri.parse(chat.getChatWriterProfilePic())).into(new Target() {
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

                //chat 작성자 이름 설정
                TextView nameView = (TextView)v.findViewById(R.id.community_chatroom_chat_name);
                nameView.setText(chat.getChatWriterName());

                //chat 내용 설정
                TextView contentView = (TextView)v.findViewById(R.id.community_chatroom_chat_content);
                contentView.setText(chat.getChatContent());

                //chat 시간 설정
                TextView dateView = (TextView)v.findViewById(R.id.community_chatroom_chat_date);
                Date date = chat.getChatDate();
                SimpleDateFormat format;
                if(date.getHours() > 12)
                    format = new SimpleDateFormat("오후 hh:mm");
                else
                    format = new SimpleDateFormat("오전 hh:mm");
                dateView.setText(format.format(date));
            }

            return v;
        }
    }

    //채팅을 실시간으로 그려주기 위한 custom adapter
    private class UserListAdapter extends ArrayAdapter<User> {
        private ArrayList<User> userList;

        public UserListAdapter(Context context, int chatLayoutId, ArrayList<User> userList) {
            super(context, chatLayoutId, userList);
            this.userList = userList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if(v == null)
                v = getLayoutInflater().inflate(R.layout.layout_community_chatroom_userlist, null);

            User user = userList.get(position);

            //chat 작성자 프로파일 이미지 설정
            final ImageView profilePicView = (ImageView)v.findViewById(R.id.community_chatroom_userlist_profilePic);
            Picasso.with(CommunityChatroomActivity.this).load(Uri.parse(user.getImageUri())).into(new Target() {
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

            //chat 작성자 이름 설정
            TextView nameView = (TextView)v.findViewById(R.id.community_chatroom_userlist_name);
            nameView.setText(user.getUserName());

            //국가 설정
            ImageView chatroomNationality = (ImageView)v.findViewById(R.id.community_chatroom_userlist_nationality);
            chatroomNationality.setBackgroundResource(R.drawable.googlelogo);

            return v;
        }
    }
}