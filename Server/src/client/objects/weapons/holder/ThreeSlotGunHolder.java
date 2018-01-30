package client.objects.weapons.holder;

import client.objects.ships.AbstractShip;
import client.objects.weapons.guns.AbstractGun;

/**
 * Created by.
 */
public class ThreeSlotGunHolder extends AbstractGunHolder{

    public AbstractGun gun;
    public AbstractGun gun2;
    public AbstractGun gun3;

    public ThreeSlotGunHolder(AbstractShip ship, AbstractGun gun, AbstractGun gun2, AbstractGun gun3) {
        super(ship);
        this.gun = gun;
        this.gun2 = gun2;
        this.gun3 = gun3;
    }

    @Override
    public void update() {
        this.gun.update();
        this.gun2.update();
        this.gun3.update();
    }

    @Override
    public void shoot() {
        this.gun.shoot();
        this.gun2.shoot();
        this.gun3.shoot();
    }
}
