package udp.network.server;

import client.graphics.UpdateController;
import client.objects.engines.BlueEngine;
import client.objects.ships.BasicShip;
import client.objects.weapons.guns.RedGun;
import client.objects.weapons.holder.ThreeSlotGunHolder;
import udp.network.utilities.WritePropertiesFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.*;

import static udp.network.utilities.HashPassword.checkPassword;
import static udp.network.utilities.WritePropertiesFile.SERVER_USERS;

/**
 * Server.
 */
public class Server implements Runnable {

    public static HashMap<String, ServerClient> serverClients = new HashMap<>();
    public static List<ServerClient> serverClientsList = new ArrayList<>();
    public static final Object lockServerClients = new Object();

    public static List<DatagramPacket> shipDataPackets = new ArrayList<>();
    public static final Object shipDataLock = new Object();

    public static List<DatagramPacket> shipUpgradesPackets = new ArrayList<>();
    public static final Object shipUpgradesLock = new Object();


    public static Server server;

    // TODO hashmap player info

    private static DatagramSocket socket;
    private int port;
    private boolean jesusIsNotReturned = false;

    private boolean raw = false;

    private WritePropertiesFile writePropertiesFile = new WritePropertiesFile();

    /**
     * constructor.
     *
     * @param port server port.
     */
    public Server(int port) {
        this.port = port;
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
            return;
        }
        Thread run = new Thread(this, "Server");
        run.start();
    }

    /**
     * run server.
     */
    public void run() {

        jesusIsNotReturned = true;
        System.out.println("Server started on port " + port);

        // writing data to file  for signing up users

        // init client management thread
        //manageClients();

        // init receiveing thread
        receive();

        // server commands
        Scanner scanner = new Scanner(System.in);
        while (jesusIsNotReturned) {
            String text = scanner.nextLine();
            if (!text.startsWith("/")) {
                sendToAll("/m/Server: " + text + "/e/");
                continue;
            }
            text = text.substring(1);
            if (text.equals("raw")) {
                if (raw) System.out.println("Raw mode off.");
                else System.out.println("Raw mode on.");
                raw = !raw;
            } else if (text.equals("serverClientsList")) {
                System.out.println("Clients:");
                System.out.println("========");
                for (int i = 0; i < serverClientsList.size(); i++) {
                    ServerClient c = serverClientsList.get(i);
                    System.out.println(c.name + "(" + c.getID() + "): " + c.address.toString() + ":" + c.port);
                }
                System.out.println("========");
            } else if (text.startsWith("kick")) {
                String name = text.split(" ")[1];
                int id = -1;
                boolean number = true;
                // try kick by name, else kick by ID
                try {
                    id = Integer.parseInt(name);
                } catch (NumberFormatException e) {
                    number = false;
                }
                if (number) {
                    boolean exists = false;
                    for (ServerClient client : serverClientsList) {
                        if (client.getID() == id) {
                            exists = true;
                            break;
                        }
                    }
                    if (exists) disconnect(name);
                    else System.out.println("Client " + id + " doesn't exist! Check ID number.");
                } else {
                    for (int i = 0; i < serverClientsList.size(); i++) {
                        ServerClient c = serverClientsList.get(i);
                        if (name.equals(c.name)) {
                            disconnect(c.name);
                            break;
                        }
                    }
                }
            } else if (text.equals("help")) {
                printHelp();
            } else if (text.equals("q")) {
                quit();
            } else {
                System.out.println("Unknown command.");
                printHelp();
            }
        }
        scanner.close();
    }

    /**
     * print help commands to server console.
     */
    private void printHelp() {
        System.out.println("Here is a list of all available commands:");
        System.out.println("=========================================");
        System.out.println("/raw - enables raw mode.");
        System.out.println("/serverClientsList - shows all connected serverClientsList.");
        System.out.println("/kick [users ID or username] - kicks a user.");
        System.out.println("/help - shows this help message.");
        System.out.println("/q - shuts down the server.");

    }

    /**
     * send status of serverClientsList to all serverClientsList.
     */
    private void sendStatus() {
        String users = "/u/";
        for (int i = 0; i < serverClientsList.size(); i++) {
            users += serverClientsList.get(i).name + "/n/";
        }
        users += "/e/";
        sendToAll(users);
    }

    /**
     * thread receiving data.
     */
    private void receive() {
        Thread receive = new Thread("Receive") {
            public void run() {
                while (jesusIsNotReturned) {
                    byte[] data = new byte[100];
                    DatagramPacket packet = new DatagramPacket(data, data.length);
                    try {
                        socket.receive(packet);
                    } catch (SocketException ignored) {
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    process(packet);
                }
            }
        };
        receive.start();
    }

    /**
     * send a string message to all connected serverClientsList.
     *
     * @param message message
     */
    public static void sendToAll(String message) {
        // print to server terminal
        if (message.startsWith("/m/")) {
            String text = message.split("/e/")[0];
            System.out.println(text);
        }

        // send to all serverClientsList
        for (int i = 0; i < serverClientsList.size(); i++) {
            ServerClient client = serverClientsList.get(i);
            send(message.getBytes(), client.address, client.port);
        }
    }


    /**
     * send byte array of data
     *
     * @param data    byte array
     * @param address ip/host address
     * @param port    port number
     */
    public static void send(final byte[] data, final InetAddress address, final int port) {


        DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    /**
     * send a string message to a specific client
     *
     * @param message string message
     * @param address ip/host address
     * @param port    port number
     */
    private void send(String message, InetAddress address, int port) {
        message += "/e/";
        send(message.getBytes(), address, port);
    }

    /**
     * process an incoming bite array.
     *
     * @param packet packet
     */
    private void process(DatagramPacket packet) {
        // bite array to string
        String string = new String(packet.getData());
        if (raw) System.out.println(string.split("/e/")[0]);

        // ------------------------ connect --------------------------------------
        if (string.startsWith("/c/")) {
            int id = 555;
            // split name
            System.out.println("[" + id + "] connected!");

            String ID = "/c/" + id;
            send(ID, packet.getAddress(), packet.getPort());

            // ------------------------ chat message ---------------------------------
        } else if (string.startsWith("/m/")) {
            sendToAll(string);

            // ------------------------ ship data ------------------------------------
        } else if (string.startsWith("/s/")) {
            synchronized (shipDataLock) {
                shipDataPackets.add(packet);
            }

            // ------------------------ disconnect -----------------------------------
        } else if (string.startsWith("/u/")) {
            synchronized (shipUpgradesLock) {
                shipUpgradesPackets.add(packet);
            }

        } else if (string.startsWith("/d/")) {
            String name = string.split("/d/|/e/")[1];
            disconnect(name);


            // ------------------------ register --------------------------------------
        } else if (string.startsWith("/r/")) {
            string = string.substring(3);
            String user = string.split("/e/")[0].split("\\|")[0];
            String saltedPassword = string.split("/e/")[0].split("\\|")[1];
            System.out.println(user + " has registered.");
            writePropertiesFile.writeToFile(user, saltedPassword);

            // ------------------------ log in ------------------------------------
        } else if (string.startsWith("/l/")) {
            Properties users = new Properties();
            try {
                // home server's file location
                users.load(new FileInputStream(SERVER_USERS));

            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                string = string.split("/e/")[0];
                String username = string.substring(3).split(",")[0];
                String password = string.substring(3).split(",")[1];


                if (checkPassword(password, users.getProperty(username))) {

                    synchronized (lockServerClients) {
                        if (serverClients.get(username) == null) {
                            // random id
                            int id = UniqueIdentifier.getIdentifier();
                            // split name
                            System.out.println("[" + id + "] connected!");

                            send("/l/t", packet.getAddress(), packet.getPort());
                            System.out.println(username + " has logged in.");
                            UniqueIdentifier.setNameAndIdOfClient(id, username);

                            ServerClient client = new ServerClient(username, packet.getAddress(), packet.getPort(), id);

                            client.pilot.ship = new BasicShip(UpdateController.worldController.gameWorld, client.pilot);
                            client.pilot.ship.name = username;
                            client.pilot.experience = 100;

                            if (UpdateController.worldController.gameWorld.npcShipsByName.get(username) == null) {
                                UpdateController.worldController.gameWorld.npcShips.add(client.pilot.ship);
                                UpdateController.worldController.gameWorld.npcShipsByName.put(username, client.pilot.ship);
                            } else {
                                client.pilot.ship = UpdateController.worldController.gameWorld.npcShipsByName.get(username);
                                client.pilot.ship.owner = client.pilot;

                            }

                            serverClients.put(username, client);
                            serverClientsList.add(client);
                        }

                    }



                } else {
                    send("/l/f", packet.getAddress(), packet.getPort());
                    System.out.println("Failed log in attempt.");
                }
            } catch (Exception e) {
                send("/l/f", packet.getAddress(), packet.getPort());
                System.out.println("Failed log in attempt.");
            }


            // ------------------------ undefined packet ------------------------------
        } else {
            System.out.println("Server closed!");
        }
    }

    /**
     * close server.
     */
    private void quit() {
        for (int i = 0; i < serverClientsList.size(); i++) {
            disconnect(serverClientsList.get(i).name);
        }

        jesusIsNotReturned = false;
        socket.close();
        System.exit(0);
    }

    /**
     * client disconnects.
     *
     * @param name name.
     */
    public static void disconnect(String name) {
        ServerClient c;

        String message;

        if (serverClients.get(name) != null) {
            c = serverClients.get(name);
            serverClientsList.remove(c);
            serverClients.remove(name);
            message = "Client [" + c.getID() + "] @ " + c.address.toString() + ":" + c.port + " disconnected.";
        } else {
            message = name + " was not connected! line 431";
        }

        System.out.println(message);
    }
}
