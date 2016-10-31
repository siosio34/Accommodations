package com.youngje.tgwing.accommodations.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.youngje.tgwing.accommodations.Data.DataFormat;
import com.youngje.tgwing.accommodations.Data.DaumDataProcessor;
import com.youngje.tgwing.accommodations.Data.NavigationDataProcessor;
import com.youngje.tgwing.accommodations.Marker;
import com.youngje.tgwing.accommodations.R;
import com.youngje.tgwing.accommodations.Util.HttpHandler;
import com.youngje.tgwing.accommodations.Util.LocationUtil;

import org.json.JSONException;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.os.Build.VERSION_CODES.M;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 8001;
    private static final int PERMISSIONS_REQUEST_CODE_ACCESS_FINE_LOCATION = 8002;
    private static final int PERMISSIONS_REQUEST_CODE_ACCESS_READ = 8003;
    private static final int PERMISSIONS_REQUEST_CODE_ACCESS_WRITE = 8004;
    private static final int PEMIISSIONS_REQUEST_ALL = 8005;
    Intent intent;

    private Location myLoc;
    LocationUtil locationUtil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkAndRequestPemission();

    }


    private void checkAndRequestPemission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION
                        , Manifest.permission.ACCESS_FINE_LOCATION
                        , Manifest.permission.READ_EXTERNAL_STORAGE
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE}, PEMIISSIONS_REQUEST_ALL);

            } else {
                Log.i("여기", "여기여기");
                getGPSFirstTime();
                startActivity(new Intent(this, UserSignInActivity.class));

            }
        } else {
            getGPSFirstTime();
            startActivity(new Intent(this, UserSignInActivity.class));

        }
    }

    private void getGPSFirstTime() {

        new LocationUtil(this); // 현재위치 한번 받아옴.
        myLoc = LocationUtil.curlocation;
        Log.i("위치", String.valueOf(myLoc.getLongitude()));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PEMIISSIONS_REQUEST_ALL:
                getGPSFirstTime();
                startActivity(new Intent(this, UserSignInActivity.class));
            default:
                intent = new Intent(this, UserSignInActivity.class);
                startActivity(new Intent(this, UserSignInActivity.class));
                break;
        }
        // TODO: 2016. 10. 1. 완성해야될 필요성 있으
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
}
