package client.objects.engines;

import client.objects.effects.BlueEngineParticle;
import client.objects.ships.AbstractShip;
import javafx.scene.paint.Color;

public class BlueEngine extends AbstractEngine {


    public BlueEngine(AbstractShip ship, int xOffsetOnShip, int yOffsetOnShip, double widthOfEngine) {
        super(ship, xOffsetOnShip, yOffsetOnShip, widthOfEngine / 1.9);
        super.color = Color.color(0,0,1,1);
    }

    @Override
    public void roar() {
        for (int i = 0; i < 3; i++) {
            double currentDegree = super.ship.rotationRadians + super.startingRadiansFromShip;

            double dist = super.widthOfEngine - Math.sqrt(AbstractEngine.random.nextInt((int) (widthOfEngine * widthOfEngine)));
            if (AbstractEngine.random.nextBoolean()) {
                dist = -dist;
            }

            particles.add(new BlueEngineParticle(super.ship.world, ship.x + (Math.cos(currentDegree) * (super.distanceFromShipCenter - AbstractEngine.random.nextInt(10))) + (Math.cos(currentDegree + Math.PI / 2) * dist), ship.y + (Math.sin(currentDegree) * (super.distanceFromShipCenter - AbstractEngine.random.nextInt(10))) + (Math.sin(currentDegree + Math.PI / 2) * dist), Math.cos(currentDegree) * 4, Math.sin(currentDegree) * 4));

        }
    }
}