package client.objects.engines;


public class GreenEngine extends AbstractEngine {
    public GreenEngine() {
        maxMoveSpeed = 8;
        accelerationSpeed = 0.1;
        brakingSpeed = 0.08;

        rotationAccelerationSpeed = 0.0012;
        maxRotationSpeed = Math.toRadians(8);
    }


}
