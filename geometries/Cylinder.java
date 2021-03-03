package geometries;

import primitives.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Represents a cylinder
 */
public class Cylinder extends Tube {
    private double height;

    //region Constructors

    /**
     * Constructor
     * @param radius the double value for the RadialGeometry base class variable
     * @param height value to determine the value of the Cylinder
     */
    public Cylinder(Ray ray, double radius, double height){
        super(radius, ray);
        this.height = height;
    }

    /**
     * Constructor
     * @param radius the double value for the RadialGeometry base class variable
     * @param height value to determine the value of the Cylinder
     * @param color The emission
     */
    public Cylinder(Ray ray, double radius, double height, Color color){
        super(radius, ray, color);
        this.height = height;
    }

    /**
     * Constructor
     * @param tube The tube base of the cylinder
     * @param height The height of the cylinder
     */
    public Cylinder(Tube tube, double height){
        super(tube.getRadius(), tube.axisRay, tube.emission);
        this.height = height;
    }

    /**
     * Constructor
     * @param ray The ray that define the Cylinder direction
     * @param radius The radius that define the Cylinder size
     * @param height Finite height of the Cylinder
     * @param color define the shape color
     * @param material define the shape material (it's self-color)
     */
    public Cylinder(Ray ray, double radius, double height, Color color, Material material){
        this(ray, radius, height, color);
        this.material = material;
    }
    //endregion

    //region Getters & Overrides

    /**
     * @return the value of the height variable
     */
    public double getHeight() {
        return height;
    }

    @Override
    public Vector getNormal(Point3D pointOnSurface) {
        // ======== Explain how the calculation going to be execute [BONUS] ========
        // we are taking the given point and subtract it from the start point of the ray
        // that, gives us a vector from the start ray position to the given point.
        // now, we are calculating the dot product between the vector to the ray
        // and that, gives us a number -
        // 1) if the number is greater then the upper base [height variable] -> just return the direction of the ray
        // 2) if the number is less then the lower base -> return the opposite direction of the ray
        // 3) in case the number is inside this interval -> it can be simplify and calculated by the Tube.getNormal()
        // due to the fact that this is a "final distance"

        Vector tempVector = pointOnSurface.subtract(axisRay.getStart());
        double projectionValue = tempVector.dotProduct(axisRay.getDirection());

        if(projectionValue >= height)
            return axisRay.getDirection();
        if(projectionValue <= 0)
            return axisRay.getDirection().scale(-1.0);

        return super.getNormal(pointOnSurface);
    }

    @Override
    public String toString() {
        return super.toString() + ", height=" + height;
    }

    @Override
    public List<GeoPoint> findIntersections(Ray ray) {
        // Workflow: check for intersections with the tube, then determine if the points are in the cylinder case.
        // If so, add the point to the returned list. if not, ignore that point.
        // Then, check for intersections in each base with the help of findBaseIntersection function.
        // Add the points that were found to the returned list, sort it by t, and return it.
        List<GeoPoint> cylinderCaseIntersections = super.findIntersections(ray);
        if (cylinderCaseIntersections != null) {
            cylinderCaseIntersections.removeIf(p -> {
                double test = axisRay.getDirection().dotProduct(p.point.subtract(axisRay.getStart()));
                return !(test > 0 && test < height);
            });
        }
        GeoPoint base1 = findBaseIntersection(axisRay.getStart(), ray);
        GeoPoint base2 = findBaseIntersection(axisRay.getStart().add(axisRay.getDirection().scale(height)), ray);
        if (cylinderCaseIntersections == null || cylinderCaseIntersections.isEmpty())
            if(base1 == null && base2 == null)
                return null;
        // There is at least one point
        List<GeoPoint> ret = new ArrayList<>();
        if (cylinderCaseIntersections != null)
            for(GeoPoint geoP : cylinderCaseIntersections)
                ret.add(new GeoPoint(this, geoP.point));
        if (base1 != null) ret.add(base1);
        if (base2 != null) ret.add(base2);
        ray.sortPointByT(ret);
        return ret;
    }

    //endregion

    // region Private functions

    /**
     * Finds Base Intersection with a given ray
     * @param center the center of the base
     * @param ray The given ay
     * @return The intersection
     */
    private GeoPoint findBaseIntersection (Point3D center, Ray ray) {
        // Workflow: check for intersection with the plane that contains the base.
        // if there is no intersection, return null.
        // if there is, determine if this point is in the circle by
        // simply checking if the distance between the point and the center of the circle
        // is grater than the radius of the circle.
        Plane plane = new Plane(center, axisRay.getDirection());
        List<GeoPoint> planeIntersections = plane.findIntersections(ray);
        if (planeIntersections == null) return null;
        GeoPoint ret = planeIntersections.get(0);
        ret.geometry = this;
        double dist = ret.point.distance(center);
        if (dist >= getRadius()) return null;
        return ret;
    }

    //endregion

}
