package client.world.game;


import client.graphics.UpdateController;
import client.objects.asteroid.Asteroid;
import client.objects.npc.AbstractNPC;
import client.objects.npc.DummyNPC;
import client.objects.player.ClientPlayer;
import client.objects.ships.AbstractShip;
import client.objects.ships.BasicShip;
import udp.network.server.Server;

import java.util.Locale;


public class GameWorld extends AbstractGameWorld {

    public static int sightLength = 3000;
    @Override
    public void update() {
        //update every ship, if removed then remove
        for (int i = 0; i < super.npcShips.size(); i++){
            super.npcShips.get(i).owner.update();
            for (int ii = 0; ii < Server.serverClientsList.size(); ii++) {
                AbstractShip ship = Server.serverClientsList.get(ii).pilot.ship;
                if ((Math.abs(ship.x - super.npcShips.get(i).x) < sightLength) && (Math.abs(ship.y - super.npcShips.get(i).y) < sightLength)){
                    if (super.npcShips.get(i) instanceof Asteroid) {
                        int asteroidNr = (super.npcShips.get(i).moving) ? 1 : 0;
                        Server.send(("/a/," + super.npcShips.get(i).name + "," + String.format(Locale.US, "%1$.3f", super.npcShips.get(i).x) + "," + String.format(Locale.US, "%1$.3f", super.npcShips.get(i).y) + "," +  String.format(Locale.US, "%1$.3f", super.npcShips.get(i).rotationRadians) + "," + asteroidNr + ",").getBytes(), Server.serverClientsList.get(ii).address, Server.serverClientsList.get(ii).port);
                    } else {
                        int moving = (super.npcShips.get(i).moving) ? 1 : 0;
                        int turbo = (super.npcShips.get(i).turbo) ? 1 : 0;
                        Server.send(("/s/," + super.npcShips.get(i).name + "," + String.format(Locale.US, "%1$.3f", super.npcShips.get(i).x)
                                + "," + String.format(Locale.US, "%1$.3f", super.npcShips.get(i).y)
                                + "," +  String.format(Locale.US, "%1$.3f", super.npcShips.get(i).rotationRadians)
                                + "," + moving + "," + super.npcShips.get(i).engineLevel +  "," + super.npcShips.get(i).gunLevel
                                + "," + super.npcShips.get(i).gunCount + "," + super.npcShips.get(i).constitution + "," + turbo
                                + "," + String.format(Locale.US, "%1$.3f", super.npcShips.get(i).curHP) + "," + (int)super.npcShips.get(i).owner.getPointerForGunsX() + "," + (int)super.npcShips.get(i).owner.getPointerForGunsY() + ",").getBytes(), Server.serverClientsList.get(ii).address, Server.serverClientsList.get(ii).port);
                    }
                }
            }
            if (super.npcShips.get(i).curHP <= 0) {
                if (npcShips.get(i).owner instanceof ClientPlayer) {
                    tiles[super.npcShips.get(i).currentTile].ships.remove(super.npcShips.get(i));
                    ClientPlayer owner = (ClientPlayer)super.npcShips.get(i).owner;
                    BasicShip ship = new BasicShip(this, super.npcShips.get(i).owner);
                    owner.ship = ship;
                    ship.name = super.npcShips.get(i).name;
                    if (Server.serverClients.get(ship.name) == null) {
                        super.npcShips.remove(super.npcShips.get(i));
                        super.npcShipsByName.remove(ship.name);
                        i--;
                        continue;
                    }
                    Server.serverClients.get(ship.name).pilot = owner;
                    super.npcShips.remove(super.npcShips.get(i));
                    super.npcShips.add(ship);
                    super.npcShipsByName.remove(ship.name);
                    super.npcShipsByName.put(ship.name, ship);
                    i--;
                    continue;
                }
                tiles[super.npcShips.get(i).currentTile].ships.remove(super.npcShips.get(i));
                npcShips.remove(super.npcShips.get(i));
                i--;
            }
        }

        //update every projectile, if removed then remove
        for (int i = 0; i < super.projectiles.size(); i++){
            super.projectiles.get(i).update();
            //Server.sendToAll("/p/," + super.projectiles.get(i).x + "," + super.projectiles.get(i).y + "," + super.projectiles.get(i));
            if (super.projectiles.get(i).removed) {
                super.projectiles.remove(i);
                i--;
            }
        }

    }


    @Override
    public void load() {

        createMap();

        for (int i = 0; i < 100; i++) {
            npcShips.add(new DummyNPC(this).ship);
            npcShips.add(new Asteroid(this));
        }
    }
}
