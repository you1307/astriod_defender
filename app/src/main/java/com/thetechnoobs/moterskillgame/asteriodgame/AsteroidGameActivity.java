package com.thetechnoobs.moterskillgame.asteriodgame;

import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class AsteroidGameActivity extends AppCompatActivity {
    private AsteroidGameView asteroidGameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);

        asteroidGameView = new AsteroidGameView(this, point.x, point.y);
        setContentView(asteroidGameView);

    }

    @Override
    protected void onPause() {
        super.onPause();
        asteroidGameView.pause();
        finish();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        asteroidGameView.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        asteroidGameView.destroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        asteroidGameView.destroy();
    }
}
