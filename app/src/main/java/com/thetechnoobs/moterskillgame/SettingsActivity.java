package com.thetechnoobs.moterskillgame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.thetechnoobs.moterskillgame.asteriodgame.ui.BackgroundStar;
import com.thetechnoobs.moterskillgame.asteriodgame.ui.PauseButtonUI;
import com.thetechnoobs.moterskillgame.asteriodgame.ui.ShootRegularButtonUI;

import java.util.ArrayList;
import java.util.Random;

public class SettingsActivity extends AppCompatActivity {
    Button UIeditBTN;
    SurfaceView backgroundStars;
    int[] screenSize = {0, 0};
    PauseButtonUI pauseButtonUI;
    RectF touchPoint;
    boolean editingUI, pressing = false;
    ShootRegularButtonUI shootRegularButtonUI;
    BackendSettings backendSettings = new BackendSettings();
    SurfaceViewUpdater surfaceViewUpdater;
    Thread UIeditThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        screenSize[0] = point.x;
        screenSize[1] = point.y;

        settup();
        startBackgroundStars();


    }

    private void startBackgroundStars() {
        surfaceViewUpdater = new SurfaceViewUpdater(getApplicationContext(), screenSize, backgroundStars);
        UIeditThread = new Thread(surfaceViewUpdater);
        UIeditThread.start();
    }

    private void settup() {
        UIeditBTN = findViewById(R.id.editUIBTN);
        backgroundStars = findViewById(R.id.settingsBackGroundSV);
    }

    public void settupUIEdit(View view) {

        if (view.getId() == R.id.editUIBTN && !editingUI) {
            UIeditBTN.setText("done");
            int[] shootButtonLoc = backendSettings.getSavedShootButtonLoc(getApplicationContext());
            int[] pauseButtonLoc = backendSettings.getSavedPauseButtonLoc(getApplicationContext());

            pauseButtonUI = new PauseButtonUI(getResources(), pauseButtonLoc[0], pauseButtonLoc[1]);
            shootRegularButtonUI = new ShootRegularButtonUI(shootButtonLoc[0], shootButtonLoc[1], screenSize, getResources());

            editingUI = true;
        }else if(view.getId() == R.id.editUIBTN && editingUI){
            saveUIplacment();
            cleanup();
            goToMainMenu();
            editingUI = false;
        }

    }

    private void goToMainMenu() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void cleanup() {
        surfaceViewUpdater.shouldStop();
    }

    private void saveUIplacment() {
        backendSettings.setSavedPauseButtonLoc(getApplicationContext(), pauseButtonUI.getLocX(), pauseButtonUI.getLocY());
        backendSettings.setSavedShootButtonLoc(getApplicationContext(), shootRegularButtonUI.getAbsoulutX(), shootRegularButtonUI.getAbsoulutY());
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

                break;
            case MotionEvent.ACTION_MOVE:
                if (touchingUI(event.getX(), event.getY())) {
                    pressing = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                pressing = false;
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }

        return true;

    }

    private boolean touchingUI(float x, float y) {
        touchPoint = new RectF(x, y, x + 5, y + 5);

        if (touchPoint.intersect(shootRegularButtonUI.getCollitionBox()) || touchPoint.intersect(pauseButtonUI.getHitBox())) {
            return true;
        }

        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public class SurfaceViewUpdater implements Runnable {
        boolean shouldRun = true;
        Context context;
        SurfaceView surfaceView;
        Canvas canvas;
        ArrayList<BackgroundStar> backgroundStars = new ArrayList<>();
        int[] screenSize;
        Paint paint = new Paint();
        private int starCount = 10;


        public SurfaceViewUpdater(Context context, int[] screenSize, SurfaceView surfaceView) {
            this.context = context;
            this.screenSize = screenSize;
            this.surfaceView = surfaceView;
            paint.setColor(context.getResources().getColor(R.color.black));

            while (backgroundStars.size() < starCount) {
                BackgroundStar backgroundStar = new BackgroundStar(new Random().nextInt(screenSize[0] - 10),
                        new Random().nextInt(screenSize[1]),
                        new Random().nextInt(10) + 1,
                        screenSize,
                        context.getResources());

                backgroundStars.add(backgroundStar);
            }
        }

        @Override
        public void run() {
            while (shouldRun) {
                update();

                drawSurfaceView();
            }
        }

        private void update() {


            for (int s = 0; s < backgroundStars.size(); s++) {
                if (backgroundStars.get(s).getCurY() > screenSize[1]) {
                    backgroundStars.remove(s);
                } else {
                    backgroundStars.get(s).setCurY(backgroundStars.get(s).getCurY() + backgroundStars.get(s).getSpeed());
                }

            }

            if (backgroundStars.size() < starCount) {
                BackgroundStar backgroundStar = new BackgroundStar(new Random().nextInt(screenSize[0] - 10), 0, new Random().nextInt(15) + 1, screenSize, context.getResources());
                backgroundStars.add(backgroundStar);
            }
        }

        private void drawSurfaceView() {
            if (surfaceView.getHolder().getSurface().isValid()) {
                canvas = surfaceView.getHolder().lockCanvas();

                canvas.drawRect(0, 0, screenSize[0], screenSize[1], paint);

                for (int s = 0; s < backgroundStars.size(); s++) {
                    canvas.drawBitmap(backgroundStars.get(s).stareBitmap, backgroundStars.get(s).getCurX(), backgroundStars.get(s).getCurY(), null);
                }


                if (editingUI) {
                    drawUI();
                }


                surfaceView.getHolder().unlockCanvasAndPost(canvas);
            }
        }


        private void drawUI() {
            if (pressing) {
                if (touchPoint.intersect(pauseButtonUI.getHitBox())) {
                    pauseButtonUI.setLocX((int) touchPoint.right - pauseButtonUI.pauseButtonBitmap.getWidth()/2);
                    pauseButtonUI.setLocY((int) touchPoint.top - pauseButtonUI.pauseButtonBitmap.getHeight()/2);
                } else if (touchPoint.intersect(shootRegularButtonUI.getCollitionBox())) {
                    shootRegularButtonUI.setX((int) touchPoint.right + shootRegularButtonUI.bitmap.getWidth()/2);
                    shootRegularButtonUI.setY((int) touchPoint.top + shootRegularButtonUI.bitmap.getWidth()/2);
                }
            }


            canvas.drawBitmap(pauseButtonUI.pauseButtonBitmap, pauseButtonUI.getLocX(), pauseButtonUI.getLocY(), null);
            canvas.drawBitmap(shootRegularButtonUI.bitmap, shootRegularButtonUI.getX(), shootRegularButtonUI.getY(), null);
        }

        public void shouldStop() {
            shouldRun = false;
        }
    }
}