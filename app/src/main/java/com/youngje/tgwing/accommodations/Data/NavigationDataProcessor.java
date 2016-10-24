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

    public static String load(String rawData, DataFormat.DATATYPE datatype) throws JSONException {

        JSONObject root = convertToJSON(rawData);
        JSONObject jsonObject = root.getJSONArray("directions").getJSONObject(0).getJSONArray("sections").getJSONObject(0);


        JSONObject guideObject = jsonObject.getJSONArray("guideList").getJSONObject(0);
        String guideMent = guideObject.getString("guideMent");
        String rotationCode = guideObject.getString("rotationCode");

        //String length = jsonObject.getString("length");
        //String points = jsonObject.getString("points");

        return (rotationCode + " " + guideMent);
    }

    private static JSONObject convertToJSON(String rawData) {
        try {
            return new JSONObject(rawData);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


}
