package client.objects.player;

import client.objects.npc.AbstractNPC;
import client.objects.ships.BasicShip;
import client.world.game.AbstractGameWorld;

import java.util.Random;


public class ClientPlayer extends AbstractNPC {

    public static Random random = new Random();

    public boolean right = false;
    public boolean left = false;
    public boolean up = false;

    public int mouseX = 0;
    public int mouseY = 0;

    public boolean flyToTarget = false;
    public boolean shooting = false;

    public boolean turbo = false;

    public ClientPlayer(AbstractGameWorld world) {
        super(world);
        super.ship = new BasicShip(super.world, this);

        ship.x = random.nextInt(world.sizeOfMap << world.TileSize);
        ship.y = random.nextInt(world.sizeOfMap << world.TileSize);
        ship.mass = 10;
    }

    @Override
    public void updateShip() {
        ship.update();
    }

    @Override
    public boolean pointerActive() {
        return false;
    }

    @Override
    public double getPointerForGunsX() {
        return mouseX;

    }

    @Override
    public double getPointerForGunsY() {
        return mouseY;
    }

    @Override
    public void update() {

        if (right || left || up) {
            ship.flyToTarget = false;
            this.flyToTarget = false;

            ship.manualControl = true;

            ship.manualControlRight = right;
            ship.manualControlLeft = left;
            ship.manualControlForward = up;

        } else if (flyToTarget) {
            ship.flyToTarget = true;
            ship.manualControl = false;
            ship.setMove(mouseX, mouseY);
        } else {
            ship.manualControl = false;
        }
        ship.turbo = this.turbo;

        updateShip();

        if (shooting) {
            ship.shoot();
        }

    }
}
