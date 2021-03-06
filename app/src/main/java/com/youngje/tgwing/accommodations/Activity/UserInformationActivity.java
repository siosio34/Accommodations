package com.youngje.tgwing.accommodations.Activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.youngje.tgwing.accommodations.R;
import com.youngje.tgwing.accommodations.User;

/**
 * Created by joyeongje on 2016. 10. 18..
 */

public class UserInformationActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private ImageView buttonBack;
    private TextView userEmail;
    private ImageView userThumbnail;
    private TextView userName;
    private String languageChoice;
    private EditText userMessage;
    private ImageView buttonSave;

    Button selCountryButton = null;
    CountryAdapter countryAdapter = null;
    ListView countryListView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        final User currrentUser = User.getMyInstance();

        buttonBack = (ImageView) findViewById(R.id.info_backbutton);

        userEmail = (TextView) findViewById(R.id.info_user_email);
        userEmail.setText(currrentUser.getUserEmail());

        userThumbnail = (ImageView) findViewById(R.id.info_user_thumbnail);
        Picasso.with(getApplicationContext()).load(currrentUser.getImageUri()).into(userThumbnail);

        userName = (TextView) findViewById(R.id.info_user_name);
        userName.setText(currrentUser.getUserName());

        userMessage = (EditText) findViewById(R.id.info_user_message);
        userMessage.setText(currrentUser.getUserDescription());

        buttonSave = (ImageView) findViewById(R.id.info_button_start);

        countryAdapter = new CountryAdapter();
        View inflatedView = getLayoutInflater().inflate(R.layout.countrydialog, null);
        countryListView = (ListView) inflatedView.findViewById(R.id.countryList);

        String[] countryStringArray = getResources().getStringArray(R.array.CountryStrings);
        Bitmap temp1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.south_korea);
        countryAdapter.addItem(countryStringArray[0],temp1,"ko");
        Bitmap temp2 = BitmapFactory.decodeResource(this.getResources(), R.drawable.japan);
        countryAdapter.addItem(countryStringArray[1],temp2,"ja");
        Bitmap temp3 = BitmapFactory.decodeResource(this.getResources(), R.drawable.china);
        countryAdapter.addItem(countryStringArray[2],temp3,"zh");
        Bitmap temp4 = BitmapFactory.decodeResource(this.getResources(), R.drawable.united_states);
        countryAdapter.addItem(countryStringArray[3],temp4,"en");
        Bitmap temp5 = BitmapFactory.decodeResource(this.getResources(), R.drawable.france);
        countryAdapter.addItem(countryStringArray[4],temp5,"fr");
        Bitmap temp6 = BitmapFactory.decodeResource(this.getResources(), R.drawable.germany);
        countryAdapter.addItem(countryStringArray[5],temp6,"de");
        Bitmap temp7 = BitmapFactory.decodeResource(this.getResources(), R.drawable.spain);
        countryAdapter.addItem(countryStringArray[6],temp7,"es");
        Bitmap temp8 = BitmapFactory.decodeResource(this.getResources(), R.drawable.italy);
        countryAdapter.addItem(countryStringArray[7],temp8,"it");
        Bitmap temp9 = BitmapFactory.decodeResource(this.getResources(), R.drawable.russia);
        countryAdapter.addItem(countryStringArray[8],temp9,"ru");
        Bitmap temp10 = BitmapFactory.decodeResource(this.getResources(), R.drawable.india);
        countryAdapter.addItem(countryStringArray[9],temp10,"hi");
        Bitmap temp11 = BitmapFactory.decodeResource(this.getResources(), R.drawable.brazil);
        countryAdapter.addItem(countryStringArray[10],temp11,"pt");
        Bitmap temp12 = BitmapFactory.decodeResource(this.getResources(), R.drawable.poland);
        countryAdapter.addItem(countryStringArray[11],temp12,"pl");
        Bitmap temp13 = BitmapFactory.decodeResource(this.getResources(), R.drawable.thailand);
        countryAdapter.addItem(countryStringArray[12],temp13,"th");
        Bitmap temp14 = BitmapFactory.decodeResource(this.getResources(), R.drawable.vietnam);
        countryAdapter.addItem(countryStringArray[13],temp14,"vi");
        Bitmap temp15 = BitmapFactory.decodeResource(this.getResources(), R.drawable.netherlands);
        countryAdapter.addItem(countryStringArray[14],temp15,"nl");
        Bitmap temp16 = BitmapFactory.decodeResource(this.getResources(), R.drawable.greece);
        countryAdapter.addItem(countryStringArray[15],temp16,"el");
        Bitmap temp17 = BitmapFactory.decodeResource(this.getResources(), R.drawable.sweden);
        countryAdapter.addItem(countryStringArray[16],temp17,"sv");
        Bitmap temp18 = BitmapFactory.decodeResource(this.getResources(), R.drawable.saudi_arabia);
        countryAdapter.addItem(countryStringArray[17],temp18,"ar");
        Bitmap temp19 = BitmapFactory.decodeResource(this.getResources(), R.drawable.belgium);
        countryAdapter.addItem(countryStringArray[18],temp19,"wa");
        Bitmap temp20 = BitmapFactory.decodeResource(this.getResources(), R.drawable.turkey);
        countryAdapter.addItem(countryStringArray[19],temp20,"tr");
        Bitmap temp21 = BitmapFactory.decodeResource(this.getResources(), R.drawable.indonesia);
        countryAdapter.addItem(countryStringArray[20],temp21,"id");

        //코드로 때려 박았기 때문에 추가하려면 array.xml 에서 수정하고 그 순서에 맞게 코드에 넣어주며
        //drawable에 국기를 추가시켜주어야 합니다.

        countryListView.setAdapter(countryAdapter);

        selCountryButton = (Button) findViewById(R.id.selCountryButton);
        selCountryButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(UserInformationActivity.this);
                //alertBuilder.setIcon(R.drawable.);//나라선택 그림
                alertBuilder.setTitle("CHOOSE LANGUAGE");
                alertBuilder.setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        });
                alertBuilder.setAdapter(countryAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Country item = countryAdapter.getItem(which);
                        Toast.makeText(UserInformationActivity.this, "국가코드: "+item.getCodeOfCountry()+"\n국가이름: "+item.getNameOfCountry(), Toast.LENGTH_SHORT).show();
                        languageChoice = item.getCodeOfCountry();
                        currrentUser.setCountry(languageChoice);

                        //국기그림: item.getImageOfCountry()
                    }
                });
                alertBuilder.show();

            }
        });

        // user정보 불러오기

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 저장 기능 코드
                /*
                DB에 넘겨야 할 데이터 :
                languageChoice, userMessage
                 */

                currrentUser.setUserName(userName.getText().toString());
                currrentUser.setUserDescription(userMessage.getText().toString());
                User.setMyInstance(currrentUser);
                databaseReference.child("users").child(User.getMyInstance().getUserId()).setValue(currrentUser);

                finish();
            }
        });
    }
}
