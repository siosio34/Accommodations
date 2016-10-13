package com.youngje.tgwing.accommodations;

/**
 * Created by joyeongje on 2016. 9. 26..
 */
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class User {


    private String userId;
    private String userName; // 유저 이름
    private String userEmail; // 유저 아이디
    private String userDescription; // 유저 소개
    private String imageUri; // 이미지 링크
    private String country; // 국가

    //////////////////////////////////////////////
    private String chatRoomID;
    private Double lat;
    private Double lon;

    private ArrayList<String> myChatRoomList;

    private static User currentUser = new User();

    public static User getMyInstance() {
        return currentUser;
    }
    public static void setMyInstance(User user) { currentUser = user;}

    public User() {}

    public User(String userId,String userName,String userEmail,String userDescription,String imageUri,String country,String chatRoomID,Double lat,Double lon,ArrayList<String> myChatRoomList) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userDescription = userDescription;
        this.imageUri = imageUri;
        this.country= country;
        this.chatRoomID = chatRoomID;
        this.lat = lat;
        this.lon = lon;
        this.myChatRoomList =myChatRoomList;

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getChatRoomID() {
        return chatRoomID;
    }

    public void setChatRoomID(String chatRoomID) {
        this.chatRoomID = chatRoomID;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public ArrayList<String> getMyChatRoomList() {
        return myChatRoomList;
    }

    public void setMyChatRoomList(ArrayList<String> myChatRoomList) {
        this.myChatRoomList = myChatRoomList;
    }
}