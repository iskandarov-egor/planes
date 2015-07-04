package Game.Models.Bonus;

import Scene.Scene;
import Scene.SceneObject;
import Scene.Sprite.StaticSprite;
/**
 * Created by egor on 02.07.15.
 */
public class Bonus extends SceneObject{
    SceneObject parachute;
    public Bonus(Scene scene, double x, double y) {
        super(x, y);

        parachute = new SceneObject(0, -10);
        parachute.setParent(this);
        scene.addObject(parachute);
        parachute.setSprite( new StaticSprite());
        setSprite(new StaticSprite());

    }

    public SceneObject getParachute() {
        return parachute;
    }
}
