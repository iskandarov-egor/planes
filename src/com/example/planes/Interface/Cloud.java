package com.example.planes.Interface;

import android.content.Context;
import android.widget.ImageView;
import com.example.planes.R;
import com.example.planes.Utils.Helper;

/**
 * Created by egor on 20.08.15.
 */
public class Cloud extends ImageView {
    float vx, x, y;
    float w2;
    public Cloud(Context context, float x, float y, float vx, float h) {
        super(context);
        setImageDrawable(MyApplication.get().getResources().getDrawable(R.drawable.cloud));
        setMinimumHeight((int) h);
        int w = (int) (h * Helper.getDrawableRatio(R.drawable.cloud));
        setMinimumWidth(w);
        setMaxHeight((int) h);
        setMaxWidth(w);
        this.x = x;
        this.y = y;
        this.vx = vx;
        w2 = w / 2;
    }

    public void move() {
        x += vx;
        int screenWidth = Helper.getScreenWidth();
        if(x - w2 > screenWidth) x = -w2;
        else if(x + w2 < 0) x = screenWidth + w2;
    }
}
