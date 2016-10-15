package com.youngje.tgwing.accommodations.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.youngje.tgwing.accommodations.Data.DataFormat;
import com.youngje.tgwing.accommodations.R;
import com.youngje.tgwing.accommodations.Util.HttpHandler;
import com.youngje.tgwing.accommodations.Util.LocationUtil;
import com.youngje.tgwing.accommodations.Util.OkhttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 8001;
    private static final int PERMISSIONS_REQUEST_CODE_ACCESS_FINE_LOCATION = 8002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean checkPermission = false;
        new LocationUtil(this); // 현재위치 한번 받아옴.


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION);
            checkPermission = true;

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_CODE_ACCESS_FINE_LOCATION);
            checkPermission = true;
        }

        if (!checkPermission) {
            Intent intent = new Intent(this, UserSignInActivity.class);
            startActivity(intent);
        }


        String createUrl;
        //--- 로케이션 테스트 and daum category search test 완료됨.
        Location culoc = LocationUtil.curlocation;
        HttpHandler httpHandler = new HttpHandler();
        createUrl = DataFormat.createSeoulOpenAPIRequestURL(DataFormat.DATATYPE.WIFI, culoc.getLatitude(), culoc.getLongitude());

        try {
            String result = httpHandler.execute(createUrl).get();
            // TODO: 2016. 10. 15. null값일때 예외처리 해야됨
            if(result != null)
                Log.i("temp3", result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


    }

    // 파이어베이스 데이터 많아지면 웹에서 수정,삭제가 힘듬 이 함수 필요
    public void firebaseUtilNodeAdd() {

        //  노드 추가

        // FirebaseDatabase database = FirebaseDatabase.getInstance();
        // DatabaseReference myRef = database.getReference();
        // myRef.child("review").child("temp").setValue("temp");
    }

    public void firebaseUtilNodeDelete() {

        // 노드 삭제
        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference myRef = database.getReference("temp");
        //myRef.removeValue();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION:
            case PERMISSIONS_REQUEST_CODE_ACCESS_FINE_LOCATION:
            default:
                Intent intent = new Intent(this, UserSignInActivity.class);
                startActivity(intent);
                break;
        }
        // TODO: 2016. 10. 1. 완성해야될 필요성 있으
    }
}
