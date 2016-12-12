package com.youngje.tgwing.accommodations.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by joyeongje on 2016. 10. 18..
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

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        SearchListViewItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        holder.titleTextView.setText(getItem(position).getTitle());
        holder.categoryView.setText(getItem(position).getCategory());
        holder.distanceView.setText(getItem(position).getDistance());


        /*
        holder.navigationImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isNavi) {
                    isNavi = true;
                    ((MapSearchActivity)activity).navimode(isNavi, position);
                    holder.navigationImageView.setImageResource(R.drawable.cancel_navi);
                    final Marker marker = markerList.get(position);

                    try {
                        //// TODO: 2016. 12. 1. navigation 함수 작동
                        onNavigation(context,marker.getLat(), marker.getLon());
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                else {
                    isNavi = false;
                    ((MapSearchActivity)activity).navimode(isNavi, position);
                    holder.navigationImageView.setImageResource(R.drawable.ic_icon_route);

                }
            }
        });
        */
        return convertView;
    }
    /*
    //public void onNavigation(Context context,double endLat,double endLon) throws ExecutionException, InterruptedException, JSONException, IOException {
        //요기까지

        curlocate = LocationUtil.curlocation;
        String naverRouteRequestUrl = DataFormat.createNaverNavigationgAPIRequestURL(DataFormat.DATATYPE.NAVI,curlocate.getLatitude(),curlocate.getLongitude(),endLat,endLon);
        Log.i("naverurl", naverRouteRequestUrl);

        // TODO: 2016. 12. 6. 네이버 네이버 

        // TODO: 2016. 12. 4. 영상 실시간 안되면 링크라도 던져주자! 
       // String result = new HttpHandler().execute(naverRouteRequestUrl).get();

         // String fromCoord = "WGS84";
         // String toCoord = "WCONGNAMUL";
         // String type = "json";
         // String apikey = activity.getString(R.string.daum_api_key);
        //
         // LocationUtil.setActivity(activity);
         // curlocate = LocationUtil.curlocation;
        //
         // String startCreateUrl;
         // String endCreateUrl;
         // String daumRouteRequestUrl;


         // // TODO: 2016. 11. 28.  시작 경도와 위도가 안바뀌는거 같은데
         // startCreateUrl = DataFormat.changeCoordRequestURL(curlocate.getLatitude(),curlocate.getLongitude(),fromCoord,toCoord,type,apikey);
         // endCreateUrl = DataFormat.changeCoordRequestURL(endLat,endLon,fromCoord,toCoord,type,apikey);
        //
         // // 주소변환후 데이터 저장
         // String myLocationResult = new HttpHandler().execute(startCreateUrl).get();
         // String DestinationResult = new HttpHandler().execute(endCreateUrl).get();
         // JSONObject startJsonObject = new JSONObject(myLocationResult);
         // JSONObject endJsonObject = new JSONObject(DestinationResult);
        //
         // final Double daumStartLat = startJsonObject.getDouble("y");
         // final Double daumStartLon = startJsonObject.getDouble("x");
         // Double daumEndLat = endJsonObject.getDouble("y");
         // Double daumEndLon = endJsonObject.getDouble("x");

          // 변환된 주소로 다음에 데이터 요청
          //daumRouteRequestUrl =

                // 네비 데이터 파싱

                // 일단 파일로하자

              //  AssetManager assetManager = context.getResources().getAssets();
        //
              //  AssetManager.AssetInputStream assetInputStream = (AssetManager.AssetInputStream)assetManager.open("navigation.json");
        //
              //  BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(assetInputStream));
        //
              //  StringBuilder sb = new StringBuilder();
        //
              //  int bufferSize = 1024 * 1024;
              //  char readBuf[] = new char[bufferSize];
              //  int resultSize = 0;
        //
              //  while((resultSize = bufferedReader.read(readBuf)) != -1) {
              //      if(resultSize == bufferSize)
              //          sb.append(readBuf);
              //      else {
              //          for(int i = 0; i<resultSize ; i++) {
              //              sb.append(readBuf[i]);
              //          }
              //      }
        //
              //  }

              //  String result = sb.toString();
            //   Log.i("resultStringNavi", result);

            //   Navi navi = NavigationDataProcessor.load(result,DataFormat.DATATYPE.NAVI);
            //   //final String guideText = navi.getGuideMent();

            //   // 스탈팅 포인트와 종점 포인트를 정함.
            //   List<NaviXY> list = navi.getListXY();
            //   List<pointOnMap> tempArray = new ArrayList<>();
            //   pointOnMap startPoint = new pointOnMap(list.get(0).getLat(), list.get(0).getLon());
            //   for(int i=1; i<list.size(); i++)
            //       tempArray.add(new pointOnMap(list.get(i).getLat(), list.get(i).getLon()));


            //   // 그림을 갱신함.

            //   ((MapSearchActivity)activity).drawLineFromStartPoint(startPoint,tempArray);


    }
    */

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
            titleTextView = (TextView) view.findViewById(R.id.listview_title);
            categoryView = (TextView) view.findViewById(R.id.listview_category);
            distanceView = (TextView) view.findViewById(R.id.listview_distance);


        }
    }
}