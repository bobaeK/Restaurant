package com.famous.restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class LoadingActivity extends AppCompatActivity {
    ImageView img_appName;
    ImageView img_loadingLogo;
    Animation loadingAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        img_appName=(ImageView)findViewById(R.id.iv_appName);
        img_loadingLogo=(ImageView)findViewById(R.id.iv_loadingLogo);

        loadingAnim= AnimationUtils.loadAnimation(this, R.anim.loading);
        loadingAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        img_loadingLogo.startAnimation(loadingAnim);
        img_appName.startAnimation(loadingAnim);

    }
}
