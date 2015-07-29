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
    private float fontSize;
    private Paint.FontMetrics metrics;
    private int color = Color.BLACK;

    public StickerText(Scene scene, String text, float x, float y, float height) {
        this.scene = scene;
        this.x = x;
        this.y = y;
        this.text = text;
        h = height;
        paint.setAntiAlias(true);
        write(text);
    }

    public void setColor(int color) {
        this.color = color;
    }

    private void write(String text) {
        for(Sticker sticker : stickers) {
            scene.removeSticker(sticker);
        }
        stickers.clear();
        ph = (int) (0.5f * h * Helper.getScreenHeight());
        fontSize = getFontSizeByHeight(ph);
        paint.setTextSize(fontSize);
        metrics = paint.getFontMetrics();

        paint.setColor(color);

        float curx = x;
        for(int i = 0; i < text.length(); i++) {
            StaticSprite letter = new StaticSprite(getLetter(i), h);
            curx += letter.getW()/2;
            Sticker sticker = scene.createSticker(curx, y);

            sticker.setSprite(letter);
            stickers.add(sticker);
            curx += letter.getW()/2;
        }
    }
    private static float[] widths = new float[1];

    Paint paint = new Paint();
    private Bitmap getLetter(int idx) {

        Pair pair = new Pair(text.charAt(idx), h);
        Bitmap bitmap = map.get(pair);
        if(bitmap == null) {


            paint.getTextWidths((CharSequence)text, idx, idx + 1, widths);

            int pw = (int)widths[0];
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
