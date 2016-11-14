package com.youngje.tgwing.accommodations.Util;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.VideoView;

/**
 * Created by joyeongje on 2016. 11. 14..
 */
public class MyVideoView extends VideoView {
    public MyVideoView(Context context) {
        super(context);
        init(context);
    }

    public MyVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
        int deviceWidth = displayMetrics.widthPixels;
        int deviceHeight = displayMetrics.heightPixels;
        setMeasuredDimension(deviceWidth, deviceHeight);
    }
}