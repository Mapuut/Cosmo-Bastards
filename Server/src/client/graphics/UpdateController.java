package client.graphics;


import client.objects.engines.BlueEngine;
import client.objects.engines.RedEngine;
import client.objects.weapons.guns.BlueGun;
import client.objects.weapons.guns.GreenGun;
import client.objects.weapons.guns.RedGun;
import client.objects.weapons.holder.OneSlotGunHolder;
import client.objects.weapons.holder.ThreeSlotGunHolder;
import client.objects.weapons.holder.TwoSlotGunHolder;
import client.world.WorldController;
import udp.network.server.Server;
import udp.network.server.ServerClient;

/**
 * All changes and actions should be started here.
 * Only one instance of this class should be created.
 */
public class UpdateController implements Runnable{

    public static long time = 0;

    public static WorldController worldController;
    public boolean jesusNotReturned = true;

    public void run() {
        worldController = new WorldController(this);
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        final double ns = 1000000000.0 / 60.0;
        double delta = 0;
        int updates = 0;
        while (jesusNotReturned) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                time++;
                synchronized (Server.lockServerClients) {
                    synchronized (Server.shipDataLock) {
                        for (int i = 0; i < Server.shipDataPackets.size(); i++) {
                            //System.out.println("package");
                            String data[] = new String(Server.shipDataPackets.get(i).getData()).split(",");
                            ServerClient player = Server.serverClients.get(data[1]);
                            if (player == null) continue;
                            player.pilot.right = Boolean.parseBoolean(data[2]);
                            player.pilot.left = Boolean.parseBoolean(data[3]);
                            player.pilot.up = Boolean.parseBoolean(data[4]);

                            player.pilot.shooting = Boolean.parseBoolean(data[5]);
                            player.pilot.flyToTarget = Boolean.parseBoolean(data[6]);
                            player.pilot.turbo = Boolean.parseBoolean(data[7]);

                            player.pilot.mouseX = Integer.parseInt(data[8]);
                            player.pilot.mouseY = Integer.parseInt(data[9]);

                            player.lastUpdate = time;
                        }
                        Server.shipDataPackets.clear();
                    }




                    synchronized (Server.shipUpgradesLock) {
                        for (int i = 0; i < Server.shipUpgradesPackets.size(); i++) {
                            String data[] = new String(Server.shipUpgradesPackets.get(i).getData()).split(",");
                            ServerClient player = Server.serverClients.get(data[1]);
                            if (player == null) continue;

                            if (data[2].equals("1")) {
                                if (player.pilot.ship.engineLevel < 2) {
                                    if (player.pilot.experience > 10 + player.pilot.ship.engineLevel * 10) {
                                        player.pilot.ship.engineLevel++;
                                        if (player.pilot.ship.engineLevel == 1) {
                                            player.pilot.ship.engine = new BlueEngine();
                                        }else if (player.pilot.ship.engineLevel == 2) {
                                            player.pilot.ship.engine = new RedEngine();
                                        }
                                    }
                                }
                            } else if (data[2].equals("2")) {
                                if (player.pilot.ship.gunLevel < 2) {
                                    if (player.pilot.experience > 10 + player.pilot.ship.gunLevel * 10) {
                                        player.pilot.ship.gunLevel++;
                                        if (player.pilot.ship.gunLevel == 1) {
                                            if (player.pilot.ship.gunCount == 0) {
                                                player.pilot.ship.gunHolder = new OneSlotGunHolder(player.pilot.ship, new BlueGun(player.pilot.ship, 0, -9));
                                            } else if (player.pilot.ship.gunCount == 1) {
                                                player.pilot.ship.gunHolder = new TwoSlotGunHolder(player.pilot.ship, new BlueGun(player.pilot.ship, 0, -9), new BlueGun(player.pilot.ship, 0, 21));
                                            } else {
                                                player.pilot.ship.gunHolder = new ThreeSlotGunHolder(player.pilot.ship, new BlueGun(player.pilot.ship, 0, -9), new BlueGun(player.pilot.ship, 29, 16), new BlueGun(player.pilot.ship, -29, 16));
                                            }

                                        }else if (player.pilot.ship.gunLevel == 2) {
                                            if (player.pilot.ship.gunCount == 0) {
                                                player.pilot.ship.gunHolder = new OneSlotGunHolder(player.pilot.ship, new RedGun(player.pilot.ship, 0, -9));
                                            } else if (player.pilot.ship.gunCount == 1) {
                                                player.pilot.ship.gunHolder = new TwoSlotGunHolder(player.pilot.ship, new RedGun(player.pilot.ship, 0, -9), new RedGun(player.pilot.ship, 0, 21));
                                            } else {
                                                player.pilot.ship.gunHolder = new ThreeSlotGunHolder(player.pilot.ship, new RedGun(player.pilot.ship, 0, -9), new RedGun(player.pilot.ship, 29, 16), new RedGun(player.pilot.ship, -29, 16));
                                            }
                                        }
                                    }
                                }
                            } else if (data[2].equals("3")) {
                                if (player.pilot.ship.gunCount < 2) {
                                    if (player.pilot.experience > 10 + player.pilot.ship.gunCount * 10) {
                                        player.pilot.ship.gunCount++;
                                        if (player.pilot.ship.gunLevel == 0) {
                                            if (player.pilot.ship.gunCount == 0) {
                                                player.pilot.ship.gunHolder = new OneSlotGunHolder(player.pilot.ship, new GreenGun(player.pilot.ship, 0, -9));
                                            } else if (player.pilot.ship.gunCount == 1) {
                                                player.pilot.ship.gunHolder = new TwoSlotGunHolder(player.pilot.ship, new GreenGun(player.pilot.ship, 0, -9), new GreenGun(player.pilot.ship, 0, 21));
                                            } else {
                                                player.pilot.ship.gunHolder = new ThreeSlotGunHolder(player.pilot.ship, new GreenGun(player.pilot.ship, 0, -9), new GreenGun(player.pilot.ship, 29, 16), new GreenGun(player.pilot.ship, -29, 16));
                                            }
                                        }else if (player.pilot.ship.gunLevel == 1) {
                                            if (player.pilot.ship.gunCount == 0) {
                                                player.pilot.ship.gunHolder = new OneSlotGunHolder(player.pilot.ship, new BlueGun(player.pilot.ship, 0, -9));
                                            } else if (player.pilot.ship.gunCount == 1) {
                                                player.pilot.ship.gunHolder = new TwoSlotGunHolder(player.pilot.ship, new BlueGun(player.pilot.ship, 0, -9), new BlueGun(player.pilot.ship, 0, 21));
                                            } else {
                                                player.pilot.ship.gunHolder = new ThreeSlotGunHolder(player.pilot.ship, new BlueGun(player.pilot.ship, 0, -9), new BlueGun(player.pilot.ship, 29, 16), new BlueGun(player.pilot.ship, -29, 16));
                                            }

                                        }else if (player.pilot.ship.gunLevel == 2) {
                                            if (player.pilot.ship.gunCount == 0) {
                                                player.pilot.ship.gunHolder = new OneSlotGunHolder(player.pilot.ship, new RedGun(player.pilot.ship, 0, -9));
                                            } else if (player.pilot.ship.gunCount == 1) {
                                                player.pilot.ship.gunHolder = new TwoSlotGunHolder(player.pilot.ship, new RedGun(player.pilot.ship, 0, -9), new RedGun(player.pilot.ship, 0, 21));
                                            } else {
                                                player.pilot.ship.gunHolder = new ThreeSlotGunHolder(player.pilot.ship, new RedGun(player.pilot.ship, 0, -9), new RedGun(player.pilot.ship, 29, 16), new RedGun(player.pilot.ship, -29, 16));
                                            }
                                        }
                                    }
                                }
                            } else if (data[2].equals("4")) {
                                if (player.pilot.ship.constitution < 2) {
                                    if (player.pilot.experience > 10 + player.pilot.ship.constitution * 10) {
                                        player.pilot.ship.constitution++;
                                        player.pilot.ship.maxHP = 10 + player.pilot.ship.constitution * 5;
                                        player.pilot.ship.curHP = player.pilot.ship.maxHP;
                                        player.pilot.ship.mass = 10 + player.pilot.ship.constitution * 10;
                                    }
                                }
                            }


                            player.lastUpdate = time;
                        }
                        Server.shipUpgradesPackets.clear();
                    }
                }
                worldController.update();
                updates++;
                delta--;
            }

            if (System.currentTimeMillis() - timer > 1000) {
                synchronized (Server.lockServerClients) {
                    synchronized (Server.shipDataLock) {
                        for (int i = 0; i < Server.serverClientsList.size(); i++) {
                            if (time - Server.serverClientsList.get(i).lastUpdate > 1500) {
                                Server.disconnect(Server.serverClientsList.get(i).name);
                            }
                        }
                    }
                }
                System.out.println("update --- " + updates + " | Amount of players --- " + Server.serverClientsList.size());
                timer += 1000;
                updates = 0;
            }

        }
    }
}
