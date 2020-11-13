package com.thetechnoobs.moterskillgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class EndGameScreen extends Activity {
    TextView ScoreTXT;
    Button RestartBtn, EndGameBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.end_game_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Settup();
        LoadScore();
    }

    private void LoadScore() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            String userScore = bundle.get("score").toString();
            ScoreTXT.setText(userScore);
        }else{
            Log.v("testing", "Error Loading Score");
        }

    }


    public void Settup(){
        ScoreTXT = findViewById(R.id.ScoreTXTView);
        RestartBtn = findViewById(R.id.RestartGameBTN);
        EndGameBtn = findViewById(R.id.EndGameBTN);

        SettupOnclickLiseners();
    }

    private void SettupOnclickLiseners() {
        RestartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RestartGame();
            }
        });

        EndGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EndGame();
            }
        });
    }

    private void EndGame() {
        finish();
    }

    private void RestartGame() {
        startActivity(new Intent(this, GameActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, GameActivity.class));//TODO when added to drunk gauge make this go to main screen, not restart game
        finish();
    }
}
