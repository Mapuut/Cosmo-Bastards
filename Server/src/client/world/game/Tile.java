package client.world.game;

import client.objects.projectiles.AbstractProjectile;
import client.objects.ships.AbstractShip;

import java.util.ArrayList;
import java.util.List;

public class Tile {

    public List<AbstractShip> ships = new ArrayList<>();
    public List<AbstractProjectile> projectiles = new ArrayList<>();

}
