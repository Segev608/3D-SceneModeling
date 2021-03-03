package geometries;

import primitives.*;
import java.util.List;
import java.util.LinkedList;

/**
 * Represents a Plane in the 3D space
 */
public class Plane extends Geometry {

    private Point3D point;
    private Vector normal;

    //region Constructors

    /**
     * Constructor
     * @param point  A point on the plane
     * @param normal The normal of the plane
     */
    public Plane(Point3D point, Vector normal) {
        this.point = point;
        this.normal = normal;
    }

    /**
     * Constructor
     * @param p1 A point on the plane
     * @param p2 A point on the plane
     * @param p3 A point on the plane
     */
    public Plane(Point3D p1, Point3D p2, Point3D p3) {
        // In order to find the normal, let's find two vectors
        // and the cross product between them would be the normal vector

        //initializing point
        point = new Point3D(p1);

        Vector firstVector = p3.subtract(p1);
        Vector secondVector = p3.subtract(p2);

        this.normal = (firstVector.crossProduct(secondVector)).normalize();
    }

    /**
     * Constructor
     * @param point A point on the plane
     * @param normal The normal of the plane
     * @param color The emission
     */
    public Plane(Point3D point, Vector normal, Color color) {
        this(point, normal);
        emission = color;
    }

    /**
     * Constructor
     * @param p1 A point on the plane
     * @param p2 A point on the plane
     * @param p3 A point on the plane
     * @param color The emission
     */
    public Plane(Point3D p1, Point3D p2, Point3D p3, Color color) {
        this(p1, p2, p3);
        emission = color;
    }

    /**
     * Constructor
     * @param point A point on the plane
     * @param normal The normal of the plane
     * @param color The emission
     * @param material The shape self-material
     */
    public Plane(Point3D point, Vector normal, Color color, Material material) {
        this(point, normal, color);
        this.material = material;
    }

    /**
     * Constructor
     * @param p1 First point
     * @param p2 Second point
     * @param p3 Third point
     * @param color Shape color
     * @param material The shape self-material
     */
    public Plane(Point3D p1, Point3D p2, Point3D p3, Color color, Material material) {
        this(p1, p2, p3, color);
        this.material = material;
    }
    //endregion

    //region Getters & Overrides

    /**
     * point getter
     * @return point
     */
    public Point3D getPoint() {
        return point;
    }

    /**
     * normal getter
     * @return normal
     */
    public Vector getNormal() {
        return normal;
    }

    @Override
    public String toString() {
        return "point=" + point +
                ", normal=" + normal;
    }

    @Override
    public Vector getNormal(Point3D point3D) {
        return normal;
    }

    @Override
    public List<GeoPoint> findIntersections(Ray ray) {
        // ===== Equation Explanation =====
        // N - plane's normal, Q - point on the plane, E - the start of the ray, D - the ray's direction
        // numerator = N * (Q-E) [The multiplication here is dot product]
        // denominator = N * D
        // t = numerator / denominator
        // Definition: if t<0 ==> the ray is pointing toward the opposite direction [and does not intersect with the plane]
        //             if t>=0 ==> there is one intersection, and the point is E+(t*D)
        // In case the dot product of the ray's direction and the Plane's normal returns 0
        // it means they orthogonal and the ray is parallel to the Plane
        if (point.equals(ray.getStart()))
            return null;
        Vector checkZeroPoint = point.subtract(ray.getStart());;

        double numerator = Util.alignZero(normal.dotProduct(checkZeroPoint));
        double denominator = Util.alignZero(normal.dotProduct(ray.getDirection()));
        // in order not to divide by zero
        if(denominator == 0) return null;

        double define = (numerator / denominator);
        if (define <= 0)
            return null;
        // one intersection
        List<GeoPoint> intersection = new LinkedList<>();
        Point3D intersectionPoint = ray.getStart().add(ray.getDirection().scale(define));
        intersection.add(new GeoPoint(this, intersectionPoint));
        return intersection;
    }
    //endregion
}

