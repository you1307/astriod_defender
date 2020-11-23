package com.thetechnoobs.moterskillgame.asteriodgame;

import android.content.Context;
import android.media.MediaPlayer;

import com.thetechnoobs.moterskillgame.R;

public class AudioThread {
    public AsteriodHitUserThread asteriodHitUserThread = new AsteriodHitUserThread();
    public SimpleShootAsteriodSound simpleShootAsteriodSound = new SimpleShootAsteriodSound();
    public SimpleShootSound simpleShootSound = new SimpleShootSound();
    public EasyEnemyHitSound easyEnemyHitSound = new EasyEnemyHitSound();
    public EasyEnemyShootSound easyEnemyShootSound = new EasyEnemyShootSound();
    public EasyEnemyDeathSound easyEnemyDeathSound = new EasyEnemyDeathSound();
    public CoinCollectSound coinCollectSound = new CoinCollectSound();
    Context context;
    MediaPlayer mediaPlayer;


    public AudioThread(Context context) {
        this.context = context;
    }

    public class AsteriodHitUserThread implements Runnable {
        public void run() {
            mediaPlayer = MediaPlayer.create(context, R.raw.hit_by_asteroid);
            mediaPlayer.start();
        }
    }

    public class SimpleShootAsteriodSound implements Runnable {
        public void run() {
            mediaPlayer = MediaPlayer.create(context, R.raw.crash);
            mediaPlayer.start();
        }
    }

    public class SimpleShootSound implements Runnable {
        public void run() {
            mediaPlayer = MediaPlayer.create(context, R.raw.simple_gun);
            mediaPlayer.start();
        }
    }

    public class EasyEnemyHitSound implements Runnable {
        public void run() {
            mediaPlayer = MediaPlayer.create(context, R.raw.easy_enemy_hit);
            mediaPlayer.start();
        }
    }

    public class EasyEnemyShootSound implements Runnable {
        public void run() {
            mediaPlayer = MediaPlayer.create(context, R.raw.easy_enemy_shoot);
            mediaPlayer.start();
        }
    }

    public class EasyEnemyDeathSound implements Runnable {
        public void run() {
            mediaPlayer = MediaPlayer.create(context, R.raw.easy_enemy_death);
            mediaPlayer.start();
        }
    }

    public class CoinCollectSound implements Runnable {
        public void run() {
            mediaPlayer = MediaPlayer.create(context, R.raw.coin_collect);
            mediaPlayer.start();
        }
    }
}


