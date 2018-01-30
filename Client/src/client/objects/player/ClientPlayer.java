package client.objects.player;

import client.graphics.ScreenController;
import client.input.Keyboard;
import client.input.Mouse;
import client.objects.npc.AbstractNPC;
import client.objects.ships.BasicShip;
import client.world.game.AbstractGameWorld;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import udp.network.client.Client;


public class ClientPlayer extends AbstractNPC {

    public double cameraPositionX = 0;
    public double cameraPositionY = 0;
    private Client client;

    public ClientPlayer(AbstractGameWorld world) {
        super(world);
        super.ship = new BasicShip(super.world, this);

        ship.x = 0;
        ship.y = 0;
//        ship.mass = 10;
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
        return world.offsetX + Mouse.getMouseX();
    }

    @Override
    public double getPointerForGunsY() {
        return world.offsetY + Mouse.getMouseY();
    }

    @Override
    public void update() {

        ship.manualControlRight = (Keyboard.keyPressed(KeyCode.D.ordinal()));
        ship.manualControlLeft = (Keyboard.keyPressed(KeyCode.A.ordinal()));
        ship.manualControlForward = (Keyboard.keyPressed(KeyCode.W.ordinal()));

//        if (Keyboard.keyPressed(KeyCode.W.ordinal()) || Keyboard.keyPressed(KeyCode.A.ordinal()) || Keyboard.keyPressed(KeyCode.S.ordinal()) || Keyboard.keyPressed(KeyCode.D.ordinal())) {
//            ship.flyToTarget = false;
//            ship.manualControl = true;
//
//
//        } else if (Mouse.rightButton) {
//            ship.flyToTarget = true;
//            ship.manualControl = false;
//            ship.setMove(world.offsetX + Mouse.getMouseX(), world.offsetY + Mouse.getMouseY());
//        } else {
//            ship.manualControl = false;
//        }


        //updateShip();
        if (Mouse.leftButton) {
            shooting = true;
        } else {
            shooting = false;
        }
        cameraPositionX = ship.x;
        cameraPositionY = ship.y;
    }

    @Override
    public void render(ScreenController screenController) {
        ship.render(screenController);
//        screenController.gc.setFill(Color.PURPLE);
//        screenController.gc.fillRect(Mouse.getMouseX() - 1, Mouse.getMouseY() - 1, 3, 3);
//        screenController.gc.setStroke(Color.PURPLE);
//        screenController.gc.strokeLine(ship.x, ship.y, ship.x + Math.cos(ship.rotationRadians) * 5, ship.y + Math.sin(ship.rotationRadians) * 5);
//        screenController.gc.setStroke(Color.PURPLE);
//        screenController.gc.strokeLine(ship.x - world.offsetX, ship.y - world.offsetY, ship.flyToTargetX - world.offsetX, ship.flyToTargetY - world.offsetY);
    }
}
