package com.example.planes.Interface;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by egor on 20.08.15.
 */
public class MainImageView extends ImageView {
    public MainImageView(Context context, int img) {
        super(context);
        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        param.addRule(RelativeLayout.CENTER_IN_PARENT);
        setLayoutParams(param);
        setScaleType(ScaleType.CENTER_INSIDE);
        setImageDrawable(context.getResources().getDrawable(img));
    }
}
