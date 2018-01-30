package client.objects.asteroid;

import client.graphics.ScreenController;
import client.objects.npc.AbstractNPC;
import client.world.game.AbstractGameWorld;

/**
 * Created.
 */
public class AsteroidOwner extends AbstractNPC{
    public AsteroidOwner(AbstractGameWorld world) {
        super(world);
    }

    @Override
    public void updateShip() {

    }

    @Override
    public boolean pointerActive() {
        return false;
    }

    @Override
    public double getPointerForGunsX() {
        return 0;
    }

    @Override
    public double getPointerForGunsY() {
        return 0;
    }

    @Override
    public void update() {
        ship.update();
    }

    @Override
    public void render(ScreenController screenController) {
        ship.render(screenController);
    }
}
