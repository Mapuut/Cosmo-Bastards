package client.objects.npc;

import client.objects.ships.AbstractShip;
import client.world.game.AbstractGameWorld;


public abstract class AbstractNPC {

    public AbstractGameWorld world;
    public AbstractShip ship;

    public double pointerForGunsX = 0;
    public double pointerForGunsY = 0;

    public double experience = 0;
    public int level = 0;

    public boolean shooting = false;

    public AbstractNPC(AbstractGameWorld world){
        this.world = world;
    }

    public abstract void updateShip();

    public void checkIfLevelUp(){
        if (experience >= Math.pow(level, 2) * 10 + 10) {
            experience -= Math.pow(level, 2) * 10 + 10;
            level++;
        }
    }

    /**
     * Tells if player/npc currently want to sight or aim with his pointer.
     * @return True if so False otherwise.
     */
    public abstract boolean pointerActive();


    public abstract double getPointerForGunsX();
    public abstract double getPointerForGunsY();

    public abstract void update();

    public void checkIfDeath(AbstractShip ship) {
        ship.remove();
        ship.world.tiles[ship.currentTile].ships.remove(ship);
    }

}
