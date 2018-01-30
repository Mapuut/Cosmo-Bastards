package client.objects.effects;


import client.world.game.AbstractGameWorld;
import javafx.scene.paint.Color;

public class GreenEngineParticle extends AbstractParticle {

    private static Color[] colors = {new Color(0, 1, 0, 1),
            new Color(0, 0.8, 0, 1)};

    public GreenEngineParticle(AbstractGameWorld world, double startX, double startY, double speedX, double speedY) {
        super(world, startX, startY, speedX, speedY);
        super.color = colors[AbstractParticle.random.nextInt(colors.length)];
        super.lifeTime = 0;
        super.fadeSpeed = 0.06;
        super.size = 20;
    }

}
