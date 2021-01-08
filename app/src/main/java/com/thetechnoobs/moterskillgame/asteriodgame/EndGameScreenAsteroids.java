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

import com.thetechnoobs.moterskillgame.R;
import com.thetechnoobs.moterskillgame.UserData;
import com.thetechnoobs.moterskillgame.UserInventory;
import com.thetechnoobs.moterskillgame.asteriodgame.entites.BadGuy;
import com.thetechnoobs.moterskillgame.asteriodgame.ui.BackgroundStar;

import java.util.ArrayList;
import java.util.Random;

public class EndGameScreenAsteroids extends Activity {
    TextView ScoreTXT, GoldTXT, MoneyTXT, TotalMoneyTXT, TotalGoldTXT, TitleTextView;
    String userScore, userGold, enemysKilled, damageTaken;
    boolean waveCompleted;
    Button RestartBTN, GoToTownBTN;
    UserInventory userInventory = new UserInventory(this);
    UserData userData = new UserData(this);
    starThread starThread;
    SurfaceView backgroundSurface;
    Point point = new Point();
    int[] screenSize;

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
        TotalMoneyTXT.setText("Total Money: " + userInventory.getMoney());
        TotalGoldTXT.setText("Total GoldCoins: " + userInventory.getGoldCoins());

        if (waveCompleted) {
            TitleTextView.setText("Wave " + (userData.getCurrentWaveCount()) + " Completed");
            TitleTextView.setTextColor(getResources().getColor(R.color.green_800));
            RestartBTN.setText(R.string.next_wave);
        } else {
            TitleTextView.setTextColor(getResources().getColor(R.color.red_800));
            RestartBTN.setText(R.string.restart);
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
        TotalMoneyTXT = findViewById(R.id.TotalMoneyTXT);
        TotalGoldTXT = findViewById(R.id.TotalGoldTXT);
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
        starThread = new starThread(this, backgroundSurface, screenSize);
        Thread starsView = new Thread(starThread);
        starsView.start();
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
        starThread.stopThread();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        starThread.stopThread();
        startActivity(new Intent(this, AsteroidGameActivity.class));
        finish();
    }
}

class starThread implements Runnable {
    Context context;
    SurfaceView surfaceView;
    Canvas canvas;
    ArrayList<BackgroundStar> backgroundStars = new ArrayList<>();
    int[] screenSize;
    Paint paint = new Paint();
    private boolean shouldRun = true;
    private int starCount = 10;

    public starThread(Context context, SurfaceView surfaceView, int[] screenSize) {
        this.context = context;
        this.surfaceView = surfaceView;
        this.screenSize = screenSize;
        paint.setColor(context.getResources().getColor(R.color.black));

        while (backgroundStars.size() < starCount) {
            BackgroundStar backgroundStar = new BackgroundStar(new Random().nextInt(screenSize[0] - 10),
                    new Random().nextInt(screenSize[1]),
                    new Random().nextInt(10) + 1,
                    screenSize,
                    context.getResources());

            backgroundStars.add(backgroundStar);
        }
    }

    @Override
    public void run() {
        while (shouldRun) {
            draw();
            update();

            try {
                Thread.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void update() {
        for (int s = 0; s < backgroundStars.size(); s++) {
            if (backgroundStars.get(s).getCurY() > screenSize[1]) {
                backgroundStars.remove(s);
            } else {
                backgroundStars.get(s).setCurY(backgroundStars.get(s).getCurY() + backgroundStars.get(s).getSpeed());
            }

        }

        if (backgroundStars.size() < starCount) {
            BackgroundStar backgroundStar = new BackgroundStar(new Random().nextInt(screenSize[0] - 10), 0, new Random().nextInt(10) + 1, screenSize, context.getResources());
            backgroundStars.add(backgroundStar);
        }
    }

    private void draw() {
        if (surfaceView.getHolder().getSurface().isValid()) {
            canvas = surfaceView.getHolder().lockCanvas();
            canvas.drawRect(0, 0, screenSize[0], screenSize[1], paint);

            for (int s = 0; s < backgroundStars.size(); s++) {
                canvas.drawBitmap(backgroundStars.get(s).stareBitmap, backgroundStars.get(s).getCurX(), backgroundStars.get(s).getCurY(), null);
            }


            surfaceView.getHolder().unlockCanvasAndPost(canvas);
        }
    }

    public void stopThread() {
        shouldRun = false;
    }
}
