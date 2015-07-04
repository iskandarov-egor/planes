package Game.Models;

import Game.Game;
import Scene.Body;
import Scene.SceneObject;
import Scene.Sprite.StaticSprite;

/**
 * Created by egor on 02.07.15.
 */
public class Base extends SceneObject{


    public Base(Game game) {
        super(0, 0);
        setSprite(new StaticSprite(/*бла.png*/));
        setBody(new Body(this, 10));
        ///поставить базу на землю
    }

}
