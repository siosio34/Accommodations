/*
 * Copyright (C) 2010- Peer internet solutions
 * 
 * This file is part of mixare.
 * 
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version. 
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details. 
 * 
 * You should have received a copy of the GNU General Public License along with 
 * this program. If not, see <http://www.gnu.org/licenses/>
 */

package ARAccomdation.mixare.data;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.youngje.tgwing.accommodations.R;


// 데이터 소스를 실질적으로 다루는 클래스
public class DataSource {


    // 데이터 소스와 데이터 포맷의 열거형 변수
    public enum DATASOURCE {
        CAFE, DOCUMENT, IMAGE, VIDEO
    };

    public enum DATAFORMAT {
        CAFE ,DOCUMENT, IMAGE, VIDEO
    };

    // 주의할것! 방대한 양의 데이터(MB단위 이상)을 산출할 때에는, 작은 반경이나 특정한 쿼리만을 사용해야한다
    /**
     * URL 부분 끝
     */

    // 아이콘들. 트위터와 버즈

    public static Bitmap cafeIcon;
    public static Bitmap documentIcon;
    public static Bitmap imageIcon;
    public static Bitmap videoIcon;


    public DataSource() {

    }

    // 리소스로부터 각 아이콘 생성
    public static void createIcons(Resources res) {

        cafeIcon = BitmapFactory.decodeResource(res, R.drawable.icon_cafe);
        documentIcon = BitmapFactory.decodeResource(res, R.drawable.icon_conveni);
        imageIcon = BitmapFactory.decodeResource(res,R.drawable.icon_conveni);
        videoIcon = BitmapFactory.decodeResource(res,R.drawable.icon_conveni);
        // TODO: 2016. 9. 20. 이거 채워넣어야됨


    }


    // 아이콘 비트맵의 게터
    public static Bitmap getBitmap(String ds) {
        Bitmap bitmap = null;
        switch (ds) {

            case "CAFE":
                bitmap = cafeIcon;
                break;
            case "DOCUMENT":
                bitmap = documentIcon;
                break;
            case "IMAGE":
                bitmap = imageIcon;
                break;
            case "VIDEO":
                bitmap = videoIcon;
                break;


        }
        return bitmap;
    }

    // 데이터 소스로부터 데이터 포맷을 추출
    public static DATAFORMAT dataFormatFromDataSource(DATASOURCE ds) {
        DATAFORMAT ret;
        // 소스 형식에 따라 포맷을 할당한다
        switch (ds) {


            case CAFE:
                ret = DATAFORMAT.CAFE;
                break;

            case DOCUMENT:
                ret = DATAFORMAT.DOCUMENT;
                break;

            case IMAGE:
                ret = DATAFORMAT.IMAGE;
                break;

            case VIDEO:
                ret = DATAFORMAT.VIDEO;
                break;

            default:
                ret = DATAFORMAT.DOCUMENT;
                break;


        }
        return ret;    // 포맷 리턴
    }


    // 각 정보들로 완성된 URL 리퀘스트를 생성
    public static String createRequestURL(DATASOURCE source, double lat, double lon, double alt, float radius, String locale) {
        String ret = "";    // 결과 스트링

        // https://dinosaur-facts.firebaseio.com/
        // https://dinosaur-facts.firebaseio.com/dinosaurs.json?orderBy="height"&startAt=3&print=pretty
        // 파일로부터 읽는 것이 아니라면
        if (!ret.startsWith("file://")) {

            // 각 소스에 따른 URL 리퀘스트를 완성한다
            switch (source) {
                // TODO: 2016. 9. 9. yj  json 작업해야됨

                // 네이버 웹페이지에서 가져오는 정보
                case CAFE:
                    ret = "http://map.naver.com/search2/interestSpot.nhn?type=CAFE&boundary=" + Double.toString(lon - 0.02) + "%3B" +
                            Double.toString(lat - 0.01) + "%3B" + Double.toString(lon + 0.02) +
                            "%3B" + Double.toString(lat + 0.01) + "&pageSize=100";
                    break;

                case DOCUMENT:
                case IMAGE:
                case VIDEO:
                    ret = "https://hackfair-c7518.firebaseio.com/posts.json";
                    break;

                default:
                    ret = "https://hackfair-c7518.firebaseio.com/posts.json";
                    break;

            }

        }

        return ret;
    }

    // 각 소스에 따른 색을 리턴
    public static int getColor(DATASOURCE datasource) {
        int ret;
        switch (datasource) {

            default:
                ret = Color.GREEN;
                break;
        }
        return ret;
    }

}
