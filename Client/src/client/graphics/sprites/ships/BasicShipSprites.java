package client.graphics.sprites.ships;


import client.graphics.sprites.SpriteLoader;
import javafx.scene.image.Image;

public class BasicShipSprites {

    private static boolean loaded = false;
    public static Image[] images;

    public static Image[] getImages() {
        if (loaded) {
            return images;
        } else {
            load();
            return images;
        }
    }


    public static void load() {
        if (!loaded) {
            images = SpriteLoader.load(new Image("ships/Ship1.png"));
            loaded = true;
        } else System.out.println("BasicShipSprites already loaded.");
    }

}
