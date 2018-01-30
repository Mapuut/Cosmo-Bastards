package client.objects.weapons.guns;

import client.objects.projectiles.bullet.RedBullet;
import client.objects.ships.AbstractShip;
import client.utilities.MathFunctions;
import udp.network.server.Server;

import java.util.Locale;

public class RedGun extends AbstractGun{

    public RedGun(AbstractShip ship, int xOffsetOnShip, int yOffsetOnShip){
        super(ship, xOffsetOnShip, yOffsetOnShip);
        super.rotationSpeed = Math.toRadians(10);
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

    private static final int cooldown = 16;
    private int curCooldown = 16;

    @Override
    public void shoot() {
        if (curCooldown <= 0) {
            curCooldown = cooldown + AbstractGun.random.nextInt(6);
            RedBullet bullet = new RedBullet(ship.world, ship, Math.cos(ship.rotationRadians + super.startingRadiansFromShip) * super.distanceFromShipCenter + ship.x, Math.sin(ship.rotationRadians + super.startingRadiansFromShip) * super.distanceFromShipCenter + ship.y, super.rotationRadians);
            ship.world.projectiles.add(bullet);

            Server.sendToAll("/p/," + "3," + ship.name + "," + (int)bullet.x + "," + (int)bullet.y + "," + String.format(Locale.US, "%1$.3f", super.rotationRadians) +  ",");
        }

    }
}