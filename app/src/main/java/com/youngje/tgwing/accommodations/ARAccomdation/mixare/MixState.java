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
package com.youngje.tgwing.accommodations.ARAccomdation.mixare;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.youngje.tgwing.accommodations.ARAccomdation.ReadDocumentActivity;
import com.youngje.tgwing.accommodations.ARAccomdation.mixare.data.DataSource;
import com.youngje.tgwing.accommodations.ARAccomdation.mixare.reality.PhysicalPlace;
import com.youngje.tgwing.accommodations.ARAccomdation.mixare.render.Matrix;
import com.youngje.tgwing.accommodations.ARAccomdation.mixare.render.MixVector;
import com.youngje.tgwing.accommodations.Util.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.logging.Handler;
import java.util.logging.LogRecord;


// 현재의 상태에 관한 클래스
public class MixState {

    // 각 상태에 대한 상수값 설정
    public static int NOT_STARTED = 0;
    public static int PROCESSING = 1;
    public static int READY = 2;
    public static int DONE = 3;

    public static boolean enterNaviEnd = false;

    public static Toast myToast;
    private static final int MSG_TOAST_THREAD = 1;

    private Thread loopThread;

    int nextLStatus = MixState.NOT_STARTED;    // 다음 상태
    String downloadId;    // 다운로드할 ID

    private float curBearing;    // 현재의 방위각
    private float curPitch;        // 현재의 장치각(?)

    private boolean detailsView;    // 디테일 뷰가 표시 중인지 여부

    // 이벤트 처리
    public boolean handleEvent(MixContext ctx, String onPress, String title, PhysicalPlace log) {

        DialogSelectOption(ctx, title, log, onPress);


        //DialogSelectOption(ctx, title, log, onPress);
        return true;
    }

    public boolean handleEventDocumentAr(MixContext ctx, DocumentARMarker documentMarker) {

        Intent readDocumentIntent = new Intent(ctx, ReadDocumentActivity.class);
        DocumentARMarker.selectedMarker = documentMarker;
        ctx.startActivity(readDocumentIntent);

        return true;
    }



    // 이벤트 처리
   // public boolean handleEvent2(MixContext ctx, DocumentARMarker documentMarker) {
   //
//
   //     boolean evtHandled= false;
   //     Intent readDocumentIntent = new Intent(ctx, ReadDocumentActivity.class);
   //     DocumentARMarker.selectedMarker = documentMarker;
   //     ctx.startActivity(readDocumentIntent);
//
   //     evtHandled = true;
//
   //     //DialogSelectOption(ctx, title, log, onPress);
   //     return evtHandled;
   // }

    public void DialogSelectOption(final MixContext ctx, final String markerTitle, final PhysicalPlace log, final String onPress) {
        final String items[] = {"상세 정보 보기", "네비게이션" };
        AlertDialog.Builder ab = new AlertDialog.Builder(ctx);
        ab.setTitle(markerTitle);
        ab.setItems(items,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // 프로그램을 종료한다
                        Toast.makeText(ctx,
                                items[id] + " 선택했습니다.",
                                Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                        if (id == 0) {
                            try {
                                ctx.loadMixViewWebPage(onPress);

                            } catch (Exception e) {

                            }
                        } else if (id == 1) {
                            // 네비게이션
                            final Intent naviBroadReceiver = new Intent();
                            naviBroadReceiver.setAction("NAVI");

                            Log.i("계속된다","으어계속된다");

                            loopThread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    // enterNaviEnd 초기엔 펄스 누르면 네비 종료 버튼 누르면 트루
                                    while (!Thread.currentThread().isInterrupted() && !MixState.enterNaviEnd) {
                                        try {
                                            Log.i("계속된다", "으어계속된다");

                                            String url = DataSource.createNaverMapRequestURL(ctx.getCurrentLocation().getLongitude(), ctx.getCurrentLocation().getLatitude(), log.getLongitude(), log.getLatitude());
                                            String result = "";
                                            String guide = "";

                                            result = new HttpHandler().execute(url).get();
                                            Log.i("result!!", result);

                                            guide = parsingNaverNaviJson(result);
                                            Log.i("guide!!", guide);

                                            if (!guide.equals("end")) {
                                                // 브로드 캐스트 리시버로 전달하는 부분
                                                naviBroadReceiver.putExtra("GUIDE", guide);
                                                ctx.sendBroadcast(naviBroadReceiver);
                                            } else {
                                                guide = "목적지에 가까워져 네비게이션이 자동종료됩니다.";
                                                naviBroadReceiver.putExtra("GUIDE", guide);
                                                ctx.sendBroadcast(naviBroadReceiver);
                                                loopThread.interrupt();
                                            }
                                            Thread.sleep(5000);

                                        } catch (ExecutionException e) {
                                            e.printStackTrace();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                }

                            });
                            loopThread.start();
                          }

                        }
                    });
        // 다이얼로그 생성
        AlertDialog alertDialog = ab.create();
        // 다이얼로그 보여주기
        alertDialog.show();
    }

    public String parsingNaverNaviJson(String naviStirng) throws JSONException {
        String temp;
        JSONObject jObject = new JSONObject(naviStirng);
        int distance = jObject.getJSONObject("result").getJSONObject("summary").getInt("totalDistance");

        if(distance < 40) {
            temp = "end";
            return temp;
        }

        JSONArray jArray = jObject.getJSONObject("result").getJSONArray("route").getJSONObject(0).getJSONArray("point");
        JSONObject firstRoute = jArray.getJSONObject(1);

        if(firstRoute == null) {
            temp = "end";
            return temp;
        }

        else {
            temp = firstRoute.getJSONObject("guide").getString("name");
            Log.i("temp",temp);



        }

        return temp;

    }


    // 현재의 방위각을 리턴
    public float getCurBearing() {
        return curBearing;
    }

    // 현재의 장치각을 리턴
    public float getCurPitch() {
        return curPitch;
    }

    // 디테일 뷰의 표시 여부를 리턴
    public boolean isDetailsView() {
        return detailsView;
    }

    // 디테일 뷰의 표시 여부를 설정
    public void setDetailsView(boolean detailsView) {
        this.detailsView = detailsView;
    }

    // 장치각과 방위각을 계산
    public void calcPitchBearing(Matrix rotationM) {
        MixVector looking = new MixVector();
        rotationM.transpose();
        looking.set(1, 0, 0);
        looking.prod(rotationM);
        this.curBearing = (int) (MixUtils.getAngle(0, 0, looking.x, looking.z) + 360) % 360;

        rotationM.transpose();
        looking.set(0, 1, 0);
        looking.prod(rotationM);
        this.curPitch = -MixUtils.getAngle(0, 0, looking.y, looking.z);
    }



    //class NonUIThreadHandler extends Handler {
//
    //    public void handleMessage(Message msg) {
    //        switch(msg.what) {
    //            case MSG_TOAST_THREAD:
    //                myToast.setText((String) msg.obj);
    //                myToast.show();
    //        }
    //    }
//
    //    @Override
    //    public void publish(LogRecord logRecord) {
//
    //    }
//
    //    @Override
    //    public void flush() {
//
    //    }
//
    //    @Override
    //    public void close() throws SecurityException {
//
    //    }
    //}



}
