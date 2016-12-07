package com.youngje.tgwing.accommodations.ARAccomdation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.youngje.tgwing.accommodations.R;
import com.youngje.tgwing.accommodations.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;

//Theme.dialog는 AppCombatAcitivity로는 만들지 못함 Activity로 해야함
public class WriteDocumentActivity extends Activity {
    private EditText editText;
    private LinearLayout picCamBtnLayout = null;
    private Button picBtn = null;
    private Button camBtn = null;
    private Button okBtn = null;
    private ImageView imageView = null;
    private VideoView videoView = null;
    private int REQUEST_IMAGE = 1;
    private int REQUEST_VIDEO = 2;

    private File destination = null;

    private static final String TAG = "WriteDocumentActivity";
    private ProgressDialog mProgressDialog;

    // Write a message to the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("posts");

    FirebaseStorage storage = FirebaseStorage.getInstance();
    // Create a storage reference from our app
    StorageReference storageRef = storage.getReferenceFromUrl("gs://hackfair-c7518.appspot.com");

    String currentUid;

    User currentUser = User.getMyInstance();

    private Uri mDownloadUrl = null;

    Double curlat = 0.0;
    Double curlon = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_write_document);

        picCamBtnLayout = (LinearLayout) findViewById(R.id.picCamBtnLayout);
        picBtn = (Button) findViewById(R.id.writeDocumentPicBtn);
        camBtn = (Button) findViewById(R.id.writeDocumentCamBtn);
        okBtn = (Button) findViewById(R.id.writeDocumentOkBtn);
        imageView = (ImageView) findViewById(R.id.writeDocumentImageView);
        videoView = (VideoView) findViewById(R.id.writeDocumentVideoView);
        editText = (EditText) findViewById(R.id.writeEdit);

        picCamBtnLayout.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.GONE);
        videoView.setVisibility(View.GONE);

        currentUid = currentUser.getUserId();

        Intent intent = getIntent();

        curlat = intent.getDoubleExtra("lat",0.0);
        curlon = intent.getDoubleExtra("lon",0.0);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        getWindow().getAttributes().width = (int) (size.x * 0.9);
        getWindow().getAttributes().height = (int) (size.y * 0.9);


        picBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/testImage/");
                if (!file.exists())
                    file.mkdir();
                destination = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/testImage/" + System.currentTimeMillis() + ".jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(destination));
                startActivityForResult(intent, REQUEST_IMAGE);
            }
        });

        camBtn.setOnClickListener(new View.OnClickListener() {
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
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText.getText().toString().length() == 0 && destination == null) {
                    Toast.makeText(WriteDocumentActivity.this, "아무런 텍스트가 입력하지 않습니다", Toast.LENGTH_SHORT).show();
                }
                else
                    WriteDocument(destination);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("asdasd1");

        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                try {
                    FileInputStream in = new FileInputStream(destination);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 10;
                    Bitmap bmp = BitmapFactory.decodeStream(in, null, options);
                    imageView.setImageBitmap(bmp);

                    picCamBtnLayout.setVisibility(View.GONE);
                    videoView.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == RESULT_CANCELED)
                Toast.makeText(this, "사진을 취소하셨습니다.", Toast.LENGTH_SHORT).show();
        } else if (requestCode == REQUEST_VIDEO) {
            if (resultCode == RESULT_OK) {
                System.out.println("asdasd2 " + requestCode);
                videoView.setVideoPath(destination.getPath());
                videoView.start();

                picCamBtnLayout.setVisibility(View.GONE);
                videoView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
            } else if (resultCode == RESULT_CANCELED)
                Toast.makeText(this, "비디오를 취소하셨습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    public void WriteDocument(File file) { // 새로운 유저 파이어 베이스에 등록


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

    public Document makeDocument(String uid) {

        Log.i("ㅇㅇㅇ","아마이게없어서 터지는거같은데");
        String content = editText.getText().toString();
        Log.i("ㅇㅇㅇ2","아마이게없어서 터지는거같은데");
        Date currentDate = new Date();

        // TODO: 2016. 9. 20. 경도 위도도 저장해야함 
        Document document = new Document(0, uid, content, 0, null, null, 0, 0, 0, 0, 0, 0, 0, currentDate, currentDate, null,curlat,curlon);
        return document;
    }

    public void uploadNoMultimediaDocument() {

        Document document = makeDocument(currentUid);
        document.setContentType(0);
        myRef.push().setValue(document);

    }

    public void uploadMultimediaDocument(File upLoadfile,int contentType) {

        final Document document = makeDocument(currentUid);
        document.setContentType(contentType);

        // Create a storage reference from our app
        StorageReference storageRef = storage.getReferenceFromUrl("gs://hackfair-c7518.appspot.com/");
        String fileRef="";

        if(contentType == 1) {
            fileRef = "images/" + upLoadfile.getName();
        }
        else if(contentType == 2)
            fileRef = "videos/" + upLoadfile.getName();

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
                        Log.i("문제1","ㅗ");
                        document.setContentUrl(mDownloadUrl.toString());
                        Log.i("문제2","ㅗ");
                        myRef.push().setValue(document);
                        Log.i("문제3","ㅗ");
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