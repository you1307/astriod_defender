package com.thetechnoobs.moterskillgame.asteriodgame;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.thetechnoobs.moterskillgame.MenuSpaceBackground;
import com.thetechnoobs.moterskillgame.R;
import com.thetechnoobs.moterskillgame.UserData;
import com.thetechnoobs.moterskillgame.UserInventory;
import com.thetechnoobs.moterskillgame.asteriodgame.ui.BackgroundStar;

import java.util.ArrayList;
import java.util.Random;

public class EndGameScreenAsteroids extends Activity {
    TextView ScoreTXT, GoldTXT, MoneyTXT, TitleTextView;
    String userScore, userGold, enemysKilled, damageTaken;
    boolean waveCompleted;
    Button RestartBTN, GoToTownBTN;
    UserInventory userInventory = new UserInventory(this);
    UserData userData = new UserData(this);
    SurfaceView backgroundSurface;
    Point point = new Point();
    int[] screenSize;
    Thread backgroundEffects;
    MenuSpaceBackground menuSpaceBackground;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.end_game_screen_asteroids);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindowManager().getDefaultDisplay().getSize(point);
        screenSize = new int[]{point.x, point.y};


        Settup();
        LoadIntentData();
        addDataToInv();
        setTXTviews();

        startStarBackground();
    }

    private void LoadIntentData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userScore = bundle.get("score").toString();
            userGold = bundle.get("gold").toString();
            enemysKilled = bundle.get("enemysKilled").toString();
            damageTaken = bundle.get("damageTaken").toString();
            waveCompleted = bundle.getBoolean("WaveComplete");
        } else {
            Log.v("testing", "Error Loading Score");
        }


    }

    @SuppressLint("SetTextI18n")
    private void setTXTviews() {
        MoneyTXT.setText(String.valueOf(calculateMoney(enemysKilled, userScore, userGold, damageTaken)));
        GoldTXT.setText(userGold);
        ScoreTXT.setText("Score: " + userScore);

        if (waveCompleted) {
            TitleTextView.setText("Wave " + (userData.getCurrentWaveCount()) + " Completed");
            TitleTextView.setTextColor(getResources().getColor(R.color.green_800));
            RestartBTN.setText(R.string.next_wave);
            userData.addOneToCurrentWaveCount();
        } else {
            TitleTextView.setTextColor(getResources().getColor(R.color.red_800));
            RestartBTN.setText(R.string.restart);
            userData.subtractOneFromWaveCount();
        }
    }

    private void addDataToInv() {
        userInventory.addGoldCoins(Integer.parseInt(userGold));
        int moneyEarned = calculateMoney(enemysKilled, userScore, userGold, damageTaken);

        if (moneyEarned < 0) {//if money earned from wave is negative then remove money
            userInventory.removeMoney(moneyEarned);
        } else {
            userInventory.addMoney(moneyEarned);
        }
    }

    private int calculateMoney(String enemysKilled, String userScore, String Gold, String damageTaken) {
        int KilledEnemys = Integer.parseInt(enemysKilled);
        int score = Integer.parseInt(userScore);
        int gold = Integer.parseInt(Gold);
        int damage = Integer.parseInt(damageTaken);

        return (KilledEnemys * gold) + (int) (score * 1.5) - damage;
    }

    public void Settup() {
        backgroundSurface = findViewById(R.id.surfaceView);
        ScoreTXT = findViewById(R.id.ScoreTXTView);
        GoldTXT = findViewById(R.id.GoldTXTView);
        MoneyTXT = findViewById(R.id.MoneyRewardTXT);
        RestartBTN = findViewById(R.id.RestartGameBTN);
        GoToTownBTN = findViewById(R.id.GoToStoreBTN);
        TitleTextView = findViewById(R.id.GameOverTXTView);

        SettupOnclickLiseners();
    }

    private void SettupOnclickLiseners() {
        RestartBTN.setOnClickListener(view -> {
            RestartGame();
            finish();
        });

        GoToTownBTN.setOnClickListener(view -> {
            GoToStore();
            finish();
        });
    }

    private void startStarBackground() {
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        int[] screenSize = {point.x, point.y};
        menuSpaceBackground = new MenuSpaceBackground(getApplicationContext(), backgroundSurface, screenSize);
        backgroundEffects = new Thread(menuSpaceBackground);
        backgroundEffects.start();
    }

    private void GoToStore() {
        Intent GoToTown = new Intent(this, WeponShopActivity.class);
        startActivity(GoToTown);
    }

    private void RestartGame() {

        startActivity(new Intent(this, AsteroidGameActivity.class));
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
           menuSpaceBackground.stopThread();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        menuSpaceBackground.stopThread();
        startActivity(new Intent(this, AsteroidGameActivity.class));
        finish();
    }
}