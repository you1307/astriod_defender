package com.thetechnoobs.moterskillgame.asteriodgame;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import com.thetechnoobs.moterskillgame.R;

public class AsteroidAudioThread {
    private static final int MAX_NUM_OF_SOUNDS = 20;
    Context context;
    SoundPool soundPool;
    int assaultRifleSound, astriodExplosionSound, userHitSound, coinCollectedSound, easyEnemyHitSound, assaultRifleReloadSound, easyEnemyDeadSound, basicGunShootSound;
    int easyEnemyShootingSound, basicGunReloadSound, shotGunReloadSound, shotGunShotSound;

    public AsteroidAudioThread(Context context) {
        this.context = context;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setMaxStreams(MAX_NUM_OF_SOUNDS)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPool = new SoundPool(MAX_NUM_OF_SOUNDS, AudioManager.STREAM_MUSIC, 0);
        }

        assaultRifleSound = soundPool.load(context, R.raw.smg_double_shot, 0);
        astriodExplosionSound = soundPool.load(context, R.raw.crash, 0);
        userHitSound = soundPool.load(context, R.raw.hit_by_asteroid, 0);
        coinCollectedSound = soundPool.load(context, R.raw.coin_collect, 0);
        easyEnemyHitSound = soundPool.load(context, R.raw.easy_enemy_hit, 0);
        assaultRifleReloadSound = soundPool.load(context, R.raw.assault_rifle_reload, 0);
        easyEnemyDeadSound = soundPool.load(context, R.raw.easy_enemy_death, 0);
        basicGunShootSound = soundPool.load(context, R.raw.simple_gun, 0);
        easyEnemyShootingSound = soundPool.load(context, R.raw.easy_enemy_shoot, 0);
        basicGunReloadSound = soundPool.load(context, R.raw.reload_basic_gun, 0);
        shotGunReloadSound = soundPool.load(context, R.raw.shotgun_reload, 0);
        shotGunShotSound = soundPool.load(context, R.raw.shotgun_shot, 0);
    }

    public void startShotGunShotSound(){
        soundPool.play(shotGunShotSound, 1,1 , 0, 0, 1);
    }

    public void startShotgunReloadSound(){
        soundPool.play(shotGunReloadSound, 1, 1, 0, 0, 1);
    }

    public void startAssaultRifleSound() {
        soundPool.play(assaultRifleSound, 1, 1, 0, 0, 1.2f);
    }

    public void startAssaultRifleReloadSound() {
        soundPool.play(assaultRifleReloadSound, 1, 1, 0, 0, 1);
    }

    public void startAsteroidExplosionSound() {
        soundPool.play(astriodExplosionSound, 0.5F, 0.5F, 0, 0, 1.5F);
    }

    public void startUserHitSound() {
        soundPool.play(userHitSound, 1, 1, 0, 0, 1);
    }

    public void startCoinCollectSound() {
        soundPool.play(coinCollectedSound, 1, 1, 0, 0, 1);
    }

    public void startEasyEnemyHitSound() {
        soundPool.play(easyEnemyHitSound, 1, 1, 0, 0, 1);
    }

    public void startEasyEnemyShootingSound() {
        soundPool.play(easyEnemyShootingSound, 1, 1, 0, 0, 1);
    }

    public void startDeadEnemySound() {
        soundPool.play(easyEnemyDeadSound, 1, 1, 0, 0, 1);
    }

    public void startBasicGunShootSound() {
        soundPool.play(basicGunShootSound, 1, 1, 0, 0, 1);
    }

    public void startBasicGunReload() {
        soundPool.play(basicGunReloadSound, 1, 1, 0, 0, 1);
    }

    public void releaseAll() {
        soundPool.release();
    }



}