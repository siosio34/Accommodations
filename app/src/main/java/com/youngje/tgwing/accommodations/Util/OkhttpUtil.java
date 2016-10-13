package com.youngje.tgwing.accommodations.Util;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by joyeongje on 2016. 10. 13..
 */

public class OkhttpUtil {

    OkHttpClient client = new OkHttpClient();

    public String run(String url) throws IOException {
        HttpUrl urlTemp = new HttpUrl.Builder()
                .scheme("https")
                .host("https://tourseoul-451de.firebaseio.com/seoul/wifi/RESULT.json")
                .addQueryParameter("orderBy", "INSTL_Y")
                .build();

        String urlurl = urlTemp.toString();

        Request request = new Request.Builder()
                .url(urlurl).build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }


}
