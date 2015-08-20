package com.example.planes.Activities;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.planes.Config.MenuConfig;
import com.example.planes.Utils.Helper;

/**
 * Created by egor on 20.08.15.
 */
public class MyTextView extends TextView {
    public MyTextView(Context context, float h, float y, String text) {
        super(context);
        setTextSize(TypedValue.COMPLEX_UNIT_PX, h);
        setTypeface(MenuConfig.MAIN_FONT);
        setTextColor(MenuConfig.DARK_BROWN);
        setText("Play");
        Helper.setY(this, (int) y);
    }


}
