package com.thetechnoobs.moterskillgame.asteriodgame;

public class ItemDropManager implements Runnable {
    AsteroidGameView asteroidGameView;
    int waveCompleted;
    WaveSpawnThread waveSpawnThread;
    private boolean run = true;

    public ItemDropManager(AsteroidGameView asteroidGameView, int numberOfWavesCompleted, WaveSpawnThread waveSpawnThread) {
        this.asteroidGameView = asteroidGameView;
        this.waveSpawnThread = waveSpawnThread;
        waveCompleted = numberOfWavesCompleted;
    }

    @Override
    public void run() {
        while (run) {

            if (asteroidGameView.userCharecter.getHeath() < asteroidGameView.userCharecter.getMaxHeath() / 2 && asteroidGameView.heathPotions.size() == 0) {
                asteroidGameView.spawnHeathPotion();
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        waveSpawnThread.waveSetDone("item drops");
    }

    public void stop() {
        run = false;
    }
}
