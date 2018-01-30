package client.objects.projectiles.bullet;

import client.graphics.ScreenController;
import client.objects.projectiles.AbstractProjectile;
import client.objects.ships.AbstractShip;
import client.world.game.AbstractGameWorld;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by.
 */
public class BlueBullet extends AbstractProjectile{

    public static final int size = 9;
    public static final double speed = 25;
    private static Random random = new Random();
    public List<EffectEntity> effectEntity = new ArrayList<>();
    public boolean used = false;

    public BlueBullet(AbstractGameWorld world, String ownerName, double x, double y, double angle) {
        super(world, ownerName, x, y);
        super.speedX = Math.cos(angle) * speed;
        super.speedY = Math.sin(angle) * speed;
        super.lifetime = 55;

    }

    @Override
    public void update() {
        if (!used) {
            this.x += speedX;
            this.y += speedY;
            updateCurrentTile();
            checkIfCollision();

            for (int i = 0; i < 3; i++) {
                effectEntity.add(new EffectEntity(this.x, this.y, opticity));
            }
        }

        if (lifetime-- <= 0) {
            opticity -= 0.02;
            if (opticity <= 0) {
                opticity = 0;
                this.remove();
                world.tiles[currentTile].projectiles.remove(this);
            }
        }

        for (int i = 0; i < effectEntity.size(); i++) {
            effectEntity.get(i).update();
            if (effectEntity.get(i).opticity <= 0) {
                effectEntity.remove(i);
                i--;
            }
        }


    }

    @Override
    public void checkIfCollision(){
        int xx = ((int)super.x >> world.TileSize) - 1;
        int yy = ((int)super.y >> world.TileSize) - 1;
        if (xx < 0) xx = 0;
        if (yy < 0) yy = 0;

        if (xx > world.sizeOfMap - 3) xx = world.sizeOfMap - 3;
        if (yy > world.sizeOfMap - 3) yy = world.sizeOfMap - 3;

        for (int y = yy; y < yy + 3; y++) {
            for (int x = xx; x < xx + 3; x++) {
                if (world.tiles[y * world.sizeOfMap + x].ships.size() != 0) {
                    int iCounter = world.tiles[y * world.sizeOfMap + x].ships.size();
                    for (int i = 0; i < iCounter; i++) {
                        AbstractShip curShip = world.tiles[y * world.sizeOfMap + x].ships.get(i);
                        if (Math.sqrt((curShip.x - super.x) * (curShip.x - super.x) + (curShip.y - super.y) * (curShip.y - super.y)) < curShip.radiusOfShip) {
                            if (curShip.name.equals(ownerName)) continue;
                            if (curShip.checkIfCollision(this.x, this.y)) {
                                used = true;
                                for (int ii = 0; ii < 40; ii++) {
                                    effectEntity.add(new EffectEntity(this.x, this.y, opticity));
                                }
                                lifetime = 0;
                                return;

                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void render(ScreenController screenController) {
        screenController.gc.setFill(Color.color(0, 0, 1, opticity));
        screenController.gc.setStroke(Color.color(0, 0, 0, opticity));
        screenController.gc.fillOval(this.x - world.offsetX - (size / 2), this.y - world.offsetY - (size / 2), size, size);
        screenController.gc.strokeOval(this.x - world.offsetX - (size / 2), this.y - world.offsetY - (size / 2), size, size);

        for (int i = 0; i < effectEntity.size(); i++) {
            effectEntity.get(i).render(screenController);
        }
    }

    public static boolean whichB = true;
    private class EffectEntity {

        public double x;
        public double y;
        public double speedX;
        public double speedY;
        public double opticity = 1;
        public double colorB;

        public EffectEntity(double x, double y, double opticity) {
            this.x = x;
            this.y = y;
            this.opticity = opticity;
            this.speedX = random.nextGaussian() * 2;
            this.speedY = random.nextGaussian() * 2;
            whichB = !whichB;
            if (whichB) {
                colorB = 1;
            } else {
                colorB = 0.8;
            }

        }

        public void update() {
            this.x += this.speedX;
            this.y += this.speedY;
            this.opticity -= 0.035;
            if (this.opticity < 0) opticity = 0;
        }

        public void render(ScreenController screenController) {
            screenController.gc.setFill(Color.color(0, 0, colorB, this.opticity));
            screenController.gc.fillRect(this.x - world.offsetX, this.y - world.offsetY, 5, 5);
        }
    }
}
