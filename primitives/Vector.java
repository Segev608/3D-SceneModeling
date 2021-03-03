package primitives;

import static primitives.Util.*;

/**
 * Represents a primitives.Vector in the 3D space
 */
public class Vector {
    private Point3D end;

    //region Constructors

    /**
     * Based Coordinate type.
     * @param x value of end point
     * @param y value of end point
     * @param z value of end point
     */
    public Vector(Coordinate x, Coordinate y, Coordinate z) {
        Point3D p = new Point3D(x, y, z);
        if (p.equals(Point3D.ZERO))
            throw new IllegalArgumentException("Vector can not be zero");
        this.end = p;
    }

    /**
     * Based double type.
     * @param x value of end point
     * @param y value of end point
     * @param z value of end point
     */
    public Vector(double x, double y, double z) {
        Point3D p = new Point3D(x, y, z);
        if (p.equals(Point3D.ZERO))
            throw new IllegalArgumentException("Vector can not be zero");
        this.end = p;
    }

    /**
     * Based primitives.Point3D
     * @param end point
     */
    public Vector(Point3D end) {
        if (end.equals(Point3D.ZERO))
            throw new IllegalArgumentException("Vector can not be zero");
        this.end = end;
    }

    /**
     * Copy Constructor
     * @param v primitives.Vector to copy
     */
    public Vector(Vector v) {
        this.end = v.end;
    }
    //endregion

    //region Getter & Overrides

    /**
     * end point getter
     * @return end point
     */
    public Point3D getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return end.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector vector = (Vector) o;
        return end.equals(vector.end);
    }
    //endregion

    //region Math

    /**
     * Adds two Vectors
     * @param other The primitives.Vector to add to This
     * @return The solution primitives.Vector
     */
    public Vector add(Vector other) {
        return new Vector(end.add(other));
    }

    /**
     * Subtract two Vectors
     * @param other The primitives.Vector to subtract from This
     * @return The solution primitives.Vector
     */
    public Vector subtract(Vector other) {
        return end.subtract(other.end);
    }

    /**
     * Scales the primitives.Vector
     * @param a The scalar
     * @return The solution primitives.Vector
     */
    public Vector scale(double a) {
        Vector ret;
        return new Vector(end.getX().get()*a,
                                end.getY().get()*a,
                                end.getZ().get()*a);
    }

    /**
     * Calculating the dot product of two Vectors.
     * @param other The second primitives.Vector
     * @return The solution
     */
    public double dotProduct(Vector other) {
        return end.getX().get() * other.end.getX().get()
                + end.getY().get() * other.end.getY().get()
                + end.getZ().get() * other.end.getZ().get();
    }

    /**
     * Calculating the cross product of two Vectors.
     * @param other The second primitives.Vector
     * @return The solution primitives.Vector
     */
    public Vector crossProduct(Vector other) {
        double v1x = end.getX().get(),
                v1y = end.getY().get(),
                v1z = end.getZ().get(),
                v2x = other.end.getX().get(),
                v2y = other.end.getY().get(),
                v2z = other.end.getZ().get();
        return new Vector(v1y*v2z - v1z*v2y,
                                v1z*v2x - v1x*v2z,
                                v1x*v2y - v1y*v2x);
    }

    /**
     * Calculating the length of the primitives.Vector squared.
     * @return The length Squared
     */
    public double lengthSquared(){
        return end.distanceSquared(Point3D.ZERO);
    }

    /**
     * Calculating the length of the primitives.Vector.
     * @return The length.
     */
    public double length(){
        return Math.sqrt(lengthSquared());
    }

    /**
     * Normalizing this primitives.Vector.
     * @return this
     */
    public Vector normalize(){
        double l = length();
        end = new Point3D(end.getX().get()/l,
                          end.getY().get()/l,
                          end.getZ().get()/l);
        return this;
    }

    /**
     * Normalizing this primitives.Vector.
     * @return A copy of this
     */
    public Vector normalized(){
        return new Vector(normalize());
    }

    /**
     * useful method to rotate vector
     * @param k Orthogonal vector which the rotation is around
     * @param theta rotation rate
     * @return rotated vector around k
     */
    public Vector rotate(Vector k, double theta) {
        //Using this formula from wikipedia in order to rotate vector *V* around other vector *K* by *t*
        // Vrot = Vcost + (KxV)sint + K(KV)(1 - cost)
        double cost = alignZero(Math.cos(theta)); // both sin() and cos() are resets in pi*(0/(pi/2)/pi...)
        double sint = alignZero(Math.sin(theta));
        Vector vcost = isZero(cost)? null : scale(cost);
        Vector kvsint = isZero(sint)? null : k.crossProduct(this).scale(sint);
        Vector kkv;
        try {
            kkv = k.scale(alignZero(k.dotProduct(this)*(1- cost)));
        }
        catch (IllegalArgumentException e) {
            kkv = null;
        }
        Point3D endrot = Point3D.ZERO;
        endrot = vcost == null? endrot : endrot.add(vcost);
        endrot = kvsint == null? endrot : endrot.add(kvsint);
        endrot = kkv == null? endrot : endrot.add(kkv);
        return new Vector(endrot);
    }

    /**
     * dotProduct(VecA, VecB) is 0 when VecA is orthogonal to VecB
     * @return orthogonal vector to the current vector
     */
    public Vector getOrthogonal() {
        // Ax+By+Cz+D=0 - The plane equation
        // (A,B,C) - The normal to the plane
        // by using this formula, it is possible to extract the orthogonal
        // to this vector
        double vx = end.getX().get();
        double vy = end.getY().get();
        double vz = end.getZ().get();
        if(!isZero(vx)) {
            double coordx = -(vy + vz)/vx;
            return new Vector(coordx, 1,1).normalize();
        }
        if(!isZero(vy)) {
            double coordy = -(vx + vz)/vy;
            return new Vector(1, coordy,1).normalize();
        }
        double coordz = -(vy + vx)/vz;
        return new Vector(1, 1,coordz).normalize();
    }
    //endregion

}
