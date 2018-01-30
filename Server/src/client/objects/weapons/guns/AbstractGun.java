package client.objects.weapons.guns;


import client.objects.ships.AbstractShip;

import java.util.Random;

public abstract class AbstractGun {

    public double rotationSpeed = 1;

    public AbstractShip ship;
    public double rotationRadians = 0;
    public double distanceFromShipCenter = 0;
    public double startingRadiansFromShip = 0;

    public static final Random random = new Random();

    public AbstractGun(AbstractShip ship, int xOffsetOnShip, int yOffsetOnShip){
        this.ship = ship;
        this.distanceFromShipCenter = (int)Math.sqrt(xOffsetOnShip * xOffsetOnShip + yOffsetOnShip * yOffsetOnShip);
        this.startingRadiansFromShip = Math.atan2((double)xOffsetOnShip, (double)yOffsetOnShip);
    }

    public abstract void update();
    public abstract void shoot();

}
