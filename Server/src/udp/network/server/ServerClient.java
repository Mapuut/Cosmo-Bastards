package udp.network.server;

import client.graphics.UpdateController;
import client.objects.player.ClientPlayer;
import client.objects.ships.BasicShip;

import java.net.InetAddress;

/**
 * server client.
 */
public class ServerClient {

    public String name;
    public InetAddress address;
    public int port;
    public final int ID;
    public long lastUpdate = UpdateController.time;

    private int cooldown = 500;
    private int curCooldown = 500;

    public ClientPlayer pilot = new ClientPlayer(UpdateController.worldController.gameWorld);


    public void update() {
        if (pilot.ship.removed) {
            curCooldown--;
            if (curCooldown <= 0) {
                pilot.ship = new BasicShip(UpdateController.worldController.gameWorld, pilot);
                curCooldown = cooldown;
            }
        }

    }

    /**
     * constructor.
     *
     * @param name    name of client
     * @param address ip/host address
     * @param port    port
     * @param ID      ID
     */
    ServerClient(String name, InetAddress address, int port, final int ID) {
        this.name = name;
        this.address = address;
        this.port = port;
        this.ID = ID;
    }

    /**
     * constructor.
     *
     * @param address ip/host address
     * @param port    port
     * @param ID      ID
     */
    ServerClient(InetAddress address, int port, final int ID) {
        this.address = address;
        this.port = port;
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * get id.
     *
     * @return id
     */
    int getID() {
        return ID;
    }

}
