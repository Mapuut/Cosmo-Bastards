package client.objects.weapons.holder;

import client.objects.ships.AbstractShip;
import client.objects.weapons.guns.AbstractGun;

/**
 * Created by.
 */
public class OneSlotGunHolder extends AbstractGunHolder{

    public AbstractGun gun;

    public OneSlotGunHolder(AbstractShip ship, AbstractGun gun) {
        super(ship);
        this.gun = gun;
    }

    @Override
    public void update() {
        this.gun.update();
    }

    @Override
    public void shoot() {
        this.gun.shoot();
    }
}
