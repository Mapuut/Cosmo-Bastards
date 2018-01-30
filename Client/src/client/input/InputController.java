package client.input;

import javafx.scene.Group;
import javafx.scene.Scene;

public class InputController {


    public static void set(Group root, Scene scene) {
        Mouse.set(root);
        Keyboard.set(scene);
    }

    public static void update() {
        Mouse.update();
        Keyboard.update();
    }
}
