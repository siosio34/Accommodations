package com.youngje.tgwing.accommodations;

import android.location.Location;

import com.youngje.tgwing.accommodations.Util.LocationUtil;

import java.util.List;


/**
 * Created by joyeongje on 2016. 9. 26..
 */
abstract public class Marker implements Comparable<Marker> {

    public static Marker selectedMarker = null;

    private String id;
    private double lat;
    private double lon;
    private String title;
    private String imageUrl;
    private String markerURL;
    private Double distance;

    public Marker() {

    }

    public Marker(String id, double lat, double lon,String title,String markerURL,String imageUrl,double distance) {

        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.title = title;
        this.markerURL = markerURL;
        this.imageUrl = imageUrl;
        this.distance = distance;
    }

    public static Marker getSelectedMarker() {
        return selectedMarker;
    }

    public static void setSelectedMarker(Marker selectedMarker) {
        Marker.selectedMarker = selectedMarker;
    }

    @Override
    public int compareTo(Marker another) {

        Marker leftPm = this;
        Marker rightPm = another;

        return Double.compare(leftPm.getDistance(), rightPm.getDistance());

    }

    // 거리를 리턴
    public double getDistance() {
        return distance;
    }

    public void setDistance() {
        this.distance = distance;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMarkerURL() {
        return markerURL;
    }

    public void setMarkerURL(String markerURL) {
        this.markerURL = markerURL;
    }

    public Double calDistance(Location markerLoc) {

        return 0.0;

    }
}
