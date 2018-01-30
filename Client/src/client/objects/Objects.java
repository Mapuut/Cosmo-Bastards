package client.objects;


import client.graphics.ScreenController;
import client.world.game.AbstractGameWorld;

public abstract class Objects {

    public double x = 0;
    public double y = 0;

    public int lastUpdateTime = 0;
    public boolean updated = false;

    public AbstractGameWorld world;

    public boolean removed = false;

    public Objects(AbstractGameWorld world) {
        this.world = world;
    }

    public abstract void update();

    public abstract void render(ScreenController screenController);


    public void remove() {
        removed = true;
    }
}
