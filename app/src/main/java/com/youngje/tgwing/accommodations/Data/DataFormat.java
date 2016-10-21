package com.youngje.tgwing.accommodations.Data;

import android.content.res.Resources;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import static android.net.Uri.encode;

/**
 * Created by joyeongje on 2016. 9. 26..
 */
public class DataFormat {

    private final String DAUM_KEY = "1dee5001636c3e99fb8d90496c9a3f8a";
    private final String TOUR_KEY = "6P%2BoZASTR%2FyMvmypmG5GiuDxZywpTc43N4KDgbWDU9nzKaYCi%2Bx%2BQAMUBIKS7s%2BOML3LSCUkENhFVKV4KY3%2FQg%3D%3D";
    private final String SEOUL_APP_KEY = "437770464173696f373353734d7345";

    // TODO: 2016. 10. 6. 이거 다 strings.xml 로 옮기고 

    //private final String TOUR_AMERICA   = "6P%2BoZASTR%2FyMvmypmG5GiuDxZywpTc43N4KDgbWDU9nzKaYCi%2Bx%2BQAMUBIKS7s%2BOML3LSCUkENhFVKV4KY3%2FQg%3D%3D";

    // TODO: 2016. 10. 2. apikey 다 같은지 비교해야됨


    public enum DATASOURCE {
        DAUM, OPENAPI, TOURAPI
    }


    public enum DATATYPE {

        // -- 다음거
        // MT1 : 대형마트
        // CS2 : 편의점
        // PS3 : 어린이집, 유치원
        // SC4 : 학교
        // AC5 : 학원
        // PK6 : 주차장
        // OL7 : 주유소, 충전소
        // SW8 : 지하철역
        // BK9 : 은행
        // CT1 : 문화시설
        // AG2 : 중개업소
        // PO3 : 공공기관
        // AT4 : 관광명소
        // AD5 : 숙박
        // FD6 : 음식점
        // HP8 : 병원
        // PM9 : 약국

        // ------------- 다음 카테고리 -------------------- //
        MART("MT1"), CONVINEIENCE("CS2"), KIDHOUSE("PS3"), SCHOOL("SC4"), INSTITUDE("AC5"), PARKING("PK6"), OIL("OL7"),
        SUBWAY("SW8"), BANK("BK9"), CUSTOM("CT1"), AG2("AG2"), PO3("PO3"), AT4("AT4"), MOTEL("AD5"), FOOD("FD6"),
        CAFE("CE7"), HOSPITAL("HP8"), PHARMACY("PM9"),


        // ------------- 서울시 공공 데이터 ----------------- //
        WIFI("wifi/RESULT.json?"), TOILET("toilet/RESULT.json?"),

        // --------- 한국관광공사 api 데이터 ----------------- //


        // ------------- 네비게이션 데이터 ------------------ //
        NAVI("walkset.json?");


        private final String value;

