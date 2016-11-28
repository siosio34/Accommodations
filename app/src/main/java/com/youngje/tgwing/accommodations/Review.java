package com.youngje.tgwing.accommodations;

import java.util.Date;

/**
 * Created by joyeongje on 2016. 10. 4..
 */

public class Review {

    private String markerId; // == locationId
    private String userId; // 유저아이디
    private String userName; // 유저이름
    private String userImageUrl; // 유저 이미지 링크.
    private String content; // 리뷰 냉ㅅㅇ
    private String reviewContentUrl; // 리뷰와 연관된 사진 동영상 링크
    private int likeNUM;
    private int contentType; // 글, 사진,동영상
    private Date createDate;
    private int star; // 별점

    public Review(){}

    public Review(String markerId,String userId,String userName,String userImageUrl,String content,
                  String reviewContentUrl,int contentType,int star) {

        this.markerId = markerId;
        this.userId = userId;
        this.userName = userName;
        this.userImageUrl = userImageUrl;
        this.content = content;
        this.reviewContentUrl = reviewContentUrl;
       // this.createDate = createDate;
        this.contentType = contentType;
        this.star = star;
    }

    public String getMarkerId() {
        return markerId;
    }

    public void setMarkerId(String markerId) {
        this.markerId = markerId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public String getreviewContentUrl() {
        return reviewContentUrl;
    }

    public void setreviewContentUrl(String reviewContentUrl) {
        this.reviewContentUrl = reviewContentUrl;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int starLate) {
        this.star = starLate;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getLikeNUM() {
        return likeNUM;
    }

    public void setLikeNUM(int likeNUM) {
        this.likeNUM = likeNUM;
    }

     public Date getCreateDate() {
        return createDate;
    }

   public void setCreateDate(Date createDate) {
      this.createDate = createDate;
   }
}
