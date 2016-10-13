package com.youngje.tgwing.accommodations.Data;

import android.location.Location;

import com.youngje.tgwing.accommodations.Marker;
import com.youngje.tgwing.accommodations.SeoulMarker;
import com.youngje.tgwing.accommodations.Util.LocationUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
        JSONArray dataArray = null;
        Marker ma;

        String dataType = datatype.getValue();

        if(dataType.equals("WIFI")) {
            dataArray = root.getJSONObject("wifi").getJSONArray("RESULT");
            int top = Math.min(MAX_JSON_OBJECTS, dataArray.length());
            for (int i = 0; i < top; i++) {
                JSONObject jo = dataArray.getJSONObject(i);

                ma = processSeoulToiletObject(jo);
                markers.add(ma);
            }
        }

        else if(dataType.equals("TOILET")) {
            dataArray = root.getJSONObject("toilet").getJSONArray("RESULT");
            int top = Math.min(MAX_JSON_OBJECTS, dataArray.length());
            for (int i = 0; i < top; i++) {
                JSONObject jo = dataArray.getJSONObject(i);
                ma = processSeoulWIFIObject(jo);
                markers.add(ma);
            }
        }

        return markers;
    }

    public Marker processSeoulToiletObject(JSONObject jsonObject) throws JSONException {

        Marker marker = null;

        marker = new SeoulMarker(jsonObject.getString("POI_ID"),jsonObject.getString("Y_WGS84"),
                jsonObject.getString("X_WGS84"),jsonObject.getString("FNAME"),"","",0,"TOILET");

        return marker;
    }

    public Marker processSeoulWIFIObject(JSONObject jsonObject) throws JSONException {

        List<Marker> markersList = new ArrayList<Marker>();
        Marker marker = null;

        marker = new SeoulMarker(jsonObject.getString("INSTL_DIV"),jsonObject.getString("INSTL_Y"),
                jsonObject.getString("INSTL_X"),jsonObject.getString("PLACE_NAME"),"","",0,"WIFI");

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
