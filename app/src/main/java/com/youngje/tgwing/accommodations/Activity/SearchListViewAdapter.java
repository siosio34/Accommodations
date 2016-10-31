package com.youngje.tgwing.accommodations.Activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.youngje.tgwing.accommodations.Data.DataFormat;
import com.youngje.tgwing.accommodations.Data.NavigationDataProcessor;
import com.youngje.tgwing.accommodations.Marker;
import com.youngje.tgwing.accommodations.Navi;
import com.youngje.tgwing.accommodations.NaviXY;
import com.youngje.tgwing.accommodations.R;
import com.youngje.tgwing.accommodations.Util.HttpHandler;
import com.youngje.tgwing.accommodations.Util.LocationUtil;

import org.json.JSONException;
import org.json.JSONObject;

import static com.youngje.tgwing.accommodations.Marker.markerList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by aswoo on 2016-10-15.
 */

public class SearchListViewAdapter extends BaseAdapter {
    private static final String TAG = "SearchListViewAdapter";
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private List<SearchListViewItem> listViewItemList = new ArrayList<SearchListViewItem>();
    private Location curlocate;
    private Activity activity;
    private boolean isNavi = false;
    private Thread loopThread;

    // ListViewAdapter의 생성자
    public SearchListViewAdapter(Activity activity) {
        this.activity = activity;
    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.ratingBar.setOnRatingBarChangeListener(onRatingChangedListener(holder, position));
        holder.ratingBar.setTag(position);
        holder.ratingBar.setRating(getItem(position).getRatingStar());


        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        SearchListViewItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        holder.titleTextView.setText(getItem(position).getTitle());
        holder.categoryView.setText(getItem(position).getCategory());
        holder.ReviewNumView.setText(getItem(position).getReviewNum());
        holder.distanceView.setText(getItem(position).getDistance());
        holder.ratingStarView.setText(new Float(getItem(position).getNumOfStar()).toString());

        holder.navigationImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isNavi) {
                    isNavi = true;
                    ((MapSearchActivity)activity).navimode(isNavi, position);
                    holder.navigationImageView.setImageResource(R.drawable.cancel_navi);

                    Log.i("POSITION", String.valueOf(position));
                    final Marker marker = markerList.get(position);
                    Log.i("POSITION1", marker.getId());
                    Log.i("POSITION2", marker.getTitle());
                    Log.i("POSITION3", marker.getMarkerType());

                    loopThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                while (!Thread.currentThread().isInterrupted()) {
                                    onNavigation(marker.getLat(), marker.getLon());
                                    Thread.sleep(5000);
                                }
                            } catch(Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    loopThread.start();
                }
                else {
                    isNavi = false;
                    ((MapSearchActivity)activity).navimode(isNavi, position);
                    holder.navigationImageView.setImageResource(R.drawable.ic_icon_route);
                    loopThread.interrupt();
                }
            }
        });
        return convertView;
    }

    public void onNavigation(double endLat,double endLon) throws ExecutionException, InterruptedException, JSONException {
        //요기까지

        // TODO: 2016. 10. 25. navigation
        String fromCoord = "WGS84";
        String toCoord = "WCONGNAMUL";
        String type = "json";
        String apikey = activity.getString(R.string.daum_api_key);

        LocationUtil.setActivity(activity);
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
        daumRouteRequestUrl = DataFormat.createNavigationAPIRequestURL(DataFormat.DATATYPE.NAVI,daumStartLon,daumStartLat,daumEndLon,daumEndLat);

        String result = new HttpHandler().execute(daumRouteRequestUrl).get();
        final Navi navi = NavigationDataProcessor.load(result,DataFormat.DATATYPE.NAVI);

        //테스트를 위한 코드입니다. 맵 위에 선을 그려줍니다.
        List<NaviXY> list = navi.getListXY();
        List<pointOnMap> tempArray = new ArrayList<>();
        pointOnMap startPoint = new pointOnMap(list.get(0).getLat(), list.get(0).getLon());
        for(int i=1; i<list.size(); i++)
            tempArray.add(new pointOnMap(list.get(i).getLat(), list.get(i).getLon()));

        //// TODO: 2016. 10. 24. 네비를 구현할수 있을것 같다
        ((MapSearchActivity)activity).drawLineFromStartPoint(startPoint,tempArray);

        if(navi == null || navi.getLength() < 30) {
            // TODO: 2016. 10. 25. 종료 메시지 남겨야된다.
            // TODO: 2016. 10. 25. 길 안내를 할수 없거나
        }
        else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity,navi.getGuideMent(),Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private RatingBar.OnRatingBarChangeListener onRatingChangedListener(final ViewHolder holder, final int position) {
        return new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                SearchListViewItem item = getItem(position);
                item.setRatingStar(v);
                Log.i("Adapter", "star: " + v);
            }
        };
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public SearchListViewItem getItem(int position) {
        return listViewItemList.get(position);
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String distance, String title, String category,float ratingbar, String ReviewNum) {
        SearchListViewItem item = new SearchListViewItem();

        item.setDistance(distance);
        item.setTitle(title);
        item.setCateGory(category);
        item.setReviewNum(ReviewNum);
        item.setRatingStar(ratingbar);
        item.setNumOfStar(ratingbar);
        listViewItemList.add(item);
    }

    private class ViewHolder {

        private TextView distanceView;
        private RatingBar ratingBar;
        private TextView titleTextView;
        private TextView categoryView;
        private TextView ReviewNumView;
        private TextView ratingStarView;
        private ImageView navigationImageView;


        public ViewHolder(View view) {
            ratingBar = (RatingBar) view.findViewById(R.id.listview_rating_bar);
            titleTextView = (TextView) view.findViewById(R.id.listview_title);
            categoryView = (TextView) view.findViewById(R.id.listview_category);
            ReviewNumView = (TextView) view.findViewById(R.id.listview_rating_num);
            distanceView = (TextView) view.findViewById(R.id.listview_distance);
            ratingStarView = (TextView) view.findViewById(R.id.listview_rating_score);
            navigationImageView = (ImageView) view.findViewById(R.id.navigation_button);

        }
    }
}
