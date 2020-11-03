package com.thetechnoobs.moterskillgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {
    private final Paint paint, BackgroundRectPaint;
    UserCharecter userCharecter;
    ShootButtonUI shootButtonUI;
    RectF backgroundRect, ShootButton;
    ArrayList<BackgroundStar> backgroundStars = new ArrayList<>();
    ArrayList<Astriod> astriods = new ArrayList<>();
    ArrayList<RegularBullet> bullets = new ArrayList<>();
    int[] screenSize = {0, 0};
    Canvas canvas;
    int Touched = 0;
    private Thread thread;
    private boolean isPlaying = false;
    private int screenx, screeny;
    private float UserPosX;
    private float UserPosY;


    public GameView(Context context, int screenx, int screeny) {
        super(context);

        this.screenx = screenx;
        this.screeny = screeny;

        //settup public screen size for ratio calculation
        screenSize[0] = screenx;
        screenSize[1] = screeny;

        backgroundRect = new RectF(0, 0, screenx, screeny);

        userCharecter = new UserCharecter(getResources(), screenSize, (float) screenx / 2, (float) screeny - 300);

        settupButtonUI(context);

        paint = new Paint();
        paint.setColor(Color.BLUE);

        BackgroundRectPaint = new Paint();
        BackgroundRectPaint.setColor(Color.BLACK);

    }

    @Override
    public void run() {
        while (isPlaying) {
            draw();
            //sleep(10);
        }
    }

    private void draw() {
        if (getHolder().getSurface().isValid()) {
            canvas = getHolder().lockCanvas();
            canvas.drawRect(backgroundRect, BackgroundRectPaint);//needed inorder to stop graphics from duplicating
            canvas.drawBitmap(userCharecter.bitmap, userCharecter.getCurX(), userCharecter.getCurY(), null);
            canvas.drawBitmap(shootButtonUI.bitmap, shootButtonUI.getX(), shootButtonUI.getY(), null);

            canvas.drawRect(shootButtonUI.getCollitionBox(), paint);//TODO remove when done testing


            drawBackgrounStars();
            drawAstroids();
            drawBullets();

            update();
            CheckForCollision();
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void update() {

        //update Bullets position
        MoveBullets();

        //deal with astroid and astroid hitbox stuff
        SpawnAndDeleteAstroids();
        MoveAstroids();

        //Deal with background stuff
        moveBackgroundStars();
        SpawnAndDeleteBackgroundStars();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX(), y = event.getY();


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(!checkUIButtons(x, y)){
                    updateUserPos(x, y);
                }
                invalidate();
            case MotionEvent.ACTION_MOVE:
                if(!checkUIButtons(x, y)){
                    updateUserPos(x, y);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:

                invalidate();
                break;
        }

        return true;
    }

    private void updateUserPos(float x, float y) {
        UserPosX = x;
        UserPosY = y;

        userCharecter.setCurX((int) (x - userCharecter.bitmap.getWidth() / 2));
        userCharecter.setCurY((int) (y - userCharecter.bitmap.getHeight() / 2));
    }


    private void CheckForCollision() {

        for (int i = 0; i < astriods.size(); i++) {

            //check if astriod hits user
            if (astriods.get(i).getCollisionBox().intersect(userCharecter.getHitBox())) {
                int Eventx, Eventy;
                Eventx = astriods.get(i).getCurX();
                Eventy = astriods.get(i).getCurY();
                astriods.remove(i);
                Touched++;
                ExplosionFrame(Eventx, Eventy);
                Log.v("testing", "touched user: " + Touched);
            }
        }
    }

    private void MoveBullets() {
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).setCurY((int) (bullets.get(i).getCurY() - bullets.get(i).getSpeed()));
        }
    }

    private void drawBullets() {
        for (RegularBullet i : bullets) {
            canvas.drawBitmap(i.bitmap, i.getCurX(), i.getCurY(), null);
        }
    }

    private void ExplosionFrame(int eventx, int eventy) {
        Explosion explosion = new Explosion(eventx, eventy, screenSize, getResources());
        canvas.drawBitmap(explosion.bitmap, explosion.x, explosion.y, null);
    }

    private void drawBackgrounStars() {
        for (int i = 0; i < backgroundStars.size(); i++) {
            canvas.drawBitmap(backgroundStars.get(i).stareBitmap, backgroundStars.get(i).getCurX(), backgroundStars.get(i).getCurY(), paint);
        }
    }

    private void drawAstroids() {
        for (int i = 0; i < astriods.size(); i++) {
            canvas.drawBitmap(astriods.get(i).bitmap, astriods.get(i).getCurX(), astriods.get(i).getCurY(), paint);
        }
    }

    private void MoveAstroids() {
        for (int i = 0; i < astriods.size(); i++) {
            astriods.get(i).setCurY(astriods.get(i).getCurY() + astriods.get(i).getSpeed());
        }
    }

    private void SpawnAndDeleteAstroids() {
        int maxAstroidSpawn = 6;

        if (astriods.size() < maxAstroidSpawn) {
            Astriod astriod = new Astriod(randomNum(screenx - 200, 200), 0, randomNum(Constants.ASTROID_MAX_SPEED, 5), screenSize, getResources());
            astriods.add(astriod);
        }

        for (int i = 0; i < astriods.size(); i++) {
            if (astriods.get(i).getCurY() > screeny) {
                astriods.remove(i);
            }
        }
    }

    private void SpawnAndDeleteBackgroundStars() {

        for (int i = 0; i < backgroundStars.size(); i++) {
            if (backgroundStars.get(i).getCurY() > screeny - 40) {
                backgroundStars.remove(i);
            }
        }

        if (backgroundStars.size() < 10) {
            BackgroundStar backgroundStar = new BackgroundStar(randomNum(screenx - 20, 20), 0, randomNum(Constants.BACKGROUND_STAR_MAX_SPEED, 5), screenSize, getResources());
            backgroundStars.add(backgroundStar);
        }
    }

    private void moveBackgroundStars() {
        for (int i = 0; i < backgroundStars.size(); i++) {
            backgroundStars.get(i).setCurY(backgroundStars.get(i).getCurY() + backgroundStars.get(i).getSpeed());
        }
    }

    private void settupButtonUI(Context context) {
        BackendSettings backendSettings = new BackendSettings();

        //settup shoot button
        shootButtonUI = new ShootButtonUI(getResources(), screenSize);
        if (backendSettings.getSavedShootButtonSide(context)) {
            shootButtonUI = new ShootButtonUI(0, shootButtonUI.bitmap.getHeight(), true, screenSize, getResources());
        } else {
            shootButtonUI = new ShootButtonUI(screenx - shootButtonUI.bitmap.getWidth(), screeny - shootButtonUI.bitmap.getHeight(), true, screenSize, getResources());
        }
    }

    private boolean checkUIButtons(float x, float y) {
        if (x > shootButtonUI.getX() && y > shootButtonUI.getY()) {
            SpawnBullet();
            return true;
        }
        return false;
    }

    private void SpawnBullet() {
        bullets.add(new RegularBullet(getResources(), 40, screenSize, userCharecter.getCurX() + userCharecter.bitmap.getWidth() / 2, userCharecter.getCurY()));
    }

    public int randomNum(int max, int min) {
        Random random = new Random();
        int ran = random.nextInt(max);

        if (ran < min) {
            ran = min;
        }

        return ran;
    }

    private void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void resume() {
        thread = new Thread(this);
        thread.start();
        isPlaying = true;
    }

    public void pause() {
        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
