package Game.Models;

import Scene.Body;
import Scene.SceneObject;
import Scene.Sprite.StaticSprite;

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
