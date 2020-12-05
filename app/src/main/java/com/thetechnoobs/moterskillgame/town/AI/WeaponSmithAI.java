package com.thetechnoobs.moterskillgame.town.AI;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.DisplayMetrics;

import com.thetechnoobs.moterskillgame.R;
import com.thetechnoobs.moterskillgame.town.ui.ChatBubble;
import com.thetechnoobs.moterskillgame.town.Town;
import com.thetechnoobs.moterskillgame.town.UserCharacter;

public class WeaponSmithAI {
    int CurX, CurY;
    int Spritewidth, Spriteheight;
    int xSpriteSheetLoc = 1, ySpriteSheetLoc = 1;
    int[] screenSize;
    UserCharacter userCharacter;
    ChatBubble chatBubble;
    Town town;
    Bitmap weaponSmithSpriteSheet;
    private boolean drawAprochText = false;

    public WeaponSmithAI(int curX, int curY, int[] screenSize, Resources resources, UserCharacter userCharacter, Town town) {
        CurX = curX;
        CurY = curY;
        this.screenSize = screenSize;
        this.userCharacter = userCharacter;
        this.town = town;
        chatBubble = new ChatBubble(resources, screenSize);

        weaponSmithSpriteSheet = BitmapFactory.decodeResource(resources, R.drawable.knight_idel_sprites_sheet);
        settupSpriteSheet();
    }

    public static float convertPixelsToDp(float px) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return Math.round(px / (metrics.densityDpi / 160f));
    }

    public static float convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return Math.round(dp * (metrics.densityDpi / 160f));
    }

    public void draw(Canvas canvas, Town town) {
        int x = Spritewidth * xSpriteSheetLoc - Spritewidth;
        int y = Spriteheight * ySpriteSheetLoc - Spriteheight;

        Rect spriteFrame = new Rect(x, y, x + Spritewidth, y + Spriteheight);
        RectF destination = new RectF(town.getOffsetPosX() + convertDpToPixel(235), town.getOffsetPosY() + convertDpToPixel(430), town.getOffsetPosX() + convertDpToPixel(215) + Spritewidth, town.getOffsetPosY() + convertDpToPixel(420) + Spriteheight);

        canvas.drawBitmap(weaponSmithSpriteSheet, spriteFrame, destination, null);

        if (drawAprochText) {
            canvas.drawBitmap(chatBubble.getChatBubbleWeponWelcomBitmap(), town.getOffsetPosX() + convertDpToPixel(250), town.getOffsetPosY() + convertDpToPixel(415), null);
        }

    }

    public void update() {
        xSpriteSheetLoc++;
        if (xSpriteSheetLoc > 4) {
            xSpriteSheetLoc = 1;
        }
    }

    public void drawAprochedText(boolean draw) {
        drawAprochText = draw;
    }

    private void settupSpriteSheet() {
        Spritewidth = weaponSmithSpriteSheet.getWidth() / 4;
        Spriteheight = weaponSmithSpriteSheet.getHeight();
    }

    public RectF getBoundry() {
        return new RectF(town.getOffsetPosX() + convertDpToPixel(220), town.getOffsetPosY() + convertDpToPixel(450), town.getOffsetPosX() + convertDpToPixel(300), town.getOffsetPosY() + convertDpToPixel(570));
    }

    public int getCurX() {
        return CurX;
    }

    public void setCurX(int curX) {
        CurX = curX;
    }

    public int getCurY() {
        return CurY;
    }

    public void setCurY(int curY) {
        CurY = curY;
    }
}
