package com.thetechnoobs.moterskillgame.asteriodgame;

public class ItemDropManager implements Runnable {
    AsteroidGameView asteroidGameView;
    int waveCompleted;
    private boolean run = true;

    public ItemDropManager(AsteroidGameView asteroidGameView, int numberOfWavesCompleted) {
        this.asteroidGameView = asteroidGameView;
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
    }

    public void stop() {
        run = false;
    }
}
