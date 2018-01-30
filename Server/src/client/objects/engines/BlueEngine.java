package client.objects.engines;


public class BlueEngine extends AbstractEngine {

    public BlueEngine() {
        maxMoveSpeed = 10;
        accelerationSpeed = 0.12;
        brakingSpeed = 0.1;

        rotationAccelerationSpeed = 0.0014;
        maxRotationSpeed = Math.toRadians(10);
    }


}