package client.objects.weapons.holder;

import client.objects.ships.AbstractShip;

import java.util.Random;

/**
 * Created by.
 */
public abstract class AbstractGunHolder {


    public AbstractShip ship;

    public static final Random random = new Random();

    public AbstractGunHolder(AbstractShip ship) {
        this.ship = ship;
    }



    public abstract void update();
    public abstract void shoot();
}
