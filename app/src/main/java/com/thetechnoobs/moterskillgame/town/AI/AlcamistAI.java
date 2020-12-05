package com.thetechnoobs.moterskillgame.town.AI;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.DisplayMetrics;

import com.thetechnoobs.moterskillgame.R;
import com.thetechnoobs.moterskillgame.town.ui.ChatBubble;
import com.thetechnoobs.moterskillgame.town.Town;
import com.thetechnoobs.moterskillgame.town.UserCharacter;

public class AlcamistAI {
    int CurX, CurY;
    int[] screenSize;
    Bitmap AlcamistAIBitmap;
    UserCharacter userCharacter;
    ChatBubble chatBubble;
    int Spritewidth, Spriteheight;
    Paint displayTextPaint = new Paint();
    Town town;
    int xSpriteSheetLoc = 1;
    int ySpriteSheetLoc = 4;
    private boolean drawAprochText = false;

    public AlcamistAI(int curX, int curY, int[] screenSize, Resources resources, UserCharacter userCharacter, Town town) {
        CurX = curX;
        CurY = curY;
        this.screenSize = screenSize;
        this.userCharacter = userCharacter;
        this.town = town;
        chatBubble = new ChatBubble(resources, screenSize);

        displayTextPaint.setColor(Color.BLACK);
        displayTextPaint.setStrokeWidth(convertDpToPixel(50));
        displayTextPaint.setTextSize(convertDpToPixel(30));

        AlcamistAIBitmap = BitmapFactory.decodeResource(resources, R.drawable.alchemist_sprite_sheet);

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

    private void settupSpriteSheet() {
        Spritewidth = AlcamistAIBitmap.getWidth() / 8;
        Spriteheight = AlcamistAIBitmap.getHeight() / 4;
    }

    public void draw(Canvas canvas, Town town) {
        int x = Spritewidth * xSpriteSheetLoc - Spritewidth;
        int y = Spriteheight * ySpriteSheetLoc - Spriteheight;

        Rect spriteFrame = new Rect(x, y, x + Spritewidth, y + Spriteheight);
        RectF destination = new RectF(town.getOffsetPosX() + convertDpToPixel(545), town.getOffsetPosY() + convertDpToPixel(225), town.getOffsetPosX() + convertDpToPixel(570) + Spritewidth, town.getOffsetPosY() + convertDpToPixel(265) + Spriteheight);

        canvas.drawBitmap(AlcamistAIBitmap, spriteFrame, destination, null);

        if (drawAprochText) {
            canvas.drawBitmap(chatBubble.getChatBubbleCemWelcomBitmap(), town.getOffsetPosX() + convertDpToPixel(575), town.getOffsetPosY() + convertDpToPixel(200), null);
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

    public RectF getBoudry() {
        return new RectF(town.getOffsetPosX() + convertDpToPixel(535), town.getOffsetPosY() + convertDpToPixel(225), town.getOffsetPosX() + convertDpToPixel(630), town.getOffsetPosY() + convertDpToPixel(320));
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
