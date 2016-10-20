package com.youngje.tgwing.accommodations;
import java.util.Date;
/**
 * Created by Mansu on 2016-10-08.
 */
public class Chat {
    private String userId;
    private String chatWriterProfilePic;
    private String chatWriterName;
    private String chatWriterNationality;
    private Date chatDate;
    private String chatContent;

    public Chat() {}

    public Chat(String userId, String chatWriterProfilePic, String chatWriterName, String chatWriterNationality, Date chatDate, String chatContent) {
        this.userId = userId;
        this.chatWriterProfilePic = chatWriterProfilePic;
        this.chatWriterName = chatWriterName;
        this.chatWriterNationality = chatWriterNationality;
        this.chatDate = chatDate;
        this.chatContent = chatContent;
    }

    public String getChatWriterProfilePic() {
        return chatWriterProfilePic;
    }

    public void setChatWriterProfilePic(String chatWriterProfilePic) {
        this.chatWriterProfilePic = chatWriterProfilePic;
    }

    public String getChatWriterName() {
        return chatWriterName;
    }

    public void setChatWriterName(String chatWriterName) {
        this.chatWriterName = chatWriterName;
    }

    public String getChatWriterNationality() {
        return chatWriterNationality;
    }

    public void setChatWriterNationality(String chatWriterNationality) {
        this.chatWriterNationality = chatWriterNationality;
    }

    public Date getChatDate() {
        return chatDate;
    }

    public void setChatDate(Date chatDate) {
        this.chatDate = chatDate;
    }

    public String getChatContent() {
        return chatContent;
    }

    public void setChatContent(String chatContent) {
        this.chatContent = chatContent;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}