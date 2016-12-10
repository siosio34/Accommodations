package com.youngje.tgwing.accommodations.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.Image;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;

import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.CompoundButton;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import android.widget.PopupWindow;

import android.widget.RelativeLayout;

import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.youngje.tgwing.accommodations.ARAccomdation.*;
import com.youngje.tgwing.accommodations.ARAccomdation.MainActivity;
import com.youngje.tgwing.accommodations.ARAccomdation.mixare.MixView;
import com.youngje.tgwing.accommodations.Chat;
import com.youngje.tgwing.accommodations.ChatManager;
import com.youngje.tgwing.accommodations.Chatroom;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.youngje.tgwing.accommodations.Marker.markerList;
import static com.youngje.tgwing.accommodations.R.string.daum_api_key;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapSearchActivity extends AppCompatActivity implements View.OnClickListener, MapView.MapViewEventListener, MapView.POIItemEventListener, MapView.CurrentLocationEventListener, NavigationView.OnNavigationItemSelectedListener {

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
    private TextView userLocaleText;


    //community chatroom activity
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private LinearLayout communityMain;
    //community chatroom activity request code
    private final int COMMUNITY_CHATROOM_REQUEST_CODE = 0;
    private PopupWindow popup;
    private View popupView;
    private ImageView arrow;
    private boolean isCommunity = false;
    private boolean isFull = false;


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

                if(!isCommunity) {
                    if (layoutMore.isShown()) {
                        layoutMore.setVisibility(View.GONE);
                        //mListLayout.setVisibility(View.GONE);
                    } else {
                        layoutMore.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    changeCommunityToMapSearch(true);
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
                changeMapSearchToCommunity();

            }
        });

        ImageView arImageBtn = (ImageView)findViewById(R.id.ar_button);
        arImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent arIntent = new Intent(MapSearchActivity.this, MixView.class);
                startActivity(arIntent);

            }
        });

        initDrawer();


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

        // 유저 이미지
        userImageView = (ImageView) mNavHeader.findViewById(R.id.userImageView);
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
        // 유저 이름
        userNameTextView = (TextView)mNavHeader.findViewById(R.id.userName);
        userNameTextView.setText(curUser.getUserName());

        // 유저의 국적
        userLocaleText = (TextView) mNavHeader.findViewById(R.id.userLocale);
        userLocaleText.setText("국적 : " + curUser.getCountry());

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
        initDrawer();
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
                        markerList = DDP.load(HTTPResult, null);

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

    public void searchListView(List<Marker> markerList) {

        ListView listview;
        ImageView navigationView;
        SearchListViewAdapter adapter;
        TextView ratingText = (TextView) findViewById(R.id.listview_rating_score);

        // Adapter 생성
        adapter = new SearchListViewAdapter(this);

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.activity_map_search_listView);
        //navigationView = (ImageView)listview.findViewById(R.id.navigation_button);
        listview.setAdapter(adapter);

        for(int i = 0; i < Marker.markerList.size(); i++) {
            Marker marker = Marker.markerList.get(i);
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

                Log.d(TAG, "Activate : " + v.getId());
                Log.d(TAG, "Activate22" + String.valueOf(id));

                if(v.getId() == R.id.navigation_button){
                    // get item
                    SearchListViewItem item = (SearchListViewItem) parent.getItemAtPosition(position);
                    // 네비게이션 이벤트
                    Log.d(TAG, "Activate Navigation : " + item.getTitle());
                } else {
                    // get item
                    Marker item = Marker.markerList.get(position);
                    Marker.selectedMarker = item;
                    startActivity(new Intent(getApplicationContext(), SearchListDetailView.class));

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
                markerList = SDP.load(HTTPResult, dataFormat);

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
                markerList = DDP.load(HTTPResult, dataFormat);

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
            case R.id.landmark: datatype = DataFormat.DATATYPE.TOUR ; break;
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
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        mDrawer.closeDrawer(mNavView);

        switch(id){
            case R.id.drawer_My_Review:
                Intent intent = new Intent(MapSearchActivity.this, MyReviewActivity.class);
                startActivity(intent);
                break;
            case R.id.drawer_Settings:
                Intent intent1 = new Intent(this, UserInformationActivity.class);
                startActivity(intent1);
                break;
                //Toast.makeText(this,"drawer_Settings",Toast.LENGTH_SHORT).show(); break;
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

            TextView textViewTitle = (TextView) mCalloutBalloon.findViewById(R.id.title);
            textViewTitle.setText(marker.getTitle());


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
    public void drawLineFromStartPoint(pointOnMap startPoint, List<pointOnMap> pointOnMapList){
        MapPolyline polyline = new MapPolyline();
        polyline.setTag(1000);
        // Polyline 색 지정
        polyline.setLineColor(Color.argb(128, 255, 51, 0));

        //시작점 추가
        polyline.addPoint(MapPoint.mapPointWithWCONGCoord(startPoint.getLat(),startPoint.getLon()));

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
        // 다음 지피에스를 계속 갱신해준다.

        if(LocationUtil.DaumCurlocation == null)
            LocationUtil.DaumCurlocation = LocationUtil.curlocation;
        else {

            synchronized (LocationUtil.DaumCurlocation) {
                LocationUtil.DaumCurlocation.setLatitude(mapPoint.getMapPointGeoCoord().latitude);
                LocationUtil.DaumCurlocation.setLongitude(mapPoint.getMapPointGeoCoord().longitude);
                Log.d(TAG,Double.toString(LocationUtil.curlocation.getLatitude()));
                Log.d(TAG,Double.toString(LocationUtil.curlocation.getLongitude()));

            }
        }

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
        if(!isCommunity) {
            int backCount = getSupportFragmentManager().getBackStackEntryCount();
            if (mDrawer.isDrawerOpen(GravityCompat.START)) {
                mDrawer.closeDrawer(GravityCompat.START);
            } else {
                if (backCount > 2) {
                    super.onBackPressed();
                } else {
                    finish();
                }
            }
        }
        else {
            if(popup != null) {
                popup.dismiss();
                popup = null;
            }
            else {
                changeCommunityToMapSearch(false);
            }
        }
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

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

    public void navimode(boolean isNavi, int position) {
        if(!isNavi) {
            findViewById(R.id.navbar).setVisibility(View.VISIBLE);
            findViewById(R.id.shadow_bottom).setVisibility(View.VISIBLE);
            findViewById(R.id.activity_main_btn_category).setVisibility(View.VISIBLE);
            findViewById(R.id.myLocationToggle).setVisibility(View.VISIBLE);
            findViewById(R.id.activity_main_btn_community).setVisibility(View.VISIBLE);
        }
        else {
            findViewById(R.id.navbar).setVisibility(View.GONE);
            findViewById(R.id.shadow_bottom).setVisibility(View.GONE);
            findViewById(R.id.activity_main_btn_category).setVisibility(View.GONE);
            findViewById(R.id.myLocationToggle).setVisibility(View.GONE);
            findViewById(R.id.activity_main_btn_community).setVisibility(View.GONE);
            //findViewById(R.id.activity_map_search_listLayout).setVisibility(View.GONE);
            //mMapView.removeAllPOIItems();
        }
    }


    void changeCommunityToMapSearch(boolean isClick) {
        isCommunity = false;

        findViewById(R.id.community_main_createChatroom).setVisibility(View.GONE);
        findViewById(R.id.community_main).setVisibility(View.GONE);
        //findViewById(R.id.activity_map_search_listLayout).setVisibility(View.VISIBLE);
        //findViewById(R.id.shadow_bottom).setVisibility(View.VISIBLE);
        if(isClick)
            findViewById(R.id.activity_main_btn_category).setVisibility(View.VISIBLE);
        else
            findViewById(R.id.activity_main_btn_category).setVisibility(View.GONE);
        //findViewById(R.id.myLocationToggle).setVisibility(View.VISIBLE);
        findViewById(R.id.activity_main_btn_community).setVisibility(View.VISIBLE);
        //findViewById(R.id.navbar).setVisibility(View.VISIBLE);
    }

    void changeMapSearchToCommunity() {
        isCommunity = true;

        findViewById(R.id.activity_main_btn_category).setVisibility(View.GONE);
        //findViewById(R.id.shadow_bottom).setVisibility(View.GONE);
        findViewById(R.id.activity_map_search_listLayout).setVisibility(View.GONE);
        //findViewById(R.id.myLocationToggle).setVisibility(View.GONE);
        findViewById(R.id.activity_main_btn_community).setVisibility(View.GONE);
        //findViewById(R.id.activity_map_search_listLayout).setVisibility(View.GONE);
        //findViewById(R.id.navbar).setVisibility(View.GONE);
        findViewById(R.id.community_main).setVisibility(View.VISIBLE);
        findViewById(R.id.community_main_createChatroom).setVisibility(View.VISIBLE);
        //communityMainActivity
        communityMain = (LinearLayout)findViewById(R.id.community_main);
        communityMain.setVisibility(View.VISIBLE);
        //arrow = (ImageView)findViewById(R.id.community_main_arrow);
        ImageView createChatroomBtn = (ImageView)findViewById(R.id.community_main_createChatroom);
        createChatroomBtn.setOnClickListener(this);

        //LinearLayout uparrowLayout = (LinearLayout)findViewById(R.id.community_main_arrowlayout);
        //uparrowLayout.setOnClickListener(this);

        ViewPager chatroomListPagerAdapter = (ViewPager)findViewById(R.id.community_main_chatroom_viewpager);
        chatroomListPagerAdapter.setAdapter(new ChatroomListPagerAdapter());
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.community_main_chatroom_body:
                enterRoom((String)v.getTag());
                break;

            //클릭시 팝업 윈도우 생성
            case R.id.community_main_createChatroom:
                popupView();
                break;

            case R.id.community_main_chatroom_popup_create:
                createRoom();
                break;

            /*
            case R.id.community_main_arrowlayout:
                if(isFull) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1200);
                    communityMain.setLayoutParams(params);
                    //arrow.setBackgroundResource(R.drawablef.uparrow);
                    isFull = false;
                }
                else {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    communityMain.setLayoutParams(params);
                    //arrow.setBackgroundResource(R.drawable.downarrow);
                    isFull = true;
                }
                break;
             */
        }
    }

    public void enterRoom(final String chatManagerId) {
        databaseReference.child("ChatManager").child(chatManagerId).child("chatroom").runTransaction(new Transaction.Handler() {
            int check=-1;
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Chatroom chatroom = mutableData.getValue(Chatroom.class);

                ArrayList<User> list = chatroom.getUserList();
                boolean isRoomMember = false;
                for(int i=0; i<list.size(); i++)
                    if(list.get(i).getUserId().equals(User.getMyInstance().getUserId())) {
                        isRoomMember = true;
                        break;
                    }

                if(isRoomMember || !chatroom.isEnd()) {
                    int maxNumber = chatroom.getChatroomMaxNumber();
                    int currentNumber = chatroom.getUserList().size();

                    if(isRoomMember)
                        check = 1;
                    else if (currentNumber < maxNumber) {
                        mutableData.child("userList").child(Integer.toString(currentNumber)).setValue(User.getMyInstance());
                        check = 1;
                    }
                    else
                        check = 2;
                }
                else if(chatroom.isEnd()) {
                    check = 0;
                }

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                if(databaseError == null) {
                    if(check == 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MapSearchActivity.this, "마감된 방입니다.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    else if(check == 2) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MapSearchActivity.this, "방이 꽉 찼습니다.", Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                    else if (check == 1) {
                        Intent intent = new Intent(MapSearchActivity.this, CommunityChatroomActivity.class);
                        intent.putExtra("chatManagerId", chatManagerId);
                        startActivityForResult(intent, COMMUNITY_CHATROOM_REQUEST_CODE);
                    }
                    else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MapSearchActivity.this, "방 입장에 문제가 생겼습니다. 네트워크를 확인해주세요.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
                else {

                    enterRoom(chatManagerId);
                }
                System.out.println(databaseError);
            }
        });
    }


    public void createRoom() {
        databaseReference.child("users").child(User.getMyInstance().getUserId()).child("chatRoomID").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue(String.class).length() == 0) {
                            databaseReference.child("ChatManager").child(User.getMyInstance().getUserId()).addListenerForSingleValueEvent(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(final DataSnapshot dataSnapshot) {
                                            try {
                                                String chatroomTitle = ((EditText) popupView.findViewById(R.id.community_main_chatroom_popup_title)).getText().toString();
                                                int limitNumber = Integer.parseInt(((EditText) popupView.findViewById(R.id.community_main_chatroom_popup_limitNumber)).getText().toString());
                                                User user = User.getMyInstance();
                                                Chatroom chatroom = new Chatroom(chatroomTitle, user.getImageUri(), user.getUserName(), user.getCountry(), limitNumber, new Date(), false, new ArrayList<User>());
                                                final ChatManager chatManager = new ChatManager(chatroom, new HashMap<String, Chat>());
                                                dataSnapshot.getRef().setValue(chatManager, new DatabaseReference.CompletionListener() {
                                                    @Override
                                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                        //add room id to User object
                                                        if(databaseError == null) {
                                                            databaseReference.getRoot().child("users").child(User.getMyInstance().getUserId()).child("chatRoomID").setValue(User.getMyInstance().getUserId(), new DatabaseReference.CompletionListener() {
                                                                @Override
                                                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                                    dataSnapshot.child("chatroom").child("userList").getRef().child("0").setValue(User.getMyInstance());
                                                                    Intent intent = new Intent(MapSearchActivity.this, CommunityChatroomActivity.class);
                                                                    intent.putExtra("chatManagerId", User.getMyInstance().getUserId());
                                                                    startActivityForResult(intent, COMMUNITY_CHATROOM_REQUEST_CODE);
                                                                    popup.dismiss();
                                                                    popup = null;
                                                                }
                                                            });
                                                        }
                                                        else {
                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    Toast.makeText(MapSearchActivity.this, "방 생성에 실패하였습니다. 네트워크를 확인해주세요.", Toast.LENGTH_LONG).show();
                                                                }
                                                            });
                                                        }
                                                    }
                                                });
                                            } catch(NullPointerException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(MapSearchActivity.this, "방 생성에 실패하였습니다. 네트워크를 확인해주세요.", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                    }
                            );
                        }
                        else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MapSearchActivity.this, "이미 만든 방이 존재합니다.", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MapSearchActivity.this, "방 생성에 실패하였습니다. 네트워크를 확인해주세요.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
    }

    public void popupView() {
        databaseReference.child("users").child(User.getMyInstance().getUserId()).child("chatRoomID").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue(String.class).length() == 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //팝업으로 띄울 커스텀뷰를 설정
                            popupView = getLayoutInflater().inflate(R.layout.layout_community_main_create_chatroom_popup, null);

                            //팝업 객체 생성
                            popup = new PopupWindow(popupView, 1000, LinearLayout.LayoutParams.WRAP_CONTENT);

                            //팝업 뷰 터치 가능하도록 설정
                            popup.setTouchable(true);

                            popup.setOutsideTouchable(true);

                            popup.setFocusable(true);

                            //popupwindow를 parent view 기준으로 띄움
                            popup.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                            //register onclick listener for create and cancel
                            popupView.findViewById(R.id.community_main_chatroom_popup_create).setOnClickListener(MapSearchActivity.this);

                            //make background dim
                            if(android.os.Build.VERSION.SDK_INT <= 22) {
                                WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                                WindowManager.LayoutParams p = (WindowManager.LayoutParams) popupView.getLayoutParams();
                                p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                                p.dimAmount = 0.5f;
                                wm.updateViewLayout(popupView, p);
                            }
                            else {
                                View parent = (View)popup.getContentView().getParent();
                                WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                                WindowManager.LayoutParams p = (WindowManager.LayoutParams)parent.getLayoutParams();
                                p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                                p.dimAmount = 0.5f;
                                wm.updateViewLayout(parent, p);
                            }
                        }
                    });
                }
                else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MapSearchActivity.this, "이미 만든 방이 존재합니다.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case COMMUNITY_CHATROOM_REQUEST_CODE:
                if(resultCode == 9998)
                    Toast.makeText(this, "방이 존재하지 않습니다.", Toast.LENGTH_LONG).show();
                else if(resultCode == 9999)
                    Toast.makeText(this, "방 접속에 실패하였습니다.", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private class ChatroomListPagerAdapter extends PagerAdapter {

        public ChatroomListPagerAdapter() {
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v;

            switch (position) {
                case 0:
                    v = getChatroomList(0);
                    container.addView(v);
                    break;

                case 1:
                    v = getChatroomList(1);
                    container.addView(v);
                    break;

                default:
                    v = null;
            }

            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View pager, Object obj) {
            return pager == obj;
        }

        public ListView getChatroomList(int flag) {
            ArrayList<Chatroom> chatroomList = new ArrayList<>();
            ChatroomListPagerAdapter.ChatRoomAdapter chatroomAdapter = new ChatroomListPagerAdapter.ChatRoomAdapter(MapSearchActivity.this, R.layout.layout_community_main_chatroom, chatroomList);
            final ListView chatroomListView = new ListView(MapSearchActivity.this);
            chatroomListView.setBackgroundColor(Color.parseColor("#66000000"));
            chatroomListView.setDividerHeight(5);
            chatroomListView.setDivider(new ColorDrawable(Color.parseColor("#FF345678")));
            chatroomListView.setAdapter(chatroomAdapter);

            //get data from db
            if (flag == 0) {
                getAroundChatroomListAndDraw(chatroomAdapter);
            } else if (flag == 1) {
                getEnteredChatroomListAndDraw(chatroomAdapter);
            }

            return chatroomListView;
        }

        private void getAroundChatroomListAndDraw(final ChatroomListPagerAdapter.ChatRoomAdapter chatroomAdapter) {
            try {
                ArrayList<String> userIdList = getAroundUserList();
                for (int i = 0; i < userIdList.size(); i++) {
                    databaseReference.child("users").child(userIdList.get(i)).child("chatRoomID").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final String chatroomId = dataSnapshot.getValue(String.class);
                            if(chatroomId != null) {
                                System.out.println("chatroomId length is " + chatroomId.length());
                                if (chatroomId.length() != 0) {
                                    databaseReference.child("ChatManager").child(chatroomId).child("chatroom").addListenerForSingleValueEvent(
                                            new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    chatroomAdapter.addWithChatManagerId(dataSnapshot.getValue(Chatroom.class), chatroomId);
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            }
                                    );
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            } catch(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MapSearchActivity.this, "문제가 생겼습니다. 다시 시도해 주세요", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }

        public ArrayList<String> getAroundUserList() throws ExecutionException, InterruptedException, JSONException {
            HttpHandler httpHandler = new HttpHandler();
            String requestUrl = DataFormat.createGetAroungRequestURL(User.getMyInstance().getLat(),User.getMyInstance().getLon());
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
        private void getEnteredChatroomListAndDraw(final ChatroomListPagerAdapter.ChatRoomAdapter chatroomAdapter) {
            databaseReference.child("users").child(User.getMyInstance().getUserId()).child("myChatroomList").addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};
                            ArrayList<String> chatroomList = dataSnapshot.getValue(t);
                            if (chatroomList != null) {
                                for(int i=0; i<chatroomList.size(); i++) {
                                    final String value = chatroomList.get(i);
                                    databaseReference.child("ChatManager").child(value).child("chatroom").addListenerForSingleValueEvent(
                                            new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    chatroomAdapter.addWithChatManagerId(dataSnapshot.getValue(Chatroom.class), value);
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                    //한개 문제생겼을 때에도 알려줘야하나?
                                                }
                                            }
                                    );
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MapSearchActivity.this, "내가 들어간 방 정보를 가져올 수 없습니다. 네트워크를 확인해주세요.", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
            );
        }

        private class ChatRoomAdapter extends ArrayAdapter<Chatroom> {
            private ArrayList<Chatroom> chatList;
            private ArrayList<String> chatManagerIdList;

            public ChatRoomAdapter(Context context, int chatLayoutId, ArrayList<Chatroom> chatList) {
                super(context, chatLayoutId, chatList);
                chatManagerIdList = new ArrayList<>();
                this.chatList = chatList;
            }

            public void addWithChatManagerId(Chatroom chatroom, String chatManagerId) {
                this.add(chatroom);
                chatManagerIdList.add(chatManagerId);
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = convertView;
                Chatroom chatroom = chatList.get(position);
                String chatManagerId = chatManagerIdList.get(position);

                if (v == null)
                    v = getLayoutInflater().inflate(R.layout.layout_community_main_chatroom, null);

                return drawChatroomToList(chatroom, v, chatManagerId);
            }

            private View drawChatroomToList(Chatroom chatroom, View chatroomView, String chatManagerId) {
                //방마다 구별하기 위해 채팅룸 id를 구별자로 추가.
                chatroomView.setTag(chatManagerId);
                chatroomView.setOnClickListener(MapSearchActivity.this);

                //set chatroom writer profile
                final ImageView profilePicView = (ImageView)chatroomView.findViewById(R.id.community_main_chatroom_writerProfilePic);

                Picasso.with(MapSearchActivity.this).load(chatroom.getChatroomWriterProfilePic()).into(new Target() {

                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        profilePicView.setImageDrawable(new RoundedAvatarDrawable(bitmap, (int)(bitmap.getWidth()*1.5), (int)(bitmap.getWidth()*1.5)));
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });

                final TextView distanceView = (TextView)chatroomView.findViewById(R.id.community_main_chatroom_distance);
                if(!User.getMyInstance().getUserId().equals(chatManagerId)) {
                    databaseReference.child("users").child(chatManagerId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            Location myLoc = new Location("");
                            myLoc.setLatitude(User.getMyInstance().getLat());
                            myLoc.setLongitude(User.getMyInstance().getLon());

                            Location userLoc = new Location("");
                            userLoc.setLatitude(user.getLat());
                            userLoc.setLongitude(user.getLon());
                            distanceView.setText(String.format("%.0f", myLoc.distanceTo(userLoc))+"m");
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else
                    distanceView.setText("0m");

                //set chatroom writer name
                TextView chatroomWriterName = (TextView)chatroomView.findViewById(R.id.community_main_chatroom_writerName);
                if(chatManagerId.equals(User.getMyInstance().getChatRoomID()))
                    chatroomWriterName.setText("나");
                else
                    chatroomWriterName.setText(chatroom.getChatroomWriterName());



                //set chatroom number
                TextView chatroomNumber = (TextView)chatroomView.findViewById(R.id.community_main_chatroom_number);
                if(chatroom.getUserList() != null)
                    chatroomNumber.setText(chatroom.getUserList().size() + "/" + chatroom.getChatroomMaxNumber());
                else
                    chatroomNumber.setText(0 + "/" + chatroom.getChatroomMaxNumber());

                //set chatroom date
                TextView chatroomDate = (TextView)chatroomView.findViewById(R.id.community_main_chatroom_date);
                Date date = chatroom.getChatroomDate();
                SimpleDateFormat format;
                if(date.getHours() > 12)
                    format = new SimpleDateFormat("오후 hh:mm");
                else
                    format = new SimpleDateFormat("오전 hh:mm");
                chatroomDate.setText(format.format(date));

                //set chatroom title
                TextView chatroomTitle = (TextView)chatroomView.findViewById(R.id.community_main_chatroom_title);
                chatroomTitle.setText(chatroom.getChatroomTitle());

                if(chatroom.isEnd()) {
                    profilePicView.setAlpha(25);
                    chatroomWriterName.setAlpha((float)0.1);
                    chatroomDate.setAlpha((float)0.1);
                    chatroomTitle.setAlpha((float)0.1);
                }
                return chatroomView;
            }
        }
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