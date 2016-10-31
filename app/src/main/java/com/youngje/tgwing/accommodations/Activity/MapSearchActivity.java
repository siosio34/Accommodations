package com.youngje.tgwing.accommodations.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.youngje.tgwing.accommodations.Data.DataFormat;
import com.youngje.tgwing.accommodations.Data.DaumDataProcessor;
import com.youngje.tgwing.accommodations.Data.NavigationDataProcessor;
import com.youngje.tgwing.accommodations.Data.SeoulDataProcessor;
import com.youngje.tgwing.accommodations.Marker;
import com.youngje.tgwing.accommodations.Navi;
import com.youngje.tgwing.accommodations.R;
import com.youngje.tgwing.accommodations.User;
import com.youngje.tgwing.accommodations.Util.HttpHandler;
import com.youngje.tgwing.accommodations.Util.LocationUtil;
import com.youngje.tgwing.accommodations.Util.RoundedAvatarDrawable;
import com.youngje.tgwing.accommodations.Util.RoundedImageView;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapSearchActivity extends AppCompatActivity implements MapView.MapViewEventListener, MapView.POIItemEventListener, MapView.CurrentLocationEventListener, NavigationView.OnNavigationItemSelectedListener {

    private final static String TAG = "MapSearchActivity";
    private ImageView btnMore;
    private View layoutMore;
    private EditText mSearchView;
    private LinearLayout mListLayout;
    private HashMap<Integer, com.youngje.tgwing.accommodations.Marker> mTagItemMap = new HashMap<Integer, com.youngje.tgwing.accommodations.Marker>();
    private MapView mMapView;
    private Location curlocate;
    private User curUser;
    private ToggleButton mToggleButton;
    private DrawerLayout mDrawer;
    private NavigationView mNavView;
    private Menu mDrawerMenu;
    private RelativeLayout mNavHeader;
    private ImageView userImageView;
    private TextView userNameTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ////http://map.daum.net/route/walkset.json?
        // sX=37.2409347&sY=127.0809925&eX=37.2517416&eY=127.070336

        setContentView(R.layout.activity_map_search);
        curlocate = LocationUtil.curlocation;
        curUser = User.getMyInstance();

        btnMore = (ImageView) findViewById(R.id.activity_main_btn_more);
        layoutMore = (View) findViewById(R.id.activity_main_btn_category);
        layoutMore.bringToFront();
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (layoutMore.isShown()) {
                    layoutMore.setVisibility(View.GONE);
                    //mListLayout.setVisibility(View.GONE);
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



        ImageView communityBtn = (ImageView)findViewById(R.id.activity_main_btn_community);
        communityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference();
                myRef.child("currentUser").child(curUser.getUserId()).setValue(curUser);

                Intent intent = new Intent(MapSearchActivity.this, CommunityMainActivity.class);
                startActivity(intent);
            }
        });
        initDrawer();

      Picasso.with(this).load(curUser.getImageUri()).into(new Target() {

          @Override
          public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
              userImageView.setImageDrawable(new RoundedAvatarDrawable(bitmap, bitmap.getWidth(), bitmap.getWidth()));
          }

          @Override
          public void onBitmapFailed(Drawable errorDrawable) {

          }

          @Override
          public void onPrepareLoad(Drawable placeHolderDrawable) {

          }
      });
        userNameTextView.setText(curUser.getUserName());
    }
    private void initDrawer(){
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerMenu = mNavView.getMenu();
        String[] meunItems = getResources().getStringArray(R.array.drawer_menu_items);
        for(int i=0;i<meunItems.length;i++){
            MenuItem item = mDrawerMenu.getItem(i);
            SpannableString s = new SpannableString(meunItems[i]);
            s.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, s.length(), 0);

            item.setTitle(s);
        }

        mNavView.setNavigationItemSelectedListener(this);
        mNavHeader = (RelativeLayout) mNavView.getHeaderView(0);
        userImageView = (ImageView) mNavHeader.findViewById(R.id.userImageView);
        userNameTextView = (TextView)mNavHeader.findViewById(R.id.userName);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("currentUser").child(curUser.getUserId()).removeValue();
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

                }

                return true;

            }
        });

    }

    // TODO: 2016. 10. 31. 리스트 뷰 작업해야됨.
    // ListView 리스트뷰
    public void searchListView(List<com.youngje.tgwing.accommodations.Marker> markerList) {
        // TODO: 2016. 10. 31. 거리 1000m 넘으면 수정이 필요하지않을가 싶네.
         
        ListView listview;
        SearchListViewAdapter adapter;
        TextView ratingText = (TextView) findViewById(R.id.listview_rating_score);

        // Adapter 생성
        adapter = new SearchListViewAdapter();

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.activity_map_search_listView);
        listview.setAdapter(adapter);

        for(int i=0; i < markerList.size(); i++) {
            Marker marker = markerList.get(i);
            int iDistance = (int) marker.getDistance();
            String iTitle;

            if(marker.getTitle().length() > 13) {
                iTitle = marker.getTitle().substring(0, 11);
                iTitle = iTitle + "...";
            } else {
                iTitle = marker.getTitle();
            }

            adapter.addItem(iDistance+" m", iTitle,marker.getMarkerType(), 3, "6");
        }


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                if(v.getId() == R.id.listview_navigation ){
                    // get item
                    SearchListViewItem item = (SearchListViewItem) parent.getItemAtPosition(position);
                    // 네비게이션 이벤트
                    Log.d(TAG, "Activate Navigation : " + item.getTitle());
                } else {
                    // get item
                    SearchListViewItem item = (SearchListViewItem) parent.getItemAtPosition(position);
                    startActivity(new Intent(getApplicationContext(), SearchListDetailView.class));
                    // TODO : use item data.
                }

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

        } else{ // 다음 거 일때
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
            case R.id.market: datatype = DataFormat.DATATYPE.MART;break;
            case R.id.restaurant: datatype = DataFormat.DATATYPE.FOOD; break;
            case R.id.hotel: datatype = DataFormat.DATATYPE.MOTEL; break;
            case R.id.cafe: datatype = DataFormat.DATATYPE.CAFE; break;
            case R.id.pharmacy: datatype = DataFormat.DATATYPE.PHARMACY; break;
            case R.id.train: datatype = DataFormat.DATATYPE.SUBWAY; break;
            default: datatype = null; break;
        }

         categorySearch(datatype);

    }

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
            case R.id.drawer_My_Review:
                Toast.makeText(this,"drawer_My_Review",Toast.LENGTH_SHORT).show(); break;
            case R.id.drawer_Settings:
                Toast.makeText(this,"drawer_Settings",Toast.LENGTH_SHORT).show(); break;
            case R.id.drawer_Announcement:
                Toast.makeText(this,"drawer_Announcement",Toast.LENGTH_SHORT).show();  break;
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

        mToggleButton = (ToggleButton) findViewById(R.id.myLocationToggle);
        mToggleButton.setChecked(false);
        mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked)
                    mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
                else{
                    mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
                }
            }
        });

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
        // TODO: 2016. 10. 31. 토스트 안뜨게 해야될것같긴하다
        // TODO: 2016. 10. 31. 아님 리스트에 하나만뜨게
        com.youngje.tgwing.accommodations.Marker marker = mTagItemMap.get(mapPOIItem.getTag());
        StringBuilder sb = new StringBuilder();
        sb.append("title=").append(marker.getTitle()).append("\n");
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

    public void onNavigation(double endLat,double endLon) throws ExecutionException, InterruptedException, JSONException {

        //테스트를 위한 코드입니다. 맵 위에 선을 그려줍니다.
       //pointOnMap startPoint = new pointOnMap(517681,1040128);
       //List<pointOnMap> tempArray = new ArrayList<pointOnMap>();

       //tempArray.add(new pointOnMap(517395,1041211));
       //tempArray.add(new pointOnMap(516844,1041182));
       //tempArray.add(new pointOnMap(516768,1041293));
       //tempArray.add(new pointOnMap(516768,1041526));
       //tempArray.add(new pointOnMap(515825,1042243));
       //tempArray.add(new pointOnMap(515864,1042269));
       //tempArray.add(new pointOnMap(516011,1042460));

       //// TODO: 2016. 10. 24. 네비를 구현할수 있을것 같다
       //drawLineFromStartPoint(startPoint,tempArray);
        //요기까지



        // TODO: 2016. 10. 25. navigation
        String fromCoord = "WGS84";
        String toCoord = "WCONGNAMUL";
        String type = "json";
        String apikey = getString(R.string.daum_api_key);

        curlocate = LocationUtil.curlocation;

        String startCreateUrl;
        String endCreateUrl;
        String daumRouteRequestUrl;

        startCreateUrl = DataFormat.changeCoordRequestURL(curlocate.getLatitude(),curlocate.getLongitude(),fromCoord,toCoord,type,apikey);
        endCreateUrl = DataFormat.changeCoordRequestURL(endLat,endLon,fromCoord,toCoord,type,apikey);

        String myLocationResult = new HttpHandler().execute(startCreateUrl).get();
        String DestinationResult = new HttpHandler().execute(endCreateUrl).get();
        JSONObject startJsonObject = new JSONObject(myLocationResult);
        JSONObject endJsonObject = new JSONObject(DestinationResult);

        Double daumStartLat = startJsonObject.getDouble("y");
        Double daumStartLon = startJsonObject.getDouble("x");
        Double daumEndLat = endJsonObject.getDouble("y");
        Double daumEndLon = endJsonObject.getDouble("x");
        daumRouteRequestUrl = DataFormat.createNavigationAPIRequestURL(DataFormat.DATATYPE.NAVI,daumStartLat,daumStartLon,daumEndLat,daumEndLon);

        Navi navi = null;
        String result = new HttpHandler().execute(daumRouteRequestUrl).get();
        navi = NavigationDataProcessor.load(result,DataFormat.DATATYPE.NAVI);

        if(navi == null || navi.getLength() < 30) {
            // TODO: 2016. 10. 25. 종료 메시지 남겨야된다.
            // TODO: 2016. 10. 25. 길 안내를 할수 없거나
        }
        else {
            Toast.makeText(getApplicationContext(),navi.getGuideMent(),Toast.LENGTH_SHORT);
        }

    }

    public void onNavigationButtonClick() {

        // TODO: 2016. 10. 25. 이버튼을 클릭햇을때  onNavigationonNavigation 작동
        // TODO: 2016. 10. 25. 한번 더 클릭시 네비게이션 모드 종료

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