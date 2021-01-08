package com.thetechnoobs.moterskillgame.asteriodgame;

import android.content.Context;
import android.util.Log;

import com.thetechnoobs.moterskillgame.UserData;

public class WaveSpawnThread extends Thread {
    AsteroidGameView asteroidGameView;
    int numberOfWavesCompleted;
    UserData userData;
    SpawnEnemys spawnEasyEnemy;
    SpawnAsteroid spawnAsteroid;
    ItemDropManager itemDropManager;
    Thread enemyThread, asteroidThread, itemDropThread;
    private boolean goToEndScreen = true;

    public WaveSpawnThread(AsteroidGameView asteroidGameView, Context context) {
        this.asteroidGameView = asteroidGameView;
        userData = new UserData(context);
        numberOfWavesCompleted = userData.getCurrentWaveCount();

        spawnEasyEnemy = new SpawnEnemys(getAmountOfEnemyToSpawn(), asteroidGameView, numberOfWavesCompleted);
        spawnAsteroid = new SpawnAsteroid(getAmountOfAsteroidToSpawn(), asteroidGameView, numberOfWavesCompleted);
        itemDropManager = new ItemDropManager(asteroidGameView, numberOfWavesCompleted);

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
        waveDone();
    }


    private void waveDone() {
        if (goToEndScreen) {
            asteroidGameView.waveDone();
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

    SpawnAsteroid(int amount, AsteroidGameView asteroidGameView, int curWave) {
        totalAmountToSpawn = amount;
        this.curWave = curWave;
        this.asteroidGameView = asteroidGameView;
    }

    @Override
    public void run() {
        while (totalAmountToSpawn > 0 && run) {
            if (asteroidGameView.EasyEnemy.size() < 5) {
                asteroidGameView.spawnAsteroids(1);
                totalAmountToSpawn -= 1;
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public void stopThread() {
        run = false;
    }
}


class SpawnEnemys extends Thread {
    int totalAmountToSpawn;
    int curWave;
    boolean run = true;
    AsteroidGameView asteroidGameView;

    SpawnEnemys(int amount, AsteroidGameView asteroidGameView, int curWave) {
        totalAmountToSpawn = amount;
        this.curWave = curWave;
        this.asteroidGameView = asteroidGameView;
    }

    @Override
    public void run() {
        while (totalAmountToSpawn > 0 && run) {

            if (curWave % 5 == 0) {
                bossWaveInitiate();
                totalAmountToSpawn -= 1;
            } else {
                if (asteroidGameView.EasyEnemy.size() < 4) {
                    asteroidGameView.spawnEasyEnemy(1);
                    totalAmountToSpawn -= 1;
                }
            }


            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    private void bossWaveInitiate() {
        if (asteroidGameView.hardEnemies.size() < 2) {
            asteroidGameView.spawnHardEnemy(2);
        }
    }

    public void stopThread() {
        run = false;
    }
}
