package client;

import client.graphics.ScreenController;
import client.graphics.UpdateController;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import udp.network.client.Client;
import udp.network.javafx.JavafxLogin;


public class GameLauncher {

    private ScreenController screenController;
    public UpdateController updateController;
    public static Client client;
    public static String username;
//    public static void main(String[] args) {
//        launch(args);
//    }


    // start
    public void start(Stage primaryStage) throws Exception {
        primaryStage.getIcons().add(new Image("ships/Ship1.png"));

        if (JavafxLogin.fullscreen) {
            this.screenController = new ScreenController((int) Screen.getPrimary().getBounds().getWidth(), (int) Screen.getPrimary().getBounds().getHeight(), "Game", true, false, primaryStage);
        } else {
            this.screenController = new ScreenController((int) Screen.getPrimary().getBounds().getWidth() >> 1, (int) Screen.getPrimary().getBounds().getHeight() >> 1, "Game", false, false, primaryStage);
        }
        this.updateController = new UpdateController(screenController);
    }

    public ScreenController getScreenController() {
        return screenController;
    }


    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
