package Game.Models;

import Game.Game;
import Scene.Scene;
import User.User;
import config.GameConfig;

/**
 * Created by egor on 02.07.15.
 */
public class SmallPlane extends Plane {


    public SmallPlane(Scene scene, User player, double x, double y) {
        super(scene, player, x, y);
        health = GameConfig.smallPlaneHealth;
    }
}
