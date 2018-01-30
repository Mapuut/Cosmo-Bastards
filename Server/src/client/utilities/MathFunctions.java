package client.utilities;


public final class MathFunctions {

    /**
     * Calculates a raw distance between 2 dots on a circle.
     *
     * @param radian1 first dot radians.
     * @param radian2 second dot radians.
     * @return minimal distance in radians between two dots.
     */
    public static double rawDistance(double radian1, double radian2) {
        double rawDist = radian1 > radian2 ? radian1 - radian2 : radian2 - radian1;
        rawDist %= 2 * Math.PI;
        return rawDist > Math.PI ? Math.PI * 2 - rawDist : rawDist;
    }


    /**
     * Result new Force direction from method addTwoForces.
     */
    public static double newForceRadians = 0;
    /**
     * Result new Force strength from method addTwoForces.
     */
    public static double newForceStrength = 0;

    /**
     * Adds 2 forces together.
     *
     * @param firstForceRadians   first direction.
     * @param firstForceStrength  first force in N.
     * @param secondForceRadians  second direction.
     * @param secondForceStrength secod force in N.
     */
    public static void addTwoForces(double firstForceRadians, double firstForceStrength, double secondForceRadians, double secondForceStrength) {
        double x = Math.cos(firstForceRadians) * firstForceStrength + Math.cos(secondForceRadians) * secondForceStrength;
        double y = Math.sin(firstForceRadians) * firstForceStrength + Math.sin(secondForceRadians) * secondForceStrength;
        newForceStrength = Math.sqrt(x * x + y * y);
        newForceRadians = Math.atan2(y, x);
    }


}
