
package com.youngje.tgwing.accommodations;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Mansu on 2016-10-08.
 */
public class Chatroom {
    private String userId; // user id
    private String chatroomId; // 채팅방 id
    private String chatroomTitle; // 채팅방 이름
    private String chatroomWriterProfilePic;
    private String chatroomWriterName;
    private String chatroomWriterNationality;
    private int chatroomNumber;
    private int chatroomMaxNumbr;
    private Date chatroomDate;
    private boolean isEnd;

    public Chatroom(String userId,String chatroomId, String chatroomTitle, String chatroomWriterProfilePic, String chatroomWriterName, String chatroomWriterNationality, int chatroomNumber, int chatroomMaxNumber, Date chatroomDate, boolean isEnd) {
        this.userId = userId;
        this.chatroomId = chatroomId;
        this.chatroomTitle = chatroomTitle;
        this.chatroomWriterProfilePic = chatroomWriterProfilePic;
        this.chatroomWriterName = chatroomWriterName;
        this.chatroomWriterNationality = chatroomWriterNationality;
        this.chatroomNumber = chatroomNumber;
        this.chatroomMaxNumbr = chatroomMaxNumber;
        this.chatroomDate = chatroomDate;
        this.isEnd = isEnd;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getChatroomId() {
        return chatroomId;
    }

    public void setChatroomId(String chatroomId) {
        this.chatroomId = chatroomId;
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

    public int getChatroomNumber() {
        return chatroomNumber;
    }

    public void setChatroomNumber(int chatroomNumber) {
        this.chatroomNumber = chatroomNumber;
    }

    public int getChatroomMaxNumbr() {
        return chatroomMaxNumbr;
    }

    public void setChatroomMaxNumbr(int chatroomMaxNumbr) {
        this.chatroomMaxNumbr = chatroomMaxNumbr;
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
}
