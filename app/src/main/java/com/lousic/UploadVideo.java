package com.lousic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class UploadVideo extends AppCompatActivity {


    private static final int PICK_VIDEO = 1;
    VideoView videoView;
    Button chooseVideo;
    CardView uploadVideoBtn;
    EditText uploadVideoTitle, uploadVideoDesc;
    private Uri videoUri;

    FirebaseAuth fAuth;
    DatabaseReference DataRef;
    StorageReference StoreVideoRef;
    String CurrentUId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);

        videoView = findViewById(R.id.videoViewChoose);
        chooseVideo = findViewById(R.id.choose_video_btn);
        uploadVideoBtn = findViewById(R.id.neumorphCardViewSubmitUpload);
        uploadVideoTitle = findViewById(R.id.upload_video_title);
        uploadVideoDesc = findViewById(R.id.upload_video_desc);

        fAuth = FirebaseAuth.getInstance();
        CurrentUId = fAuth.getCurrentUser().getUid();
        DataRef = FirebaseDatabase.getInstance().getReference().child("Videos").child(CurrentUId);
        StoreVideoRef = FirebaseStorage.getInstance().getReference();

        DatabaseReference videoKeyRef = FirebaseDatabase.getInstance().getReference();
        String videoKey = videoKeyRef.push().getKey();




        chooseVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent chooseVidIntent = new Intent();
                chooseVidIntent.setType("video/*");
                chooseVidIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(chooseVidIntent, PICK_VIDEO);
            }
        });


        uploadVideoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String videoTitleEditText = uploadVideoTitle.getText().toString();
                String videoDescEditText = uploadVideoDesc.getText().toString();

                if (TextUtils.isEmpty(videoTitleEditText)){
                    uploadVideoTitle.setError("Please Enter a Title for your Video");
                    return;
                }
                if (TextUtils.isEmpty(videoDescEditText)){
                    uploadVideoDesc.setError("Please Enter a Description for your video.");
                    return;

                }
                else {

                    HashMap videoMap = new HashMap();
                    videoMap.put("title", videoTitleEditText);
                    videoMap.put("description", videoDescEditText);
                    DataRef.child(videoKey).updateChildren(videoMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()){
                                Toast.makeText(UploadVideo.this, "Video details saved Successfully!", Toast.LENGTH_LONG).show();
                            }
                            else {
                                String errorMsg = task.getException().getMessage();
                                Toast.makeText(UploadVideo.this, "Error!! : "+errorMsg, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyyHH-mm-ss");
                Date date = new Date(System.currentTimeMillis());
                String videoName = CurrentUId + uploadVideoTitle.getText() + formatter.format(date);

                StorageReference videoPath = StoreVideoRef.child("videos").child( videoName +".mp4");

                videoPath.putFile(videoUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task)
                    {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(UploadVideo.this, "Video stored in FB Storage", Toast.LENGTH_SHORT).show();
                            Toast.makeText(UploadVideo.this, formatter.format(date), Toast.LENGTH_SHORT).show();
                            videoPath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final String downloadUrl = uri.toString();

                                    DataRef.child(videoKey).child("url").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {Toast.makeText(UploadVideo.this, "Video stored in FB Database", Toast.LENGTH_SHORT).show();}
                                    });
                                }
                            });

                        }
                    }
                });
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_VIDEO ||
                resultCode == RESULT_OK ||
                data != null ||
                data.getData() != null)
        {
            videoView.setBackgroundColor(Color.TRANSPARENT);
            videoUri = data.getData();
            videoView.setVideoURI(videoUri);
            videoView.start();

        }
    }
}