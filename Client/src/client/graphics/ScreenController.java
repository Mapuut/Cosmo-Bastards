package client.graphics;


import client.GameLauncher;
import client.input.InputController;
import client.input.Keyboard;
import client.world.WorldController;
import client.world.game.GameWorld;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.FillRule;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import udp.network.controllers.ConnectServerController;
import udp.network.javafx.JavafxLogin;

import java.util.Locale;

/**
 * All information about GameLauncher window are stored here.
 * Only one instance of this class should be created.
 */
public class ScreenController {

    /**
     * width of window.
     */
    public int width = 600;

    /**
     * height of window.
     */
    public int height = 400;


    /**
     * title of window.
     */
    private String title = "Cosmo Bastards";

    /**
     * fullscreen option of window.
     */
    private boolean fullScreen = false;

    /**
     * always on top option of window.
     */
    private boolean alwaysOnTop = false;

    /**
     * always on top option of window.
     */
    private Stage primaryStage;

    boolean partyMode = false;
    public static boolean debug = false;

    private ConnectServerController connectServerController;


    /**
     * Lazy Constructor.
     */
    public ScreenController(Stage primaryStage) {
        this.primaryStage = primaryStage;
        build(primaryStage);
    }

    /**
     * Full constructor.
     *
     * @param width       width of window.
     * @param height      height of window.
     * @param title       title of window.
     * @param fullScreen  fullscreen option of window.
     * @param alwaysOnTop always on top option of window.
     */
    public ScreenController(int width, int height, String title, boolean fullScreen, boolean alwaysOnTop, Stage primaryStage) {
        this.width = width;
        this.height = height;
        this.title = title;
        this.fullScreen = fullScreen;
        this.alwaysOnTop = alwaysOnTop;
        this.primaryStage = primaryStage;
        build(primaryStage);
    }


    /**
     * Graphic context of the window.
     */
    public GraphicsContext gc;


    private Group group;
    public Canvas canvas;

