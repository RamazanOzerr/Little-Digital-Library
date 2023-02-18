package com.example.spring_android_project.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.spring_android_project.Activities.MainActivity;
import com.example.spring_android_project.R;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    ImageView imageViewSplash;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
        animation();

    }

    private void init(){
        imageViewSplash = findViewById(R.id.imageViewSplash);
        auth = FirebaseAuth.getInstance();
    }

    private void animation(){
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
        imageViewSplash.startAnimation(animation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //TODO şimdilik direkt log in e atıyoruz
//                startActivity(new Intent(getApplicationContext(), LogInActivity.class));
                if(auth.getCurrentUser() != null){
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }else{
                    startActivity(new Intent(getApplicationContext(), LogInActivity.class));
                }
                finish();
//                music.stop();
            }
        },2000);
    }
}