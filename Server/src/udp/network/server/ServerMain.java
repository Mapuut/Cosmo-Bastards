package udp.network.server;

import client.ServerLauncher;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;


/**
 * ServerMain.
 */
public class ServerMain {

    private int port;

    public ServerMain(int port) {
        this.port = port;
        Server.server = new Server(port);
    }

    /**
     * Launch the server.
     * CMD example: java -jar -server iti0011.jar 1200
     * NB! Appoint a userdata location.
     * @param args args
     */
    public static void main(String[] args) {
        int port;


        if (args.length == 0) {
            port = 1222;
        } else {
            port = Integer.parseInt(args[0]);
        }
        new ServerMain(port);

        new ServerLauncher().start();
        systemTrayMenu();
    }



    public static void systemTrayMenu() {
        //Check the SystemTray is supported
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }
        final PopupMenu popup = new PopupMenu();


        Image image = null;

        try {
            image = ImageIO.read(ServerMain.class.getResourceAsStream("/icons/icon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        final TrayIcon trayIcon = new TrayIcon(image, "Cosmo Bastards Server");
        final SystemTray tray = SystemTray.getSystemTray();

        // Create a pop-up menu components
        MenuItem aboutItem = new MenuItem("About");
        MenuItem exitItem = new MenuItem("Stop Server");

        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        //Add components to pop-up menu
        popup.add(aboutItem);
        popup.addSeparator();
        popup.add(exitItem);

        trayIcon.setPopupMenu(popup);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }
    }
}
