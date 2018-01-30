package client.objects.player;

import client.objects.npc.AbstractNPC;
import client.world.game.AbstractGameWorld;

public abstract class AbstractPlayer extends AbstractNPC {

    public AbstractPlayer(AbstractGameWorld world) {
        super(world);
    }

    public abstract void update();

}
