package Game;

import config.GameConfig;

/**
 * Created by egor on 02.07.15.
 */
public class GameLoop {
    private boolean isRunning = false;
    private final int NANO_IN_SECOND = 1000000000;
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
                    game.onFrame();
                }
                game.render();
                game.processMessages();
            }
            last = now;
        }
    }
    public void pause(){
        isRunning = false;
    }
    private Game game;
    public GameLoop(Game game) {
        this.game = game;
    }
}
