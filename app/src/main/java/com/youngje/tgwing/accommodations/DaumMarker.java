package com.youngje.tgwing.accommodations;

/**
 * Created by joyeongje on 2016. 10. 5..
 */

public class DaumMarker extends Marker {


    private String phoneNum;
    private String address;
    private String newAddress;
    private String category;

    DaumMarker() {};


    public DaumMarker(String id, double lat, double lon,String title, String imageUrl, String markerURL, double distance,String phoneNum,String address,String newAddress,String category) {
        super(id, lat, lon,title, imageUrl, markerURL, distance);

        this.phoneNum = phoneNum;
        this.address = address;
        this.newAddress = newAddress;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
