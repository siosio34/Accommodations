package com.youngje.tgwing.accommodations.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
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
import com.youngje.tgwing.accommodations.Util.MyVideoView;
import com.youngje.tgwing.accommodations.Util.StretchVideoView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
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
   // VideoView loadVideo;

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
    
    private String mDownloadUrl = null;

    private String locationId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);
        findVieByidwWriteReviewActivity();

        locationId = Marker.selectedMarker.getId();

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
                else {
                    WriteReview(destination);
                }

            }
        });

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/TourSeoul/");

                if (!file.exists())
                    file.mkdir();

                destination = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/TourSeoul/" + System.currentTimeMillis() + ".jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(destination));
                startActivityForResult(intent, REQUEST_IMAGE);

            }
        });

        gallaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/TourSeoul/");
                if (!file.exists())
                    file.mkdir();

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                //destination = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/TourSeoul/" + System.currentTimeMillis() + ".jpg");
               // intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(destination));
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_GALLREY);

                // TODO: 2016. 10. 16.  갤러리거 불러오기

            }
        });

        camCorderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/TourSeoul/");
                if (!file.exists())
                    file.mkdir();
                destination = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/TourSeoul/" + System.currentTimeMillis() + ".mp4");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(destination));
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                startActivityForResult(intent, REQUEST_VIDEO);


            }
        });

         
        ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                String rateValue = String.valueOf(ratingBar.getRating());
                Toast.makeText(WriteReviewActivity.this,new Float(ratingBar.getRating()).toString(),Toast.LENGTH_SHORT).show();
                Log.i("별점 테스트 : ", rateValue);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                try {

                  FileInputStream in = new FileInputStream(destination);
                  Bitmap bmp = BitmapFactory.decodeStream(in, null, null);
                  loadImage.setImageBitmap(bmp);

                  //picCamBtnLayout.setVisibility(View.GONE);
                  //videoView.setVisibility(View.GONE);
                  //imageView.setVisibility(View.VISIBLE);

                   //FileInputStream in = new FileInputStream(destination);
                   //BitmapFactory.Options options = new BitmapFactory.Options();
                   //options.inSampleSize = 10;
//
                   //Picasso.with(this).load(data.getData()).noPlaceholder().centerCrop().fit()
                   //        .into((ImageView)findViewById(R.id.loadImage));

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

                StretchVideoView videoView = (StretchVideoView)findViewById(R.id.loadVideo);
                videoView.setVideoPath(destination.getPath());
                RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relative_layout_size);
                videoView.setVideoSize(relativeLayout.getWidth(),relativeLayout.getHeight());
                videoView.start();

                videoView.setVisibility(View.VISIBLE);
                cameraButton.setVisibility(View.GONE);
                camCorderButton.setVisibility(View.GONE);
                gallaryButton.setVisibility(View.GONE);

            } else if (resultCode == RESULT_CANCELED)
                Toast.makeText(this, "비디오를 취소하셨습니다.", Toast.LENGTH_SHORT).show();

        } else if (requestCode == REQUEST_GALLREY) {

            if (resultCode == RESULT_OK) {

                Uri selectedImage = data.getData();
                String filePath = null;

                try {
                    filePath = getFilePath(getApplicationContext(),selectedImage);
                    Log.i("filepath",filePath);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

                destination = new File(filePath);

                Picasso.with(this).load(data.getData()).noPlaceholder().centerCrop().fit()
                        .into((ImageView)findViewById(R.id.loadImage));


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
        //loadVideo = (VideoView) findViewById(R.id.loadVideo);

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

        String markerId = locationId;
        String userId = curUser.getUserId();
        String userName = curUser.getUserName();
        String userImageUrl = curUser.getImageUri();
        String userCountry = curUser.getCountry();

        String content = inputText.getText().toString();
        Date currentDate = new Date();

        Review review = new Review(markerId,userId,userName,userImageUrl,content,null,0,0,userCountry,currentDate);
        return review;
    }

    public void uploadNoMultimediaDocument() {

        Review review = makeReview(locationId);
        review.setContentType(0);
        myRef.child(locationId).push().setValue(review);
        finish();

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
                        mDownloadUrl = taskSnapshot.getMetadata().getDownloadUrl().toString();
                        review.setreviewContentUrl(mDownloadUrl);
                        myRef.child(locationId).push().setValue(review);
                        finish();

                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(Exception exception) {
                        // Upload failed
                        Log.w(TAG, "uploadFromUri:onFailure", exception);
                        hideProgressDialog();
                        mDownloadUrl = null;
                        // [END_EXCLUDE]
                    }
                });

      //  myRef.child(locationId).push().setValue(review);

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

    @SuppressLint("NewApi")
    public static String getFilePath(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }




}
