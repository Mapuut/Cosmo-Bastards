package client.input;

import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;

public class Keyboard {


    private static int numOfKeys = 300;
    private static boolean[] keysPressedByIndexLive = new boolean[numOfKeys];

    private static boolean[] keysPressedByIndexLastUpdate = new boolean[numOfKeys];
    private static boolean[] keysPressedByIndexCurrent = new boolean[numOfKeys];


    public static void set(Scene scene) {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> keysPressedByIndexLive[e.getCode().ordinal()] = true);
        scene.addEventFilter(KeyEvent.KEY_RELEASED, e -> keysPressedByIndexLive[e.getCode().ordinal()] = false);
    }

    public static void update() {
        for (int i = 0; i < numOfKeys; i++) {
            keysPressedByIndexLastUpdate[i] = keysPressedByIndexCurrent[i];
            keysPressedByIndexCurrent[i] = keysPressedByIndexLive[i];
        }
    }

    public static boolean keyPressed(int key) {
        return keysPressedByIndexCurrent[key];
    }


    public static boolean keyTyped(int key) {
        return keysPressedByIndexCurrent[key] && !keysPressedByIndexLastUpdate[key];
    }

}
