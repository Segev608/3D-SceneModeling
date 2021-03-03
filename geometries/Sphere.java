package geometries;

import primitives.*;
import java.util.List;
import java.util.ArrayList;
import static primitives.Util.*;

/**
 * Represents a sphere
 */
public class Sphere extends RadialGeometry{
    private Point3D center;

    //region Constructors

    /**
     * Constructor
     * @param radius The double value for the RadialGeometry base class variable
     * @param center The center of the sphere
     */
    public Sphere(double radius, Point3D center){
        super(radius);
        this.center = center;
    }

    /**
     * Constructor
     * @param radius the double value for the RadialGeometry base class variable
     * @param center The center of the sphere
     * @param color The emission
     */
    public Sphere(double radius, Point3D center, Color color){
        super(radius, color);
        this.center = center;
    }

    /**
     * Constructor
     * @param radius The double value for the RadialGeometry base class variable
     * @param center The center of the sphere
     * @param color The emission
     * @param material The sphere self-material
     */
    public Sphere(Color color, Material material, double radius, Point3D center){
        this(radius, center, color);
        this.material = material;
    }
    //endregion

    //region Getters & Overrides

    /**
     * @return the value of the Point3D center variable
     */
    public Point3D getCenter() {
        return center;
    }

    @Override
    public Vector getNormal(Point3D point3D) {
        Vector ret = point3D.subtract(center);
        return ret.normalize();
    }

    @Override
    public String toString() {
        return super.toString() + ", center=" + center;
    }

    @Override
    public List<GeoPoint> findIntersections(Ray ray) {
        Vector dir = ray.getDirection();
        Point3D start = ray.getStart();
        // (x,y,z) is on the sphere if  P*P = r^2 where P = (x,y,z) - center
        // Any point on the ray sustains S + tD (t>=0)
        // So, P = S + tD - center = E + tD (E = S - center)
        // (E + tD)*(E + tD) = r^2 => t^2(D*D) + t(2E*D) + (E*E - r^2) = 0
        // This is the equation we are solving for t.

        // if center==start, then E == Vector0 and that's an error.
        // The same equation substitutes to t^2 = r^2/D*D => t = r/length(D)
        if (center.equals(start)) {
            double t = getRadius() / (dir.length());
            List<GeoPoint> ret = new ArrayList<>();
            ret.add(new GeoPoint(this, start.add(dir.scale(t))));
            return ret;
        }

        Vector e = (start.subtract(center));
        double a = dir.lengthSquared();
        double b = 2 * e.dotProduct(dir);
        double c = e.lengthSquared() - getRadiusSquared();
        double delta = alignZero(b*b - 4*a*c);
        if (delta <= 0) //Ray's line not intersecting or tangent (delta == 0)
            return null;
        double sDelta = Math.sqrt(delta);
        double t1 = alignZero((-b+sDelta)/(2*a));
        double t2 = alignZero((-b-sDelta)/(2*a));
        if (t1<=0) return null; // Both t1, t2 are non-positive case

        List<GeoPoint> ret = new ArrayList<>();
        ret.add(new GeoPoint(this, start.add(dir.scale(t1))));
        if (t2 > 0)
            ret.add(new GeoPoint(this, start.add(dir.scale(t2))));
        return ret;
    }
    //endregion
}
