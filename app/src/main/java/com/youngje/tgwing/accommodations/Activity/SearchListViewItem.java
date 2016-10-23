package com.youngje.tgwing.accommodations.Activity;

import android.graphics.drawable.Drawable;

/**
 * Created by aswoo on 2016-10-15.
 */

public class SearchListViewItem {
    private Drawable iconDrawable ;
    private String titleStr;
    private String kateGoryStr;
    private String ReviewNumStr;
    private String descStr;
    private float ratingStar;
    private String distanceStr;
    private float numOfStar;

    public void setNumOfStar(float numOfStar){this.numOfStar = numOfStar;}
    public void setIcon(Drawable icon) { iconDrawable = icon ; }
    public void setTitle(String title) { titleStr = title ; }
    public void setDesc(String desc) { descStr = desc ; }
    public void setKateGory(String kateGory){ kateGoryStr = kateGory; }
    public void setReviewNum(String ReviewNum) {ReviewNumStr = ReviewNum;}
    public void setRatingStar(float ratingStar) {this.ratingStar = ratingStar;}
    public void setDistance(String distance) {distanceStr = distance;}

    public float getNumOfStar(){return this.numOfStar;}
    public Drawable getIcon() {
        return this.iconDrawable ;
    }
    public String getTitle() { return this.titleStr ; }
    public String getDesc() { return this.descStr ; }
    public String getKateGory(){ return this.kateGoryStr ; }
    public String getReviewNum(){return this.ReviewNumStr;}
    public float getRatingStar(){return ratingStar;}
    public String getDistance() {return distanceStr;}
}
