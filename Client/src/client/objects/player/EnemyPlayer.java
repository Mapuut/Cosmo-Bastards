package client.objects.player;

import client.graphics.ScreenController;
import client.objects.ships.BasicShip;
import client.world.game.AbstractGameWorld;

public class EnemyPlayer extends AbstractPlayer {

    private String clientName;

    public EnemyPlayer(AbstractGameWorld world, String clientName) {
        super(world);
        super.ship = new BasicShip(super.world, this);
        this.clientName = clientName;
    }

    @Override
    public void updateShip() {
        ship.update();
    }

    /**
     * Tells if player/npc currently want to sight or aim with his pointer.
     *
     * @return True if so False otherwise.
     */
    @Override
    public boolean pointerActive() {
        return false;
    }

    @Override
    public double getPointerForGunsX() {
        return 0;
    }

    @Override
    public double getPointerForGunsY() {
        return 0;
    }

    @Override
    public void update() {
        updateShip();
    }

    @Override
    public void render(ScreenController screenController) {
        ship.render(screenController);
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
