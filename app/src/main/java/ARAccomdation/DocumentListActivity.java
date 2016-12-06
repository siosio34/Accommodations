package ARAccomdation;

import android.app.ActionBar;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.VideoView;

import com.youngje.tgwing.accommodations.R;


public class DocumentListActivity extends AppCompatActivity {
    private RelativeLayout topLayout = null;

    private int commentListLayoutPadding = 16;
    //comment
    private int commentLayoutPadding = 5;
    private int commentCircleSize = 80;
    private int commentBorder = 2;
    private int commentNameSize = 16;
    private int commentSize = 16;
    private int commentPadding = 10;
    private int commentNameMarginLeft = 10;
    private int commentNameMarginTop = 20;
    private int commentIconMarginRight = 10;
    private int commentIconMarginTop = 10;
    private int iconTextSize = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        topLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams topLayoutParams = new RelativeLayout.LayoutParams(size.x, size.y);
        topLayout.setLayoutParams(topLayoutParams);
        setContentView(topLayout);

        ScrollView documentListScroll = new ScrollView(this);
        documentListScroll.setBackgroundColor(Color.parseColor("#E6E6E6"));
        RelativeLayout.LayoutParams documentListScrollParams = new RelativeLayout.LayoutParams((int)(size.x*0.8), (int)(size.y*0.8));
        documentListScrollParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        documentListScroll.setLayoutParams(documentListScrollParams);
        topLayout.addView(documentListScroll);

        LinearLayout documentListLayout = new LinearLayout(this);
        documentListLayout.setPadding(commentListLayoutPadding, commentListLayoutPadding, commentListLayoutPadding, commentListLayoutPadding);
        LinearLayout.LayoutParams documentListLayoutParams =  new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        documentListLayout.setOrientation(LinearLayout.VERTICAL);
        documentListLayout.setLayoutParams(documentListLayoutParams);
        documentListScroll.addView(documentListLayout);

