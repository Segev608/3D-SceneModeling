package Tests.IntegrationTests;

import primitives.*;
import geometries.*;
import org.junit.Test;
import java.util.List;
import java.util.ArrayList;
import static org.junit.Assert.*;

/**
 * Test class to check if the findIntersection() method works correctly
 * in order to perform the composite
 */
public class GeometriesTest {

    /**
     * Test method for {@link Geometries#findIntersections(Ray)}.
     */
    @Test
    public void testFindIntersections() {

        //region Initializing elements
        Polygon polygon = new Polygon(
                new Point3D(-6,-2,0),
                new Point3D(-6,-1,2),
                new Point3D(-6,0,2),
                new Point3D(-6,1,0),
                new Point3D(-6,-0.5,-2));
        Sphere sphere = new Sphere(2.0, new Point3D(-3,3,2));
        Plane plane = new Plane(new Point3D(2,2,0), new Point3D(2,0,2), new Point3D(2,-2,0));
        Cylinder cylinder = new Cylinder(new Ray(new Point3D(4,4,0), new Vector(0,0,3)), 1.0, 3.0);
        Triangle triangle = new Triangle(
                new Point3D(10,10,0),
                new Point3D(10,2,0),
                new Point3D(10,4,5));
        Tube tube = new Tube(3.0, new Ray(new Point3D(18,5,0), new Vector(0,0,10)));
        Ray intersectionRayTest;
        List<Point3D> expectedIntersection;
        Geometries geometriesCollection = new Geometries(polygon, sphere, plane, cylinder, triangle, tube);
        //endregion

        //region ============ Equivalence Partitions Tests ==============

        //region TC01: Some shapes intersected (but not all of them)
        intersectionRayTest = new Ray(new Point3D(-10,-1,1), new Vector(1,0,0));
        expectedIntersection = new ArrayList<>();
        expectedIntersection.add(new Point3D(-6,-1,1));
        expectedIntersection.add(new Point3D(2,-1,1));
        assertEquals("The findIntersection() method in Geometries returned unexpected result " +
                "in case some shapes intersected", expectedIntersection.size(), geometriesCollection.findIntersections(intersectionRayTest).size());
        //endregion

        //endregion

        //region =============== Boundary Values Tests ==================

        //region TC11: Empty shape list
        Geometries emptyShapesCollection = new Geometries();
        // random values - it does not matter - it does not intersect with nothing
        Ray noIntersections = new Ray(new Point3D(1,2,3), new Vector(-1,4,3));
        assertNull("The findIntersection() method in Geometries returned unexpected result " +
                "in case there is no shapes at all", emptyShapesCollection.findIntersections(noIntersections));
        //endregion

        //region TC12: Non of the shape has intersection
        noIntersections = new Ray(new Point3D(-12,0,2), new Vector(0,3,0));
        assertNull("The findIntersection() method in Geometries returned unexpected result " +
                "in case the ray is pointing at different direction at does not intersect at all",
                geometriesCollection.findIntersections(noIntersections));
        //endregion

        //region TC13: Just one shape has intersection
        intersectionRayTest = new Ray(new Point3D(-3,-3,2), new Vector(0,2,0));
        expectedIntersection = new ArrayList<>();
        //intersection with Sphere
        expectedIntersection.add(new Point3D(-3,1,2));
        expectedIntersection.add(new Point3D(-3,5,2));
        assertEquals("The findIntersection() method in Geometries returned unexpected result " +
                "in case the ray and one shape has just one intersection", expectedIntersection.size(),
                geometriesCollection.findIntersections(intersectionRayTest).size());
        //endregion

        //region TC14: All the shapes has intersection
        Polygon polygonUpdated = new Polygon(
                new Point3D(-6,2,0),
                new Point3D(-6,4,3),
                new Point3D(-6,6,3),
                new Point3D(-6,7,0),
                new Point3D(-6,5.5,-2));
        Sphere sphereUpdated = new Sphere(2.0, new Point3D(-3,5,2));
        Cylinder cylinderUpdated = new Cylinder(new Ray(new Point3D(4,5,0),
                new Vector(0,0,3)), 1.0, 3.0);
        Geometries shapeCollection = new Geometries(polygonUpdated, sphereUpdated,
                cylinderUpdated, plane, triangle, tube);

        expectedIntersection = new ArrayList<>();
        expectedIntersection.add(new Point3D(-6,5,2));
        expectedIntersection.add(new Point3D(-5,5,2));
        expectedIntersection.add(new Point3D(-1,5,2));
        expectedIntersection.add(new Point3D(2,5,2));
        expectedIntersection.add(new Point3D(3,5,2));
        expectedIntersection.add(new Point3D(5,5,2));
        expectedIntersection.add(new Point3D(10,5,2));
        expectedIntersection.add(new Point3D(15,5,2));
        expectedIntersection.add(new Point3D(21,5,2));

        intersectionRayTest = new Ray(new Point3D(-14,5,2), new Vector(2,0,0));

        assertEquals("The findIntersection() method in Geometries returned unexpected result " +
                "in case the ray has intersection with all the shapes in the 3D space", expectedIntersection.size(),
                shapeCollection.findIntersections(intersectionRayTest).size());
        //endregion

        //endregion
    }
}