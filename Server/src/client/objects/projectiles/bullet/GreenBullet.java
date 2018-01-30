package client.objects.projectiles.bullet;

import client.objects.projectiles.AbstractProjectile;
import client.objects.ships.AbstractShip;
import client.world.game.AbstractGameWorld;

/**
 * Created by.
 */
public class GreenBullet extends AbstractProjectile{

    public static final double speed = 20;
    public boolean used = false;

    public GreenBullet(AbstractGameWorld world, AbstractShip ownerShip, double x, double y, double angle) {
        super(world, ownerShip, x, y);
        super.speedX = Math.cos(angle) * speed;
        super.speedY = Math.sin(angle) * speed;
        super.damage = 0.5;
        super.lifetime = 50;
    }

    @Override
    public void update() {
        if (!used) {
            this.x += speedX;
            this.y += speedY;
            updateCurrentTile();
            checkIfCollision();
        }

        if (lifetime-- <= 0) {
            opticity -= 0.02;
            if (opticity <= 0) {
                opticity = 0;
                this.remove();
                world.tiles[currentTile].projectiles.remove(this);
            }
        }
    }

    @Override
    public void checkIfCollision(){
        int xx = ((int)super.x >> world.TileSize) - 1;
        int yy = ((int)super.y >> world.TileSize) - 1;
        if (xx < 0) xx = 0;
        if (yy < 0) yy = 0;

        if (xx > world.sizeOfMap - 3) xx = world.sizeOfMap - 3;
        if (yy > world.sizeOfMap - 3) yy = world.sizeOfMap - 3;

        for (int y = yy; y < yy + 3; y++) {
            for (int x = xx; x < xx + 3; x++) {
                if (world.tiles[y * world.sizeOfMap + x].ships.size() != 0) {
                    int iCounter = world.tiles[y * world.sizeOfMap + x].ships.size();
                    for (int i = 0; i < iCounter; i++) {
                        AbstractShip curShip = world.tiles[y * world.sizeOfMap + x].ships.get(i);
                        if (Math.sqrt((curShip.x - super.x) * (curShip.x - super.x) + (curShip.y - super.y) * (curShip.y - super.y)) < curShip.radiusOfShip) {
                            if (curShip.equals(ownerShip)) continue;
                            if (curShip.checkIfCollision(this.x, this.y)) {
                                curShip.doDamage(this, this.damage * opticity);
                                if (!curShip.removed && (curShip.curHP <= 0)) {
                                    curShip.removed = true;
                                    ownerShip.owner.experience += curShip.experienceForKill;
                                    ownerShip.owner.checkIfLevelUp();
                                }
                                used = true;
                                lifetime = 0;
                                return;

                            }
                        }
                    }
                }
            }
        }
    }
}