        Bitmap testIcon = BitmapFactory.decodeResource(getResources(), R.drawable.busicon);
        for(int i=0; i<10; i++)
            documentListLayout.addView(makeDocumentLayout(testIcon, testIcon, null, "조영제", "ㅁㄴㅇㅁㅇㅁㅇㅁㄴㅇㅁㄴㅇㅁㄴㅇㅁㄴㅇㅁㄴㅇㅁㅇㄴ"));
    }

    public RelativeLayout makeDocumentLayout(Bitmap profileImg, Bitmap pic, Uri video, String name, String comment) {
        RelativeLayout documentLayout = new RelativeLayout(this);
        documentLayout.setPadding(commentLayoutPadding, commentLayoutPadding, commentLayoutPadding, commentLayoutPadding);
        RelativeLayout.LayoutParams documentLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        documentLayout.setLayoutParams(documentLayoutParams);

        TextView nameView = new TextView(this);
        RelativeLayout.LayoutParams nameViewParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        nameViewParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        nameViewParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        nameViewParams.setMargins(commentCircleSize+commentNameMarginLeft, commentNameMarginTop, 0, 0);
        nameView.setLayoutParams(nameViewParams);
        nameView.setText(name);
        nameView.setTextColor(0x40000000);
        nameView.setTypeface(Typeface.DEFAULT_BOLD);
        nameView.setTextSize(commentNameSize);
        documentLayout.addView(nameView);

        LinearLayout iconsLayout = new LinearLayout(this);
        iconsLayout.setOrientation(LinearLayout.HORIZONTAL);
        RelativeLayout.LayoutParams iconLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        iconLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        iconLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        iconLayoutParams.setMargins(0, commentIconMarginTop, commentIconMarginRight, 0);
        iconsLayout.setLayoutParams(iconLayoutParams);
        documentLayout.addView(iconsLayout);

        LinearLayout iconWithmeLayout = new LinearLayout(this);
        iconWithmeLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams iconWithmeLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        iconWithmeLayout.setLayoutParams(iconWithmeLayoutParams);
        iconWithmeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ImageView iconWithme = new ImageView(this);
        iconWithme.setBackgroundResource(R.drawable.icon_emotion_bewithme2);
        iconWithmeLayout.addView(iconWithme);

        TextView iconWithmeTextView = new TextView(this);
        LinearLayout.LayoutParams iconWithmeTextViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        iconWithmeTextView.setText("10");
        iconWithmeTextView.setTextColor(Color.parseColor("#000000"));
        iconWithmeTextView.setTextSize(iconTextSize);
        iconWithmeTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        iconWithmeTextView.setLayoutParams(iconWithmeTextViewParams);
        iconWithmeLayout.addView(iconWithmeTextView);

        iconsLayout.addView(iconWithmeLayout);

        LinearLayout iconNotNiceLayout = new LinearLayout(this);
        iconNotNiceLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams iconNotNiceLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        iconNotNiceLayout.setLayoutParams(iconNotNiceLayoutParams);

        ImageView iconNotNice = new ImageView(this);
        iconNotNice.setBackgroundResource(R.drawable.icon_emotion_notnice2);
        iconNotNiceLayout.addView(iconNotNice);
        iconNotNiceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        TextView iconNotNiceTextView = new TextView(this);
        LinearLayout.LayoutParams iconNotNiceTextViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        iconNotNiceTextView.setText("10");
        iconNotNiceTextView.setTextColor(Color.parseColor("#000000"));
        iconNotNiceTextView.setTextSize(iconTextSize);
        iconNotNiceTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        iconNotNiceTextView.setLayoutParams(iconNotNiceTextViewParams);
        iconNotNiceLayout.addView(iconNotNiceTextView);

        iconsLayout.addView(iconNotNiceLayout);

        LinearLayout iconSeeyaLayout = new LinearLayout(this);
        iconSeeyaLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams iconSeeyaLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        iconSeeyaLayout.setLayoutParams(iconSeeyaLayoutParams);
        iconSeeyaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ImageView iconSeeya = new ImageView(this);
        iconSeeya.setBackgroundResource(R.drawable.icon_emotion_seeya2);
        iconSeeyaLayout.addView(iconSeeya);

        TextView iconSeeyaTextView = new TextView(this);
        LinearLayout.LayoutParams iconSeeyaTextViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        iconSeeyaTextView.setText("10");
        iconSeeyaTextView.setTextColor(Color.parseColor("#000000"));
        iconSeeyaTextView.setTextSize(iconTextSize);
        iconSeeyaTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        iconSeeyaTextView.setLayoutParams(iconSeeyaTextViewParams);
        iconSeeyaLayout.addView(iconSeeyaTextView);

        iconsLayout.addView(iconSeeyaLayout);

        LinearLayout contentLayout = new LinearLayout(this);
        contentLayout.setPadding(commentPadding, commentPadding, commentPadding, commentPadding);
        contentLayout.setBackgroundColor(0xFFFFFFFF);
        contentLayout.setOrientation(LinearLayout.VERTICAL);
        RelativeLayout.LayoutParams contentLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        contentLayoutParams.setMargins(0, 9*commentCircleSize/10, 0, 0);
        contentLayout.setLayoutParams(contentLayoutParams);
        documentLayout.addView(contentLayout);

        if(pic != null) {
            ImageView imgView = new ImageView(this);
            LinearLayout.LayoutParams imgViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            imgView.setLayoutParams(imgViewParams);
            imgView.setBackground(new BitmapDrawable(pic));
            contentLayout.addView(imgView);
        }

        if(video != null) {
            VideoView videoView = new VideoView(this);
            videoView.setVideoURI(video);
            contentLayout.addView(videoView);
        }

        TextView commentView = new TextView(this);
        LinearLayout.LayoutParams commentViewParams = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        commentView.setLayoutParams(commentViewParams);
        commentView.setText(comment);
        commentView.setTextSize(commentSize);
        commentView.setTextColor(Color.BLACK);
        commentView.setGravity(Gravity.LEFT | Gravity.TOP);
        contentLayout.addView(commentView);

        ImageView profile = new ImageView(this);
        RelativeLayout.LayoutParams profileParams = new RelativeLayout.LayoutParams(commentCircleSize, commentCircleSize);
        profileParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        profileParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        Drawable img = new BitmapDrawable(getCircularBitmapWithWhiteBorder(profileImg, commentCircleSize, commentCircleSize, commentBorder));
        profile.setBackground(img);
        profile.setLayoutParams(profileParams);
        documentLayout.addView(profile);

        return documentLayout;
    }

    public Bitmap getCircularBitmapWithWhiteBorder(Bitmap bitmap, int b_width, int b_height, int borderWidth) {
        if (bitmap == null || bitmap.isRecycled()) {
            return null;
        }

        final int width =  b_width+ borderWidth;
        final int height = b_height + borderWidth;

        Bitmap canvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);

        Canvas canvas = new Canvas(canvasBitmap);
        float radius = width > height ? ((float) height) / 2f : ((float) width) / 2f;
        canvas.drawCircle(width / 2, height / 2, radius, paint);
        paint.setShader(null);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(borderWidth);
        canvas.drawCircle(width / 2, height / 2, radius - borderWidth / 2, paint);
        return canvasBitmap;
    }
}