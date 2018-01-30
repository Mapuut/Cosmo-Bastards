package client.objects.projectiles;

import client.objects.Objects;
import client.objects.ships.AbstractShip;
import client.world.game.AbstractGameWorld;

/**
 * Created by.
 */
public abstract class AbstractProjectile extends Objects{

    public AbstractShip ownerShip;
    public int lifetime = 50;
    public double opticity = 1;
    public double damage;
    public double speedX;
    public double speedY;
    public int currentTile = 0;

    public AbstractProjectile(AbstractGameWorld world, AbstractShip owner, double x, double y){
        super(world);
        this.ownerShip = owner;
        super.x = x;
        super.y = y;
        world.tiles[currentTile].projectiles.add(this);
    }

    @Override
    public void update() {
        this.x += speedX;
        this.y += speedY;
        updateCurrentTile();
        checkIfCollision();
        if (lifetime-- <= 0) {
            opticity -= 0.02;
            if (opticity <= 0) {
                opticity = 0;
                this.remove();
                world.tiles[currentTile].projectiles.remove(this);
            }
        }
    }

    protected void updateCurrentTile() {
        int xx = (int) super.x;
        int yy = (int) super.y;
        if (super.x < 0) xx = 0;
        if (super.y < 0) yy = 0;

        if (super.x >= world.sizeOfMap << world.TileSize) xx = (world.sizeOfMap << world.TileSize) - 1;
        if (super.y >= world.sizeOfMap << world.TileSize) yy = (world.sizeOfMap << world.TileSize) - 1;

        int tile = (yy >> world.TileSize) * world.sizeOfMap + (xx >> world.TileSize);
        if (tile != currentTile) {
            world.tiles[currentTile].projectiles.remove(this);
            currentTile = tile;
            world.tiles[currentTile].projectiles.add(this);
        }

    }

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
                                curShip.curHP -= this.damage;
                                if (!curShip.removed && (curShip.curHP <= 0)) {
                                    curShip.removed = true;
                                    ownerShip.owner.experience += curShip.experienceForKill;
                                    ownerShip.owner.checkIfLevelUp();
                                }
                                this.remove();
                                world.tiles[currentTile].projectiles.remove(this);
                                return;

                            }
                        }
                    }
                }
            }
        }
    }
}
