package com.youngje.tgwing.accommodations.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.youngje.tgwing.accommodations.Marker;
import com.youngje.tgwing.accommodations.R;
import com.youngje.tgwing.accommodations.Review;
import com.youngje.tgwing.accommodations.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;

import static android.widget.RatingBar.*;



public class WriteReviewActivity extends AppCompatActivity {

    private static final String TAG = "WriteReviewActivity";
    ImageView backButton;
    ImageView writeButton;
    EditText inputText;

    ImageButton cameraButton;
    ImageButton gallaryButton;
    ImageButton camCorderButton;
    RatingBar ratingBar;

    ImageView loadImage;
    VideoView loadVideo;

    private File destination = null;

    private final int REQUEST_IMAGE = 1;
    private final int REQUEST_GALLREY = 2;
    private final int REQUEST_VIDEO = 3;

    private ProgressDialog mProgressDialog;

    // Write a message to the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("review");

    FirebaseStorage storage = FirebaseStorage.getInstance();
    // Create a storage reference from our app
    StorageReference storageRef = storage.getReferenceFromUrl("gs://tourseoul-451de.appspot.com");
    
    private Uri mDownloadUrl = null;

    // TODO: 2016. 10. 16. locationI 가져와야됨 
    private String locationId = "1234";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);
        findVieByidwWriteReviewActivity();

        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        writeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(inputText.getText().toString().length() == 0 && destination == null) {
                    Toast.makeText(WriteReviewActivity.this, "아무런 텍스트가 입력하지 않습니다", Toast.LENGTH_SHORT).show();
                }
                else
                    WriteReview(destination);
                // TODO: 2016. 10. 16. 파이어베이스에 글올리는거 구현하기~ 이미지 받아오거 사진받아오기

            }
        });

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/testImage/");
                if (!file.exists())
                    file.mkdir();

                destination = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/testImage/" + System.currentTimeMillis() + ".jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(destination));
                startActivityForResult(intent, REQUEST_IMAGE);

                // TODO: 2016. 10. 16. 카메라찍기 ,찍은거 이미지뷰들에 올리기.

            }
        });

        gallaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_GALLREY);

                // TODO: 2016. 10. 16.  갤러리거 불러오기

            }
        });

        camCorderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/testCamera/");
                if (!file.exists())
                    file.mkdir();
                destination = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/testCamera/" + System.currentTimeMillis() + ".mp4");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(destination));
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                startActivityForResult(intent, REQUEST_VIDEO);

                // TODO: 2016. 10. 16. 캠코더 ~
            }
        });

        ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                String rateValue = String.valueOf(ratingBar.getRating());
                Log.i("별점 테스트 : ", rateValue);

            }
        });
        // TODO: 2016. 10. 15.  각 버튼에 대한 이벤트 처리
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                try {
                    FileInputStream in = new FileInputStream(destination);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 10;
                    Bitmap bmp = BitmapFactory.decodeStream(in, null, options);
                    loadImage.setImageBitmap(bmp);

                    loadImage.setVisibility(View.VISIBLE);
                    cameraButton.setVisibility(View.GONE);
                    camCorderButton.setVisibility(View.GONE);
                    gallaryButton.setVisibility(View.GONE);


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == RESULT_CANCELED)
                Toast.makeText(this, "사진을 취소하셨습니다.", Toast.LENGTH_SHORT).show();
        } else if (requestCode == REQUEST_VIDEO) {
            if (resultCode == RESULT_OK) {
                System.out.println("asdasd2 " + requestCode);
                loadVideo.setVideoPath(destination.getPath());
                loadVideo.start();

                loadVideo.setVisibility(View.VISIBLE);
                cameraButton.setVisibility(View.GONE);
                camCorderButton.setVisibility(View.GONE);
                gallaryButton.setVisibility(View.GONE);

            } else if (resultCode == RESULT_CANCELED)
                Toast.makeText(this, "비디오를 취소하셨습니다.", Toast.LENGTH_SHORT).show();
        } else if (requestCode == REQUEST_GALLREY) {

            if (resultCode == RESULT_OK) {
                    //Get ImageURi and load with help of picasso
                    //Uri selectedImageURI = data.getData();

                    Picasso.with(this).load(data.getData()).noPlaceholder().centerCrop().fit()
                            .into((ImageView) findViewById(R.id.loadImage));
                loadImage.setVisibility(View.VISIBLE);
                }

            }

        }


    public void findVieByidwWriteReviewActivity() {
        backButton = (ImageView) findViewById(R.id.backbutton);
        writeButton = (ImageView) findViewById(R.id.writeReview);

        cameraButton = (ImageButton) findViewById(R.id.cameraButton);
        gallaryButton = (ImageButton) findViewById(R.id.galleryButton);
        camCorderButton = (ImageButton) findViewById(R.id.camcorderButton);

        inputText = (EditText) findViewById(R.id.inputText);
        ratingBar = (RatingBar) findViewById(R.id.ratingbar);

        loadImage = (ImageView)findViewById(R.id.loadImage);
        loadVideo = (VideoView) findViewById(R.id.loadVideo);

    }

    public void WriteReview(File file) { // 새로운 유저 파이어 베이스에 등록

        int contentType = checkFileType(file);

        if(contentType == 0) {
            uploadNoMultimediaDocument();
            // 글만  쓰는것
        }
        else if(contentType == 1 || contentType == 2) {
            uploadMultimediaDocument(file,contentType);
            // 이미지, 동영상 올리는것
        }
    }

    public int checkFileType(File file) {
        if(file == null) return 0;

        String[] extension = file.getName().split("\\.");

        Log.i("extension",extension[1]);
        if (extension[1].equals("jpg") || extension[1].equals("png")) {
            return 1;
        } else if (extension[1].equals("mp4")) {
            return 2;
        }
        return 0;
    }

    public Review makeReview(String uid) {

        Marker marker = Marker.getSelectedMarker();
        User curUser = User.getMyInstance();

        String markerId = "1224";
        String userId = curUser.getUserId();
        String userName = curUser.getUserName();
        String userImageUrl = curUser.getImageUri();
        String userCountry = curUser.getCountry();

        String content = inputText.getText().toString();
        Date currentDate = new Date();

        // TODO: 2016. 10. 16. 생성자에맞게
        // TODO: 2016. 10. 16. 리뷰 클래스에 맞게 하자

        // TODO: 2016. 10. 16. wifi 후기남길필요있나 ? 없다 ! 따라서 와이파이 후기는 제외

        // TODO: 2016. 10. 16. test 할때는 markerId 로 먼저하자

        Review review = new Review(markerId,userId,userName,userImageUrl,userCountry,content,"",currentDate,0,0);

        // TODO: 2016. 10. 16. 별점 저장해야됨 맨마지막거 스탈 레이트


        return review;
    }

    public void uploadNoMultimediaDocument() {

        Review review = makeReview(locationId);
        review.setContentType(0);
        myRef.push().setValue(review);

    }
    
    public void uploadMultimediaDocument(File upLoadfile,int contentType) {

        final Review review = makeReview(locationId);
        review.setContentType(contentType);

        // Create a storage reference from our app
        StorageReference storageRef = storage.getReferenceFromUrl("gs://tourseoul-451de.appspot.com/");
        String fileRef="";

        // TODO: 2016. 10. 16. location id는 나중에 장소 id 를 받아오자
        if(contentType == 1) 
            fileRef = locationId + "/" + upLoadfile.getName();
        
        else if(contentType == 2)
            fileRef = locationId + "/" + upLoadfile.getName();

        showProgressDialog();

        Uri file = Uri.fromFile(upLoadfile);
        StorageReference contentRef = storageRef.child(fileRef);

        contentRef.putFile(file)
                .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Upload succeeded
                        Log.d(TAG, "uploadFromUri:onSuccess");
                        // Get the public download URL
                        hideProgressDialog();
                        mDownloadUrl = taskSnapshot.getMetadata().getDownloadUrl();
                        review.setreviewContentUrl(mDownloadUrl.toString());
                        myRef.push().setValue(review);
                        // TODO: 2016. 9. 19. 여기서 url 을 받고 서버에 글 써주거나 아니면 글 아이디를 받아서 거기에 url 값을 업데이트해줘야함

                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Upload failed
                        Log.w(TAG, "uploadFromUri:onFailure", exception);
                        hideProgressDialog();
                        mDownloadUrl = null;

                        // [END_EXCLUDE]
                    }
                });

    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

}
