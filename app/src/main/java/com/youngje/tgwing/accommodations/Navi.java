package com.youngje.tgwing.accommodations;

import com.youngje.tgwing.accommodations.NaviXY;

import java.util.List;

/**
 * Created by joyeongje on 2016. 10. 18..
 */

public class Navi {

    private String guideMent;
    private int length;
    private int time;
    private List<NaviXY> listXY;

    public static boolean NaviFlag = false;

    Navi(){}

    public Navi(String guideMent,int length,int time,List<NaviXY> listXY) {

        this.guideMent = guideMent;
        this.length = length;
        this.time = time;
        this.listXY = listXY;
    }


    public String getGuideMent() {
        return guideMent;
    }

    public void setGuideMent(String guideMent) {
        this.guideMent = guideMent;
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
}