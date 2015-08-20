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
import com.example.planes.Config.MenuConfig;
import com.example.planes.R;

/**
 * Created by egor on 19.08.15.
 */
public class MenuActivity extends Activity {
    Activity that = this;
    TextView tvPlay;
    TextView tvBeClient;
    TextView tvBeServer;
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


        layout.setBackgroundColor(MenuConfig.SKY_COLOR);
        tvPlay = new MyTextView(this, (142f*scale), (0.49f*h), "Play");
        tvChangeName = new MyTextView(this, scale * 46, 0.78f * h, "Change name");
        tvBeServer = new MyTextView(this, scale * 97, 0.49f * h, "Create");
        tvBeClient = new MyTextView(this, scale * 97, 0.68f * h, "Connect");

        tvPlay.setOnClickListener(makePlayClickListener());
    }

    private View.OnClickListener makePlayClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leaveMain();
                goToPlayMode();
            }
        };
    }

    private void goToPlayMode() {
        layout.addView(tvBeClient);
        layout.addView(tvBeServer);
    }

    private void goToMain() {
        layout.addView(tvPlay);
        layout.addView(tvChangeName);
    }

    private void leaveMain() {
        layout.removeView(tvPlay);
        layout.removeView(tvChangeName);
    }

}
