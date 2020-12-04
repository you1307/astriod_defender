package com.thetechnoobs.moterskillgame.town;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.thetechnoobs.moterskillgame.R;

public class ChemShopActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chem_shop);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent GoToTown = new Intent(this, TownActivity.class);
        startActivity(GoToTown);
        finish();
    }
}