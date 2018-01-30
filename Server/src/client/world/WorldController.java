package client.world;

import client.graphics.UpdateController;
import client.world.game.GameWorld;

public class WorldController {

    public UpdateController updateController;
    public GameWorld gameWorld;

    public WorldController(UpdateController updateController) {
        this.updateController = updateController;
        this.gameWorld = new GameWorld();
        this.gameWorld.load();
    }

    public void update() {
        gameWorld.update();
//        Server.server.
    }
}
