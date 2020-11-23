package com.thetechnoobs.moterskillgame.asteriodgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.thetechnoobs.moterskillgame.R;
import com.thetechnoobs.moterskillgame.UserInventory;
import com.thetechnoobs.moterskillgame.town.TownActivity;

public class EndGameScreenAsteroids extends Activity {
    TextView ScoreTXT, GoldTXT, MoneyTXT, TotalMoneyTXT, TotalGoldTXT;
    String userScore, userGold, enemysKilled;
    Button RestartBTN, GoToTownBTN;
    UserInventory userInventory = new UserInventory(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.end_game_screen_asteroids);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Settup();
        Load();
    }

    private void Load() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userScore = bundle.get("score").toString();
            userGold = bundle.get("gold").toString();
            enemysKilled = bundle.get("enemysKilled").toString();


            addDataToInv();
            setTXTviews();
        } else {
            Log.v("testing", "Error Loading Score");
        }


    }

    private void setTXTviews() {
        MoneyTXT.setText(String.valueOf(calculateMoney(enemysKilled, userScore, userGold)));
        GoldTXT.setText(userGold);
        ScoreTXT.setText("Score: " + userScore);
        TotalMoneyTXT.setText("Total Money: " + userInventory.getMoney());
        TotalGoldTXT.setText("Total GoldCoins: " + userInventory.getGoldCoins());
    }

    private void addDataToInv() {
        userInventory.addGoldCoins(Integer.parseInt(userGold));
        userInventory.addMoney(calculateMoney(enemysKilled, userScore, userGold));
    }

    private int calculateMoney(String enemysKilled, String userScore, String Gold) {
        int KilledEnemys = Integer.parseInt(enemysKilled);
        int score = Integer.parseInt(userScore);
        int gold = Integer.parseInt(Gold);
        int result = 0;

        if (score != 0 || KilledEnemys != 0 || gold != 0) {
            result = score / KilledEnemys * gold;
        }


        return result;
    }


    public void Settup() {
        ScoreTXT = findViewById(R.id.ScoreTXTView);
        GoldTXT = findViewById(R.id.GoldTXTView);
        MoneyTXT = findViewById(R.id.MoneyRewardTXT);
        RestartBTN = findViewById(R.id.RestartGameBTN);
        GoToTownBTN = findViewById(R.id.GoToTownBTN);
        TotalMoneyTXT = findViewById(R.id.TotalMoneyTXT);
        TotalGoldTXT = findViewById(R.id.TotalGoldTXT);

        SettupOnclickLiseners();
    }

    private void SettupOnclickLiseners() {
        RestartBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RestartGame();
                finish();
            }
        });

        GoToTownBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToTown();
                finish();
            }


        });
    }

    private void GoToTown() {
        Intent GoToTown = new Intent(this, TownActivity.class);
        startActivity(GoToTown);
    }

    private void RestartGame() {
        startActivity(new Intent(this, AsteroidGameActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, AsteroidGameActivity.class));
        finish();
    }
}
