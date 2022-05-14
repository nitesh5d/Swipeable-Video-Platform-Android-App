package com.lousic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.VideoView;

public class SplashScreen extends AppCompatActivity {

    VideoView SplashScreenAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        SplashScreenAnim = findViewById(R.id.SplashScreenAnim);
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.lousic_anim);

        SplashScreenAnim.setVideoURI(uri);
        SplashScreenAnim.setZOrderOnTop(true);
        SplashScreenAnim.start();

        SplashScreenAnim.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(false);

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this, RegisterActivity.class);
                startActivity(i);
                // close this activity
                finish();
            }
        }, 2500);

    }
}