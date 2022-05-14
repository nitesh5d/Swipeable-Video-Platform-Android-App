package com.lousic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth fAuth;
    DatabaseReference UserRef, myProfileRef;
    String currentUserId;

    ViewPager2 viewPager2;
    videoAdapter adapter;
    CircleImageView currentUserDp;
    ProgressBar progressBarMain;
    TextView loadingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        progressBarMain =findViewById(R.id.progressBarMain);
        loadingText = findViewById(R.id.loadingText);
        currentUserDp = findViewById(R.id.current_user_dp);

        fAuth = FirebaseAuth.getInstance();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        final String current_user_id = fAuth.getCurrentUser().getUid();
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.hasChild(current_user_id)){
                    Intent register2 = new Intent(MainActivity.this, RegisterActivity2.class);
                    startActivity(register2);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Error"+error, Toast.LENGTH_SHORT).show();
            }
        });
        currentUserId = fAuth.getCurrentUser().getUid();

        viewPager2 = findViewById(R.id.viewpager);

        FirebaseRecyclerOptions<videomodel> options =
                new FirebaseRecyclerOptions.Builder<videomodel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Videos").child(currentUserId), videomodel.class)
                        .build();

        adapter = new videoAdapter(options);

        viewPager2.setAdapter(adapter);



        myProfileRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
        myProfileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String myDp = snapshot.child("profileimage").getValue().toString();

                    Glide.with(getApplicationContext()).load(myDp).into(currentUserDp);
                    progressBarMain.setVisibility(View.GONE);
                    loadingText.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                String errorMsg = error.toException().getMessage();
                Toast.makeText(MainActivity.this, "Error!! : "+errorMsg, Toast.LENGTH_SHORT).show();
            }
        });



        currentUserDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myProfileActivity = new Intent(getApplicationContext(), MyProfileActivity.class);
                startActivity(myProfileActivity);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}