package com.youngje.tgwing.accommodations;

import com.youngje.tgwing.accommodations.Data.DataFormat;

/**
 * Created by joyeongje on 2016. 9. 26..
 */
public class Marker {

    public static Marker selectedMarker = null;

    private String id;
    private String lat;
    private String lon;
    private String title;
    private String category;
    private String phoneNum;
    private String address;
    private String newAddress;
    private String imageUrl;
    private String placeUrl;
    private String distance; // 중심 좌표로 부터 거리

    public Marker() {

    }

    public Marker(String id, String lat, String lon, String title, String category, String phoneNum, String address,
                  String newAddress, String imageUrl, String placeUrl, String distance) {

        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.title = title;
        this.category = category;
        this.phoneNum = phoneNum;
        this.address = address;
        this.newAddress = newAddress;
        this.imageUrl = imageUrl;
        this.placeUrl = placeUrl;
        this.distance = distance;
    }


    public static Marker getSelectedMarker() {
        return selectedMarker;
    }

    public static void setSelectedMarker(Marker selectedMarker) {
        Marker.selectedMarker = selectedMarker;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNewAddress() {
        return newAddress;
    }

    public void setNewAddress(String newAddress) {
        this.newAddress = newAddress;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPlaceUrl() {
        return placeUrl;
    }

    public void setPlaceUrl(String placeUrl) {
        this.placeUrl = placeUrl;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
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


}
