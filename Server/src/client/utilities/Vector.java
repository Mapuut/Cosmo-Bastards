package client.utilities;

/**
 * Created by.
 */
public class Vector {
    //Newton 2nd law F=ma
    //F and a represent vectors.
    //Fx=max Fy=may

    public double x;
    public double y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector add(Vector a) {
        return new Vector(a.x + this.x, a.y + this.y);
    }

    public Vector subtract(Vector a) {
        return new Vector(this.x - a.x, this.y - a.y);
    }

    public Vector scale(double s) {
        return new Vector(this.x * s, this.y * s);
    }

    public double cross(Vector a) {
        return (this.x * a.y - this.y * a.x);
    }

    public Vector crossPerpendicular(double angle) {
        return new Vector(this.y * angle, this.x * angle);
    }

    public double dot(Vector a) {
        return (this.x * a.x + this.y * a.y);
    }

    public double length() {
        return Math.sqrt(this.x*this.x + this.y*this.y);
    }


    public Vector rotate(double angle, Vector vector) {
        double x = this.x - vector.x;
        double y = this.y - vector.y;

        double x_prime = vector.x + ((x * Math.cos(angle)) - (y * Math.sin(angle)));
        double y_prime = vector.y + ((x * Math.sin(angle)) + (y * Math.cos(angle)));

        return new Vector(x_prime, y_prime);
    }



}
