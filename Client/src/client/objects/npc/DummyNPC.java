package client.objects.npc;


import client.graphics.ScreenController;
import client.objects.ships.BasicShip;
import client.world.game.AbstractGameWorld;

import java.util.Random;

public class DummyNPC extends AbstractNPC {


//    private static Random random = new Random();
//
//    private static final int MIN_X_BOARDER = 1000;
//    private static final int MIN_Y_BOARDER = 1000;
//
//    private static final int MAX_X_BOARDER = 3000;
//    private static final int MAX_Y_BOARDER = 3000;

    public DummyNPC(AbstractGameWorld world) {
        super(world);
//        super.ship = new BasicShip(super.world, this);
//
//        ship.x = MIN_X_BOARDER + random.nextInt(Math.abs(MAX_X_BOARDER - MIN_X_BOARDER));
//        ship.y = MIN_Y_BOARDER + random.nextInt(Math.abs(MAX_Y_BOARDER - MIN_Y_BOARDER));
//
//        ship.setMove(MIN_X_BOARDER + random.nextInt(Math.abs(MAX_X_BOARDER - MIN_X_BOARDER)), MIN_Y_BOARDER + random.nextInt(Math.abs(MAX_Y_BOARDER - MIN_Y_BOARDER)));
    }

    @Override
    public void update() {
//        if (Math.sqrt((ship.x - ship.flyToTargetX) * (ship.x - ship.flyToTargetX) + (ship.y - ship.flyToTargetY) * (ship.y - ship.flyToTargetY)) < 40) {
//            ship.setMove(MIN_X_BOARDER + random.nextInt(Math.abs(MAX_X_BOARDER - MIN_X_BOARDER)), MIN_Y_BOARDER + random.nextInt(Math.abs(MAX_Y_BOARDER - MIN_Y_BOARDER)));
//        }
//
//        updateShip();
    }

    @Override
    public void render(ScreenController screenController) {
        ship.render(screenController);
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
        return pointerForGunsX;
    }

    @Override
    public double getPointerForGunsY() {
        return pointerForGunsY;
    }
}
