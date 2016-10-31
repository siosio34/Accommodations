package com.youngje.tgwing.accommodations.Data;

import android.location.Location;

import com.youngje.tgwing.accommodations.Marker;
import com.youngje.tgwing.accommodations.SeoulMarker;
import com.youngje.tgwing.accommodations.Util.LocationUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by joyeongje on 2016. 10. 8..
 */

public class SeoulDataProcessor implements DataProcessor {

    public static final int MAX_JSON_OBJECTS = 50;
    //와이파이네임

    @Override
    public List<Marker> load(String rawData, DataFormat.DATATYPE datatype) throws JSONException {

        List<Marker> markers = new ArrayList<Marker>();
        JSONObject root = convertToJSON(rawData);

        Marker ma;
        String dataType = datatype.toString();

        if(dataType.equals("WIFI")) {

            Iterator iterator = root.keys();
            while(iterator.hasNext()) {
                String key = (String)iterator.next();
                JSONObject jo = root.getJSONObject(key);
                ma = processSeoulWIFIObject(jo);
                markers.add(ma);
            }

        }

        else if(dataType.equals("TOILET")) {

            Iterator iterator = root.keys();
            while(iterator.hasNext()) {
                String key = (String)iterator.next();
                JSONObject data = root.getJSONObject(key);
                ma = processSeoulToiletObject(data);
                markers.add(ma);
            }

        }

        return markers;
    }

    public Marker processSeoulToiletObject(JSONObject jsonObject) throws JSONException {

        Marker marker = null;

        Location myLoc = LocationUtil.getLocation();
        Location desLoc =  LocationUtil.getLocation();
        desLoc.setLatitude(jsonObject.getDouble("Y_WGS84"));
        desLoc.setLongitude(jsonObject.getDouble("X_WGS84"));

        Double distance = (double) myLoc.distanceTo(desLoc);

        marker = new SeoulMarker(jsonObject.getString("POI_ID"),Double.parseDouble(jsonObject.getString("Y_WGS84")),
                Double.parseDouble(jsonObject.getString("X_WGS84")),jsonObject.getString("FNAME"),"TOILET","",distance,"TOILET");

        return marker;
    }

    public Marker processSeoulWIFIObject(JSONObject jsonObject) throws JSONException {

        Marker marker = null;

        Location myLoc = LocationUtil.getLocation();
        Location desLoc =  LocationUtil.getLocation();

        desLoc.setLatitude(jsonObject.getDouble("INSTL_Y"));
        desLoc.setLongitude(jsonObject.getDouble("INSTL_X"));

        Double distance = (double) myLoc.distanceTo(desLoc);


        marker = new SeoulMarker(jsonObject.getString("INSTL_DIV"),Double.parseDouble(jsonObject.getString("INSTL_Y")),
                Double.parseDouble(jsonObject.getString("INSTL_X")),jsonObject.getString("PLACE_NAME"),"WIFI","",distance,"WIFI");

        return  marker;

    }

    private JSONObject convertToJSON(String rawData) {
        try {
            return new JSONObject(rawData);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


}

