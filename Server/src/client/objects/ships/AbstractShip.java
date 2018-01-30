package client.objects.ships;


import client.objects.Objects;
import client.objects.engines.AbstractEngine;
import client.objects.engines.GreenEngine;
import client.objects.npc.AbstractNPC;
import client.objects.projectiles.AbstractProjectile;
import client.objects.weapons.holder.AbstractGunHolder;
import client.utilities.MathFunctions;
import client.utilities.Point;
import client.utilities.Vector;
import client.world.game.AbstractGameWorld;
import udp.network.server.Server;

import java.awt.image.BufferedImage;


public abstract class AbstractShip extends Objects {

    public String name;
    public static int id;
    public AbstractNPC owner;
    public AbstractGunHolder gunHolder;
    public AbstractEngine engine = new GreenEngine();
    public int currentTile = 0;

    public int engineLevel = 0;
    public int gunLevel = 0;
    public int gunCount = 0;
    public int constitution = 0;
    public boolean turbo = false;

    public boolean moving = false;

    public BufferedImage sprite;

    public double rotationRadians = 0;
    public double currentRotationSpeed = 0;



    public double currentMoveSpeed = 0;
    public double movingForceDirectionInRadians = 0;


    public boolean flyToTarget = false;
    public double flyToTargetX = 0;
    public double flyToTargetY = 0;

    public boolean manualControl = false;
    public boolean manualControlForward = false;
    public boolean manualControlRight = false;
    public boolean manualControlLeft = false;

    public int radiusOfShip = 40;
    public double mass = 10;
    public double width = 75;
    public double height = 75;

    public double maxHP = 10;
    public double curHP = 10;
    public double experienceForKill = 0.5;

    protected Point[] collisionPoints;

    public AbstractShip(AbstractGameWorld world, AbstractNPC owner, Point[] collisionPoints){
        super(world);
        this.owner = owner;
        this.name = Integer.toString(id);
        id++;
        world.tiles[currentTile].ships.add(this);

        if (collisionPoints != null) {
            this.collisionPoints = collisionPoints;
        } else {
            this.collisionPoints = new Point[4];
            this.collisionPoints[0] = new Point(20, 20);
            this.collisionPoints[1] = new Point(20, -20);
            this.collisionPoints[2] = new Point(-20, 20);
            this.collisionPoints[3] = new Point(-20, -20);
        }
    }

    public void update(){
        moving = false;
        updateCurrentTile();
        if (flyToTarget){
            updateMove();
        }else if(manualControl) {
            updateMoveManual();
        }else {
            stop();
        }
        move();
        updateGuns();
//        System.out.println(this.x + ";" + this.y);
    }

    protected void updateCurrentTile() {
        int xx = (int) super.x;
        int yy = (int) super.y;
        if (super.x < 0) xx = 0;
        if (super.y < 0) yy = 0;

        if (super.x >= world.sizeOfMap << world.TileSize)  xx = (world.sizeOfMap << world.TileSize) - 1;
        if (super.y >= world.sizeOfMap << world.TileSize) yy = (world.sizeOfMap << world.TileSize) - 1;

        int tile = (yy >> world.TileSize) * world.sizeOfMap + (xx >> world.TileSize);
        if (tile != currentTile) {
            world.tiles[currentTile].ships.remove(this);
            currentTile = tile;
            world.tiles[currentTile].ships.add(this);
        }

    }

