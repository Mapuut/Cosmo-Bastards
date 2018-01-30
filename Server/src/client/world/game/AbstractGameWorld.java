package client.world.game;

import client.objects.projectiles.AbstractProjectile;
import client.objects.ships.AbstractShip;
import client.world.AbstractWorld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by.
 */
public abstract class AbstractGameWorld extends AbstractWorld {

    public List<AbstractShip> npcShips = new ArrayList<>();
    public HashMap<String, AbstractShip> npcShipsByName = new HashMap<>();
    public List<AbstractProjectile> projectiles = new ArrayList<>();
//    public List<> players = new ArrayList<>();

    public Tile[] tiles = new Tile[10000];
    public final int sizeOfMap = 100;
    public final int TileSize = 7;

    protected void createMap() {
        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = new Tile();
        }
    }

}
