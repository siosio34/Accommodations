package com.youngje.tgwing.accommodations.Data;

import android.content.res.Resources;

/**
 * Created by joyeongje on 2016. 9. 26..
 */
public class DataFormat {


    public enum DATASOURCE {
        DAUM, OPENAPI,TOURAPI
    }

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

    public enum DATAFORMAT {
        MART("MT1"), CONVINEIENCE("CS2"), KIDHOUSE("PS3"), SCHOOL("SC4"), INSTITUDE("AC5"),PARKING("PK6"), OIL("OL7"), SUBWAY("SW8"), BANK("BK9"), CUSTOM("CT1"), AG2("AG2"),PO3("PO3"),AT4("AT4"),MOTEL("AD5"),FOOD("FD6"),CAFE("CE7"),HOSPITAL("HP8"),PHARMACY("PM9");

        private final String value;

        private DATAFORMAT(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    //public static String[] DATSAFORMATNAME = {, "CS2", "PS3", "SC4", "AC5", "PK6", "OL7", "SW8", "BK9", "CT1", "AG2", "PO3", "AT4", "AD5", "FD6", "CE7", "HP8", "PM9"};

    public static String createDaumRequestURL(DATASOURCE datasource,DATAFORMAT dataformat, double lat,double lon, int radius,int sort,String format,String daumApikey) { // 카테고리
        String requestUrl  = "";
        String APIkey = daumApikey;
        String searchType = dataformat.getValue(); // 뭘 검색해야하나
        String curloc =  Double.toString(lat) +"," + Double.toString(lon);

        // TODO: 2016. 9. 26.
        // getString 함수는 컨텍스트나 액티비티에서 받아야된다

        // TODO: 2016. 9. 27. 이거는 다시짤가 생각좀

        switch (datasource) {
            case DAUM:
                requestUrl = "https://apis.daum.net/local/v1/search/category.json?" +
                        "apikey=" + APIkey + "&code=" + searchType +"&location=" + curloc +
                        "&radius=" + radius + "&sort=" + sort;

                // sort 0 정확도 1 인기 2 거리
                // xml 요청도 가능한데 그건 나중에 ㅇㅇ
                break;
            case OPENAPI:
                break;

            case TOURAPI:
                break;

        }
        return requestUrl;

        //ex https://apis.daum.net/local/v1/search/category.json?apikey={apikey}&code=PM9&location=37.514322572335935,127.06283102249932&radius=20000
        // 서울 강남구 삼성동 20km 반경에서 약국을 찾고 json 받기
    }

    public static String createOpenAPIRequestURL(DATASOURCE datasource,DATAFORMAT dataformat, double lat,double lon, int radius,int sort,String format,String daumApikey) {
        String requestUrl = "";
        return requestUrl;
    }

    public static String createTourAPIRequestURL() {
        String requestUrl = "";
        return requestUrl;
    }






}
