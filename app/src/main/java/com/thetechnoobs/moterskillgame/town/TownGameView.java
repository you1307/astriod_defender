package com.thetechnoobs.moterskillgame.town;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.List;

public class TownGameView extends SurfaceView implements Runnable {

    public boolean isPlaying = false;
    int[] screenSize = {0, 0};
    Canvas canvas;
    Paint TextPaint;
    Town town;
    UserCharacter userCharacter;
    AlcamistAI alcamistAI;
    List<RectF> ArrowUI;
    ControllerUI controllerUI;
    CurrencyUI currencyUI;
    int MoveUserDirection = 2;
    private Thread thread;
    private long FPS = 0;
    private long fps;
    private boolean moving = false;

    public TownGameView(Context context, int screenx, int screeny) {
        super(context);
        screenSize[0] = screenx;
        screenSize[1] = screeny;


        userCharacter = new UserCharacter((int) convertDpToPixel(410), (int) convertDpToPixel(30), screenSize, getResources());
        userCharacter.moveUser(MoveUserDirection);//set so user is facing the right direction upon spawn

        town = new Town(getResources(), screenSize, userCharacter);

        alcamistAI = new AlcamistAI(0, 0, screenSize, getResources());


        controllerUI = new ControllerUI(getResources(), screenSize);
        ArrowUI = controllerUI.getControllerHitBoxs();

        currencyUI = new CurrencyUI(getContext(), getResources(), screenSize);

        TextPaint = new Paint();
        TextPaint.setTextSize(70f);
        TextPaint.setColor(Color.BLACK);

    }

    public static float convertPixelsToDp(float px) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return Math.round(px / (metrics.densityDpi / 160f));
    }

    public static float convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return Math.round(dp * (metrics.densityDpi / 160f));
    }

    @Override
    public void run() {
        while (isPlaying) {
            long startFrameTime = System.currentTimeMillis();

            update();
            draw();

            LimitFPS(startFrameTime);
        }
    }

    public void update() {
        town.update();
        alcamistAI.update();
        userCharacter.update();
        checkBoundres();
    }

    private void checkBoundres() {
        List<RectF> boundres = town.getBoundaries();
        List<RectF> AIBounds = town.getAIBoundries();

        for (int b = 0; b < boundres.size(); b++) {
            if (userCharacter.getHitbox().intersect(boundres.get(b))) {
                moving = false;
                break;
            }
        }

        for (int a = 0; a < AIBounds.size(); a++) {
            if (userCharacter.getHitbox().intersect(AIBounds.get(a))) {
                alcamistAI.drawAprochedText(true);
                Log.v("testing", "Show chat for Alcamist");//TODO show chat alcmy store icon to user
            } else {
                alcamistAI.drawAprochedText(false);
            }
        }
    }

    public void draw() {
        if (getHolder().getSurface().isValid()) {
            canvas = getHolder().lockCanvas();

            town.draw(canvas);


            alcamistAI.draw(canvas, town);
            userCharacter.draw(canvas);


            DrawUI();


            if (moving) {
                town.updateMapView(MoveUserDirection);
                userCharacter.updateSprite(MoveUserDirection);
            }
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void DrawUI() {
        controllerUI.draw(canvas);
        currencyUI.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // get pointer index from the event object
        int pointerIndex = event.getActionIndex();

        // get pointer ID
        int pointerId = event.getPointerId(pointerIndex);

        // get masked (not specific to a pointer) action
        int maskedAction = event.getActionMasked();

        switch (maskedAction) {
            case MotionEvent.ACTION_DOWN:
                if (MoveUI(event.getX(), event.getY(), true)) {
                    moving = true;
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:
                if (!MoveUI(event.getX(), event.getY(), false)) {
                    moving = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (MoveUI(event.getX(), event.getY(), true)) {
                    moving = false;
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        invalidate();

        return true;
    }

    private boolean MoveUI(float x, float y, boolean ActOn) {
        for (int i = 0; i < ArrowUI.size(); i++) {

            if (x < ArrowUI.get(i).right && x > ArrowUI.get(i).left && y < ArrowUI.get(i).bottom && y > ArrowUI.get(i).top) {
                if (ActOn) {
                    MoveUserDirection = i;
                    userCharacter.updateSprite(i);
                    town.updateMapView(i);
                }
                return true;
            }
        }
        return false;
    }

    private boolean CheckUI(float x, float y) {

        return false;
    }

    public void LimitFPS(long startFrameTime) {
        long timeThisFrame = System.currentTimeMillis() - startFrameTime;
        if (timeThisFrame >= 1) {
            fps = 1000 / timeThisFrame;
        }

        if (timeThisFrame < 40) {
            try {
                Thread.sleep(33 - timeThisFrame);
            } catch (Exception e) {
                e.printStackTrace();
            }
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