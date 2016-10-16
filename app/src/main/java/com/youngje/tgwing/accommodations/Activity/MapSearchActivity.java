package com.youngje.tgwing.accommodations.Activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.youngje.tgwing.accommodations.Data.DataFormat;
import com.youngje.tgwing.accommodations.Marker;
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
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.youngje.tgwing.accommodations.R.string.daum_api_key;
import net.daum.android.map.openapi.search.Item;

public class MapSearchActivity extends AppCompatActivity implements MapView.MapViewEventListener, MapView.POIItemEventListener, MapView.CurrentLocationEventListener {

    private ImageView btnMore;
    private View layoutMore;
    private EditText mSearchView;
    private HashMap<Integer, Item> mTagItemMap = new HashMap<Integer, Item>();
    private MapView mMapView;
    private Location curlocate;
    private User curUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_search);
        curlocate = LocationUtil.getLocation();
        curUser = User.getMyInstance();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("currentUser").setValue(curUser);

        btnMore = (ImageView) findViewById(R.id.activity_main_btn_more);
        layoutMore = (View) findViewById(R.id.activity_main_btn_category);
        layoutMore.bringToFront();
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (layoutMore.isShown()) {
                    layoutMore.setVisibility(View.GONE);
                } else {
                    layoutMore.setVisibility(View.VISIBLE);
                }
            }
        });

        // // java code
        // MapView mapView = new MapView(this);
        // mapView.setDaumMapApiKey("b751bc30d4efe34c1ef20e644f655766");

        // ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        // mapViewContainer.addView(mapView);

        mMapView = (MapView)findViewById(R.id.map_view);
        mMapView.setDaumMapApiKey(getString(daum_api_key));
        mMapView.setMapViewEventListener(this);
        mMapView.setPOIItemEventListener(this);
        mMapView.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());
        addSearch();
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
                    int radius = 20000; // 중심 좌표부터의 반경거리. 특정 지역을 중심으로 검색하려고 할 경우 사용. meter 단위 (0 ~ 10000)
                    int page = 1; // 페이지 번호 (1 ~ 3). 한페이지에 15개
                    String apikey = getString(R.string.daum_api_key);

                    Searcher searcher = new Searcher(); // net.daum.android.map.openapi.search.Searcher
                    searcher.searchKeyword(getApplicationContext(), query, latitude, longitude, radius, page, apikey, new OnFinishSearchListener() {
                        @Override
                        public void onSuccess(List<Item> itemList) {
                            mMapView.removeAllPOIItems(); // 기존 검색 결과 삭제
                            showResult(itemList); // 검색 결과 보여줌
                        }

                        @Override
                        public void onFail() {
                            showToast("API_KEY의 제한 트래픽이 초과되었습니다.");
                        }
                    });
                }
                return false;
            }
        });
    }
    public void categorySearch(String categoryCode){
        mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);

        hideSoftKeyboard(); // 키보드 숨김
        MapPoint.GeoCoordinate geoCoordinate = mMapView.getMapCenterPoint().getMapPointGeoCoord();
        double latitude = geoCoordinate.latitude; // 위도
        double longitude = geoCoordinate.longitude; // 경도
        int radius = 20000; // 중심 좌표부터의 반경거리. 특정 지역을 중심으로 검색하려고 할 경우 사용. meter 단위 (0 ~ 10000)
        int page = 1; // 페이지 번호 (1 ~ 3). 한페이지에 15개
        String apikey = getString(R.string.daum_api_key);

        Searcher searcher = new Searcher(); // net.daum.android.map.openapi.search.Searcher
        searcher.searchCategory(getApplicationContext(), categoryCode, latitude, longitude, radius, page, apikey, new OnFinishSearchListener() {
            @Override
            public void onSuccess(List<Item> itemList) {
                mMapView.removeAllPOIItems(); // 기존 검색 결과 삭제
                showResult(itemList); // 검색 결과 보여줌
            }

            @Override
            public void onFail() {
                showToast("API_KEY의 제한 트래픽이 초과되었습니다.");
            }
        });
    }

    public void categoryClicked(View v) throws ExecutionException, InterruptedException {
        String temp = null;
        DataFormat.DATATYPE datatype = null;
        switch (v.getId()){
            case R.id.landmark: temp = "AT4"; break;
            case R.id.restroom: datatype = DataFormat.DATATYPE.TOILET; break;
            case R.id.wifizone: datatype = DataFormat.DATATYPE.WIFI; break;
            case R.id.bank: temp = "BK9"; break;
            case R.id.market: temp = "CS2"; break;
            case R.id.restaurant: temp = "FD6"; break;
            case R.id.hotel: temp = "AD5"; break;
            case R.id.cafe: temp = "CE7"; break;
            case R.id.pharmacy: temp = "PM9"; break;
            case R.id.train: temp = "SW8"; break;
            default: temp = ""; break;
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

         categorySearch(temp);


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


    class CustomCalloutBalloonAdapter implements CalloutBalloonAdapter {

        private final View mCalloutBalloon;

        public CustomCalloutBalloonAdapter() {
            mCalloutBalloon = getLayoutInflater().inflate(R.layout.custom_callout_balloon, null);
        }

        @Override
        public View getCalloutBalloon(MapPOIItem poiItem) {
            if (poiItem == null) return null;
            Item item = mTagItemMap.get(poiItem.getTag());
            if (item == null) return null;
            ImageView imageViewBadge = (ImageView) mCalloutBalloon.findViewById(R.id.badge);
            TextView textViewTitle = (TextView) mCalloutBalloon.findViewById(R.id.title);
            textViewTitle.setText(item.title);
            TextView textViewDesc = (TextView) mCalloutBalloon.findViewById(R.id.desc);
            textViewDesc.setText(item.address);
            imageViewBadge.setImageDrawable(createDrawableFromUrl(item.imageUrl));
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
    private void showResult(List<Item> itemList) {
        MapPointBounds mapPointBounds = new MapPointBounds();

        for (int i = 0; i < itemList.size(); i++) {
            Item item = itemList.get(i);

            MapPOIItem poiItem = new MapPOIItem();
            poiItem.setItemName(item.title);
            poiItem.setTag(i);
            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(item.latitude, item.longitude);
            poiItem.setMapPoint(mapPoint);
            mapPointBounds.add(mapPoint);
            poiItem.setMarkerType(MapPOIItem.MarkerType.CustomImage);
            poiItem.setCustomImageResourceId(R.drawable.map_pin_blue);
            poiItem.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
            poiItem.setCustomSelectedImageResourceId(R.drawable.map_pin_red);
            poiItem.setCustomImageAutoscale(false);
            poiItem.setCustomImageAnchor(0.5f, 1.0f);

            mMapView.addPOIItem(poiItem);
            mTagItemMap.put(poiItem.getTag(), item);
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

//        mCurLocOnOffButton.setChecked(true);
//        mCurLocOnOffButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                if (isChecked)
//                    mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
//                else{
//                    mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
//                }
//            }
//        });
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
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {
        Item item = mTagItemMap.get(mapPOIItem.getTag());
        StringBuilder sb = new StringBuilder();
        sb.append("title=").append(item.title).append("\n");
        sb.append("imageUrl=").append(item.imageUrl).append("\n");
        sb.append("address=").append(item.address).append("\n");
        sb.append("newAddress=").append(item.newAddress).append("\n");
        sb.append("zipcode=").append(item.zipcode).append("\n");
        sb.append("phone=").append(item.phone).append("\n");
        sb.append("category=").append(item.category).append("\n");
        sb.append("longitude=").append(item.longitude).append("\n");
        sb.append("latitude=").append(item.latitude).append("\n");
        sb.append("distance=").append(item.distance).append("\n");
        sb.append("direction=").append(item.direction).append("\n");
        Toast.makeText(this, sb.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }
}
