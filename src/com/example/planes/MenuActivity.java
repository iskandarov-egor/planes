package com.example.planes;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.planes.Utils.Helper;

/**
 * Created by egor on 19.08.15.
 */
public class MenuActivity extends Activity {
    Activity that = this;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        tv= new TextView(this);
        tv.setTextColor(Color.parseColor("#423e2c"));
        final RelativeLayout layout = (RelativeLayout) findViewById(R.id.relativelayout);
        layout.addView(tv);
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                ViewTreeObserver obs = tv.getViewTreeObserver();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    obs.removeOnGlobalLayoutListener(this);
                } else {
                    obs.removeGlobalOnLayoutListener(this);
                }

                stuff();
            }

        });
    }

    private void stuff() {
        tv.setText("Play");
        final RelativeLayout layout = (RelativeLayout) findViewById(R.id.relativelayout);



        int h = layout.getBottom();
        float scale = h / 720f;
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, 142 * scale);
        Typeface face= Typeface.createFromAsset(getAssets(), "fonts/expressway rg.ttf");
        tv.setTypeface(face);

        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }

        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        param.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        param.topMargin = (int) (h * 0.52f);
        Log.d("wwww", String.valueOf(tv.getBaseline()));

        tv.setLayoutParams(param);
        //param.topMargin = (int) (240);


    }
}
