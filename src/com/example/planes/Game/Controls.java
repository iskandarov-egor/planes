package com.example.planes.Game;

import android.util.Log;
import com.example.planes.Communication.Message.TurnMessage;
import com.example.planes.Config.Config;
import com.example.planes.Engine.Scene.AnimatedSprite;
import com.example.planes.Engine.Scene.Scene;
import com.example.planes.Engine.Scene.SceneButton;
import com.example.planes.Engine.Scene.StaticSprite;
import com.example.planes.Engine.SceneButtonListener;
import com.example.planes.Game.Models.Bullet;
import com.example.planes.R;
import com.example.planes.Utils.Helper;
import com.example.planes.Utils.MathHelper;

/**
 * Created by egor on 30.07.15.
 */
public class Controls implements SceneButtonListener{
    private final Game game;
    private SceneButton buttonUp;
    private SceneButton buttonDown;
    private SceneButton buttonStopGo;
    private SceneButton buttonFire;
    private SceneButton buttonResurrect;
    private StaticSprite stopSprite, goSprite;

    public Controls(Game game) {
        this.game = game;
        Scene scene = game.getEngine().getScene();
        float r = -Helper.getScreenRatio();
        buttonDown = scene.createButton(r + Config.btnMargin, -Config.btnMargin,
                new StaticSprite(R.drawable.btn_up, 2 * Config.btnRadius));
        buttonDown.setAngle(MathHelper.PI);
        buttonUp = scene.createButton(-Helper.getScreenRatio() + Config.btnMargin, Config.btnMargin,
                new StaticSprite(R.drawable.btn_up, 2 * Config.btnRadius));
        buttonFire = scene.createButton(Helper.getScreenRatio() - Config.btnMargin, Config.btnMargin,
                new StaticSprite(R.drawable.btn_fire, 2 * Config.btnRadius));
        stopSprite = new StaticSprite(R.drawable.btn_stop, 2*Config.btnRadius);
        goSprite = new StaticSprite(R.drawable.btn_go, 2*Config.btnRadius);
        buttonStopGo = scene.createButton(Helper.getScreenRatio() - Config.btnMargin, -Config.btnMargin, goSprite);
        buttonResurrect = scene.createButton(0, .8f, new AnimatedSprite(R.drawable.propeller, 0.3f, 8, 1f));
    }
    private boolean leftDown = false;
    private boolean rightDown = false;
    private boolean goin = false;

    @Override
    public void onButtonDown(SceneButton btn) {
        if(btn == buttonUp) {
            Log.d("multitouch madness", ".. it is a goLeft");
            leftDown = true;
            goLeft();
        }
        if(btn == buttonDown) {
            Log.d("multitouch madness", ".. it is a goRight");
            rightDown = true;
            goRight();

        }
        if(btn == buttonStopGo) {
            Log.d("multitouch madness", ".. it is a stopGo");
            goin = !goin;
            if(goin){
                buttonStopGo.setSprite(stopSprite);
                game.getMyPlane().startEngine();
            } else {
                buttonStopGo.setSprite(goSprite);
                game.getMyPlane().stopEngine();
            }
            game.getMessageListener().broadcastMessage(new TurnMessage(TurnMessage.Action.ENGINE, game.getMyPlane()));
        }
        if(btn == buttonFire) {
            Log.d("multitouch madness", ".. it is a FAYA ZA MISAELOZ");
            Bullet bullet = game.getMyPlane().fire();
            game.getGameObjects().add(bullet);
            game.getBulletsGroup().add(bullet.getSceneObject());
            game.getMessageListener().broadcastMessage(new TurnMessage(TurnMessage.Action.FIRE, game.getMyPlane()));
        }
        if(btn == buttonResurrect) {
            game.getMyPlane().getSceneObject().setXY(0, 0);
            game.getMyPlane().resurrect();
        }
    }

    @Override
    public void onButtonUp(SceneButton btn, boolean pointInside) {
        if(btn == buttonUp) {
            leftDown = false;
            Log.d("multitouch madness", ".. it is a goLeft");
        }
        if(btn == buttonDown) {
            rightDown = false;
            Log.d("multitouch madness", ".. it is a goRight");
        }
        if(btn == buttonFire) {
            Log.d("multitouch madness", ".. it is a FAYA ZA MISAELOZ");
        }
        if(btn == buttonUp || btn == buttonDown) {
            if (rightDown) goRight();
            else if (leftDown) goLeft();
            else goStraight();
        }
    }

    private void goRight() {
        game.getMyPlane().goRight();
        game.getMessageListener().broadcastMessage(new TurnMessage(TurnMessage.Action.GO_RIGHT, game.getMyPlane()));
    }

    private void goLeft() {
        game.getMyPlane().goLeft();
        game.getMessageListener().broadcastMessage(new TurnMessage(TurnMessage.Action.GO_LEFT, game.getMyPlane()));
    }

    private void goStraight() {
        game.getMyPlane().goStraight();
        game.getMessageListener().broadcastMessage(new TurnMessage(TurnMessage.Action.GO_STRAIGHT, game.getMyPlane()));
    }
}
