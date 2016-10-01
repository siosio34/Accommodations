package com.youngje.tgwing.accommodations.Activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.youngje.tgwing.accommodations.R;

public class Permissions extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 9001;
    private Context context;

    public Permissions() {

    }

    public Permissions(Context context) {
        this.context = context;
        checkGPSPermission();
    }

    public void checkGPSPermission() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION);
        }

    }




}
