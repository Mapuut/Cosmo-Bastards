package client.objects.engines;


import client.graphics.ScreenController;
import client.objects.effects.AbstractParticle;
import client.objects.ships.AbstractShip;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public abstract class AbstractEngine {

    public List<AbstractParticle> particles = new ArrayList<>();
    protected static Random random = new Random();

    public AbstractShip ship;
    public double distanceFromShipCenter = 0;
    public double startingRadiansFromShip = 0;
    public double widthOfEngine = 0;
    public Color color;

    public AbstractEngine(AbstractShip ship, int xOffsetOnShip, int yOffsetOnShip, double widthOfEngine) {
        this.ship = ship;
        this.distanceFromShipCenter = (int) Math.sqrt(xOffsetOnShip * xOffsetOnShip + yOffsetOnShip * yOffsetOnShip);
        this.startingRadiansFromShip = Math.atan2((double) xOffsetOnShip, (double) yOffsetOnShip);
        this.widthOfEngine = widthOfEngine;
    }

    public abstract void roar();

    public void update() {
        for (int i = 0; i < particles.size(); i++) {
            particles.get(i).update();
            if (particles.get(i).removed) {
                particles.remove(particles.get(i));
                i--;
            }
        }
    }

    public void render(ScreenController screenController) {
        for (int i = 0; i < particles.size(); i++) {
            particles.get(i).render(screenController);
        }
    }


}