    protected void updateCollisionDetection(){
        int xx = ((int)super.x >> world.TileSize) - 1;
        int yy = ((int)super.y >> world.TileSize) - 1;
        if (xx < 0) xx = 0;
        if (yy < 0) yy = 0;

        if (xx > world.sizeOfMap - 3) xx = world.sizeOfMap - 3;
        if (yy > world.sizeOfMap - 3) yy = world.sizeOfMap - 3;

        for (int y = yy; y < yy + 3; y++) {
            for (int x = xx; x < xx + 3; x++) {
                if (world.tiles[y * world.sizeOfMap + x].ships.size() != 0) {
                    final int iCounter = world.tiles[y * world.sizeOfMap + x].ships.size();
                    for (int i = 0; i < iCounter; i++) {
                        if (world.tiles[y * world.sizeOfMap + x].ships.get(i).equals(this)) continue;
                        double anotherShipX = world.tiles[y * world.sizeOfMap + x].ships.get(i).x;
                        double anotherShipY = world.tiles[y * world.sizeOfMap + x].ships.get(i).y;
                        if (Math.sqrt((super.x - anotherShipX) * (super.x - anotherShipX) + (super.y - anotherShipY) * (super.y - anotherShipY)) < this.radiusOfShip + world.tiles[y * world.sizeOfMap + x].ships.get(i).radiusOfShip) {

                            for (int c = 0; c < collisionPoints.length; c++) {
                                double x_prime = this.x + collisionPoints[c].x * Math.cos(rotationRadians) - collisionPoints[c].y * Math.sin(rotationRadians);
                                double y_prime = this.y + collisionPoints[c].x * Math.sin(rotationRadians) + collisionPoints[c].y * Math.cos(rotationRadians);

                                if (world.tiles[y * world.sizeOfMap + x].ships.get(i).checkIfCollision(x_prime, y_prime)) {
                                    AbstractShip shipB = world.tiles[y * world.sizeOfMap + x].ships.get(i);
                                    //if (this.owner instanceof DummyNPC) break;
                                    double massA = this.mass; //M1
                                    double massB = shipB.mass; //M2

                                    Vector distanceFromAtoP = new Vector(x_prime - this.x, y_prime - this.y); // distance vector from center of mass of body A to point P
                                    Vector distanceFromBtoP = new Vector(x_prime - shipB.x, y_prime - shipB.y); // distance vector from center of mass of body B to point P

                                    double preCollisionAngularVelocityA = this.currentRotationSpeed; //initial pre-collision angular velocity of bodies A, B
                                    double preCollisionAngularVelocityB = shipB.currentRotationSpeed; //initial pre-collision angular velocity of bodies A, B
                                    double postCollisionAngularVelocityA;// final post-collision angular velocity of bodies A, B
                                    double postCollisionAngularVelocityB;// final post-collision angular velocity of bodies A, B

                                    Vector preCollisionVelocityA = new Vector( Math.cos(this.movingForceDirectionInRadians) * this.currentMoveSpeed, Math.sin(this.movingForceDirectionInRadians) * this.currentMoveSpeed); // initial pre-collision velocities of center of mass bodies A, B
                                    Vector preCollisionVelocityB = new Vector( Math.cos(shipB.movingForceDirectionInRadians) * shipB.currentMoveSpeed, Math.sin(shipB.movingForceDirectionInRadians) * shipB.currentMoveSpeed); // initial pre-collision velocities of center of mass bodies A, B
                                    Vector postCollisionVelocityA; // final post-collision velocities of center of mass bodies A, B
                                    Vector postCollisionVelocityB; // final post-collision velocities of center of mass bodies A, B

                                    Vector preCollisionVelocityOfImpactA = distanceFromAtoP.crossPerpendicular(preCollisionAngularVelocityA).add(preCollisionVelocityA);
                                    Vector preCollisionVelocityOfImpactB = distanceFromBtoP.crossPerpendicular(preCollisionAngularVelocityB).add(preCollisionVelocityB);


                                    Vector n_PerpendicularToEdgeB; // normal (perpendicular) vector to edge of body B
                                    double collisionDegree = Math.atan2(y_prime - shipB.y, x_prime - shipB.x);
                                    double currentDegree = shipB.rotationRadians - collisionDegree;
                                    currentDegree %= 2*Math.PI;
                                    if (currentDegree < - Math.PI) currentDegree = Math.PI + (currentDegree + Math.PI);
                                    if (currentDegree > Math.PI) currentDegree = - Math.PI + (currentDegree - Math.PI);

                                    if ((currentDegree <= 0.25 * Math.PI) && (currentDegree >= -0.25 * Math.PI)) {
                                        n_PerpendicularToEdgeB = new Vector(Math.cos(shipB.rotationRadians), Math.sin(shipB.rotationRadians));

                                    } else if ((currentDegree <= -0.25 * Math.PI) && (currentDegree >= -0.75 * Math.PI)) {
                                        n_PerpendicularToEdgeB = new Vector(Math.cos(shipB.rotationRadians + 0.5 * Math.PI), Math.sin(shipB.rotationRadians + 0.5 * Math.PI));

                                    } else if (((currentDegree <= -0.75 * Math.PI) && (currentDegree >= -Math.PI)) || ((currentDegree <= Math.PI) && (currentDegree >= 0.75 * Math.PI))) {
                                        n_PerpendicularToEdgeB = new Vector(Math.cos(shipB.rotationRadians - Math.PI), Math.sin(shipB.rotationRadians - Math.PI));

                                    } else if ((currentDegree <= 0.75 * Math.PI) && (currentDegree >= 0.25 * Math.PI)){
                                        n_PerpendicularToEdgeB = new Vector(Math.cos(shipB.rotationRadians - 0.5 * Math.PI), Math.sin(shipB.rotationRadians - 0.5 * Math.PI));
                                    } else {
                                        System.out.println("Error in collision - n not set.");
                                        System.exit(0);
                                        n_PerpendicularToEdgeB = new Vector(0, 0);

                                    }

                                    double e = 0.7; // elasticity (0 = inelastic, 1 = perfectly elastic)


                                    Vector preRelativeVelocity = preCollisionVelocityOfImpactA.subtract(preCollisionVelocityOfImpactB); //Vab1

                                    double relativeNormalVelocity = preRelativeVelocity.dot(n_PerpendicularToEdgeB);
                                    if (relativeNormalVelocity >= 0) break;

                                    double Ia = this.mass * (this.width * this.width + this.height * this.height) / 12 ; //Moment of inertia for A
                                    double Ib = shipB.mass * (shipB.width * shipB.width + shipB.height * shipB.height) / 12 ; //Moment of inertia for A

                                    double j = (-(1 + e) * preRelativeVelocity.dot(n_PerpendicularToEdgeB)) / (1 / massA + 1 / massB + (Math.pow(distanceFromAtoP.cross(n_PerpendicularToEdgeB) , 2)) / Ia + (Math.pow(distanceFromBtoP.cross(n_PerpendicularToEdgeB) , 2)) / Ib); // impulse

                                    postCollisionVelocityA = preCollisionVelocityA.add(n_PerpendicularToEdgeB.scale(j / massA));
                                    postCollisionVelocityB = preCollisionVelocityB.subtract(n_PerpendicularToEdgeB.scale(j / massB));

                                    postCollisionAngularVelocityA = preCollisionAngularVelocityA + (distanceFromAtoP.cross(n_PerpendicularToEdgeB.scale(j))) / Ia;
                                    postCollisionAngularVelocityB = preCollisionAngularVelocityB - (distanceFromBtoP.cross(n_PerpendicularToEdgeB.scale(j))) / Ib;

                                    this.movingForceDirectionInRadians = Math.atan2(postCollisionVelocityA.y , postCollisionVelocityA.x);
                                    this.currentMoveSpeed = postCollisionVelocityA.length();

                                    shipB.movingForceDirectionInRadians = Math.atan2(postCollisionVelocityB.y , postCollisionVelocityB.x);
                                    shipB.currentMoveSpeed = postCollisionVelocityB.length();

                                    this.currentRotationSpeed = postCollisionAngularVelocityA;
                                    shipB.currentRotationSpeed = postCollisionAngularVelocityB;

//                                    for (int p = 0; p < Math.abs(relativeNormalVelocity * 10); p++){
//                                        shipB.engine.particles.add(new CollisionParticle(shipB.world, x_prime, y_prime));
//                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if ((this.x < this.radiusOfShip) || (this.y < this.radiusOfShip) || (this.x >= (world.sizeOfMap << world.TileSize) - radiusOfShip) || (this.y >= (world.sizeOfMap << world.TileSize) - radiusOfShip)) {
            for (int c = 0; c < collisionPoints.length; c++) {
                double x_prime = this.x + collisionPoints[c].x * Math.cos(rotationRadians) - collisionPoints[c].y * Math.sin(rotationRadians);
                double y_prime = this.y + collisionPoints[c].x * Math.sin(rotationRadians) + collisionPoints[c].y * Math.cos(rotationRadians);

                if (x_prime <= 0 || y_prime <= 0 || (y_prime >= (world.sizeOfMap << world.TileSize)) || (x_prime >= (world.sizeOfMap << world.TileSize))) {
                    Vector preCollisionVelocityA = new Vector(Math.cos(this.movingForceDirectionInRadians) * this.currentMoveSpeed, Math.sin(this.movingForceDirectionInRadians) * this.currentMoveSpeed);

                    Vector n_PerpendicularToEdgeB;
                    if (x_prime <= 0) {
                        n_PerpendicularToEdgeB = new Vector(1, 0);
                    } else if (y_prime <= 0) {
                        n_PerpendicularToEdgeB = new Vector(0, 1);
                    } else if(x_prime >= (world.sizeOfMap << world.TileSize)) {
                        n_PerpendicularToEdgeB = new Vector(-1, 0);
                    } else if(y_prime >= world.sizeOfMap << world.TileSize) {
                        n_PerpendicularToEdgeB = new Vector(0, -1);
                    } else {
                        System.out.println("Error in collision - n not set (wall).");
                        System.exit(0);
                        n_PerpendicularToEdgeB = new Vector(0, 0);
                    }

                    Vector distanceFromAtoP = new Vector(x_prime - this.x, y_prime - this.y);
                    double Ia = this.mass * (this.width * this.width + this.height * this.height) / 12; //Moment of inertia for A

                    double j = (-(1 + 1) * preCollisionVelocityA.dot(n_PerpendicularToEdgeB)) / (1 / this.mass + (Math.pow(distanceFromAtoP.cross(n_PerpendicularToEdgeB), 2)) / Ia); // impulse

                    double preCollisionAngularVelocityA = this.currentRotationSpeed;
                    Vector preCollisionVelocityOfImpactA = distanceFromAtoP.crossPerpendicular(preCollisionAngularVelocityA).add(preCollisionVelocityA);

                    double relativeNormalVelocity = preCollisionVelocityOfImpactA.dot(n_PerpendicularToEdgeB);
                    if (relativeNormalVelocity >= 0) break;
                    Vector postCollisionVelocityA = preCollisionVelocityA.add(n_PerpendicularToEdgeB.scale(j / this.mass));
                    double postCollisionAngularVelocityA = this.currentRotationSpeed + (distanceFromAtoP.cross(n_PerpendicularToEdgeB.scale(j))) / Ia;


                    this.movingForceDirectionInRadians = Math.atan2(postCollisionVelocityA.y, postCollisionVelocityA.x);
                    this.currentMoveSpeed = postCollisionVelocityA.length();


                    this.currentRotationSpeed = postCollisionAngularVelocityA;

                }
            }
        }
        if (this.x < 0) this.x = 0;
        if (this.y < 0) this.y = 0;

        if (this.x >= (world.sizeOfMap << world.TileSize)) this.x = (world.sizeOfMap << world.TileSize) - 1;
        if (this.y >= (world.sizeOfMap << world.TileSize)) this.y = (world.sizeOfMap << world.TileSize) - 1;
    }

    public boolean checkIfCollision(double x, double y) {

        x -= this.x;
        y -= this.y;

        double pixelX =  (x * Math.cos(Math.PI * 2 - this.rotationRadians)) - (y * Math.sin(Math.PI * 2 - this.rotationRadians)) + sprite.getWidth() / 2;
        double pixelY =  (x * Math.sin(Math.PI * 2 - this.rotationRadians)) + (y * Math.cos(Math.PI * 2 - this.rotationRadians)) + sprite.getHeight() / 2;
        if (pixelX < 0 || pixelY < 0 || pixelX >= sprite.getWidth() || pixelY >= sprite.getHeight()) return false;
        return sprite.getRGB((int) pixelX, (int) pixelY) != 0xffffff;

    }

    protected void updateGuns() {
        gunHolder.update();
    }


    protected void updateMove(){
        double expectedRadians = Math.atan2(flyToTargetY - super.y, flyToTargetX - super.x);
        if (Math.sqrt((this.x - this.flyToTargetX) * (this.x - this.flyToTargetX) + (this.y - this.flyToTargetY) * (this.y - this.flyToTargetY)) < 40) {
            flyToTarget = false;
            return;
        }

        if ((Math.abs(expectedRadians - rotationRadians) > 0.01) && (MathFunctions.rawDistance(rotationRadians, expectedRadians) > (currentRotationSpeed * ((currentRotationSpeed/engine.rotationAccelerationSpeed) / 2))) && ((currentRotationSpeed <= engine.maxRotationSpeed) && (currentRotationSpeed >= -engine.maxRotationSpeed))){
            if (MathFunctions.rawDistance(rotationRadians + currentRotationSpeed + engine.rotationAccelerationSpeed, expectedRadians) < MathFunctions.rawDistance(rotationRadians  + currentRotationSpeed, expectedRadians)){
                currentRotationSpeed += engine.rotationAccelerationSpeed;
            }else {
                currentRotationSpeed -= engine.rotationAccelerationSpeed;
            }
        }else {
            if (currentRotationSpeed > engine.maxRotationSpeed) currentRotationSpeed = engine.maxRotationSpeed;
            if (currentRotationSpeed < -engine.maxRotationSpeed) currentRotationSpeed = -engine.maxRotationSpeed;
            if (currentRotationSpeed != 0){
                if (currentRotationSpeed < 0){
                    if (Math.abs(currentRotationSpeed) < engine.rotationAccelerationSpeed){
                        currentRotationSpeed = 0;
                    }else {
                        currentRotationSpeed += engine.rotationAccelerationSpeed;

                    }
                }else {
                    if (currentRotationSpeed < engine.rotationAccelerationSpeed){
                        currentRotationSpeed = 0;
                    }else {
                        currentRotationSpeed -= engine.rotationAccelerationSpeed;
                    }
                }
            }
        }

        rotationRadians += currentRotationSpeed;

        rotationRadians %= 2*Math.PI;


        if (((MathFunctions.rawDistance(expectedRadians, movingForceDirectionInRadians) < Math.PI / 5) || currentMoveSpeed < 0.5) && (Math.sqrt((this.x - flyToTargetX) * (this.x - flyToTargetX) + (this.y - flyToTargetY) * (this.y - flyToTargetY)) > 5 + (currentMoveSpeed * ((currentMoveSpeed / engine.brakingSpeed) / 2))) && ((currentRotationSpeed <= engine.maxRotationSpeed) && (currentRotationSpeed >= -engine.maxRotationSpeed))) {
            moving = true;
            MathFunctions.addTwoForces(movingForceDirectionInRadians, currentMoveSpeed, rotationRadians, engine.accelerationSpeed);
            movingForceDirectionInRadians = MathFunctions.newForceRadians;
            currentMoveSpeed = MathFunctions.newForceStrength;

        }else {
            if (currentMoveSpeed != 0){
                currentMoveSpeed -= engine.brakingSpeed;
                if (currentMoveSpeed < 0) currentMoveSpeed = 0;
            }
        }


    }


    protected void stop(){
        if (currentRotationSpeed != 0){
            if (currentRotationSpeed < 0){
                if (Math.abs(currentRotationSpeed) < engine.rotationAccelerationSpeed){
                    currentRotationSpeed = 0;
                }else {
                    currentRotationSpeed += engine.rotationAccelerationSpeed;
                }
            }else {
                if (currentRotationSpeed < engine.rotationAccelerationSpeed){
                    currentRotationSpeed = 0;
                }else {
                    currentRotationSpeed -= engine.rotationAccelerationSpeed;
                }
            }
        }
        rotationRadians += currentRotationSpeed;




        if (currentMoveSpeed != 0){
            currentMoveSpeed -= engine.brakingSpeed;
            if (currentMoveSpeed < 0) currentMoveSpeed = 0;
        }
    }

    /**
     * Manual control for players only.
     */
    protected void updateMoveManual() {

        if (this.manualControlLeft && !this.manualControlRight) {
            currentRotationSpeed -= engine.rotationAccelerationSpeed;
        } else if (this.manualControlRight && !this.manualControlLeft) {
            currentRotationSpeed += engine.rotationAccelerationSpeed;
        }else {
            if (currentRotationSpeed != 0){
                if (currentRotationSpeed < 0){
                    if (Math.abs(currentRotationSpeed) < engine.rotationAccelerationSpeed){
                        currentRotationSpeed = 0;
                    }else {
                        currentRotationSpeed += engine.rotationAccelerationSpeed;
                    }
                }else {
                    if (currentRotationSpeed < engine.rotationAccelerationSpeed){
                        currentRotationSpeed = 0;
                    }else {
                        currentRotationSpeed -= engine.rotationAccelerationSpeed;
                    }
                }
            }
        }

        rotationRadians += currentRotationSpeed;


        if (this.manualControlForward) {
            moving = true;
            MathFunctions.addTwoForces(movingForceDirectionInRadians, currentMoveSpeed, rotationRadians, engine.accelerationSpeed);
            movingForceDirectionInRadians = MathFunctions.newForceRadians;
            currentMoveSpeed = MathFunctions.newForceStrength;
        }else {
            if (currentMoveSpeed != 0){
                currentMoveSpeed -= engine.brakingSpeed;
                if (currentMoveSpeed < 0) currentMoveSpeed = 0;
            }
        }
    }

    protected void move() {
        updateCollisionDetection();
        if (currentMoveSpeed > engine.maxMoveSpeed) currentMoveSpeed = engine.maxMoveSpeed;
        this.x += Math.cos(movingForceDirectionInRadians) * currentMoveSpeed;
        this.y += Math.sin(movingForceDirectionInRadians) * currentMoveSpeed;

        if (currentRotationSpeed > engine.maxRotationSpeed) currentRotationSpeed = engine.maxRotationSpeed;
        if (currentRotationSpeed < -engine.maxRotationSpeed) currentRotationSpeed = -engine.maxRotationSpeed;

        rotationRadians %= 2 * Math.PI;
        if (rotationRadians < - Math.PI) rotationRadians = Math.PI + (rotationRadians + Math.PI);
        if (rotationRadians > Math.PI) rotationRadians = - Math.PI + (rotationRadians - Math.PI);
    }


    public void shoot() {
     gunHolder.shoot();
    }

    public void setMove(double x, double y){
        flyToTarget = true;
        flyToTargetX = x;
        flyToTargetY = y;
    }

    public void doDamage(AbstractProjectile bullet, double damage) {
        this.curHP -= damage;
        if (this.curHP <= 0){
            curHP = 0;
            remove();
        }

    }


    public void stopMove(){
        flyToTarget = false;
    }

    public void checkIfDeath() {
        if (this.curHP <= 0) {
            this.owner.checkIfDeath(this);
        }
    }

    public abstract void setWeaponsToDeafultPosition();



}
