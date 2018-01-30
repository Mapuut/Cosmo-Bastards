package client.objects.engines;

public class RedEngine extends AbstractEngine {

    public RedEngine() {
        maxMoveSpeed = 12;
        accelerationSpeed = 0.14;
        brakingSpeed = 0.12;

        rotationAccelerationSpeed = 0.0016;
        maxRotationSpeed = Math.toRadians(12);
    }


}