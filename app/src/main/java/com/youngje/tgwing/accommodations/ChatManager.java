package com.youngje.tgwing.accommodations;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by joyeongje on 2016. 11. 28..
 */


public class ChatManager {
    Chatroom chatroom;
    HashMap<String, Chat> chatList;

    public ChatManager() {}

    public ChatManager(Chatroom chatroom, HashMap<String, Chat> chatList) {
        this.chatroom = chatroom;
        this.chatList = chatList;
    }

    public void setChatroom(Chatroom chatroom) {
        this.chatroom = chatroom;
    }

    public Chatroom getChatroom() {
        return chatroom;
    }
}