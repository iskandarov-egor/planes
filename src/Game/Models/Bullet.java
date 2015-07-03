package Game.Models;

import Scene.SceneObject;
import Scene.Scene;
import Scene.Body;
import Sprite.StaticSprite;
import Utils.Vector;
import config.GameConfig;

/**
 * Created by egor on 02.07.15.
 */
public class Bullet extends SceneObject{

    public Bullet(double x, double y) {
        super(x, y);



        setSprite(new StaticSprite(/*бла.png*/));
        setBody(new Body(this, 2));
    }

}
