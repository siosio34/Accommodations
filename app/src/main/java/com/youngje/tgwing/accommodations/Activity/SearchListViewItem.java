package com.youngje.tgwing.accommodations.Activity;

import android.graphics.drawable.Drawable;

/**
 * Created by aswoo on 2016-10-15.
 */

public class SearchListViewItem {
    private String titleStr;
    private String categoryStr;
    private String ReviewNumStr;
    private float ratingStar;
    private String distanceStr;
    private float numOfStar;

    public void setNumOfStar(float numOfStar){this.numOfStar = numOfStar;}
    public void setTitle(String title) { titleStr = title ; }
    public void setCateGory(String kateGory){ categoryStr = kateGory; }
    public void setReviewNum(String ReviewNum) {ReviewNumStr = ReviewNum;}
    public void setRatingStar(float ratingStar) {this.ratingStar = ratingStar;}
    public void setDistance(String distance) {distanceStr = distance;}

    public float getNumOfStar(){return this.numOfStar;}
    public String getTitle() { return this.titleStr ; }
    public String getCategory(){ return this.categoryStr; }
    public String getReviewNum(){return this.ReviewNumStr;}
    public float getRatingStar(){return ratingStar;}
    public String getDistance() {return distanceStr;}
}
