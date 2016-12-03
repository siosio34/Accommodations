package com.youngje.tgwing.accommodations;

import static android.R.attr.category;

/**
 * Created by joyeongje on 2016. 10. 5..
 */

public class SeoulMarker extends Marker{

    private String seoulDataType;

    SeoulMarker() {};

    public SeoulMarker(String id, double lat, double lon,String title, String imageUrl, String markerType, double distance,String seoulDataType) {
        super(id, lat, lon,title, imageUrl, markerType, distance);
        this.seoulDataType = seoulDataType;

      // this.phoneNum = phoneNum;
      // this.address = address;
      //  this.newAddress = newAddress;
      // this.category = category;
    }


    public String getSeoulDataType() {
        return seoulDataType;
    }

    public void setSeoulDataType(String seoulDataType) {
        this.seoulDataType = seoulDataType;
    }
}
