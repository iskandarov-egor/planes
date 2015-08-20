package com.example.planes.Game;

import android.util.Log;
import com.example.planes.Communication.Message.TurnMessage;
import com.example.planes.Config.BmpConfig;
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
    private final Round game;
    private SceneButton buttonUp;
    private SceneButton buttonDown;
    private SceneButton buttonStopGo;
    private SceneButton buttonFire;
    private SceneButton buttonResurrect;
    private StaticSprite stopSprite, goSprite;
    private boolean locked = false;

    public Controls(Round round) {
        this.game = round;
        Scene scene = round.getScene();
        float r = -Helper.getScreenRatio();
        buttonDown = scene.createButton(r + BmpConfig.btnMargin, -BmpConfig.btnMargin,
                new StaticSprite(R.drawable.btn_up), 2 * BmpConfig.btnRadius);
        buttonDown.setAngle(MathHelper.PI);
        buttonUp = scene.createButton(-Helper.getScreenRatio() + BmpConfig.btnMargin, BmpConfig.btnMargin,
                new StaticSprite(R.drawable.btn_up), 2 * BmpConfig.btnRadius);
        buttonFire = scene.createButton(Helper.getScreenRatio() - BmpConfig.btnMargin, BmpConfig.btnMargin,
                new StaticSprite(R.drawable.btn_fire), 2 * BmpConfig.btnRadius);
        stopSprite = new StaticSprite(R.drawable.btn_stop);
        goSprite = new StaticSprite(R.drawable.btn_go);
        buttonStopGo = scene.createButton(Helper.getScreenRatio() - BmpConfig.btnMargin, -BmpConfig.btnMargin, goSprite, 2 * BmpConfig.btnRadius);

        buttonResurrect = scene.createButton(0, .8f, goSprite, 0.1f);

    }
    private boolean leftDown = false;
    private boolean rightDown = false;
    private boolean goin = false;

    @Override
    public void onButtonDown(SceneButton btn) {
        if(locked) return;
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
            Bullet bullet = game.getMyPlane().fire(game.getBulletsGroup());
//            game.getGameObjects().add(bullet);
//            .add(bullet.getSceneObject());
            game.getMessageListener().broadcastMessage(new TurnMessage(TurnMessage.Action.FIRE, game.getMyPlane()));
        }
        if(btn == buttonResurrect) {
            game.getMyPlane().setXY(0, 0);
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

    public void setButtonsVisible(boolean visible) {
        buttonUp.setVisible(visible);
        buttonDown.setVisible(visible);
        buttonFire.setVisible(visible);
        buttonStopGo.setVisible(visible);
    }

    public void lock() {
        locked = true;
    }

    public void unlock() {
        locked = false;
    }
}
