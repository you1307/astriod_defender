package com.thetechnoobs.moterskillgame;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.thetechnoobs.moterskillgame.asteriodgame.AsteroidGameActivity;
import com.thetechnoobs.moterskillgame.asteriodgame.WeponShopActivity;

public class MainActivity extends AppCompatActivity {
    UserData userData = new UserData(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        settup();
        startBackgroundEffects();
    }

    SurfaceView surfaceView;
    Button PlayBTN, SettingsBTN;
    TextView TitleTXT;
    private void settup() {
        surfaceView = findViewById(R.id.backGroundSurfaceView);
        PlayBTN = findViewById(R.id.playBTN);
        SettingsBTN = findViewById(R.id.settingsBTN);
        TitleTXT = findViewById(R.id.GameTitleTXT);
    }

    Thread backgroundEffects;
    MenuSpaceBackground menuSpaceBackground;

    private void startBackgroundEffects() {
        YoYo.with(Techniques.ZoomInRight).duration(500).playOn(PlayBTN);
        YoYo.with(Techniques.ZoomInLeft).duration(500).playOn(SettingsBTN);
        YoYo.with(Techniques.Shake).duration(1500).repeat(1).playOn(TitleTXT);



        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        int[] screenSize = {point.x, point.y};
        menuSpaceBackground = new MenuSpaceBackground(getApplicationContext(), surfaceView, screenSize);
        backgroundEffects = new Thread(menuSpaceBackground);
        backgroundEffects.start();
    }


    public void PlayGame(View view) {
        YoYo.with(Techniques.FadeOutUp).duration(100).playOn(PlayBTN);
        YoYo.with(Techniques.FadeOut).duration(100).playOn(SettingsBTN);
        YoYo.with(Techniques.SlideOutUp).duration(100).playOn(TitleTXT);

        if(userData.isNewPLayer()){
            Intent intent = new Intent(getApplicationContext(), AsteroidGameActivity.class);
            startActivity(intent);
            cleanup();
            userData.saveNewPlayer(false);
            finish();
        }else{
            Intent intent = new Intent(getApplicationContext(), WeponShopActivity.class);
            startActivity(intent);
            cleanup();
            finish();
        }

    }

    public void goToSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        cleanup();
        finish();
    }

    private void cleanup() {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                menuSpaceBackground.stopThread();
            }
        });

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if(!backgroundEffects.isAlive()){
            startBackgroundEffects();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        menuSpaceBackground.stopThread();
        try {
            backgroundEffects.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        menuSpaceBackground.stopThread();
        try {
            backgroundEffects.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}