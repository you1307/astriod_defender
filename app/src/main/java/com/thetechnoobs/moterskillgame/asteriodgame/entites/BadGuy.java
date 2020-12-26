package com.thetechnoobs.moterskillgame.asteriodgame.entites;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;

import com.thetechnoobs.moterskillgame.R;
import com.thetechnoobs.moterskillgame.asteriodgame.AsteroidAudioThread;
import com.thetechnoobs.moterskillgame.asteriodgame.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BadGuy {
    public int x, y;
    public int MaxHeath = EnemyParameters.easyEnemyHeathMax;
    public Bitmap EasyEnemyAlive, EnemyHeathHeart;
    public int[] screenSize;
    public int speed = new Random().nextInt(Constants.DEFULT_EASY_ENEMY_SPEED);
    public ShootThread shootThread = new ShootThread(3000);
    int CurHeath = MaxHeath;
    Boolean direction = false; //true means move right, false means move left
    AsteroidAudioThread asteroidAudioThread;
    ArrayList<EasyEnemyBullet> bullets = new ArrayList<>();
    Resources resources;
    UserCharecter userCharecter;

    public BadGuy(Resources resources, Context context, UserCharecter userCharecter, int[] screenSize, int PosX, int PosY) {
        this.screenSize = screenSize;
        x = PosX;
        y = PosY;
        this.resources = resources;
        this.userCharecter = userCharecter;

        asteroidAudioThread = new AsteroidAudioThread(context);
        shootThread.start();

        EasyEnemyAlive = BitmapFactory.decodeResource(resources, R.drawable.enemy_alive_easy);
        EasyEnemyAlive = Bitmap.createScaledBitmap(EasyEnemyAlive,
                screenSize[0] / Constants.SCALE_RATIO_NUM_X_EASY_ENEMY,
                screenSize[1] / Constants.SCALE_RATIO_NUM_Y_EASY_ENEMY,
                false);

        EnemyHeathHeart = BitmapFactory.decodeResource(resources, R.drawable.enemy_heart_full);
        EnemyHeathHeart = Bitmap.createScaledBitmap(EnemyHeathHeart,
                screenSize[0] / Constants.SCALE_RATIO_NUM_X_ENEMY_HEART,
                screenSize[1] / Constants.SCALE_RATIO_NUM_Y_ENEMY_HEART,
                false);
    }

    public void update(Canvas canvas) {
        Move();
        DrawHeath(canvas);
        updateBullets(canvas);
        CheckBulletHitBox();
    }

    public void Move() {
        if (getX() + EasyEnemyAlive.getWidth() > screenSize[0]) {
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
        int yOffset = getY() - EnemyHeathHeart.getHeight();
        int xOffset = getX();
        List<BadGuy> heathHearts = new ArrayList<>();

        for (int i = 0; i < getMaxHeath(); i++) {
            heathHearts.add(this);


            if (i < getCurHeath()) {//if heart should be full
                canvas.drawBitmap(heathHearts.get(i).EnemyHeathHeart, xOffset, yOffset, null);
                xOffset = xOffset + heathHearts.get(i).EnemyHeathHeart.getWidth();//adjust location for next heart
            }

            if (i - 1 == getMaxHeath()) {
                yOffset = getY() - EnemyHeathHeart.getHeight();
                xOffset = getX();
            }
        }
    }

    private void CheckBulletHitBox() {
        for (int b = 0; b < bullets.size(); b++) {
            if (bullets.get(b).getHitbox().intersect(userCharecter.getHitBox())) {
                bullets.remove(b);
                userCharecter.setHeath(userCharecter.getHeath() - 2);
                asteroidAudioThread.startUserHitSound();
                break;
            }
        }
    }

    private void updateBullets(Canvas canvas) {
        for (int b = 0; b < bullets.size(); b++) {

            bullets.get(b).setCurY(bullets.get(b).getCurY() + bullets.get(b).getSpeed());

            canvas.drawBitmap(bullets.get(b).bitmap, bullets.get(b).getCurX(), bullets.get(b).getCurY(), null);


            if (bullets.get(b).getCurY() > screenSize[1]) {
                bullets.remove(b);
                b--;
            }
        }
    }

    public void cleanup() {
        shootThread.interrupt();
        shootThread.stopThread();
    }

    public void Shoot() {
        EasyEnemyBullet easyEnemyBullet = new EasyEnemyBullet(resources, 20, screenSize, getX() + (float) EasyEnemyAlive.getWidth() / 2, getY() + EasyEnemyAlive.getHeight());
        bullets.add(easyEnemyBullet);
        asteroidAudioThread.startEasyEnemyShootingSound();
    }

    public RectF getHitBox() {
        return new RectF(getX(), getY(), getX() + EasyEnemyAlive.getWidth(), getY() + EasyEnemyAlive.getHeight());
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