        private DATATYPE(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    //public static String[] DATSAFORMATNAME = {, "CS2", "PS3", "SC4", "AC5", "PK6", "OL7", "SW8", "BK9", "CT1", "AG2", "PO3", "AT4", "AD5", "FD6", "CE7", "HP8", "PM9"};


    public static String createDaumCategoryRequestURL(DATATYPE dataformat, double lat, double lon, int radius, int sort, String format, String daumApikey) { // 카테고리
        String requestUrl = "";
        String searchType = dataformat.getValue(); // 뭘 검색해야하나
        String curloc = Double.toString(lat) + "," + Double.toString(lon);

        // TODO: 2016. 9. 27. 이거는 다시짤가 생각좀

        requestUrl = "https://apis.daum.net/local/v1/search/category.json?" +
                "apikey=" + daumApikey + "&code=" + searchType + "&location=" + curloc +
                "&radius=" + radius + "&sort=" + sort;

        // sort 0 정확도 1 인기 2 거리
        // xml 요청도 가능한데 그건 나중에 ㅇㅇ

        return requestUrl;

        //ex https://apis.daum.net/local/v1/search/category.json?apikey={apikey}&code=PM9&location=37.514322572335935,127.06283102249932&radius=20000
        // 서울 강남구 삼성동 20km 반경에서 약국을 찾고 json 받기
    }

    public static String createDaumKeywordRequestURL(String searchString, double lat, double lon, int radius, int sort, String format, String daumApikey) {
        String requestUrl = "";
        String curloc = Double.toString(lat) + "," + Double.toString(lon);

        requestUrl = "https://apis.daum.net/local/v1/search/keyword.json?" +
                "apikey=" + daumApikey + "&query=" + searchString +
                "&location=" + curloc + "&radius=" + radius +
                "&sort=" + sort;

        return requestUrl;


        //0 : 정확도순
        //1 : 인기순
        //2 : 거리순

        //https://apis.daum.net/local/v1/search/keyword.json?apikey={apikey}&query=카카오프렌즈
        // &location=37.514322572335935,127.06283102249932&radius=20000
    }


    public static String createSeoulOpenAPIRequestURL(DATATYPE dataformat, double lat, double lon) {
        String requestUrl = "";
        String dataType = dataformat.getValue();
        requestUrl = "https://tourseoul-451de.firebaseio.com/seoul/" + dataType;

        String orderQuery = "";

        List<NameValuePair> params = new LinkedList<NameValuePair>();

        params.add(new BasicNameValuePair("orderBy", "\"INSTL_Y\""));
        params.add(new BasicNameValuePair("startAt", "\"" + String.valueOf(lat - 0.5) + "\""));
        params.add(new BasicNameValuePair("endAt", "\"" + String.valueOf(lat + 0.5) + "\""));
        //params.add(new BasicNameValuePair("orderBy", "\"INSTL_X\""));
        //params.add(new BasicNameValuePair("startAt", String.valueOf(126.9780 - 0.03)));
        //params.add(new BasicNameValuePair("endAt", String.valueOf(126.9780 + 0.03)));

        String paramString = URLEncodedUtils.format(params, "utf-8");

        requestUrl += paramString;

        Log.i("requestUrl : ", requestUrl);
        return requestUrl;


    }


    public static String createTourAPIRequestURL(Locale locale, String appName) {

        //  MobileApp 파라미터는 서비스(웹,앱 등)별로 활용 통계를 산출하기 위한 항목입니다. URL요청 시 반드시 기재 부탁드립니다.//====//== 파라미터 인코딩 예시(JSP 기준)
        //  <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> //=== 서비스명이 영문인 경우 (인코딩 불필요)
        //  String appName = “KoreaTourismOrganization”; //=== 서비스명이 한글(일문, 중문 등)인 경우 (인코딩 필수)
        //  String appName = URLEncoder.encode(“한국관광공사”, "UTF-8"); http://api.visitkorea.or.kr/openapi/service/rest/EngService/areaCode?ServiceKey=ServiceKey&n
        //  umOfRows=10&pageNo=1&MobileOS=AND&MobileApp=appName&_type=json

        // EngService 영문
        // JpnService 일문
        // ChsService 중문간체
        // ChtService 중문번체
        // GerService 독어(독일어)
        // FreService 불어(프랑스어)
        // SpnService 서어(스페인어)
        // RusService 노어(러시아어)


        //String appName = "Accommdations";
        // TODO: 2016. 10. 2. locale 에따라서 바까야됨 일단은 영어로
        String requestUrl = "http://api.visitkorea.or.kr/openapi/service/rest/EngService + ";
        return requestUrl;
    }

    public static String createNavigationAPIRequestURL(DATATYPE dataformat, double startlat, double startLon, double endlat, double endLon) {

        //http://map.daum.net/route/walkset.json?sX=37.2409347&sY=127.0809925&eX= 37.2517416&eY=127.070336
        //http://map.daum.net/route/walkset.json?sX=37.2409347&sY=127.0809925&eX=37.2517416&eY=127.070336
        //http://map.daum.net/route/walkset.json?sX=37.2409347&sY=127.0809925&eX=37.2517416&eY=127.070336

        String requestUrl = "http://map.daum.net/route/" + dataformat.getValue();

        List<NameValuePair> params = new LinkedList<NameValuePair>();

        params.add(new BasicNameValuePair("sX", "37.2409347"));
        params.add(new BasicNameValuePair("sY", "127.0809925"));
        //params.add(new BasicNameValuePair("eName","영통역 홈플러스"));
        params.add(new BasicNameValuePair("eX", "37.251741"));
        params.add(new BasicNameValuePair("eY", "127.070336"));
        //params.add(new BasicNameValuePair("orderBy", "\"INSTL_X\""));
        //params.add(new BasicNameValuePair("startAt", String.valueOf(126.9780 - 0.03)));
        //params.add(new BasicNameValuePair("endAt", String.valueOf(126.9780 + 0.03)));

        String paramString = URLEncodedUtils.format(params, "utf-8");

        requestUrl += paramString;


        String requestUrl2 = "http://map.daum.net/route/walkset.json?sX=37.2409347&sY=127.0809925&eX=37.2517416&eY=127.070336";
        String requestUrl3 = "http://map.daum.net/route/walkset.json?sName=경희대학교+국제캠퍼스&sX=517685&sY=1040009&eName=영통역+분당선&eX=515821&eY=1042318&ids=P24254845%2CP15110708";
        return requestUrl2;


    }

    public static String createGetAroungRequestURL(double lat, double lon) {

        String requestUrl = "";
        requestUrl = "https://tourseoul-451de.firebaseio.com/currentUser.json?";

        List<NameValuePair> params = new LinkedList<NameValuePair>();

        params.add(new BasicNameValuePair("orderBy", "\"lat\""));
        params.add(new BasicNameValuePair("startAt", String.valueOf(lat - 0.0225)));
        params.add(new BasicNameValuePair("endAt", String.valueOf(lat + 0.0225)));
        params.add(new BasicNameValuePair("orderBy", "\"lon\""));
        params.add(new BasicNameValuePair("startAt", String.valueOf(lon - 0.0225)));
        params.add(new BasicNameValuePair("endAt", String.valueOf(lon + 0.0225)));

        String paramString = URLEncodedUtils.format(params, "utf-8");
        requestUrl += paramString;

        Log.i("requestUrl : ", requestUrl);
        return requestUrl;


    }


}
