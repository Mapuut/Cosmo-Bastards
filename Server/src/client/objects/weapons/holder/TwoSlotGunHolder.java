package client.objects.weapons.holder;

import client.objects.ships.AbstractShip;
import client.objects.weapons.guns.AbstractGun;

/**
 * Created by.
 */
public class TwoSlotGunHolder extends AbstractGunHolder{

    public AbstractGun gun;
    public AbstractGun gun2;

    public TwoSlotGunHolder(AbstractShip ship, AbstractGun gun, AbstractGun gun2) {
        super(ship);
        this.gun = gun;
        this.gun2 = gun2;
    }

    @Override
    public void update() {
        this.gun.update();
        this.gun2.update();
    }

    @Override
    public void shoot() {
        this.gun.shoot();
        this.gun2.shoot();
    }
}
