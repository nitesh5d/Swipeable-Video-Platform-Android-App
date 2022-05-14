package com.lousic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import soup.neumorphism.NeumorphCardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {
    TextView registerScreenLoginBtn;
    EditText emailEditText, passwordEditText;
    NeumorphCardView neumorphCardViewSubmit;
    ProgressBar registerProgressBar;
    FirebaseAuth fAuth;
    DatabaseReference UserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        registerScreenLoginBtn = findViewById(R.id.registerScreenLoginBtn);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        neumorphCardViewSubmit = findViewById(R.id.neumorphCardViewSubmit);
        registerProgressBar = findViewById(R.id.registerProgressBar);
        fAuth = FirebaseAuth.getInstance();

        if (fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        registerScreenLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginScreenBtn = new Intent(RegisterActivity.this, Login.class);
                startActivity(loginScreenBtn);
                overridePendingTransition( R.anim.activity_fade_in , R.anim.activity_fade_out);
                finish();
            }
        });


        neumorphCardViewSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String InputEmail = emailEditText.getText().toString().trim();
                String InputPassword = passwordEditText.getText().toString().trim();

                if (TextUtils.isEmpty(InputEmail)){
                    emailEditText.setError("Please Enter an Email Address.");
                    return;
                }
                if (TextUtils.isEmpty(InputPassword)){
                    passwordEditText.setError("Please Enter a Password.");
                    return;
                }
                if (InputPassword.length()<8){
                    passwordEditText.setError("Password must be atleast 8 characters long.");
                    return;
                }
                registerProgressBar.setVisibility(View.VISIBLE);

                fAuth.createUserWithEmailAndPassword(InputEmail,InputPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "Account created Successfully", Toast.LENGTH_SHORT).show();
                            registerProgressBar.setVisibility(View.GONE);
                            startActivity(new Intent(getApplicationContext(),RegisterActivity2.class));
                            overridePendingTransition(R.anim.activity_entry_right,R.anim.activity_exit_right);
                            finish();
                        }
                        else {
                            Toast.makeText(RegisterActivity.this, "Error!! :"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            registerProgressBar.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });







    }
}