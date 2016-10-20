package com.youngje.tgwing.accommodations.Data;

import android.util.Log;

import com.youngje.tgwing.accommodations.Marker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joyeongje on 2016. 10. 18..
 */

public class NavigationDataProcessor {

    public static final int MAX_JSON_OBJECTS = 10;


    public String load(String rawData, DataFormat.DATATYPE datatype) throws JSONException {

        List<Marker> markers = new ArrayList<Marker>();
        JSONObject root = convertToJSON(rawData);
        JSONArray dataArray = root.getJSONObject("directions").getJSONObject("sections").getJSONArray("guideList");
        Log.i("dataArray", dataArray.toString());

        JSONObject jsonObject = dataArray.getJSONObject(0);
        String guideMent = jsonObject.getString("guideMent");
        String length = jsonObject.getString("length");
        String points = jsonObject.getString("points");
        String rotationCode = jsonObject.getString("rotationCode");

        return (rotationCode + guideMent);
    }

    public Marker processDAUMNavigationObject(JSONObject jsonObject) {
        return null;
    }

    private JSONObject convertToJSON(String rawData) {
        try {
            return new JSONObject(rawData);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


}
