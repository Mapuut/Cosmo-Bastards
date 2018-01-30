package client.objects.asteroid;

import client.graphics.ScreenController;
import client.graphics.sprites.asteroids.Asteroid1Sprites;
import client.graphics.sprites.asteroids.Asteroid2Sprites;
import client.objects.projectiles.AbstractProjectile;
import client.objects.ships.AbstractShip;
import client.world.game.AbstractGameWorld;
import javafx.geometry.Point2D;

import java.util.Random;

/**
 * Created by.
 */
public class Asteroid extends AbstractShip {

    public static Random random = new Random();


    public Asteroid(AbstractGameWorld world, String i) {
        super(world, new AsteroidOwner(world));
        owner.ship = this;
        if (i.equals("1")) {
            super.sprites = Asteroid1Sprites.getImages();
        } else {
            super.sprites = Asteroid2Sprites.getImages();
        }

        super.maxHP = 200;
        super.curHP = 200;

    }


    public void update(){
        updateCurrentTile();
    }

    public void render(ScreenController screenController) {
        rotationRadians %= 2 * Math.PI;
        if (rotationRadians < - Math.PI) rotationRadians = Math.PI + (rotationRadians + Math.PI);
        if (rotationRadians > Math.PI) rotationRadians = - Math.PI + (rotationRadians - Math.PI);
        screenController.gc.drawImage(this.sprites[(int)(Math.toDegrees(this.rotationRadians) + 180)], (this.x - world.offsetX - sprites[(int)(Math.toDegrees(this.rotationRadians) + 180)].getWidth() / 2), (this.y - world.offsetY - sprites[(int)(Math.toDegrees(this.rotationRadians) + 180)].getHeight() / 2));

    }
}
