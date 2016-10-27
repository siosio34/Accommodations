package com.youngje.tgwing.accommodations.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.youngje.tgwing.accommodations.Data.DataFormat;
import com.youngje.tgwing.accommodations.Data.DaumDataProcessor;
import com.youngje.tgwing.accommodations.Data.NavigationDataProcessor;
import com.youngje.tgwing.accommodations.Data.SeoulDataProcessor;
import com.youngje.tgwing.accommodations.R;
import com.youngje.tgwing.accommodations.User;
import com.youngje.tgwing.accommodations.Util.HttpHandler;
import com.youngje.tgwing.accommodations.Util.LocationUtil;

import net.daum.android.map.openapi.search.OnFinishSearchListener;
import net.daum.android.map.openapi.search.Searcher;
import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.youngje.tgwing.accommodations.R.string.daum_api_key;
import net.daum.android.map.openapi.search.Marker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapSearchActivity extends AppCompatActivity implements MapView.MapViewEventListener, MapView.POIItemEventListener, MapView.CurrentLocationEventListener, NavigationView.OnNavigationItemSelectedListener {

    private ImageView btnMore;
    private View layoutMore;
    private EditText mSearchView;
    private LinearLayout mListLayout;
    private HashMap<Integer, com.youngje.tgwing.accommodations.Marker> mTagItemMap = new HashMap<Integer, com.youngje.tgwing.accommodations.Marker>();
    private MapView mMapView;
    private Location curlocate;
    private User curUser;

    private DrawerLayout mDrawer;
    private NavigationView mNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ////http://map.daum.net/route/walkset.json?
        // sX=37.2409347&sY=127.0809925&eX=37.2517416&eY=127.070336

        setContentView(R.layout.activity_map_search);
        curlocate = LocationUtil.curlocation;
        curUser = User.getMyInstance();

        String startCreateUrl;
        String endCreateUrl;
        String daumRouteRequestUrl;
        String fromCoord = "WGS84";
        String toCoord = "WCONGNAMUL";
        String type = "json";
        String apikey = getString(R.string.daum_api_key);

       // 127.07282485359492
       // 37.252086477197

        // 127.08058512777377
        // 37.24433967711934

        startCreateUrl = DataFormat.changeCoordRequestURL(37.24433967711934,127.08058512777377,fromCoord,toCoord,type,apikey);
        endCreateUrl = DataFormat.changeCoordRequestURL(37.252086477197,127.07282485359492,fromCoord,toCoord,type,apikey);

       try {
           //http://map.naver.com/findroute2/findWalkRoute.nhn?call=route2&output=json&coord_type=naver&search=0&start=127.0798535%2C37.2433617%2C%EA%B2%BD%ED%9D%AC%EB%8C%80%ED%95%99%EA%B5%90+%EA%B5%AD%EC%A0%9C%EC%BA%A0%ED]
           String myLocationResult = new HttpHandler().execute(startCreateUrl).get();
           String DestinationResult = new HttpHandler().execute(endCreateUrl).get();
           JSONObject startJsonObject = new JSONObject(myLocationResult);
           JSONObject endJsonObject = new JSONObject(DestinationResult);
           Double naverStartLat = startJsonObject.getDouble("y");
           Double naverStartLon = startJsonObject.getDouble("x");
           Double naverEndLat = endJsonObject.getDouble("y");
           Double naverEndLon = endJsonObject.getDouble("x");
           daumRouteRequestUrl = DataFormat.createNavigationAPIRequestURL(DataFormat.DATATYPE.NAVI,naverStartLat,naverStartLon,naverEndLat,naverEndLon);

           String result = new HttpHandler().execute(daumRouteRequestUrl).get();
           String resultString = NavigationDataProcessor.load(result,DataFormat.DATATYPE.NAVI);

           if(resultString != null)
               Log.i("resultString", resultString);

       } catch (InterruptedException | ExecutionException e) {
           e.printStackTrace();
       } catch (JSONException e) {
           e.printStackTrace();
       }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("currentUser").child(curUser.getUserId()).setValue(curUser);

        btnMore = (ImageView) findViewById(R.id.activity_main_btn_more);
        layoutMore = (View) findViewById(R.id.activity_main_btn_category);
        layoutMore.bringToFront();
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (layoutMore.isShown()) {
                    layoutMore.setVisibility(View.GONE);
                    mListLayout.setVisibility(View.GONE);
                } else {
                    layoutMore.setVisibility(View.VISIBLE);
                }
            }
        });

        mMapView = (MapView)findViewById(R.id.map_view);
        mMapView.setDaumMapApiKey(getString(daum_api_key));
        mMapView.setMapViewEventListener(this);
        mMapView.setPOIItemEventListener(this);
        mMapView.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());
        addSearch();

        ///////////////////////////////////drawer 부분입니다.
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavView = (NavigationView) findViewById(R.id.nav_view);
        mNavView.setNavigationItemSelectedListener(this);

        ImageView communityBtn = (ImageView)findViewById(R.id.activity_main_btn_community);
        communityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapSearchActivity.this, CommunityMainActivity.class);
                startActivity(intent);
            }
        });
    }

    void onDrawer(View view){
        mDrawer.openDrawer(mNavView);
        hideSoftKeyboard();
    }
    public void addSearch(){

        mSearchView = (EditText) findViewById(R.id.activity_main_searchbar); // 검색창
        mSearchView.setOnEditorActionListener(new EditText.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    String query = mSearchView.getText().toString();
                    mSearchView.setText(null);
                    if (query == null || query.length() == 0) {
                        showToast("검색어를 입력하세요.");
                        return false;
                    }
                    mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
                    hideSoftKeyboard(); // 키보드 숨김
                    MapPoint.GeoCoordinate geoCoordinate = mMapView.getMapCenterPoint().getMapPointGeoCoord();
                    double latitude = geoCoordinate.latitude; // 위도
                    double longitude = geoCoordinate.longitude; // 경도
                    int radius = 1000; // 중심 좌표부터의 반경거리. 특정 지역을 중심으로 검색하려고 할 경우 사용. meter 단위 (0 ~ 10000)
                    int page = 1; // 페이지 번호 (1 ~ 3). 한페이지에 15개
                    String apikey = getString(R.string.daum_api_key);

                    HttpHandler httpHandler = new HttpHandler();
                    String createUrl = null;

                    try {
                        createUrl = DataFormat.createDaumKeywordRequestURL(query, curUser.getLat(), curUser.getLon(),radius, page,"",apikey);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    Log.i("유알엘",createUrl);

                    try{
                        String HTTPResult = httpHandler.execute(createUrl).get();
                        Log.i("키워드",HTTPResult);
                        DaumDataProcessor DDP = new DaumDataProcessor();
                        List<com.youngje.tgwing.accommodations.Marker> markerList = DDP.load(HTTPResult, null);

                        mMapView.removeAllPOIItems();
                        showResult(markerList);
                        showSearchResult(markerList); //검색 목록 보여줌

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

//                    Searcher searcher = new Searcher(); // net.daum.android.map.openapi.search.Searcher
//                    searcher.searchKeyword(getApplicationContext(), query, latitude, longitude, radius, page, apikey, new OnFinishSearchListener() {
//                        @Override
//                        public void onSuccess(List<com.youngje.tgwing.accommodations.Marker> markerList) {
//                            mMapView.removeAllPOIItems(); // 기존 검색 결과 삭제
//                            showResult(markerList); // 검색 결과 보여줌
//                            showSearchResult(markerList); //검색 목록 보여줌
//                        }
//
//                        @Override
//                        public void onFail() {
//                            showToast("API_KEY의 제한 트래픽이 초과되었습니다.");
//                        }
//
//                    });


                }

                return true;

            }
        });

    }

    // ListView 리스트뷰
    public void searchListView(List<com.youngje.tgwing.accommodations.Marker> markerList) {
        ListView listview;
        SearchListViewAdapter adapter;
        TextView ratingText = (TextView) findViewById(R.id.listview_rating_score);

        // Adapter 생성
        adapter = new SearchListViewAdapter();

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.activity_map_search_listView);
        listview.setAdapter(adapter);

        for(int i=0; i < markerList.size(); i++) {
            com.youngje.tgwing.accommodations.Marker marker = markerList.get(i);
            int iDistance = (int) marker.getDistance();

            adapter.addItem(iDistance+"m",
                    marker.getTitle(), "종류", 3, "(6)");
        }

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                SearchListViewItem item = (SearchListViewItem) parent.getItemAtPosition(position);
                startActivity(new Intent(getApplicationContext(), SearchListDetailView.class));
                // TODO : use item data.
            }
        });
    }

    public void categorySearch(DataFormat.DATATYPE dataFormat){
        mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);

        hideSoftKeyboard(); // 키보드 숨김
        MapPoint.GeoCoordinate geoCoordinate = mMapView.getMapCenterPoint().getMapPointGeoCoord();
        double latitude = geoCoordinate.latitude; // 위도
        double longitude = geoCoordinate.longitude; // 경도
        int radius = 1000; // 중심 좌표부터의 반경거리. 특정 지역을 중심으로 검색하려고 할 경우 사용. meter 단위 (0 ~ 10000)
        int page = 1; // 페이지 번호 (1 ~ 3). 한페이지에 15개
        String apikey = getString(R.string.daum_api_key);

