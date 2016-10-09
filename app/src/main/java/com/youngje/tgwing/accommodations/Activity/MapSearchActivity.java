package com.youngje.tgwing.accommodations.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.youngje.tgwing.accommodations.R;

import net.daum.mf.map.api.MapView;


public class MapSearchActivity extends AppCompatActivity {

    private ImageView btnMore;
    private View layoutMore;
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_search);

        btnMore = (ImageView) findViewById(R.id.activity_main_btn_more);
        layoutMore = (View) findViewById(R.id.activity_main_btn_category);
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(layoutMore.isShown()) {
                    layoutMore.setVisibility(View.GONE);
                } else {
                    layoutMore.setVisibility(View.VISIBLE);
                }
            }
        });

      // // java code
      // MapView mapView = new MapView(this);
      // mapView.setDaumMapApiKey("b751bc30d4efe34c1ef20e644f655766");

      // ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
      // mapViewContainer.addView(mapView);

        mEditText= (EditText) findViewById(R.id.activity_main_searchbar);
        mEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    //Do your action
                }
                return false;
            }
        });




    }
}
