package client.graphics.sprites;


import client.graphics.sprites.armor.ThreeGunHolderSprites;
import client.graphics.sprites.asteroids.Asteroid1Sprites;
import client.graphics.sprites.asteroids.Asteroid2Sprites;
import client.graphics.sprites.ships.BasicShipSprites;
import client.graphics.sprites.weapons.BlueGunSprites;
import client.graphics.sprites.weapons.GreenGunSprites;
import client.graphics.sprites.weapons.RedGunSprites;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public abstract class SpriteLoader {


    public static Image[] load(Image image) {

        Image[] images = new Image[360];
        ImageView imageTransformer = new ImageView(image);
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);

        for (int i = 0; i < 360; i++) {
            imageTransformer.setRotate(i);
            images[i] = imageTransformer.snapshot(params, null);
        }

        return images;
    }

    public static void loadAll() {
        BasicShipSprites.load();
        GreenGunSprites.load();
        BlueGunSprites.load();
        RedGunSprites.load();
        Asteroid1Sprites.load();
        Asteroid2Sprites.load();
        ThreeGunHolderSprites.load();
    }

}
