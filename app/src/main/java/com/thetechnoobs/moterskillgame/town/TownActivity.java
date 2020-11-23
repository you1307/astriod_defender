package com.thetechnoobs.moterskillgame.town;

import android.graphics.Point;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.thetechnoobs.moterskillgame.asteriodgame.AsteroidGameView;

public class TownActivity extends AppCompatActivity {
    public TownGameView townGameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);

        townGameView = new TownGameView(this, point.x, point.y);
        setContentView(townGameView);

    }

    @Override
    protected void onPause() {
        super.onPause();
        townGameView.pause();
        finish();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        townGameView.resume();
    }
}
