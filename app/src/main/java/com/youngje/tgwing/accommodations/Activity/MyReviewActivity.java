package com.youngje.tgwing.accommodations.Activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.youngje.tgwing.accommodations.R;

public class MyReviewActivity extends AppCompatActivity {
    GridView mGridView;
    MyReviewAdapter myReviewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_review);
        searchListView();
    }

    public void searchListView() { //파라미터로 리뷰에 해당하는 칸을 넣어서 각각에 해당하는것들을 넣어두기
        //리뷰는 다음을 포함해야함
        //
        //
        // 리뷰 타이틀 String, 리뷰 작성시기 String, 리뷰 이미지 Bitmap, 리뷰 별점 float, 리뷰 내용 String

        mGridView = (GridView) findViewById(R.id.myReviewGrid);
        myReviewAdapter = new MyReviewAdapter(this,R.layout.gridview_item,null);


        //아래는 사용법을 적어놓은 샘플코드입니다.

        Bitmap temp = BitmapFactory.decodeResource(this.getResources(), R.drawable.test_img_palace);
        myReviewAdapter.addItem("경복궁", "1995.04.21", "What a wonderful place", 3.5f, temp);
        myReviewAdapter.addItem("경복궁", "1995.04.21", "What a wonderful place", 3.5f, temp);
        myReviewAdapter.addItem("경복궁", "1995.04.21", "What a wonderful place", 3.5f, temp);
        myReviewAdapter.addItem("경복궁", "1995.04.21", "What a wonderful place", 3.5f, temp);
        myReviewAdapter.addItem("경복궁", "1995.04.21", "What a wonderful place", 3.5f, temp);
        myReviewAdapter.addItem("경복궁", "1995.04.21", "What a wonderful place", 3.5f, temp);
        myReviewAdapter.addItem("경복궁", "1995.04.21", "What a wonderful place", 3.5f, temp);

        mGridView.setAdapter(myReviewAdapter);
        //리뷰 타이틀 String, 리뷰 작성시기 String, 리뷰 내용 String, 리뷰 별점 float, 리뷰 이미지 Bitmap,


        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(MyReviewActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
