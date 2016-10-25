package com.youngje.tgwing.accommodations.Data;

import android.util.Log;

import com.youngje.tgwing.accommodations.Navi;
import com.youngje.tgwing.accommodations.NaviXY;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joyeongje on 2016. 10. 18..
 */

public class NavigationDataProcessor {

    public static final int MAX_JSON_OBJECTS = 30;

    public static Navi load(String rawData, DataFormat.DATATYPE datatype) throws JSONException {

        Navi navi = null;
        JSONObject root = convertToJSON(rawData);
        JSONObject jsonObject = root.getJSONArray("directions").getJSONObject(0).getJSONArray("sections").getJSONObject(0);

        int length = jsonObject.getInt("length");
        int time = jsonObject.getInt("time");

        JSONArray guideListArray = jsonObject.getJSONArray("guideList");
        JSONObject guideObject = guideListArray.getJSONObject(0);

        String guideMent = guideObject.getString("guideMent"); // 얼마나 가세요
        String rotationCode = guideObject.getString("rotationCode"); // 방향은 어디로

        Double x;
        Double y;

        List<NaviXY> naviXYList = new ArrayList<>();
        for(int i=0; i < guideListArray.length() ; i++) {
            JSONObject guide = guideListArray.getJSONObject(i);
            x = guide.getDouble("x");
            y = guide.getDouble("y");
            naviXYList.add(new NaviXY(x,y));

            // TODO: 2016. 10. 24.  여기에 경로 그리는거 넣어야됨
        }

        navi = new Navi((rotationCode + " " + guideMent),length,time,naviXYList);
        Log.i("navi", navi.getGuideMent());
        Log.i("navi2", Double.toString(navi.getLength()));
        Log.i("navi3", Double.toString(navi.getTime()));
        Log.i("navi4", navi.getListXY().toString());

        return navi;

    }

    private static JSONObject convertToJSON(String rawData) {
        try {
            return new JSONObject(rawData);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


}
