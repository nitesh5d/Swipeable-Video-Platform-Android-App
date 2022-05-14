package com.lousic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import soup.neumorphism.NeumorphCardView;
import soup.neumorphism.ShapeType;

import android.content.Intent;
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

public class Login extends AppCompatActivity {
    TextView loginScreenRegisterBtn, forgotPasswordBtn;
    EditText loginEmailEditText, loginPasswordEditText;
    NeumorphCardView neumorphCardViewLogin;
    ProgressBar loginProgressBar;
    FirebaseAuth fAuth;
    DatabaseReference UserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginScreenRegisterBtn = findViewById(R.id.loginScreenRegisterBtn);
        loginEmailEditText = findViewById(R.id.loginEmailEditText);
        loginPasswordEditText= findViewById(R.id.loginPasswordEditText);
        neumorphCardViewLogin = findViewById(R.id.neumorphCardViewLogin);
        loginProgressBar = findViewById(R.id.loginProgressBar);
        forgotPasswordBtn = findViewById(R.id.forgotPasswordBtn);
        fAuth = FirebaseAuth.getInstance();


        neumorphCardViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String InputEmail = loginEmailEditText.getText().toString().trim();
                String InputPassword = loginPasswordEditText.getText().toString().trim();

                if (TextUtils.isEmpty(InputEmail)){
                    loginEmailEditText.setError("Please Enter your Email Address.");
                    return;
                }
                if (TextUtils.isEmpty(InputPassword)){
                    loginPasswordEditText.setError("Please Enter a Password.");
                    return;
                }
                loginProgressBar.setVisibility(View.VISIBLE);

                fAuth.signInWithEmailAndPassword(InputEmail, InputPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Login.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                            loginProgressBar.setVisibility(View.GONE);
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            finish();
                        }
                        else {
                            Toast.makeText(Login.this, "Error!! :"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            loginProgressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });





        loginScreenRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerScreenBtn = new Intent(Login.this, RegisterActivity.class);
                startActivity(registerScreenBtn);
                overridePendingTransition( R.anim.activity_fade_in , R.anim.activity_fade_out);
                finish();

            }
        });

        forgotPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, ForgotPasswordActivity.class));
            }
        });

    }

}