package com.youngje.tgwing.accommodations.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.youngje.tgwing.accommodations.R;

public class ReadMarkerActivity extends Activity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_marker);

        imageView = (ImageView) findViewById(R.id.addReviewButton);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), WriteReviewActivity.class));
            }
        });

    }
}
