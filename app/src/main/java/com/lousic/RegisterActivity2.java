package com.lousic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import soup.neumorphism.NeumorphCardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

public class RegisterActivity2 extends AppCompatActivity {

    CircleImageView UploadDp;
    EditText FullNameEditText, DisplayNameEditText;
    NeumorphCardView neumorphCardViewFinish;
    FirebaseAuth fAuth;
    DatabaseReference UserRef;
    StorageReference UserDpRef;
    ProgressBar Register2ProgressBar;

    String CurrentUId;
    final static int GalleryPick = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        fAuth = FirebaseAuth.getInstance();
        CurrentUId = fAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(CurrentUId);
        UserDpRef = FirebaseStorage.getInstance().getReference().child("profile Images");

        UploadDp = findViewById(R.id.uploadDp);
        FullNameEditText = findViewById(R.id.fullNameEditText);
        DisplayNameEditText = findViewById(R.id.displayNameEditText);
        neumorphCardViewFinish = findViewById(R.id.neumorphCardViewFinish);
        Register2ProgressBar = findViewById(R.id.register2ProgressBar);

        neumorphCardViewFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fullNameEditText = FullNameEditText.getText().toString();
                String displayNameEditText = DisplayNameEditText.getText().toString();

                if (TextUtils.isEmpty(fullNameEditText)){
                    FullNameEditText.setError("Please Enter Your Full Name");
                    return;
                }
                if (TextUtils.isEmpty(displayNameEditText)){
                    DisplayNameEditText.setError("Please Enter a Display Name.");
                    return;
                }
                else {
                    Register2ProgressBar.setVisibility(View.VISIBLE);
                    HashMap userMap = new HashMap();
                    userMap.put("Full Name", fullNameEditText);
                    userMap.put("Display Name", displayNameEditText);
                    UserRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()){
                                Register2ProgressBar.setVisibility(View.GONE);
                                Toast.makeText(RegisterActivity2.this, "Account details saved Successfully!", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                finish();
                            }
                            else {
                                Register2ProgressBar.setVisibility(View.GONE);
                                String errorMsg = task.getException().getMessage();
                                Toast.makeText(RegisterActivity2.this, "Error!! : "+errorMsg, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        UploadDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickDpIntent = new Intent();
                pickDpIntent.setAction(Intent.ACTION_GET_CONTENT);
                pickDpIntent.setType("image/*");
                startActivityForResult(pickDpIntent, GalleryPick);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GalleryPick && resultCode==RESULT_OK && data!=null)
        {
            Uri ImageUri = data.getData();

            CropImage.activity(ImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if(resultCode == RESULT_OK)
            {
                Uri resultUri = result.getUri();

                Register2ProgressBar.setVisibility(View.VISIBLE);

                StorageReference filePath = UserDpRef.child(CurrentUId + ".jpg");

                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task)
                    {
                        if(task.isSuccessful())
                        {
                            Register2ProgressBar.setVisibility(View.GONE);

                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final String downloadUrl = uri.toString();

                                    Toast.makeText(RegisterActivity2.this, "Display Picture stored in FB Storage", Toast.LENGTH_SHORT).show();
                                    UserRef.child("profileimage").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {Toast.makeText(RegisterActivity2.this, "Display Picture stored in FB Database", Toast.LENGTH_SHORT).show();}
                                    });
                                    Glide.with(getApplicationContext()).load(downloadUrl).into(UploadDp);
                                }
                            });

                        }
                    }
                });
            }
            else
            {
                Toast.makeText(this, "Error Occured. Please try Again", Toast.LENGTH_SHORT).show();
            }
        }
    }
}