    /**
     * Creates window.
     *
     * @param primaryStage primary stages given by javaFX.
     */
    private void build(Stage primaryStage) {

        Group root = new Group();
        Scene scene = new Scene(root, width, height);

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX((screenBounds.getWidth() - width) / 2);
        primaryStage.setY((screenBounds.getHeight() - height) / 2);

        InputController.set(root, scene);

        primaryStage.setScene(scene);


        Canvas canvas = new Canvas(width, height);
        this.canvas = canvas;
        root.getChildren().add(canvas);
        root.getChildren().add(debugCanvas);
        root.getChildren().add(textArea);
        root.getChildren().add(writeField);
        root.getChildren().add(speedButton);
        root.getChildren().add(damageButton);
        root.getChildren().add(gunCountButton);
        root.getChildren().add(healthButton);

        // buttons
        speedButton.setMaxHeight(50);
        speedButton.setMinWidth(50);
        speedButton.setLayoutX(canvas.getWidth() - 85);
        speedButton.setLayoutY(10);
        speedButton.setOnAction(event -> GameLauncher.client.send(("/u/," + GameLauncher.client.getName() + ",1,").getBytes()));
        speedButton.getStylesheets().add("client/graphics/chatstyle.css");

        damageButton.setMaxHeight(50);
        damageButton.setMinWidth(50);
        damageButton.setLayoutX(canvas.getWidth() - 161);
        damageButton.setLayoutY(10);
        damageButton.setOnAction(event -> GameLauncher.client.send(("/u/," + GameLauncher.client.getName() + ",2,").getBytes()));
        damageButton.getStylesheets().add("client/graphics/chatstyle.css");

        gunCountButton.setMaxHeight(50);
        gunCountButton.setMinWidth(50);
        gunCountButton.setLayoutX(canvas.getWidth() - 255);
        gunCountButton.setLayoutY(10);
        gunCountButton.setOnAction(event -> GameLauncher.client.send(("/u/," + GameLauncher.client.getName() + ",3,").getBytes()));
        gunCountButton.getStylesheets().add("client/graphics/chatstyle.css");

        healthButton.setMaxHeight(50);
        healthButton.setMinWidth(50);
        healthButton.setLayoutX(canvas.getWidth() - 330);
        healthButton.setLayoutY(10);
        healthButton.setOnAction(event -> GameLauncher.client.send(("/u/," + GameLauncher.client.getName() + ",4,").getBytes()));
        healthButton.getStylesheets().add("client/graphics/chatstyle.css");

        /*
//        damageButton.setLayoutX(canvas.getWidth() - damageButton.getWidth());
//        damageButton.setLayoutY(30);
//        damageButton.setLayoutY(speedButton.getHeight() + 10);

//        gunCountButton.setLayoutX(canvas.getWidth() - gunCountButton.getWidth());
//        gunCountButton.setLayoutY(50);
//        gunCountButton.setLayoutY(speedButton.getHeight() + damageButton.getHeight() + 10);

//        healthButton.setLayoutX(canvas.getWidth() - healthButton.getWidth());
//        healthButton.setLayoutY(70);
//        healthButton.setLayoutY(speedButton.getHeight() + damageButton.getHeight() + gunCountButton.getHeight() + 10);
//        connectServerController.getClient().send();
            */

        // debug canvas
        debugCanvas.setLayoutX(10);
        debugCanvas.setLayoutY(10);

        // write field
        writeField.getStylesheets().add("client/graphics/chatstyle.css");
        writeField.setFocusTraversable(false);
        writeField.setEditable(false);

        writeField.setMaxWidth(300);
        writeField.setMinWidth(300);
        writeField.setMaxHeight(18);
        writeField.setMinHeight(18);
        writeField.setLayoutX(10);
        writeField.setLayoutY(canvas.getHeight() - 18 - 10);

        // text area
        textArea.getStylesheets().add("client/graphics/chatstyle.css");
        textArea.setEditable(false);
        textArea.setFocusTraversable(false);

        textArea.setMaxWidth(300);
        textArea.setMinWidth(300);
        textArea.setMaxHeight(75);
        textArea.setMinHeight(75);
        textArea.setLayoutX(10);
        textArea.setLayoutY(canvas.getHeight() - 28 - 75 - 5);

        this.connectServerController = JavafxLogin.connectServerController;
        connectServerController.setInChat(true);
        connectServerController.setTextArea(textArea);
        connectServerController.setWriteField(writeField);
        textArea.setText("");
        writeField.setFocusTraversable(true);
        writeField.requestFocus();

        console("Successfully connected as: " + connectServerController.getClient().getName() + "!");
        console("Welcome!");

        this.gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);

        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 11));
        gc.fillText("Loading...", 20, 20);

        primaryStage.setTitle(title);
        primaryStage.sizeToScene();
        primaryStage.setResizable(false);
        if (alwaysOnTop) primaryStage.setAlwaysOnTop(true);
        if (fullScreen) primaryStage.setFullScreen(true);
        primaryStage.setOnCloseRequest(event -> {
            System.out.println("Stage is closing");
            System.out.println("Exiting System.");
            System.exit(0);
        });
        primaryStage.show();
    }

    /**
     * Fully rebuilds window. Can be used for changing game settings.
     *
     * @param width       width of window.
     * @param height      height of window.
     * @param title       title of window.
     * @param fullScreen  fullscreen option of window.
     * @param alwaysOnTop always on top option of window.
     */
    public void rebuild(int width, int height, String title, boolean fullScreen, boolean alwaysOnTop) {

        this.width = width;
        this.height = height;
        this.title = title;
        this.fullScreen = fullScreen;
        this.alwaysOnTop = alwaysOnTop;

        build(primaryStage);
    }

    public void rotateScreen(int degree) {
        this.group.setRotate(degree);
    }

    public Canvas debugCanvas = new Canvas(170, 40);
    public TextArea textArea = new TextArea("Welcome!");
    public TextField writeField = new TextField("Write a message");
    public Button speedButton = new Button("Speed+");
    public Button damageButton = new Button("Damage+");
    public Button gunCountButton = new Button("Gun Count+");
    public Button healthButton = new Button("Health+");

    /**
     * Render worldController on the screen.
     *
     * @param worldController worldController which will be rendered.
     */
    public void render(WorldController worldController) {
        // Don't fill background for paint game
        if (!partyMode) {
            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());
        }

        worldController.render(this);
        if (worldController.currentWorld instanceof GameWorld) {

            debugCanvas.getGraphicsContext2D().setFont(new Font("Arial", 9));
            debugCanvas.getGraphicsContext2D().setFillRule(FillRule.NON_ZERO);
            debugCanvas.getGraphicsContext2D().setFill(new Color(0, 0, 0, 0.6));
            debugCanvas.getGraphicsContext2D().setStroke(Color.WHITE);
            debugCanvas.getGraphicsContext2D().clearRect(0, 0, debugCanvas.getWidth(), debugCanvas.getHeight());
            debugCanvas.getGraphicsContext2D().fillRect(0, 0, debugCanvas.getWidth(), debugCanvas.getHeight());
            debugCanvas.getGraphicsContext2D().strokeRect(0, 0, debugCanvas.getWidth(), debugCanvas.getHeight());
            debugCanvas.getGraphicsContext2D().setFill(Color.WHITE);
            debugCanvas.getGraphicsContext2D().fillText(String.format(Locale.US, "offset X: %1$5.2f | offset Y: %2$5.2f ", worldController.currentWorld.offsetX, worldController.currentWorld.offsetY), 5, 10);
            debugCanvas.getGraphicsContext2D().fillText(String.format(Locale.US, "ship X: %1$5.2f | shipY: %2$5.2f ", ((GameWorld) worldController.currentWorld).clientPlayer.ship.x, ((GameWorld) worldController.currentWorld).clientPlayer.ship.y), 5, 20);

        }
    }

    public void refreshTitle(String status) {
        primaryStage.setTitle(this.title + " | " + status);
    }


    public void update() {
        if (Keyboard.keyTyped(KeyCode.ENTER.ordinal())) {
            if (!writeField.isEditable()) {
                writeField.setEditable(true);
                writeField.setFocusTraversable(true);
                writeField.requestFocus();
                writeField.setText("");
            } else {
                String string = writeField.getText();
                switch (string) {
                    case "/help":
                        console("commands:\n/party - turn on/off party mode\n/quit - quit the game\n/debug - debug the game");
                        writeField.setText("");
                        break;
                    case "/party":
                        partyMode = !partyMode;
                        writeField.setText("");
                        break;
                    case "/debug":
                        debug = !debug;
                        writeField.setText("");
                        break;

                    case "/quit":
                        System.exit(0);
                    default:
                        sendPressed();
                        break;
                }
                writeField.setEditable(false);
                writeField.setFocusTraversable(false);
            }
        }
    }

    /**
     * send message sent from writeField upon send pressed.
     */
    public void sendPressed() {
        connectServerController.send(writeField.getText(), true);
    }


    /**
     * write to chat.
     *
     * @param message message to write
     */
    private void console(String message) {
        textArea.appendText(message + "\n\r");
    }
}
