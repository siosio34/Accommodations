package com.youngje.tgwing.accommodations.Util;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by joyeongje on 2016. 10. 10..
 */

public class JSONtoDBUtil extends SQLiteOpenHelper {

    private Context context;


    private static final String SEOUL_WIFI_PATH = "../Wifi.json";
    private static final String SEOUL_TOILET_PATH = "../Toilet.json";

    public JSONtoDBUtil(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query;
        query = "CREATE TABLE WIFI ( id INTEGER PRIMARY KEY, lat DOUBLE, lon DOUBLE, name TEXT)";
        sqLiteDatabase.execSQL(query);
        Log.d("make Wifi Table", "animals Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {


    }

    public void jsonParser() throws IOException, ParseException, JSONException {

        SQLiteDatabase db = getWritableDatabase();

        JSONParser parser = new JSONParser();
        FileReader fileReader = new FileReader(SEOUL_WIFI_PATH);
        JSONObject json = (JSONObject) parser.parse(fileReader);

        JSONArray jsonArray = (JSONArray) json.get("DATA");

        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject jsonObject = (JSONObject) jsonArray.get(i);

            String wifiPlace = (String) jsonObject.get("PLACE_NAME");
            Double lat = (Double) jsonObject.get("INSTL_Y");
            Double lon = (Double) jsonObject.get("INSTL_X");
            String INSTL_DIV = (String) jsonObject.get("INSTL_DIV");


        }

    }

}
