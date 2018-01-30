package client.objects.weapons.guns;

import client.objects.projectiles.bullet.GreenBullet;
import client.objects.ships.AbstractShip;
import client.utilities.MathFunctions;
import udp.network.server.Server;

import java.util.Locale;

public class GreenGun extends AbstractGun{

    public GreenGun(AbstractShip ship, int xOffsetOnShip, int yOffsetOnShip){
        super(ship, xOffsetOnShip, yOffsetOnShip);
        super.rotationSpeed = Math.toRadians(6);
    }



    @Override
    public void update() {
        curCooldown--;
        double expectedRadians = Math.atan2(ship.owner.getPointerForGunsY() - ship.y - Math.sin((ship.rotationRadians % (Math.PI * 2)) + super.startingRadiansFromShip) * super.distanceFromShipCenter, ship.owner.getPointerForGunsX() - ship.x - Math.cos((ship.rotationRadians % (Math.PI * 2)) + super.startingRadiansFromShip) * super.distanceFromShipCenter);

        if (MathFunctions.rawDistance(expectedRadians, super.rotationRadians) > super.rotationSpeed){
            if (MathFunctions.rawDistance(expectedRadians, super.rotationRadians + super.rotationSpeed) < MathFunctions.rawDistance(expectedRadians, super.rotationRadians)){
                super.rotationRadians += super.rotationSpeed;
            }else {
                super.rotationRadians -= super.rotationSpeed;
            }
        } else {
            super.rotationRadians = expectedRadians;
        }
        if (rotationRadians < - Math.PI) rotationRadians = Math.PI + (rotationRadians + Math.PI);
        if (rotationRadians > Math.PI) rotationRadians = - Math.PI + (rotationRadians - Math.PI);

    }

    private static final int cooldown = 20;
    private int curCooldown = 20;

    @Override
    public void shoot() {
        if (curCooldown <= 0) {
            curCooldown = cooldown + AbstractGun.random.nextInt(10);
            GreenBullet bullet = new GreenBullet(ship.world, ship, Math.cos(ship.rotationRadians + super.startingRadiansFromShip) * super.distanceFromShipCenter + ship.x, Math.sin(ship.rotationRadians + super.startingRadiansFromShip) * super.distanceFromShipCenter + ship.y, super.rotationRadians);
            ship.world.projectiles.add(bullet);

            Server.sendToAll("/p/," + "1," + ship.name + "," + (int)bullet.x + "," + (int)bullet.y + "," + String.format(Locale.US, "%1$.3f", super.rotationRadians) +  ",");
        }

    }
}
