package client.objects.effects;

import client.world.game.AbstractGameWorld;
import javafx.scene.paint.Color;

public class RedEngineParticle extends AbstractParticle {

    private static Color[] colors = {new Color(1, 0, 0, 1),
            new Color(0.9, 0, 0, 1),
            new Color(0.8, 0, 0, 1),
            new Color(0.7, 0, 0, 1)};

    public RedEngineParticle(AbstractGameWorld world, double startX, double startY, double speedX, double speedY) {
        super(world, startX, startY, speedX, speedY);
        super.color = colors[AbstractParticle.random.nextInt(colors.length)];
        super.lifeTime = 0;
        super.fadeSpeed = 0.04;
        super.size = 20 + AbstractParticle.random.nextInt(4);
    }

}
