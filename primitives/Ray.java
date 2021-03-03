package primitives;

import java.util.Comparator;
import java.util.List;
import static geometries.Intersectable.GeoPoint;

/**
 * set of points on line
 */
public class Ray {
    private Point3D start;
    private Vector direction;

    /**
     * In order to determine the amount of moving, shadow and reflection
     */
    private static final double DELTA = 0.1;

    //region Constructors

    /**
     * Constructor
     * @param start the beginning of the Ray
     * @param direction the direction of the Ray
     */
    public Ray(Point3D start, Vector direction){
        this.start = new Point3D(start);
        this.direction = direction.normalized();
    }

    /**
     * Copy constructor
     * @param other item to copy
     */
    public Ray(Ray other){
        this.start = new Point3D(other.start);
        this.direction = new Vector(other.direction);
    }

    /**
     * Because there is need to move the ray, this is new constructor to handle this operation
     * @param originP the point where the movement begin
     * @param direction the direction of the ray which does not change in this calculation
     * @param normal the normal to the surface
     */
    public Ray(Point3D originP, Vector direction, Vector normal){
        // Implementing the fix in the correct way
        // The amount of movement down/up
        Vector deltaVector = normal.scale(normal.dotProduct(direction) > 0 ? DELTA : -DELTA);

        // Initializing start
        this.start = originP.add(deltaVector);

        // There was no movement in the direction, just in the position
        this.direction = new Vector(direction).normalized();
    }
    //endregion

    //region Getters & Overrides

    /**
     * @return the value of the start Point
     */
    public Point3D getStart() {
        return new Point3D(start);
    }

    /**
     * @return the value of the direction vector
     */
    public Vector getDirection() {
        return new Vector(direction);
    }


    @Override
    public String toString() {
        return "start=" + start + ", direction=" + direction;
    }

    /**
     * @param obj the variable that being equalized
     * @return if obj and this are equals
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Ray ray = (Ray) obj;
        return start.equals(ray.start) && direction.equals(ray.direction);
    }
    //endregion

    // region Helpful functions

    /**
     * Ray = Start + t * Direction, each point on the ray has its own 't'.
     * @param p the point it calculates on (assuming p is on the ray)
     * @return t value of p
     */
    public double getT(Point3D p) {
        // (Xs, Ys, Zs) + t(Xd, Yd, Zd) = (Xs + tXd, Ys + tYd, Zs + tZd) = (Xp, Yp, Zp)
        double rayDirX = direction.getEnd().getX().get();
        if (rayDirX != 0)
            return (p.getX().get() - start.getX().get())/ rayDirX;
        double rayDirY = direction.getEnd().getY().get();
        if (rayDirY != 0)
            return (p.getY().get() - start.getY().get())/ rayDirY;
        double rayDirZ = direction.getEnd().getZ().get();
        return (p.getZ().get() - start.getZ().get())/ rayDirZ;
    }

    /**
     * Sorting by t, means sorting according to the distance between the point and the start.
     * as the t gets bigger, the point gets further from the start.
     * @param points List of points on the ray
     */
    public void sortPointByT(List<GeoPoint> points){
        if (points == null)
            return;
        points.sort(Comparator.comparingDouble(p -> getT(p.point)));
    }
    //endregion

}
