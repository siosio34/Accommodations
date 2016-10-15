package com.youngje.tgwing.accommodations.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;

import com.youngje.tgwing.accommodations.R;

public class WriteReviewActivity extends AppCompatActivity {

    ImageButton backButton;
    ImageButton writeButton;
    EditText inputText;

    ImageButton cameraButton;
    ImageButton gallaryButton;
    ImageButton camCorderButton;
    RatingBar ratingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);
        findVieByidwWriteReviewActivity();

    }

    public void findVieByidwWriteReviewActivity() {
        backButton = (ImageButton) findViewById(R.id.backbutton);
        writeButton = (ImageButton) findViewById(R.id.writeReview);
        inputText = (EditText) findViewById(R.id.inputText);
        cameraButton = (ImageButton) findViewById(R.id.cameraButton);
        gallaryButton = (ImageButton) findViewById(R.id.galleryButton);
        camCorderButton = (ImageButton) findViewById(R.id.camcorderButton);
        ratingBar = (RatingBar) findViewById(R.id.ratinbar);

    }





}
