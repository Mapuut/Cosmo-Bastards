package client.graphics.sprites.asteroids;

import client.graphics.sprites.SpriteLoader;
import javafx.scene.image.Image;

public class Asteroid2Sprites {

    private static boolean loaded = false;
    public static Image[] images;

    public static Image[] getImages() {
        if (loaded){
            return images;
        }else {
            load();
            return images;
        }
    }


    public static void load() {
        if (!loaded){
            images = SpriteLoader.load(new Image("asteroids/asteroid2.png"));
            loaded = true;
        }else System.out.println("asteroid2 already loaded.");
    }

}
