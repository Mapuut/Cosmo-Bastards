package client.objects.asteroid;

import client.objects.projectiles.AbstractProjectile;
import client.objects.ships.AbstractShip;
import client.utilities.Point;
import client.world.game.AbstractGameWorld;
import client.world.game.GameWorld;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * Created by.
 */
public class Asteroid extends AbstractShip {

    private static BufferedImage image, image2;

    static {
        try {
            image = ImageIO.read(GameWorld.class.getResourceAsStream("/asteroids/asteroid1.png"));
            image2 = ImageIO.read(GameWorld.class.getResourceAsStream("/asteroids/asteroid2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean imageBoolean;
    public static Random random = new Random();

    public static Point[] collisionPoints1 = {new Point(-29, -25),
            new Point(-8, -28),
            new Point(30, 22),
            new Point(17, -29),
            new Point(23, -22),

            new Point(24, -12),
            new Point(29, -7),
            new Point(29, 13),
            new Point(26, 17),
            new Point(24, 26),
            new Point(21, 29),

            new Point(-14, 29),
            new Point(-16, 27),
            new Point(-17, 20),
            new Point(-21, 17),

            new Point(-22, 7),
            new Point(-29, 1)
    };

    public static Point[] collisionPoints2 = {new Point(-23, -21),
            new Point(-9, -22),
            new Point(-8, -29),
            new Point(9, -29),
            new Point(15, -23),

            new Point(17, -12),
            new Point(26, -10),
            new Point(28, -8),
            new Point(28, 14),
            new Point(25, 17),
            new Point(24, 25),

            new Point(20, 29),
            new Point(3, 29),
            new Point(-20, 28),
            new Point(-23, 25),

            new Point(-23, 20),
            new Point(-28, 15),

            new Point(-28, 9),
            new Point(-24, 1),

    };

    public Asteroid(AbstractGameWorld world) {
        super(world, new AsteroidOwner(world), collisionPoints1);
        owner.ship = this;
        if (random.nextBoolean()) {
            sprite = image;
            super.moving = true;
        } else {
            sprite = image2;
            super.moving = false;
            super.collisionPoints = collisionPoints2;
        }

        super.mass = 20;
        super.maxHP = 200;
        super.curHP = 200;

        this.x = random.nextInt(1000);
        this.y = random.nextInt(1000);
        super.currentMoveSpeed = random.nextDouble();
        super.movingForceDirectionInRadians = Math.PI * 2 - random.nextDouble() * Math.PI * 4;
    }


    public void update(){
        updateCurrentTile();
        rotationRadians += currentRotationSpeed;
        move();
    }

    public void doDamage(AbstractProjectile bullet, double damage) {
        this.curHP -= damage;
        if (this.curHP <= 0){
            curHP = 0;
            remove();
        }

    }


    @Override
    public void setWeaponsToDeafultPosition() {

    }
}
