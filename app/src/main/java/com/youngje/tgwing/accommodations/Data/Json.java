package com.youngje.tgwing.accommodations.Data;

import com.youngje.tgwing.accommodations.Marker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joyeongje on 2016. 9. 26..
 */
public class Json {

    public static final int MAX_JSON_OBJECTS = 50;    // JSON 객체의 최대 수

    public List<Marker> load(JSONObject root, DataFormat.DATASOURCE datasource) {

        JSONObject jo = null;
        JSONArray dataArray = null;
        List<Marker> markerList = new ArrayList<Marker>();

        try {
            if (root.has("channel")) { // 다음 카테고리 url인 경우
                dataArray = root.getJSONObject("channel").getJSONArray("item");
                if (dataArray != null) {

                    int top = Math.min(MAX_JSON_OBJECTS, dataArray.length());
                    for (int i = 0; i < top; i++) {
                        jo = dataArray.getJSONObject(i);
                        Marker ma = null;
                        switch (datasource) {
                            case DAUM:
                                ma = processDAUMJSONObject(jo);
                                break;

                            case OPENAPI:
                                break;

                        }

                        if (ma != null) {
                            markerList.add(ma);
                        }
                    }

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return markerList;
    }



    public Marker processDAUMJSONObject(JSONObject jsonObject) throws JSONException {
        Marker marker = null;
        marker = new Marker(jsonObject.getString("id"),jsonObject.getString("latitude"), jsonObject.getString("longitude"),jsonObject.getString("title"),
                jsonObject.getString("category"),jsonObject.getString("phone"),jsonObject.getString("address"),jsonObject.getString("newAddress"),
                jsonObject.getString("imageUrl"),jsonObject.getString("placeUrl"),jsonObject.getString("distance"));

        return marker;
    }

    public Marker processTourapiJSONObject(JSONObject jsonObject) throws JSONException {
        Marker marker = null;

        return marker;
    }

    public Marker processSeoulApiJSONObject(JSONObject jsonObject) throws JSONException {
        Marker marker = null;

        return marker;
    }








}
