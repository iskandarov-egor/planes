package Game.Models;

import Scene.SceneObject;
import Game.Game;
import Sprite.StaticSprite;

/**
 * Created by egor on 02.07.15.
 */
public class Base extends SceneObject{


    public Base(Game game) {
        super(0, 0);
        setSprite(new StaticSprite(/*бла.png*/));
        ///поставить базу на землю
    }

}
