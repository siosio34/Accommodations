package com.youngje.tgwing.accommodations;

import com.youngje.tgwing.accommodations.NaviXY;

import java.util.List;

/**
 * Created by joyeongje on 2016. 10. 18..
 */

public class Navi {

    private List<String> guideList;
    private int length;
    private int time;
    private List<NaviXY> listXY;

    public static boolean NaviFlag = false;

    Navi(){}

    public Navi(List<String> guideList,int length,int time,List<NaviXY> listXY) {

        this.guideList = guideList;
        this.length = length;
        this.time = time;
        this.listXY = listXY;
    }


    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public List<NaviXY> getListXY() {
        return listXY;
    }

    public void setListXY(List<NaviXY> listXY) {
        this.listXY = listXY;
    }

    public List<String> getGuideList() {
        return guideList;
    }

    public void setGuideList(List<String> guideList) {
        this.guideList = guideList;
    }
}