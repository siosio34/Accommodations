package com.youngje.tgwing.accommodations;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Translate {
    private String key;
    private String source;
    private String target;

    public Translate(String key, String source, String target) {
        this.key = key;
        this.source = source;
        this.target = target;
    }

    public String genRequest(String msg) throws Exception {
        msg = URLEncoder.encode(msg, "UTF-8");
        String req = "https://www.googleapis.com/language/translate/v2?"
                + "key=" + key
                + "&q=" + msg
                + "&source=" + source
                + "&target=" + target;

        Log.i("req",req);
        return req;
    }

    public String getTranslatedText(String json) throws Exception {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject)jsonParser.parse(json);
        JSONObject data_obj = (JSONObject)jsonObject.get("data");
        JSONArray trans_arr = (JSONArray)data_obj.get("translations");
        JSONObject text_obj = (JSONObject)trans_arr.get(0);
        return text_obj.get("translatedText").toString();
    }


    public String request(String msg) throws Exception {
        String req = genRequest(msg);
        URL url = new URL(req);
        StringBuffer buf = new StringBuffer("");
        URLConnection urlcon = url.openConnection();
        InputStream in = urlcon.getInputStream();
        BufferedReader brin = new BufferedReader(new InputStreamReader(in));
        String str;
        while ((str = brin.readLine()) != null) {
            buf.append(str);
        }
        return getTranslatedText(buf.toString());
    }


}