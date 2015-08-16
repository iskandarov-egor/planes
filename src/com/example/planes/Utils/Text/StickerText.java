package com.example.planes.Utils.Text;

import android.graphics.*;
import com.example.planes.Engine.Scene.Scene;
import com.example.planes.Engine.Scene.StaticSprite;
import com.example.planes.Engine.Scene.Sticker;
import com.example.planes.Utils.Helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by egor on 27.07.15.
 */
public class StickerText {
    private Scene scene;
    private List<Sticker> stickers = new ArrayList<>();
    private float x, y, h;
    private static Map<Pair, Bitmap> map = new HashMap<>();
    private String text;
    private int ph;
    private Paint.FontMetrics metrics;
    private int color = Color.BLACK;
    private boolean visible = true;

    public StickerText(Scene scene, String text, float x, float y, float height) {
        this(scene, x, y, height);
        this.text = text;
        if(scene.getEngine().isViewportReady()) {
            write(text);
        }

    }

    public StickerText(Scene scene, float x, float y, float height) {
        this.scene = scene;
        this.x = x;
        this.y = y;
        h = height;
        paint.setAntiAlias(true);
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        for(Sticker sticker: stickers) {
            sticker.setVisible(visible);
        }
    }

    public void setText(String text) {
        this.text = text;
        if(scene.getEngine().isViewportReady()) write(text);
    }

    private void write(String text) {
        for(Sticker sticker : stickers) {
            scene.removeSticker(sticker);
        }
        stickers.clear();
        ph = (int) (0.5f * h * Helper.getScreenHeight());
        float fontSize = getFontSizeByHeight(ph); //todo!!
        paint.setTextSize(fontSize);
        metrics = paint.getFontMetrics();

        widths = new float[text.length()];

        paint.getTextWidths((CharSequence)text, 0, text.length(), widths);

        paint.setColor(color);

        float halfLen = getLength()/2;
        float engineHalfLen =  scene.getViewport().screenToEngine(halfLen);
        float curx = -engineHalfLen + x;
        float scrToEngRatio = engineHalfLen / halfLen;

        float cury = -scene.getViewport().screenToEngine(metrics.descent - metrics.ascent)/2 + y;
        for(int i = 0; i < text.length(); i++) {
            StaticSprite letter = new StaticSprite(getLetter(i));
            curx += widths[i] * scrToEngRatio / 2;
            Sticker sticker = scene.createSticker(curx, cury, h);

            sticker.setSprite(letter);
            sticker.setVisible(visible);
            stickers.add(sticker);
            curx += widths[i] * scrToEngRatio / 2;

        }
    }



    public void onScreenChanged() {
        if(text != null && text.length() != 0) write(text);
    }

    private float getLength() {
        float w = 0;
        for(int i = 0; i < widths.length; i++) {
            w += widths[i];
        }
        return w;
    }

    private static float[] widths;

    Paint paint = new Paint();
    private Bitmap getLetter(int idx) {

        Pair pair = new Pair(text.charAt(idx), h);
        Bitmap bitmap = map.get(pair);
        if(bitmap == null) {
            int pw = (int)widths[idx];
            bitmap = Bitmap.createBitmap(pw, ph, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            bitmap.eraseColor(Color.YELLOW);
            canvas.drawText(String.valueOf(text.charAt(idx)), 0, -metrics.ascent, paint);
            map.put(pair, bitmap);
        }
        return bitmap;
    }
    private static class Pair {
        char c;
        float height;

        public Pair(char c, float height) {
            this.c = c;
            this.height = height;
        }

        @Override
        public boolean equals(Object o) {
            if(o instanceof Pair) {
                Pair p = (Pair)o;
                return (c == p.c && height == p.height);
            } else return this == o;
        }

        @Override
        public int hashCode() {
            return (int) (height * c);
        }
    }

    Paint paint2 = new Paint();
    private float getFontSizeByHeight(int height) {
        for (int i = 1; i < 272; i++) {
            paint2.setTextSize(i);
            Paint.FontMetrics m = paint2.getFontMetrics();
            float newdiff =  height - (m.descent - m.ascent);
            if(newdiff < 0) return i;
        }
        return 272;
    }
}
