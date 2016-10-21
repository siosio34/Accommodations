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

        marker = new SeoulMarker(jsonObject.getString("POI_ID"),Double.parseDouble(jsonObject.getString("Y_WGS84")),
                Double.parseDouble(jsonObject.getString("X_WGS84")),jsonObject.getString("FNAME"),"","",0,"TOILET");

        return marker;
    }

    public Marker processSeoulWIFIObject(JSONObject jsonObject) throws JSONException {

        List<Marker> markersList = new ArrayList<Marker>();
        Marker marker = null;

        marker = new SeoulMarker(jsonObject.getString("INSTL_DIV"),Double.parseDouble(jsonObject.getString("INSTL_Y")),
                Double.parseDouble(jsonObject.getString("INSTL_X")),jsonObject.getString("PLACE_NAME"),"","",0,"WIFI");

        return  marker;

    }

    private JSONObject convertToJSON(String rawData) {
        try {
            return new JSONObject(rawData);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    //public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
    //    Map<String, Object> retMap = new HashMap<String, Object>();
//
    //    if(json != JSONObject.NULL) {
    //        retMap = toMap(json);
    //    }
    //    return retMap;
    //}
//
    //public static Map<String, Object> toMap(JSONObject object) throws JSONException {
    //    Map<String, Object> map = new HashMap<String, Object>();
//
    //    Iterator<String> keysItr = object.keys();
    //    while(keysItr.hasNext()) {
    //        String key = keysItr.next();
    //        Object value = object.get(key);
//
    //        if(value instanceof JSONArray) {
    //            value = toList((JSONArray) value);
    //        }
//
    //        else if(value instanceof JSONObject) {
    //            value = toMap((JSONObject) value);
    //        }
    //        map.put(key, value);
    //    }
    //    return map;
    //}
//
    //public static List<Object> toList(JSONArray array) throws JSONException {
    //    List<Object> list = new ArrayList<Object>();
    //    for(int i = 0; i < array.length(); i++) {
    //        Object value = array.get(i);
    //        if(value instanceof JSONArray) {
    //            value = toList((JSONArray) value);
    //        }
//
    //        else if(value instanceof JSONObject) {
    //            value = toMap((JSONObject) value);
    //        }
    //        list.add(value);
    //    }
    //    return list;
    //}
}

