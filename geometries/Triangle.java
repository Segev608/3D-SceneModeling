package geometries;

import primitives.*;

/**
 * Represents a triangle in the 3D space
 */
public class Triangle extends Polygon {

    //region Constructors

    /**
     * @param p1 First vertex of the triangle
     * @param p2 Second vertex of the triangle
     * @param p3 Third vertex of the triangle
     */
    public Triangle(Point3D p1, Point3D p2, Point3D p3) {
        super(p1, p2, p3);
    }

    /**
     * @param p1 First vertex of the triangle
     * @param p2 Second vertex of the triangle
     * @param p3 Third vertex of the triangle
     * @param color The emission
     */
    public Triangle(Color color, Point3D p1, Point3D p2, Point3D p3) {
        super(color, p1, p2, p3);
    }

    /**
     * Constructor
     * @param color The emission
     * @param p1 First vertex of the triangle
     * @param p2 Second vertex of the triangle
     * @param p3 Third vertex of the triangle
     * @param material the Triangle self-material
     */
    public Triangle(Color color,Material material, Point3D p1, Point3D p2, Point3D p3){
        this(color, p1, p2, p3);
        this.material = material;
    }
    //endregion

    //region Overrides

    @Override
    public String toString() {
        return "vertices=" + _vertices + ", plane=" + _plane.toString();
    }

    // There is no need to implement findIntersections(Ray) (and getNormal()) because it's already
    // implemented in Polygon class (super).

    //endregion
}
