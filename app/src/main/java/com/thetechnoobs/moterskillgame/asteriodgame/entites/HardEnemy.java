package com.thetechnoobs.moterskillgame.asteriodgame.entites;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.DisplayMetrics;

import com.thetechnoobs.moterskillgame.R;
import com.thetechnoobs.moterskillgame.asteriodgame.AsteroidAudioThread;
import com.thetechnoobs.moterskillgame.asteriodgame.Constants;
import com.thetechnoobs.moterskillgame.asteriodgame.projectiles.HardEnemyBomb;
import com.thetechnoobs.moterskillgame.asteriodgame.ui.Explosion;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HardEnemy {
    public int x, y;
    public int MaxHeath = EnemyParameters.hardEnemyHeathMax;
    public Bitmap hardEnemyAlive, hardHeathHeart;
    public int[] screenSize;
    public int speed = new Random().nextInt(Constants.DEFULT_EASY_ENEMY_SPEED);
    public ShootThread shootThread = new ShootThread(3000);
    int CurHeath = MaxHeath;
    Boolean direction = false; //true means move right, false means move left
    AsteroidAudioThread asteroidAudioThread;
    ArrayList<HardEnemyBomb> bullets = new ArrayList<>();
    Resources resources;
    UserCharecter userCharecter;

    public HardEnemy(Resources resources, Context context, UserCharecter userCharecter, int[] screenSize, int PosX, int PosY) {
        this.screenSize = screenSize;
        x = PosX;
        y = PosY;
        this.resources = resources;
        this.userCharecter = userCharecter;

        asteroidAudioThread = new AsteroidAudioThread(context);
        shootThread.start();

        hardEnemyAlive = BitmapFactory.decodeResource(resources, R.drawable.hard_enemy);
        hardEnemyAlive = Bitmap.createScaledBitmap(hardEnemyAlive,
                screenSize[0] / Constants.SCALE_RATIO_NUM_X_EASY_ENEMY,
                screenSize[1] / Constants.SCALE_RATIO_NUM_Y_EASY_ENEMY,
                false);

        hardHeathHeart = BitmapFactory.decodeResource(resources, R.drawable.enemy_heart_full);
        hardHeathHeart = Bitmap.createScaledBitmap(hardHeathHeart,
                screenSize[0] / Constants.SCALE_RATIO_NUM_X_ENEMY_HEART,
                screenSize[1] / Constants.SCALE_RATIO_NUM_Y_ENEMY_HEART,
                false);
    }

    public static float convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return Math.round(dp * (metrics.densityDpi / 160f));
    }

    public void update(Canvas canvas) {
        Move();
        DrawHeath(canvas);
        updateBullets(canvas);
    }

    public void Move() {
        if (getX() + hardEnemyAlive.getWidth() > screenSize[0]) {
            //if enemy is far left of screen
            direction = false;
        } else if (getX() < 0) {
            //if enemy is far right of screen
            direction = true;
        }

        if (direction) {
            setX(getX() + speed);
        } else {
            setX(getX() - speed);
        }
    }

    public void DrawHeath(Canvas canvas) {
        int yOffset = getY() - hardHeathHeart.getHeight();
        int xOffset = getX();
        List<HardEnemy> heathHearts = new ArrayList<>();

        for (int i = 0; i < getMaxHeath(); i++) {
            heathHearts.add(this);


            if (i < getCurHeath()) {//if heart should be full
                canvas.drawBitmap(heathHearts.get(i).hardHeathHeart, xOffset, yOffset, null);
                xOffset = xOffset + heathHearts.get(i).hardHeathHeart.getWidth();//adjust location for next heart
            }

            if (i - 1 == getMaxHeath()) {
                yOffset = getY() - hardHeathHeart.getHeight();
                xOffset = getX();
            }
        }
    }

    private void bombHitUser() {
        userCharecter.setHeath(userCharecter.getHeath() - 4);
        asteroidAudioThread.startUserHitSound();
    }

    private void updateBullets(Canvas canvas) {
        for (int b = 0; b < bullets.size(); b++) {

            if (bullets.get(b).getCurY() >= userCharecter.getCurY()) {
                explode(bullets.get(b).getCurX(), bullets.get(b).getCurY(), canvas, bullets.get(b));

                bullets.remove(b);
                break;
            } else {
                bullets.get(b).setCurY(bullets.get(b).getCurY() + bullets.get(b).getSpeed());
                canvas.drawBitmap(bullets.get(b).bitmap, bullets.get(b).getCurX(), bullets.get(b).getCurY(), null);
            }

            if (bullets.get(b).getCurY() > screenSize[1]) {
                bullets.remove(b);
                b--;
            }
        }
    }

    private void explode(float X, float Y, Canvas canvas, HardEnemyBomb hardEnemyBomb) {
        Explosion explosion = new Explosion(X - convertDpToPixel(50), Y - convertDpToPixel(50), convertDpToPixel(100), convertDpToPixel(100), resources);
        canvas.drawBitmap(explosion.bitmap, explosion.getX(), explosion.getY(), null);
        asteroidAudioThread.startAsteroidExplosionSound();

        if (userCharecter.getHitBox().intersect(hardEnemyBomb.getHitbox())) {
            bombHitUser();
        }
    }

    public void cleanup() {
        shootThread.interrupt();
        shootThread.stopThread();
    }

    public void Shoot() {
        HardEnemyBomb hardEnemyBomb = new HardEnemyBomb(resources, 6, screenSize, getX() + (float) hardEnemyAlive.getWidth() / 2, getY() + hardEnemyAlive.getHeight());
        bullets.add(hardEnemyBomb);
        asteroidAudioThread.startEasyEnemyShootingSound();
    }

    public RectF getHitBox() {
        return new RectF(getX(), getY(), getX() + hardEnemyAlive.getWidth(), getY() + hardEnemyAlive.getHeight());
    }

    public int getMaxHeath() {
        return MaxHeath;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getCurHeath() {
        return CurHeath;
    }

    public void setCurHeath(int curHeath) {
        CurHeath = curHeath;
    }

    public class ShootThread extends Thread {
        public boolean run = true;
        long WaitTime;

        ShootThread(int WaitTime) {
            this.WaitTime = WaitTime;
        }

        public void stopThread() {
            shootThread.run = false;
        }


        public void run() {

            while (run) {
                try {
                    Thread.sleep(WaitTime);
                } catch (InterruptedException e) {
                    break;
                }
                Shoot();
            }
        }


    }
}
