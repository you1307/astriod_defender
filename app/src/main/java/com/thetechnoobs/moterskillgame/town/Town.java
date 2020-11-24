package com.thetechnoobs.moterskillgame.town;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.DisplayMetrics;

import com.thetechnoobs.moterskillgame.R;

import java.util.ArrayList;
import java.util.List;

public class Town {
    Bitmap TownBitmap;
    UserCharacter userCharacter;
    int OffsetPosX = 0, OffsetPosY = 0;
    int OffsetPosXDP = 0, OffsetPosYDP = 0;
    int[] screenSize;
    int speed = TownConstants.USER_SPEED;

    public Town(Resources resources, int[] screenSize, UserCharacter userCharacter) {
        this.screenSize = screenSize;
        this.userCharacter = userCharacter;

        TownBitmap = BitmapFactory.decodeResource(resources, R.drawable.town);
    }

    public static float convertPixelsToDp(float px) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return Math.round(px / (metrics.densityDpi / 160f));
    }

    public static float convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return Math.round( dp * (metrics.densityDpi / 160f));
    }

    public void update() {

    }

    public void updateMapView(int direction) {
        for (int b = 0; b < getBoundaries().size(); b++) {//reverse user direction when colliding with a boundary
            if (userCharacter.getHitbox().intersect(getBoundaries().get(b))) {
                switch (direction) {
                    case 0:
                        direction = 2;
                        userCharacter.moveUser(direction);
                        break;
                    case 1:
                        direction = 3;
                        userCharacter.moveUser(direction);
                        break;
                    case 2:
                        direction = 0;
                        userCharacter.moveUser(direction);
                        break;
                    case 3:
                        direction = 1;
                        userCharacter.moveUser(direction);
                        break;
                }
            }
        }


        switch (direction) {//simulate moving character
            //up=0 right=1 down=2 left=3
            case 0:
                if (OffsetPosY == 0) {
                    userCharacter.moveUser(direction);
                } else {
                    setPosY(getPosY() + speed);
                }
                return;
            case 1:
                if (OffsetPosX + TownBitmap.getWidth() < screenSize[0]) {
                    userCharacter.moveUser(direction);
                } else {
                    setPosX(getPosX() - speed);
                }
                return;
            case 2:
                if (OffsetPosY + TownBitmap.getHeight() < screenSize[1]) {
                    userCharacter.moveUser(direction);
                } else {
                    setPosY(getPosY() - speed);
                }
                return;
            case 3:
                if (OffsetPosX == 0) {
                    userCharacter.moveUser(direction);
                } else {
                    setPosX(getPosX() + speed);
                }
                return;
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(TownBitmap, OffsetPosX, OffsetPosY, null);

        for (int b = 0; b < getBoundaries().size(); b++) {
            //canvas.drawRect(getBoundaries().get(b), new Paint());
        }
    }

    public List<RectF> getBoundaries() {
        List<RectF> boundaries = new ArrayList<>();
        RectF BoundFence1, BoundFence2, BoundInn1, BoundInn2, BoundChem1, BoundChem2, BoundRegHouse1, BoundRegHouse2, BoundWepon1, BoundWepon2;
        boundaries.clear();

        BoundFence1 = new RectF(OffsetPosX, OffsetPosY, OffsetPosX + convertDpToPixel(380), OffsetPosY + convertDpToPixel(35));
        BoundFence2 = new RectF(OffsetPosX, OffsetPosY, OffsetPosX + convertDpToPixel(90), OffsetPosY + convertDpToPixel(270));

        BoundInn1 = new RectF(OffsetPosX + convertDpToPixel(128), OffsetPosY + convertDpToPixel(95), OffsetPosX + convertDpToPixel(352), OffsetPosY + convertDpToPixel(190));
        BoundInn2 = new RectF(OffsetPosX + convertDpToPixel(160), OffsetPosY + convertDpToPixel(190), OffsetPosX + convertDpToPixel(255), OffsetPosY + convertDpToPixel(225));

        BoundChem1 = new RectF(OffsetPosX + convertDpToPixel(512), OffsetPosY + convertDpToPixel(130), OffsetPosX + convertDpToPixel(737), OffsetPosY + convertDpToPixel(225));
        BoundChem2 = new RectF(OffsetPosX + convertDpToPixel(610), OffsetPosY + convertDpToPixel(97), OffsetPosX + convertDpToPixel(737), OffsetPosY + convertDpToPixel(130));

        BoundRegHouse1 = new RectF(OffsetPosX + convertDpToPixel(512), OffsetPosY + convertDpToPixel(332), OffsetPosX + convertDpToPixel(737), OffsetPosY + convertDpToPixel(455));
        BoundRegHouse2 = new RectF(OffsetPosX + convertDpToPixel(640), OffsetPosY + convertDpToPixel(455), OffsetPosX + convertDpToPixel(737), OffsetPosY + convertDpToPixel(555));

        BoundWepon1 = new RectF(OffsetPosX + convertDpToPixel(98), OffsetPosY + convertDpToPixel(330), OffsetPosX + convertDpToPixel(352), OffsetPosY + convertDpToPixel(450));
        BoundWepon2 = new RectF(OffsetPosX + convertDpToPixel(98), OffsetPosY + convertDpToPixel(450), OffsetPosX + convertDpToPixel(225), OffsetPosY + convertDpToPixel(520));

        boundaries.add(BoundFence1);
        boundaries.add(BoundFence2);
        boundaries.add(BoundInn1);
        boundaries.add(BoundInn2);
        boundaries.add(BoundChem1);
        boundaries.add(BoundChem2);
        boundaries.add(BoundRegHouse1);
        boundaries.add(BoundRegHouse2);
        boundaries.add(BoundWepon1);
        boundaries.add(BoundWepon2);


        return boundaries;
    }

    public List<RectF> getAIBoundries(){
        List<RectF> AIBounds = new ArrayList<>();
        AIBounds.clear();

        RectF AlcamistBound = new RectF(OffsetPosX+convertDpToPixel(535), OffsetPosY+convertDpToPixel(225), OffsetPosX+convertDpToPixel(630), OffsetPosY+convertDpToPixel(320));


        AIBounds.add(AlcamistBound);

        return AIBounds;
    }

    public int getOffsetPosX() {
        return OffsetPosX;
    }

    public void setOffsetPosX(int offsetPosX) {
        OffsetPosX = offsetPosX;
    }

    public int getOffsetPosY() {
        return OffsetPosY;
    }

    public void setOffsetPosY(int offsetPosY) {
        OffsetPosY = offsetPosY;
    }

    public int getPosX() {
        return OffsetPosX;
    }

    public void setPosX(int posX) {
        OffsetPosX = posX;
    }

    public int getPosY() {
        return OffsetPosY;
    }

    public void setPosY(int posY) {
        OffsetPosY = posY;
    }
}
