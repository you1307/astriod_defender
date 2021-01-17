package com.thetechnoobs.moterskillgame.asteriodgame;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.thetechnoobs.moterskillgame.UserData;

public class WaveSpawnThread extends Thread {
    AsteroidGameView asteroidGameView;
    int numberOfWavesCompleted;
    int threadsToComplete = 3;
    UserData userData;
    SpawnEnemys spawnEasyEnemy;
    SpawnAsteroid spawnAsteroid;
    ItemDropManager itemDropManager;
    Thread enemyThread, asteroidThread, itemDropThread;
    public boolean goToEndScreen = true;
    private boolean pause = false;

    public WaveSpawnThread(AsteroidGameView asteroidGameView, Context context) {
        this.asteroidGameView = asteroidGameView;
        userData = new UserData(context);
        numberOfWavesCompleted = userData.getCurrentWaveCount();
        waveDone(false);
        spawnEasyEnemy = new SpawnEnemys(getAmountOfEnemyToSpawn(), asteroidGameView, numberOfWavesCompleted, this);
        spawnAsteroid = new SpawnAsteroid(getAmountOfAsteroidToSpawn(), asteroidGameView, numberOfWavesCompleted, this);
        itemDropManager = new ItemDropManager(asteroidGameView, numberOfWavesCompleted, this);

        enemyThread = new Thread(spawnEasyEnemy);
        asteroidThread = new Thread(spawnAsteroid);
        itemDropThread = new Thread(itemDropManager);


        Log.v("testing", "wave: " + userData.getCurrentWaveCount());
    }

    private int getAmountOfEnemyToSpawn() {
        return numberOfWavesCompleted++;
    }

    private int getAmountOfAsteroidToSpawn() {
        int increaseCount = 1;

        if (numberOfWavesCompleted > 14) {
            increaseCount = 4;
        }

        return numberOfWavesCompleted * increaseCount;
    }

    @Override
    public void run() {
        enemyThread.start();
        asteroidThread.start();
        itemDropThread.start();

        while (true){
            if(threadsToComplete<2){
                itemDropManager.stop();
            }
        }
    }

    public void waveSetDone(@Nullable String identifier) {
        threadsToComplete--;
        if (threadsToComplete == 0) {
            waveDone(true);
        }

        if(identifier != null){
            Log.v("testing", identifier+" is done");
        }
    }


    private void waveDone(boolean done) {
        if (goToEndScreen) {
            asteroidGameView.waveDone(done);
        }
    }

    public void stopAll() {
        goToEndScreen = false;
        spawnAsteroid.stopThread();
        spawnEasyEnemy.stopThread();
        itemDropManager.stop();

        try {
            spawnAsteroid.join();
            spawnEasyEnemy.join();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


class SpawnAsteroid extends Thread {
    int totalAmountToSpawn;
    int curWave;
    AsteroidGameView asteroidGameView;
    boolean run = true;
    WaveSpawnThread waveSpawnThread;

    SpawnAsteroid(int amount, AsteroidGameView asteroidGameView, int curWave, WaveSpawnThread waveSpawnThread) {
        totalAmountToSpawn = amount;
        this.waveSpawnThread = waveSpawnThread;
        this.curWave = curWave;
        this.asteroidGameView = asteroidGameView;
    }

    @Override
    public void run() {
        while (totalAmountToSpawn > 0 && run) {
            if (!asteroidGameView.paused) {
                if (asteroidGameView.EasyEnemy.size() < 5) {
                    asteroidGameView.spawnAsteroids(1);
                    totalAmountToSpawn -= 1;
                }
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                break;
            }
        }

        waveSpawnThread.waveSetDone("asteriods");

    }

    public void stopThread() {
        run = false;
    }
}


class SpawnEnemys extends Thread {
    int totalAmountToSpawn;
    int curWave;
    WaveSpawnThread waveSpawnThread;
    boolean run = true;
    AsteroidGameView asteroidGameView;

    SpawnEnemys(int amount, AsteroidGameView asteroidGameView, int curWave, WaveSpawnThread waveSpawnThread) {
        totalAmountToSpawn = amount;
        this.waveSpawnThread = waveSpawnThread;
        this.curWave = curWave;
        this.asteroidGameView = asteroidGameView;
    }

    @Override
    public void run() {
        while (totalAmountToSpawn > 0 && run) {

            if (!asteroidGameView.paused) {
                if (curWave % 6 == 0) {
                    bossWaveInitiate();
                    totalAmountToSpawn = -1;
                } else {
                    if (asteroidGameView.EasyEnemy.size() < 4) {
                        asteroidGameView.spawnEasyEnemy(1);
                        totalAmountToSpawn -= 1;
                    }
                }
            }


            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }
        waveSpawnThread.waveSetDone("enemys");
    }

    private void bossWaveInitiate() {
        asteroidGameView.spawnHardEnemy(3);
    }

    public void stopThread() {
        run = false;
    }
}
