package com.thetechnoobs.moterskillgame;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.thetechnoobs.moterskillgame.asteriodgame.AsteroidGameActivity;
import com.thetechnoobs.moterskillgame.asteriodgame.WeponShopActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UserData userData = new UserData(getApplicationContext());


        if(userData.isNewPLayer()){
            userData.saveNewPlayer(false);
            startActivity(new Intent(MainActivity.this, AsteroidGameActivity.class));
        }else{
            startActivity(new Intent(MainActivity.this, WeponShopActivity.class));
        }

        finish();

    }
}