package Scene;

import Game.Game;
import config.GameConfig;

/**
 * Created by egor on 02.07.15.
 */
public class GameLoop {
    private boolean isRunning = false;
    private final static int NANO_IN_SECOND = 1000000000;
    private int physicsFps = 60;
    private int graphicsFps = 60;

    public void run(){
        long now, dt = 0;
        long last = System.nanoTime();
        long physStepTime = NANO_IN_SECOND / GameConfig.PHYSICS_FPS;
        long graphStepTime = NANO_IN_SECOND / GameConfig.FPS;
        while (isRunning) {
            now = System.nanoTime();
            dt += Math.min(NANO_IN_SECOND, (now - last));
            if (dt > graphStepTime) {
                while (dt > physStepTime) {
                    dt -= physStepTime;
                    onPhysicsFrame.run();
                }
                onGraphicsFrame.run();
            }
            last = now;
        }
    }
    public void pause(){
        isRunning = false;
    }

    public GameLoop(Runnable onPhysicsFrame, Runnable onGraphicsFrame) {
        this.onPhysicsFrame = onPhysicsFrame;
        this.onGraphicsFrame = onGraphicsFrame;
    }

    public GameLoop(int graphicsFps, int physicsFps, Runnable onPhysicsFrame, Runnable onGraphicsFrame) {
        this(onPhysicsFrame, onGraphicsFrame);
        this.graphicsFps = graphicsFps;
        this.physicsFps = physicsFps;
    }

    private Runnable onPhysicsFrame;
    private Runnable onGraphicsFrame;
}
