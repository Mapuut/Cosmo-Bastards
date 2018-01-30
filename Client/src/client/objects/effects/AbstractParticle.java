package client.objects.effects;

import client.graphics.ScreenController;
import client.objects.Objects;
import client.world.game.AbstractGameWorld;
import javafx.scene.paint.Color;

import java.util.Random;

public abstract class AbstractParticle extends Objects {

    protected static Random random = new Random();

    public double speedX = 0;
    public double speedY = 0;
    public int lifeTime = 60;
    public double opacity = 1.0;
    public double fadeSpeed = 0.02;
    public double size = 5;
    public Color color = Color.WHITE;

    public AbstractParticle(AbstractGameWorld world, double startX, double startY, double speedX, double speedY) {
        super(world);
        this.x = startX;
        this.y = startY;
        this.speedX = speedX;
        this.speedY = speedY;
    }

    @Override
    public void update() {

        if (lifeTime > 0) {
            lifeTime--;
            this.x += this.speedX;
            this.y += this.speedY;

        } else {
            if (opacity > 0) {
                opacity -= this.fadeSpeed;
                if (opacity < 0) opacity = 0;
                this.color = new Color(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), opacity);
                this.speedX -= this.speedX / 50;
                this.speedY -= this.speedY / 50;
                this.x += this.speedX;
                this.y += this.speedY;
            } else {
                remove();
            }
        }
    }

    @Override
    public void render(ScreenController screenController) {
        screenController.gc.setFill(this.color);
        //screenController.gc.fillRect(this.x - this.world.offsetX - this.size / 2, this.y - this.world.offsetY - this.size / 2, this.size, this.size);
        screenController.gc.fillRoundRect(this.x - this.world.offsetX - this.size / 2, this.y - this.world.offsetY - this.size / 2, this.size, this.size, this.size / 2, this.size / 2);
    }
}
