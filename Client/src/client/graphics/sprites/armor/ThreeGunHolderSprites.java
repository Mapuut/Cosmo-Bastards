package client.graphics.sprites.armor;


import client.graphics.sprites.SpriteLoader;
import javafx.scene.image.Image;

public class ThreeGunHolderSprites {
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
            images = SpriteLoader.load(new Image("armor/ShipThreeSlotHolder.png"));
            loaded = true;
        } else System.out.println("ShipThreeSlotHolder already loaded.");
    }

}