package geometries;

import primitives.*;
import java.util.List;
import java.util.ArrayList;
import static primitives.Util.*;
/**
 * Represents a tube
 */
public class Tube extends RadialGeometry {

    protected Ray axisRay;

    //region Constructors
    
    /**
     * Constructor
     * @param radius the double value for the RadialGeometry base class variable
     * @param axisRay the Ray value
     */
    public Tube(double radius, Ray axisRay){
        super(radius);
        this.axisRay = new Ray(axisRay);
    }

    /**
     * Constructor
     * @param radius the double value for the RadialGeometry base class variable
     * @param axisRay the Ray value
     * @param color The emission
     */
    public Tube(double radius, Ray axisRay, Color color){
        super(radius,color);
        this.axisRay = new Ray(axisRay);
    }

    /**
     * Constructor
     * @param radius the double value for the RadialGeometry base class variable
     * @param axisRay the Ray that define the Tube value
     * @param color The emission
     * @param material the Tube self-material
     */
    public Tube(double radius, Ray axisRay, Color color, Material material){
        this(radius, axisRay, color);
        this.material = material;
    }
    //endregion

    //region Getter & Overrides

    @Override
    public Vector getNormal(Point3D point3D) {
        Vector other = point3D.subtract(axisRay.getStart());
        double mag = axisRay.getDirection().dotProduct(other);
        Point3D o = mag==0? axisRay.getStart(): axisRay.getStart().add(axisRay.getDirection().scale(mag));
        Vector ret = point3D.subtract(o);
        return ret.normalize();
    }

    @Override
    public String toString() {
        return super.toString() + ", axisRay=" + axisRay;
    }

    @Override
    public List<GeoPoint> findIntersections(Ray ray) {

        Point3D start = ray.getStart();
        Vector dir = ray.getDirection();
        // P = (x,y,z) is on the tube if |(P - O)xD|^2 = r^2 where O is a point on the center line of the tube,
        // and D is the direction of the tube.
        // Any point on the ray sustains S + tV (t>=0)
        // So, P = S + tV => |((S - O)+tV)xD|^2 = r^2 => |(S - O)xD + t(VxD)|^2 = r^2
        // E = (S - O)xD, K = VxD => |E + tK|^2 = r^2 =>
        // t^2(|K|^2) + t(2E*K) + (|E|^2 - r^2) = 0
        // This is the equation we are solving for t.

        Vector K;
        try {
           K = dir.crossProduct(axisRay.getDirection());
        } catch (IllegalArgumentException e) {
            return null; // Ray is parallel to the center line
        }
        Vector E;
        try {
            Vector temp = start.subtract(axisRay.getStart());
            E = temp.crossProduct(axisRay.getDirection());
        } catch (IllegalArgumentException e) {
            // Same equation, but E = Vector0. eventually gives:
            double t = getRadius()/K.length();
            List<GeoPoint> ret = new ArrayList<>();
            ret.add(new GeoPoint(this,start.add(dir.scale(t))));
            return ret;
        }

        double a = K.lengthSquared();
        double b = 2 * K.dotProduct(E);
        double c = E.lengthSquared() - getRadiusSquared();
        double delta = alignZero(b*b - 4*a*c);
        if (delta <= 0) //Line not intersecting or tangent (delta == 0)
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
