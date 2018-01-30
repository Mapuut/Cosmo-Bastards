package client.objects.ships;


import client.objects.npc.AbstractNPC;
import client.objects.weapons.guns.GreenGun;
import client.objects.weapons.holder.OneSlotGunHolder;
import client.utilities.Point;
import client.world.game.AbstractGameWorld;
import client.world.game.GameWorld;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class BasicShip extends AbstractShip{

    private static BufferedImage image;

    static {
        try {
            image = ImageIO.read(GameWorld.class.getResourceAsStream("/ships/Ship1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Point[] collisionPoints = {new Point(37, 0),
            new Point(24, -22),
            new Point(24, 22),
            new Point(-7, -29),
            new Point(-7, 29),
            new Point(-27, -29),
            new Point(-27, 29),
            new Point(-7, 29),
            new Point(-38, -19),
            new Point(-38, 19),
            new Point(-38, 0)};

    public BasicShip(AbstractGameWorld world, AbstractNPC owner){
        super(world, owner, collisionPoints);
        super.gunHolder = new OneSlotGunHolder(this, new GreenGun(this, 0, -9));
        sprite = image;
        radiusOfShip = 45;
    }



    @Override
    public void setWeaponsToDeafultPosition() {
    }
}
