package client.objects.effects;

import client.world.game.AbstractGameWorld;
import javafx.scene.paint.Color;

public class BlueEngineParticle extends AbstractParticle {

    private static Color[] colors = {new Color(0, 0, 1, 1),
            new Color(0, 0, 0.9, 1),
            new Color(0, 0, 0.8, 1)};

    public BlueEngineParticle(AbstractGameWorld world, double startX, double startY, double speedX, double speedY) {
        super(world, startX, startY, speedX, speedY);
        super.color = colors[AbstractParticle.random.nextInt(colors.length)];
        super.lifeTime = 0;
        super.fadeSpeed = 0.05;
        super.size = 20 + AbstractParticle.random.nextInt(2);
    }

}
