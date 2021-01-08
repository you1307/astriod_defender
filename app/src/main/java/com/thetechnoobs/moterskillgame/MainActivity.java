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
    private void settup() {
        surfaceView = findViewById(R.id.backGroundSurfaceView);
        PlayBTN = findViewById(R.id.playBTN);
        SettingsBTN = findViewById(R.id.settingsBTN);
    }

    Thread backgroundEffects;
    MenuSpaceBackground menuSpaceBackground;
    private void startBackgroundEffects() {
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        int[] screenSize = {point.x, point.y};
        menuSpaceBackground = new MenuSpaceBackground(getApplicationContext(), surfaceView, screenSize);
        backgroundEffects = new Thread(menuSpaceBackground);
        backgroundEffects.start();
    }


    public void PlayGame(View view) {
        if(userData.isNewPLayer()){
            Intent intent = new Intent(getApplicationContext(), AsteroidGameActivity.class);
            startActivity(intent);
            cleanup();
            finish();
        }else{
            Intent intent = new Intent(getApplicationContext(), WeponShopActivity.class);
            startActivity(intent);
            cleanup();
            finish();
        }

    }

    public void goToSettings(View view) {
        //not ending activity incase user comes back from settings, but do end background thread

        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        cleanup();
    }

    private void cleanup() {
        menuSpaceBackground.stopThread();
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