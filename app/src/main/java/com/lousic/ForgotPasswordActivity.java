package com.lousic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import soup.neumorphism.NeumorphCardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;

public class ForgotPasswordActivity extends AppCompatActivity {

    TextView FpwScreenScreenBacktoLogin;
    EditText ForgotPwEmailEditText;
    NeumorphCardView neumorphCardViewResetPw;
    ProgressBar loginProgressBar;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        FpwScreenScreenBacktoLogin = findViewById(R.id.ForgotPwEmailEditText);
        ForgotPwEmailEditText = findViewById(R.id.ForgotPwEmailEditText);
        neumorphCardViewResetPw = findViewById(R.id.neumorphCardViewResetPw);
        loginProgressBar = findViewById(R.id.loginProgressBar);
        fAuth = FirebaseAuth.getInstance();

        neumorphCardViewResetPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String InputEmail = ForgotPwEmailEditText.getText().toString().trim();

                if (TextUtils.isEmpty(InputEmail)){
                    ForgotPwEmailEditText.setError("Please Enter your Email Address.");
                    return;
                }
                fAuth.sendPasswordResetEmail(InputEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgotPasswordActivity.this, "Password Reset link has been sent to your Email.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ForgotPasswordActivity.this, Login.class));
                            finish();
                        }
                        else {
                            String passwordResetError = task.getException().getMessage();
                            Toast.makeText(ForgotPasswordActivity.this, "Error Occured : "+ passwordResetError, Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });

        FpwScreenScreenBacktoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent BacktoLogin = new Intent(ForgotPasswordActivity.this, Login.class);
                startActivity(BacktoLogin);
                finish();
            }
        });

    }
}