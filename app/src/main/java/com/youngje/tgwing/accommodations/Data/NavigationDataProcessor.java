package com.youngje.tgwing.accommodations.Data;

import android.util.Log;

import com.youngje.tgwing.accommodations.Navi;
import com.youngje.tgwing.accommodations.NaviXY;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by joyeongje on 2016. 10. 18..
 */

public class NavigationDataProcessor {

    public static final int MAX_JSON_OBJECTS = 30;

    public static Navi load(String rawData, DataFormat.DATATYPE datatype) throws JSONException {

        Navi navi = null;
        Log.i("rawdata", rawData);
     //   JSONObject root = convertToJSON(rawData);
     //   JSONObject jsonObject = root.getJSONArray("directions").getJSONObject(0).getJSONArray("sections").getJSONObject(0);
//
     //   // 거리랑 시간
     //   int length = jsonObject.getInt("length");
     //   int time = jsonObject.getInt("time");
//
     //   JSONArray guideListArray = jsonObject.getJSONArray("guideList");
//
     //   // 지도에 그릴 x좌표 y좌표 리스트
     //   List<NaviXY> naviXYList = new ArrayList<>();
     //   JSONObject guideFirstObject = guideListArray.getJSONObject(0).getJSONObject("link");
     //   String pointString = guideFirstObject.getString("points");
     //   String[] pointList = pointString.split("\\|");
//
     //   //Log.i("pointList", Arrays.toString(pointList));
//
     //   for (String aPointList : pointList) {
     //       String temp[] = aPointList.split(",");
     //       naviXYList.add(new NaviXY(Double.parseDouble(temp[0]), Double.parseDouble(temp[1])));
     //   }
//
     //   // 가이드 멘트 추가.
     //   ArrayList<String> myGuideList = new ArrayList<>();
     //   for(int i=0; i < guideListArray.length() ; i++) {
     //       JSONObject naviGuideMent = guideListArray.getJSONObject(i);
     //       myGuideList.add(naviGuideMent.getString("guideMent"));
     //   }
//
     //   Log.i("guideMentList", Arrays.toString(pointList));
//
     //   navi = new Navi(myGuideList,length,time,naviXYList);
     //   return navi;

        return null;

    }

    private static JSONObject convertToJSON(String rawData) {
        try {
            return new JSONObject(rawData);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


}
