package com.youngje.tgwing.accommodations.Activity;

import android.graphics.Bitmap;

/**
 * Created by SEGU on 2016-10-31.
 */

public class MyReviewItem {

    public MyReviewItem(){}
    public MyReviewItem(String titleStr, float ratingStar, String reviewDateStr, String reviewContentStr, float numOfStar, Bitmap reviewImage){
        setTitleStr(titleStr);
        setRatingStar(ratingStar);
        setReviewDateStr(reviewDateStr);
        setReviewContentStr(reviewContentStr);
        setNumOfStar(numOfStar);
        setReviewImage(reviewImage);
    }

    public String getTitleStr() {
        return titleStr;
    }

    public void setTitleStr(String titleStr) {
        this.titleStr = titleStr;
    }

    public float getRatingStar() {
        return ratingStar;
    }

    public void setRatingStar(float ratingStar) {
        this.ratingStar = ratingStar;
    }

    public String getReviewDateStr() {
        return reviewDateStr;
    }

    public void setReviewDateStr(String reviewDateStr) {
        this.reviewDateStr = reviewDateStr;
    }

    public String getReviewContentStr() {
        return reviewContentStr;
    }

    public void setReviewContentStr(String reviewContentStr) {
        this.reviewContentStr = reviewContentStr;
    }

    public float getNumOfStar() {
        return numOfStar;
    }

    public void setNumOfStar(float numOfStar) {
        this.numOfStar = numOfStar;
    }

    private String titleStr;
    private float ratingStar;
    private String reviewDateStr;
    private String reviewContentStr;
    private float numOfStar;

    public Bitmap getReviewImage() {
        return reviewImage;
    }

    public void setReviewImage(Bitmap reviewImage) {
        this.reviewImage = reviewImage;
    }

    private Bitmap reviewImage;

}
