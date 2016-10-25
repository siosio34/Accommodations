package com.youngje.tgwing.accommodations;

/**
 * Created by joyeongje on 2016. 10. 24..
 */

public class NaviXY {
    private double lat;
    private double lon;

    NaviXY(){};

    public NaviXY (double lat,double lon) {
        this.lat = lat;
        this.lon = lon;
    }


    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }


}
