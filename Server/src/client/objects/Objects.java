package client.objects;


import client.world.game.AbstractGameWorld;

public abstract class Objects {

    public double x = 0;
    public double y = 0;

    public AbstractGameWorld world;

    public boolean removed = false;

    public Objects(AbstractGameWorld world) {
        this.world = world;
    }

    public abstract void update();

    public void remove() {
        removed = true;
    }
}
