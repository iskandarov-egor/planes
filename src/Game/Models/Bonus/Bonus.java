package Game.Models.Bonus;

import Scene.SceneObject;
import Sprite.StaticSprite;
import Sprite.Sprite;
/**
 * Created by egor on 02.07.15.
 */
public class Bonus extends SceneObject{
    Sprite box, parachute;
    public Bonus(double x, double y) {
        super(x, y);
        box = new StaticSprite(x, y, 10);
        parachute = new StaticSprite(x, y - 10 - 20, 40);
        addSprite(box);
        addSprite(parachute);
    }

    public Sprite getBox() {
        return box;
    }

    public Sprite getParachute() {
        return parachute;
    }
}
