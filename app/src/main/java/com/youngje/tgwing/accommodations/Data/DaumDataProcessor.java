package com.youngje.tgwing.accommodations.Data;

import com.youngje.tgwing.accommodations.DaumMarker;
import com.youngje.tgwing.accommodations.Marker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by joyeongje on 2016. 10. 6..
 */

public class DaumDataProcessor implements DataProcessor {

    public static final int MAX_JSON_OBJECTS = 50;

    // TODO: 2016. 10. 6. 다른 함수 추가해야됨. 
    @Override
    public List<Marker> load(String rawData, DataFormat.DATATYPE datatype) throws JSONException {

        List<Marker> markers = new ArrayList<Marker>();
        JSONObject root = convertToJSON(rawData);
        JSONArray dataArray = root.getJSONObject("channel").getJSONArray("item");
        Marker ma;

        //String dataType = datatype.getValue();

        int top = Math.min(MAX_JSON_OBJECTS, dataArray.length());
        for (int i = 0; i < top; i++) {
            JSONObject jo = dataArray.getJSONObject(i);
            ma = processDAUMJSONObject(jo);
            markers.add(ma);
        }
        return markers;
    }

    public Marker processDAUMJSONObject(JSONObject jsonObject) throws JSONException {
        Marker marker = null;

        marker = new DaumMarker(jsonObject.getString("id"), Double.parseDouble(jsonObject.getString("latitude")), Double.parseDouble(jsonObject.getString("longitude")), jsonObject.getString("title"), jsonObject.getString("placeUrl")
                , jsonObject.getString("placeUrl"), jsonObject.getDouble("distance"), jsonObject.getString("phone"), jsonObject.getString("address"), jsonObject.getString("newAddress")
                , jsonObject.getString("category"));

        return marker;
    }


    private JSONObject convertToJSON(String rawData) {
        try {
            return new JSONObject(rawData);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

}
