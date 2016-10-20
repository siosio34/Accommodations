package com.youngje.tgwing.accommodations;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Mansu on 2016-10-08.
 */
public class Chatroom {
    private String chatroomTitle;
    private String chatroomWriterProfilePic;
    private String chatroomWriterName;
    private String chatroomWriterNationality;
    private int chatroomMaxNumber;
    private Date chatroomDate;
    private boolean isEnd;
    private ArrayList<User> userList;

    public Chatroom() {}

    public Chatroom(String chatroomTitle, String chatroomWriterProfilePic, String chatroomWriterName, String chatroomWriterNationality, int chatroomMaxNumber, Date chatroomDate, boolean isEnd, ArrayList<User> userList) {
        this.chatroomTitle = chatroomTitle;
        this.chatroomWriterProfilePic = chatroomWriterProfilePic;
        this.chatroomWriterName = chatroomWriterName;
        this.chatroomWriterNationality = chatroomWriterNationality;
        this.chatroomMaxNumber = chatroomMaxNumber;
        this.chatroomDate = chatroomDate;
        this.isEnd = isEnd;
        this.userList = userList;
    }

    public String getChatroomTitle() {
        return chatroomTitle;
    }

    public void setChatroomTitle(String chatroomTitle) {
        this.chatroomTitle = chatroomTitle;
    }

    public String getChatroomWriterProfilePic() {
        return chatroomWriterProfilePic;
    }

    public void setChatroomWriterProfilePic(String chatroomWriterProfilePic) {
        this.chatroomWriterProfilePic = chatroomWriterProfilePic;
    }

    public String getChatroomWriterName() {
        return chatroomWriterName;
    }

    public void setChatroomWriterName(String chatroomWriterName) {
        this.chatroomWriterName = chatroomWriterName;
    }

    public String getChatroomWriterNationality() {
        return chatroomWriterNationality;
    }

    public void setChatroomWriterNationality(String chatroomWriterNationality) {
        this.chatroomWriterNationality = chatroomWriterNationality;
    }

    public int getChatroomMaxNumber() {
        return chatroomMaxNumber;
    }

    public void setChatroomMaxNumber(int chatroomMaxNumber) {
        this.chatroomMaxNumber = chatroomMaxNumber;
    }

    public Date getChatroomDate() {
        return chatroomDate;
    }

    public void setChatroomDate(Date chatroomDate) {
        this.chatroomDate = chatroomDate;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

    public ArrayList<User> getUserList() {
        return userList;
    }

    public void setUserList(ArrayList< User> userList) {
        this.userList = userList;
    }
}