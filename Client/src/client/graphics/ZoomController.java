package client.graphics;

import client.input.Mouse;

public class ZoomController {


    public static double Zoom = 800;
    public static double ZoomLive = 800;
    public static double ZoomSpeed = 800;

    public static void update() {
        Zoom += Mouse.ZoomDelta;
        ZoomLive += (Zoom - ZoomLive) / 50;
        if (Math.abs(Zoom - ZoomLive) < 1) {
            ZoomLive = Zoom;
        }

        if (Zoom < 280) Zoom = 280;
        if (Zoom > 800) Zoom = 800;
    }

    public static void render(ScreenController screenController) {

        if (screenController.canvas.getScaleX() != ZoomLive / ZoomSpeed) {
            screenController.canvas.setScaleX(ZoomLive / ZoomSpeed);
            screenController.canvas.setScaleY(ZoomLive / ZoomSpeed);
            if (screenController.canvas.getWidth() != screenController.width / (ZoomLive / ZoomSpeed)) {
                screenController.canvas.setHeight(screenController.height / (ZoomLive / ZoomSpeed));
                screenController.canvas.setWidth(screenController.width / (ZoomLive / ZoomSpeed));
                screenController.canvas.setLayoutX(-(screenController.canvas.getWidth() - screenController.width) / 2);
                screenController.canvas.setLayoutY(-(screenController.canvas.getHeight() - screenController.height) / 2);
            }

        }
    }
}
