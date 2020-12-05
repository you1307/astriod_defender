package com.thetechnoobs.moterskillgame.asteriodgame;

import android.content.Context;
import android.media.MediaPlayer;

import com.thetechnoobs.moterskillgame.R;

public class AsteroidAudioThread {
    public AsteriodHitUserThread asteriodHitUserThread = new AsteriodHitUserThread();
    public SimpleShootAsteriodSound simpleShootAsteriodSound = new SimpleShootAsteriodSound();
    public SimpleShootSound simpleShootSound = new SimpleShootSound();
    public EasyEnemyHitSound easyEnemyHitSound = new EasyEnemyHitSound();
    public EasyEnemyShootSound easyEnemyShootSound = new EasyEnemyShootSound();
    public EasyEnemyDeathSound easyEnemyDeathSound = new EasyEnemyDeathSound();
    public CoinCollectSound coinCollectSound = new CoinCollectSound();
    public AssaultRifleShootSound assaultRifleShootSound = new AssaultRifleShootSound();
    public BasicGunReloadSound basicGunReloadSound = new BasicGunReloadSound();
    public AssaultRifleReloadSound assaultRifleReloadSound = new AssaultRifleReloadSound();
    Context context;
    MediaPlayer mediaPlayer;


    public AsteroidAudioThread(Context context) {
        this.context = context;
    }

    public class AssaultRifleReloadSound implements Runnable {
        @Override
        public void run() {
            mediaPlayer = MediaPlayer.create(context, R.raw.assault_rifle_reload);
            mediaPlayer.start();
        }
    }

    public class BasicGunReloadSound implements Runnable {
        @Override
        public void run() {
            mediaPlayer = MediaPlayer.create(context, R.raw.reload_basic_gun);
            mediaPlayer.start();
        }
    }

    public class AssaultRifleShootSound implements Runnable {
        @Override
        public void run() {
            mediaPlayer = MediaPlayer.create(context, R.raw.smg_double_shot);
            mediaPlayer.start();
        }
    }

    public class AsteriodHitUserThread implements Runnable {
        @Override
        public void run() {
            mediaPlayer = MediaPlayer.create(context, R.raw.hit_by_asteroid);
            mediaPlayer.start();
        }
    }

    public class SimpleShootAsteriodSound implements Runnable {
        @Override
        public void run() {
            mediaPlayer = MediaPlayer.create(context, R.raw.crash);
            mediaPlayer.start();
        }
    }

    public class SimpleShootSound implements Runnable {
        @Override
        public void run() {
            mediaPlayer = MediaPlayer.create(context, R.raw.simple_gun);
            mediaPlayer.start();
        }
    }

    public class EasyEnemyHitSound implements Runnable {
        @Override
        public void run() {
            mediaPlayer = MediaPlayer.create(context, R.raw.easy_enemy_hit);
            mediaPlayer.start();
        }
    }

    public class EasyEnemyShootSound implements Runnable {
        @Override
        public void run() {
            mediaPlayer = MediaPlayer.create(context, R.raw.easy_enemy_shoot);
            mediaPlayer.start();
        }
    }

    public class EasyEnemyDeathSound implements Runnable {
        @Override
        public void run() {
            mediaPlayer = MediaPlayer.create(context, R.raw.easy_enemy_death);
            mediaPlayer.start();
        }
    }

    public class CoinCollectSound implements Runnable {
        @Override
        public void run() {
            mediaPlayer = MediaPlayer.create(context, R.raw.coin_collect);
            mediaPlayer.start();
        }
    }
}