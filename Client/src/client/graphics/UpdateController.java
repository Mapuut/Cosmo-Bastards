package client.graphics;


import client.input.InputController;
import client.world.WorldController;
import javafx.animation.AnimationTimer;
import udp.network.client.Client;

import java.util.ArrayList;
import java.util.List;

/**
 * Adjusts game speed and fps count. All changes and actions should be started here.
 * Only one instance of this class should be created.
 */
public class UpdateController {

    public double updateTimeNanoSeconds = 1000000000.0 / 60;
    public int updatesPerSecond = 120;
    public ScreenController screenController;
    public WorldController worldController;
    private Client client;

    public AnimationTimer mainLoop;


    public static double delta = 0;
    public static long lastTime = System.nanoTime();

    public UpdateController(ScreenController screenController) {
        this.screenController = screenController;
        this.worldController = new WorldController(this);
        this.client = client;
        createMainLoop();
    }

    public static List<String> recived = new ArrayList<>();
    public static Object recivedLock = new Object();

    public UpdateController() {
    }


    public UpdateController(ScreenController screenController, WorldController worldController, int updatesPerSecond) {
        this.updateTimeNanoSeconds = 1000000000.0 / updatesPerSecond;
        this.updatesPerSecond = updatesPerSecond;
        this.screenController = screenController;
        this.worldController = worldController;
        createMainLoop();
    }

    public void createMainLoop() {
        lastTime = System.nanoTime();
        this.mainLoop = new AnimationTimer() {

            long timer = System.currentTimeMillis();
            int frames = 0;
            int updates = 0;

            int runTime = 0;
            long renderTimeMesurment = 0;

            @Override
            public void handle(long now) {
                delta += (now - lastTime) / updateTimeNanoSeconds;
                lastTime = now;

                boolean render = false;

                while (delta >= 1) {
                    ZoomController.update();
                    InputController.update();
                    worldController.update();
                    screenController.update();
                    updates += 1;
                    delta = delta - 1;
                    render = true;
                }

                if (render) {
                    ZoomController.render(screenController);
                    screenController.render(worldController);
                    frames++;
                    renderTimeMesurment += System.nanoTime() - now;

                } else {
                    renderTimeMesurment += System.nanoTime() - now;
                }


                if (System.currentTimeMillis() - timer > 1000) {
                    timer += 1000;
                    runTime++;
                    long potentialFPS;
                    try {
                        potentialFPS = Math.round(((updateTimeNanoSeconds) / (renderTimeMesurment / frames)) * frames);
                    } catch (ArithmeticException e) {
                        potentialFPS = -1;
                    }
                    renderTimeMesurment = 0;
                    String runtime = String.format("%1$02d:%2$02d:%3$02d", (runTime / (60 * 60)), (runTime / (60)) % 60, runTime % 60);
                    System.out.println(runtime + " | " + updates + " ups | " + frames + " fps | ~" + potentialFPS + " performance | " + Runtime.getRuntime().totalMemory() + " total memory | " + Runtime.getRuntime().freeMemory() + " free memory | " + Thread.activeCount() + " active Threads");
                    screenController.refreshTitle(updates + " ups, " + frames + " fps");
                    updates = 0;
                    frames = 0;
                }


            }
        };
        this.mainLoop.start();

    }

    public void setUpdatesPerSecond(int updatesPerSecond) {
        this.updateTimeNanoSeconds = 1000000000.0 / updatesPerSecond;
        this.updatesPerSecond = updatesPerSecond;
    }

    public void stop() {
        this.mainLoop.stop();
    }
}
