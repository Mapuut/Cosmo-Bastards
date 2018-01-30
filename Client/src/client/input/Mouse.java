package client.input;

import client.graphics.ZoomController;
import javafx.scene.Group;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public class Mouse {

    private static int mouseX = 0;
    private static int mouseY = 0;
    public static boolean rightButton = false;
    public static boolean leftButton = false;
    public static boolean middleButton = false;

    public static double ZoomDelta = 0;
    private static double ZoomDeltaLive = 0;

    public static void set(Group root) {
        root.addEventFilter(MouseEvent.MOUSE_MOVED, e -> {
            mouseX = (int) (e.getSceneX());
            mouseY = (int) (e.getSceneY());
        });

        root.addEventFilter(MouseEvent.MOUSE_DRAGGED, e -> {
            mouseX = (int) (e.getSceneX());
            mouseY = (int) (e.getSceneY());
        });

        root.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            if (e.getButton().equals(MouseButton.PRIMARY)) {
                leftButton = true;
            } else if (e.getButton().equals(MouseButton.MIDDLE)) {
                middleButton = true;
            } else if (e.getButton().equals(MouseButton.SECONDARY)) {
                rightButton = true;
            }
        });

        root.addEventFilter(MouseEvent.MOUSE_RELEASED, e -> {
            if (e.getButton().equals(MouseButton.PRIMARY)) {
                leftButton = false;
            } else if (e.getButton().equals(MouseButton.MIDDLE)) {
                middleButton = false;
            } else if (e.getButton().equals(MouseButton.SECONDARY)) {
                rightButton = false;
            }
        });

        root.addEventFilter(ScrollEvent.SCROLL, e -> ZoomDeltaLive += e.getDeltaY());
    }

    public static double getMouseX() {
        return (double) mouseX / (ZoomController.ZoomLive / ZoomController.ZoomSpeed);
    }

    public static double getMouseY() {
        return (double) mouseY / (ZoomController.ZoomLive / ZoomController.ZoomSpeed);
    }

    public static void update() {
        ZoomDelta = ZoomDeltaLive;
        ZoomDeltaLive = 0;
    }

}
