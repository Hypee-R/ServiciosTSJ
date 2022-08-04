package com.example.manuel.serviciostsj.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.example.manuel.serviciostsj.R;

public class SplashActivity extends AppCompatActivity {
    int SPLASH_SCREEN_DELAY= 3000;
    ProgressBar myProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);


        myProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        myProgressBar.getIndeterminateDrawable()
                .setColorFilter(Color.rgb(126,0,23), PorterDuff.Mode.SRC_IN);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN_DELAY);
    }
}
