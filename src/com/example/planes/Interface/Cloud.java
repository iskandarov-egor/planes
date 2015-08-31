package com.example.planes.Interface;

import android.content.Context;
import android.graphics.Matrix;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.example.planes.R;
import com.example.planes.Utils.Helper;
import com.example.planes.Utils.MathHelper;

/**
 * Created by egor on 20.08.15.
 */
public class Cloud extends ImageView {
    float vx, x, y;
    float w2;
    public Cloud(Context context, float x, float y, float vx, float h) {
        super(context);
        setImageDrawable(MyApplication.get().getResources().getDrawable(R.drawable.cloud));

        int w = (int) (h * Helper.getDrawableRatio(R.drawable.cloud));

        this.x = x;
        this.y = y;
        this.vx = vx;
        w2 = w / 2;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w, (int) h);
        params.leftMargin = (int) (x - w2);
        params.topMargin = (int) y;
        //setAdjustViewBounds(true);
        setLayoutParams(params);
        setMinimumWidth(w);
        setMaxHeight((int) h);
        setMaxWidth(w);
        setMinimumHeight((int) h);
        setScaleType(ScaleType.MATRIX);
        Matrix mtr = new Matrix();
        float scale = h / Helper.getDrawableWH(R.drawable.cloud).y;
        mtr.setScale(scale, scale);
        setImageMatrix(mtr);
    }

    public Cloud(Context context, int w, int h) {
        this(context, MathHelper.rand(0, w), MathHelper.rand(0, h), MathHelper.rand(0.7f, 1.5f),
                MathHelper.rand(h*0.09f, h*0.14f));

    }

    static long lastSpam = System.currentTimeMillis();
    static long spamRate = 2000;

    public void move() {
        if(System.currentTimeMillis() - lastSpam > spamRate) {
            lastSpam = System.currentTimeMillis();
            Log.d("hey", "clouds moving...");
        }
        x += vx;
        int screenWidth = Helper.getScreenWidth();
        if(x - w2 > screenWidth) x = -w2;
        else if(x + w2 < 0) x = screenWidth + w2;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getLayoutParams();
        params.leftMargin = (int) (x - w2);
        setLayoutParams(params);
    }
}
