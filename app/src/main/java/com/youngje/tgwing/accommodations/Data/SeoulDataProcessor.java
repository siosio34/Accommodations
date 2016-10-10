package com.youngje.tgwing.accommodations.Data;

import android.location.Location;

import com.youngje.tgwing.accommodations.Marker;
import com.youngje.tgwing.accommodations.SeoulMarker;
import com.youngje.tgwing.accommodations.Util.LocationUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joyeongje on 2016. 10. 8..
 */

public class SeoulDataProcessor implements DataProcessor {

    //와이파이네임

    @Override
    public List<Marker> load(String rawData, DataFormat.DATATYPE datatype) throws JSONException {

        List<Marker> markers = new ArrayList<Marker>();
        String type = datatype.getValue();

        if(type.equals("WIFI"))
            markers = processSeoulWIFIObject();

        else if(type.equals("TOILET"))
            markers = processSeoulToiletObject();

        return markers;
    }

    public List<Marker> processSeoulToiletObject()  {

        List<Marker> markersList = new ArrayList<Marker>();
        Marker marker = null;

        Location curloc = LocationUtil.curlocation;

        // TODO: 2016. 10. 10. db 쿼리문 던져야됨

        //marker = new SeoulMarker();
        // TODO: 2016. 10. 10. 경도, 위도, 아이디 ,이름


        return markersList;
    }

    public List<Marker> processSeoulWIFIObject() {

        List<Marker> markersList = new ArrayList<Marker>();
        Marker marker = null;

        Location curloc = LocationUtil.curlocation;

        //marker = new SeoulMarker();

        // TODO: 2016. 10. 10. db 쿼리문 던져야됨
        // TODO: 2016. 10. 10. 경도, 위도, 아이디 ,이름

        return markersList;
    }







    // TODO: 2016. 10. 10. 앤 디비에서 불러와야된다

    // 아아아 시이바




}
