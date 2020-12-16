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
    String userScore, userGold, enemysKilled, damageTaken;
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
            damageTaken = bundle.get("damageTaken").toString();


            addDataToInv();
            setTXTviews();
        } else {
            Log.v("testing", "Error Loading Score");
        }


    }

    private void setTXTviews() {
        MoneyTXT.setText(String.valueOf(calculateMoney(enemysKilled, userScore, userGold, damageTaken)));
        GoldTXT.setText(userGold);
        ScoreTXT.setText("Score: " + userScore);
        TotalMoneyTXT.setText("Total Money: " + userInventory.getMoney());
        TotalGoldTXT.setText("Total GoldCoins: " + userInventory.getGoldCoins());
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
        RestartBTN.setOnClickListener(view -> {
            RestartGame();
            finish();
        });

        GoToTownBTN.setOnClickListener(view -> {
            GoToTown();
            finish();
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
