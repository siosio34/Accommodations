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
    private String imageUri;

    public static User currentUser = new User();

    public static User getInstance() {
        return currentUser;
    }

    public User() {}
    public User(String userId,String userName,String userEmail,String userDescription,String imageUri) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userDescription = userDescription;
        this.imageUri = imageUri;
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


}