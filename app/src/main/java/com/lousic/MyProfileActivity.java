package com.lousic;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyProfileActivity extends AppCompatActivity {

    FirebaseAuth fAuth;
    DatabaseReference UserRef, myProfileRef;
    String currentUserId;

    CircleImageView currentUserDp;
    TextView currentUserFn, currentUserUn;
    Button UploadVideoBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);


        currentUserDp = findViewById(R.id.current_user_dp);
        currentUserFn = findViewById(R.id.current_user_fn);
        currentUserUn = findViewById(R.id.current_user_un);
        UploadVideoBtn = findViewById(R.id.upload_video_btn);

        fAuth = FirebaseAuth.getInstance();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");

        currentUserId = fAuth.getCurrentUser().getUid();
        myProfileRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
        myProfileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String myDp = snapshot.child("profileimage").getValue().toString();
                    String myFullName = snapshot.child("Full Name").getValue().toString();
                    String myUserName = snapshot.child("Display Name").getValue().toString();

                    Glide.with(getApplicationContext()).load(myDp).into(currentUserDp);
                    currentUserFn.setText(myFullName);
                    currentUserUn.setText(myUserName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                String errorMsg = error.toException().getMessage();
                Toast.makeText(MyProfileActivity.this, "Error!! : "+errorMsg, Toast.LENGTH_SHORT).show();
            }
        });


        UploadVideoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent uploadVideoIntent = new Intent(getApplicationContext(), UploadVideo.class);
                startActivity(uploadVideoIntent);
            }
        });

    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }
}