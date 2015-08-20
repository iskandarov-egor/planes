package com.example.planes.Activities;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.planes.R;

/**
 * Created by egor on 19.08.15.
 */
public class MenuActivity extends Activity {
    Activity that = this;
    TextView tvPlay;
    TextView tvChangeName;
    RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);


        layout = (RelativeLayout) findViewById(R.id.relativelayout);
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                ViewTreeObserver obs = layout.getViewTreeObserver();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    obs.removeOnGlobalLayoutListener(this);
                } else {
                    obs.removeGlobalOnLayoutListener(this);
                }

                setupViews();
                goToMain();
            }

        });
    }

    private void setupViews() {
        int h = layout.getBottom(); // get height
        float scale = h / 720f;

        int brown = Color.parseColor("#423e2c");


        tvPlay = new MyTextView(this, (142f*scale), (0.49f*h), "Play");


    }

    //private TextView

    private void setY(View view, int height) {
        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        param.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        param.topMargin = height;
        view.setLayoutParams(param);
    }

    private void goToMain() {
        layout.addView(tvPlay);

    }

    private int getStatusBarH() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void stuff() {











        //param.topMargin = (int) (240);


    }
}