//        Searcher searcher = new Searcher(); // net.daum.android.map.openapi.search.Searcher
//        searcher.searchCategory(getApplicationContext(), categoryCode, latitude, longitude, radius, page, apikey, new OnFinishSearchListener() {
//            @Override
//            public void onSuccess(final List<com.youngje.tgwing.accommodations.Marker> markerList) {
//                mMapView.removeAllPOIItems(); // 기존 검색 결과 삭제
//
//                showResult(markerList); // 검색 결과 보여줌
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        showSearchResult(markerList); // 검색 리스트 보여줌
//                    }
//                });
//
//            }
//
//            @Override
//            public void onFail() {
//                showToast("API_KEY의 제한 트래픽이 초과되었습니다.");
//            }
//        });

        if(dataFormat.equals(DataFormat.DATATYPE.WIFI) || dataFormat.equals(DataFormat.DATATYPE.TOILET)) {
            HttpHandler httpHandler = new HttpHandler();
            String createUrl = null;
            createUrl = DataFormat.createSeoulOpenAPIRequestURL(dataFormat, curUser.getLat(), curUser.getLon());
            try {
                String HTTPResult = httpHandler.execute(createUrl).get();
                SeoulDataProcessor SDP = new SeoulDataProcessor();
                final List<com.youngje.tgwing.accommodations.Marker> markerList = SDP.load(HTTPResult, dataFormat);

                mMapView.removeAllPOIItems();
                showResult(markerList);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showSearchResult(markerList); // 검색 리스트 보여줌
                    }
                });
                Log.i("temptemptemp", HTTPResult);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                temp();
            } catch (ExecutionException | InterruptedException | JSONException e) {
                e.printStackTrace();
            }
        }else{
//            Toast.makeText(this,dataFormat.toString(),Toast.LENGTH_SHORT).show();

            HttpHandler httpHandler = new HttpHandler();
            String createUrl = null;
            createUrl = DataFormat.createDaumCategoryRequestURL(dataFormat, curUser.getLat(), curUser.getLon(),radius, page,"",apikey);
            try{
                String HTTPResult = httpHandler.execute(createUrl).get();
                DaumDataProcessor DDP = new DaumDataProcessor();
                List<com.youngje.tgwing.accommodations.Marker> markerList = DDP.load(HTTPResult, dataFormat);

                mMapView.removeAllPOIItems();
                showResult(markerList);
                showSearchResult(markerList);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    public void categoryClicked(View v) throws ExecutionException, InterruptedException {
        DataFormat.DATATYPE datatype = null;
        switch (v.getId()){
            case R.id.landmark: datatype = DataFormat.DATATYPE.AT4 ; break;
            case R.id.restroom: datatype = DataFormat.DATATYPE.TOILET; break;
            case R.id.wifizone: datatype = DataFormat.DATATYPE.WIFI; break;
            case R.id.bank: datatype = DataFormat.DATATYPE.BANK; break;
            case R.id.market: datatype = DataFormat.DATATYPE.CONVINEIENCE;break;
            case R.id.restaurant: datatype = DataFormat.DATATYPE.FOOD; break;
            case R.id.hotel: datatype = DataFormat.DATATYPE.MOTEL; break;
            case R.id.cafe: datatype = DataFormat.DATATYPE.CAFE; break;
            case R.id.pharmacy: datatype = DataFormat.DATATYPE.PHARMACY; break;
            case R.id.train: datatype = DataFormat.DATATYPE.SUBWAY; break;
            default: datatype = null; break;
        }


        // TODO: 2016. 10. 15. 합쳐야된다.
       // if(datatype != null) {
       //     HttpHandler httpHandler = new HttpHandler();
        //     String createUrl = null;
        //     DataFormat.DATATYPE dataFormat = DataFormat.DATATYPE.WIFI;
        //     createUrl = DataFormat.createSeoulOpenAPIRequestURL(dataFormat, curlocate.getLatitude(), curlocate.getLongitude());
        //     String HTTPResult = httpHandler.execute(createUrl).get();
//
       //     // TODO: 2016. 10. 15. parsing 하는거 만들어야됨
       // }

         categorySearch(datatype);

    }
    /** category codes
     MT1 대형마트
     CS2 편의점
     PS3 어린이집, 유치원
     SC4 학교
     AC5 학원
     PK6 주차장
     OL7 주유소, 충전소
     SW8 지하철역
     BK9 은행
     CT1 문화시설
     AG2 중개업소
     PO3 공공기관
     AT4 관광명소
     AD5 숙박
     FD6 음식점
     CE7 카페
     HP8 병원
     PM9 약국
     */
    private void showToast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MapSearchActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        mDrawer.closeDrawer(mNavView);

        switch(id){
            case R.id.nav_home:
                Toast.makeText(this,"nav_home",Toast.LENGTH_SHORT).show(); break;
            case R.id.nav_attend:
                Toast.makeText(this,"nav_attend",Toast.LENGTH_SHORT).show(); break;
            case R.id.nav_attendlist:
                Toast.makeText(this,"nav_attendlist",Toast.LENGTH_SHORT).show();  break;
            case R.id.nav_info:
                Toast.makeText(this,"nav_info",Toast.LENGTH_SHORT).show(); break;
            case R.id.nav_logout:
                Toast.makeText(this,"nav_logout",Toast.LENGTH_SHORT).show(); break;
        }
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }


    class CustomCalloutBalloonAdapter implements CalloutBalloonAdapter {

        private final View mCalloutBalloon;

        public CustomCalloutBalloonAdapter() {
            mCalloutBalloon = getLayoutInflater().inflate(R.layout.custom_callout_balloon, null);
        }

        @Override
        public View getCalloutBalloon(MapPOIItem poiItem) {
            if (poiItem == null) return null;
            com.youngje.tgwing.accommodations.Marker marker = mTagItemMap.get(poiItem.getTag());
            if (marker == null) return null;
            ImageView imageViewBadge = (ImageView) mCalloutBalloon.findViewById(R.id.badge);
            TextView textViewTitle = (TextView) mCalloutBalloon.findViewById(R.id.title);
            textViewTitle.setText(marker.getTitle());
            TextView textViewDesc = (TextView) mCalloutBalloon.findViewById(R.id.desc);
            //textViewDesc.setText(item.address);
            imageViewBadge.setImageDrawable(createDrawableFromUrl(marker.getImageUrl()));
            return mCalloutBalloon;
        }

        @Override
        public View getPressedCalloutBalloon(MapPOIItem poiItem) {
            return null;
        }

    }
    protected void hideSoftKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void showSearchResult(List<com.youngje.tgwing.accommodations.Marker> markerList) {
        mListLayout = (LinearLayout) findViewById(R.id.activity_map_search_listLayout);
        searchListView(markerList);
        mListLayout.setVisibility(View.VISIBLE);
    }

    private Drawable createDrawableFromUrl(String url) {
        try {
            InputStream is = (InputStream) this.fetch(url);
            Drawable d = Drawable.createFromStream(is, "src");
            return d;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private void showResult(List<com.youngje.tgwing.accommodations.Marker> markerList) {
        MapPointBounds mapPointBounds = new MapPointBounds();

        for (int i = 0; i < markerList.size(); i++) {
            com.youngje.tgwing.accommodations.Marker marker = markerList.get(i);

            MapPOIItem poiItem = new MapPOIItem();
            poiItem.setItemName(marker.getTitle());
            poiItem.setTag(i);
            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(marker.getLat(), marker.getLon());
            poiItem.setMapPoint(mapPoint);
            mapPointBounds.add(mapPoint);
            poiItem.setMarkerType(MapPOIItem.MarkerType.CustomImage);
            poiItem.setCustomImageResourceId(R.drawable.map_pin_blue);
            poiItem.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
            poiItem.setCustomSelectedImageResourceId(R.drawable.map_pin_red);
            poiItem.setCustomImageAutoscale(false);
            poiItem.setCustomImageAnchor(0.5f, 1.0f);

            mMapView.addPOIItem(poiItem);
            mTagItemMap.put(poiItem.getTag(), marker);
        }
        MapPoint.GeoCoordinate geoCoordinate = mMapView.getMapCenterPoint().getMapPointGeoCoord();
        double latitude = geoCoordinate.latitude; // 위도
        double longitude = geoCoordinate.longitude; // 경도
        mMapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(latitude, longitude), 3, true);

        MapPOIItem[] poiItems = mMapView.getPOIItems();
        if (poiItems.length > 0) {
            //mMapView.selectPOIItem(poiItems[0], false);
        }
    }

    //맵 Lon 이랑 Lan 나타내주는게 없어서 맨 밑에다가 두개 정보만 담고있는 class임시로 만들어놨습니다.
    //나중에 원하는 걸로 수정때리면 되여
    private void drawLineFromStartPoint(pointOnMap startPoint, List<pointOnMap> pointOnMapList){
        MapPolyline polyline = new MapPolyline();
        polyline.setTag(1000);
        // Polyline 색 지정
        polyline.setLineColor(Color.argb(128, 255, 51, 0));

        //시작점 추가
        polyline.addPoint(MapPoint.mapPointWithCONGCoord(startPoint.getLat(),startPoint.getLon()));

        //리스트에서 그 어진 포인트로 선그려줌
        for(int i=0;i<pointOnMapList.size();i++){
            //bessel 좌표 -> 형이 예제에서 보여준 좌표 라면 CONGCoord쓰고

            polyline.addPoint(MapPoint.mapPointWithWCONGCoord(pointOnMapList.get(i).getLat(),pointOnMapList.get(i).getLon()));
            //아니고 WGS84인 위도 경도라면 요거 쓰면됨
            //polyline.addPoint(MapPoint.mapPointWithGeoCoord(pointOnMapList.get(i).getLat(),pointOnMapList.get(i).getLon()));
    }

        //맵에 추가
        mMapView.addPolyline(polyline);

        //경로가 모두 나오는 화면으로 이동
        MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
        int padding = 100; // px
        mMapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));
    }
    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {

    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }

    @Override
    public void onMapViewInitialized(MapView mapView) {
        mMapView.setCurrentLocationEventListener(this);
        mMapView.setShowCurrentLocationMarker(true);
        mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);


        //테스트를 위한 코드입니다. 맵 위에 선을 그려줍니다.
        pointOnMap startPoint = new pointOnMap(517681,1040128);
        List<pointOnMap> tempArray = new ArrayList<pointOnMap>();

        tempArray.add(new pointOnMap(517395,1041211));
        tempArray.add(new pointOnMap(516844,1041182));
        tempArray.add(new pointOnMap(516768,1041293));
        tempArray.add(new pointOnMap(516768,1041526));
        tempArray.add(new pointOnMap(515825,1042243));
        tempArray.add(new pointOnMap(515864,1042269));
        tempArray.add(new pointOnMap(516011,1042460));


        // TODO: 2016. 10. 24. 네비를 구현할수 있을것 같다
        drawLineFromStartPoint(startPoint,tempArray);
        //요기까지
    }

    private Object fetch(String address) throws MalformedURLException,IOException {
        URL url = new URL(address);
        Object content = url.getContent();
        return content;
    }
    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onBackPressed() {
        int backCount = getSupportFragmentManager().getBackStackEntryCount();
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            if(backCount > 2) {
                super.onBackPressed();
            } else {
                finish();
            }
        }
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {
        com.youngje.tgwing.accommodations.Marker marker = mTagItemMap.get(mapPOIItem.getTag());
        StringBuilder sb = new StringBuilder();
        sb.append("title=").append(marker.getTitle()).append("\n");
        sb.append("imageUrl=").append(marker.getImageUrl()).append("\n");
        //sb.append("address=").append(item.address).append("\n");
        //sb.append("newAddress=").append(item.newAddress).append("\n");
        //sb.append("zipcode=").append(item.zipcode).append("\n");
        //sb.append("phone=").append(item.phone).append("\n");
        //sb.append("category=").append(item.category).append("\n");
        sb.append("longitude=").append(marker.getLon()).append("\n");
        sb.append("latitude=").append(marker.getLat()).append("\n");
        sb.append("distance=").append(marker.getDistance()).append("\n");
        //sb.append("direction=").append(item.direction).append("\n");
        Toast.makeText(this, sb.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }

    public ArrayList<String> temp() throws ExecutionException, InterruptedException, JSONException {
        HttpHandler httpHandler = new HttpHandler();
        String requestUrl = DataFormat.createGetAroungRequestURL(curUser.getLat(),curUser.getLon());
        String result = httpHandler.execute(requestUrl).get();
        JSONObject root = new JSONObject(result);

        ArrayList<String> userIdList = new ArrayList<>();

        String jsonArr = "[";
        Iterator iterator = root.keys();
        while(iterator.hasNext()) {
            String key = (String)iterator.next();
            JSONObject data = root.getJSONObject(key);
            jsonArr+=data.toString();
            jsonArr+=",";
        }
        jsonArr = jsonArr.substring(0, jsonArr.length()-1)+"]";

        JSONArray jsonArray = new JSONArray(jsonArr);
        for(int i=0 ; i<jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            userIdList.add(jsonObject.getString("userId"));
        }

        Log.i("temptemp",userIdList.toString());
        return userIdList;
    }
}


class pointOnMap{
    public pointOnMap(double Lat, double Lon){
        setLat(Lat);
        setLon(Lon);
    }
    public pointOnMap(){}
    private double Lat;
    private double Lon;
    public double getLat(){return Lat;}
    public double getLon(){return Lon;}
    public void setLat(double Lat){this.Lat = Lat;}
    public void setLon(double Lon){this.Lon = Lon;}
}