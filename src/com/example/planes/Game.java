package com.example.planes;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import com.example.planes.Config.GameConfig;
import com.example.planes.Engine.*;
import com.example.planes.Engine.Scene.*;
import com.example.planes.Engine.Scene.Object;
import com.example.planes.Utils.MathHelper;

/**
 * Created by egor on 12.07.15.
 */
public class Game implements EngineEventsListener {
    Engine engine;
    ObjectGroup squaresGroup = new ObjectGroup();
    ObjectGroup trianglesGroup = new ObjectGroup();
    StupidTestCamera camera;
    Sticker button;

    public Game() {
        Log.d("hey", "Game()");
        //1. create scene
        this.engine = new Engine();
        engine.setGraphicsFPS(GameConfig.FPS);
        engine.setPhysicsFPS(GameConfig.PHYSICS_FPS);
        final Scene scene = engine.getScene();
        scene.setHorizontalPeriod(1.5f);
        scene.setBackgroundColor(0, 0, 1);

        //2. create objects
        Object squareObject = new Object(0, 0);
        Square square = new Square();
        square.setColor(1, 1, 1);

        squareObject.setSprite(square);
        squareObject.setBody(0.1f);
        scene.addObject(squareObject);

        Object triangleObject = new Object(0.7f, 0 * 0.3f);
        triangleObject.setSprite(new Triangle());
        triangleObject.setAngle(MathHelper.PI);
        triangleObject.setAngleSpeed(MathHelper.PI2); // 2*PI per second
        triangleObject.setSpeed(-0.1f, 0);
        triangleObject.setBody(0.1f);
        scene.addObject(triangleObject);

        //3. create buttons
        button = new Sticker(-getScreenRatio() + 0.2f, 1 - 0.2f);
        scene.addSticker(button);
        button.setSprite(new Square());
        button.setBody(0.1f);

        //4. create collision listeners
        CollisionListener listener = new CollisionListener(squaresGroup, trianglesGroup);
        scene.addToGroup(trianglesGroup, triangleObject);
        scene.addToGroup(squaresGroup, squareObject);
        listener.setOnCollisionStart(new CollisionProcessor() {
            @Override
            public void process(StaticObject object, StaticObject other) {
                scene.setBackgroundColor(1, 0, 0);
            }
        });
        listener.setOnCollisionEnd(new CollisionProcessor() {
            @Override
            public void process(StaticObject object, StaticObject other) {
                scene.setBackgroundColor(0, 0, 1);
            }
        });
        scene.addCollisionListener(listener);

        engine.setEventsListener(this);
        camera = new StupidTestCamera(scene);
        engine.run();
    }


    public boolean onTouchEvent(MotionEvent e) {
        Utils.FloatPoint pos = engine.getScene().getViewport().screenToEngine(e.getRawX(), e.getRawY());
        float x = pos.x;
        float y = pos.y;
        switch(e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(button.isPointInside(x, y)) {
                    engine.getScene().setBackgroundColor(0, 0, 0);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if(button.isPointInside(x, y)) {
                    engine.getScene().setBackgroundColor(0, 1, 1);
                    return true;
                }
                break;
        }
        return false;
    }

    private float getScreenRatio() {
        WindowManager wm = (WindowManager) MyApplication.context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        if(size.x < size.y) throw new RuntimeException("что то не так"); //debug
        return (float)size.x / size.y;
    }



    private int getScreenHeight(){
        WindowManager wm = (WindowManager) MyApplication.context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        if(size.x < size.y) throw new RuntimeException("что то не так"); //debug
        return size.y;
    }

    @Override
    public void onGraphicsFrame(float graphicsFPS) {
        camera.onFrame();
    }

    @Override
    public void onPhysicsFrame(float physicsFPS) {
        //lalala
    }

    public Engine getEngine() {
        return engine;
    }

    public View createView(Context context) {
        return engine.createView(context);
    }
}
