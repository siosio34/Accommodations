package com.youngje.tgwing.accommodations;

import java.util.Date;
import java.util.List;

/**
 * Created by joyeongje on 2016. 10. 4..
 */

public class ReviewPost {

    private String markerId;
    private String userId;
    private String title;
    private String content;
    private int commentNum; // 댓글 수
    private int contentType; // 글, 사진,동영상
    private String reviewUrl; // 리뷰와 연관된 사진 동영상 링크
    private Date CreateDate; // 글 쓴 시간
    private int starLate; // 별점

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public String getReviewUrl() {
        return reviewUrl;
    }

    public void setReviewUrl(String reviewUrl) {
        this.reviewUrl = reviewUrl;
    }

    public Date getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(Date createDate) {
        CreateDate = createDate;
    }


    public int getStarLate() {
        return starLate;
    }

    public void setStarLate(int starLate) {
        this.starLate = starLate;
    }
}
