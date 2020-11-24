package com.thetechnoobs.moterskillgame;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.thetechnoobs.moterskillgame.asteriodgame.AsteroidGameActivity;
import com.thetechnoobs.moterskillgame.town.TownActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //startActivity(new Intent(MainActivity.this, AsteroidGameActivity.class));
        //finish();

        startActivity(new Intent(MainActivity.this, TownActivity.class));//TODO remove and make game start menu
        finish();


    }
}