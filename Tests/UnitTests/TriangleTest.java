package Tests.UnitTests;

import primitives.*;
import geometries.Triangle;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

import static geometries.Intersectable.GeoPoint.convertPointsToGPoints;

/**
 * Unit tests for geometries.Triangle class
 */
public class TriangleTest {

    /**
     * Test method for {@link geometries.Triangle#getNormal(Point3D)}.
     */
    @Test
    public void testGetNormal() {

        // ============ Equivalence Partitions Tests ==============
        // Test that the getNormal in the Triangle class returns the correct normal

        //let's generate some points that lives in the same plane
        //we'll calculate for them vectors and from them - Normal

        Point3D firstInPlane = new Point3D(1,2,-3);
        Point3D secondInPlane = new Point3D(-5,7,-4);
        Point3D thirdInPlane = new Point3D(2,-4,9);

        // First Vector is (1,-6,12)
        // Second Vector is (7,-11,13)
        // So, The correct Vector (1,-6,12)x(7,-11,13) => (54,71,31)

        Vector correctNormal = new Vector(54,71,31);
        correctNormal.normalize();

        Triangle checkTriangle = new Triangle(firstInPlane, secondInPlane, thirdInPlane);

        // Because of two-dimensional shapes returns the normal to their plane
        // there is no need to give point as input, we've inserted ZERO Vector as default
        Vector checkNormal = checkTriangle.getNormal(Point3D.ZERO);

        // Test result
        assertEquals("getNormal() gave unexpected result", correctNormal, checkNormal);
    }

    /**
     * Test method for {@link geometries.Triangle#findIntersections(Ray)}.
     */
    @Test
    public void testFindIntersections(){
        //Initializing Resources
        Triangle triangle;
        Ray testedRay;
        List<Point3D> expectedIntersection;

        //region ============ Equivalence Partitions Tests ==============

        //region TC01: one intersection [the ray starts inside & above]
        triangle = new Triangle(
                new Point3D(0,-2,0),
                new Point3D(-2,0,0),
                new Point3D(-2,-2,0));
        testedRay = new Ray(new Point3D(-1.5,-1,2), new Vector(0,0,-1));
        expectedIntersection = new ArrayList<>();
        expectedIntersection.add(new Point3D(-1.5,-1,0));
        assertEquals("The findIntersection() returned unexpected result" +
                " in case the ray is starting inside the shape and intersecting with the Triangle's plane",convertPointsToGPoints(triangle, expectedIntersection), triangle.findIntersections(testedRay));
        //endregion

        //region TC02: non intersection [the ray starts next to one of the vertices & under]
        //using the executeTest1 triangle - same position in the 3D space
        testedRay = new Ray(new Point3D(-3,-3,-1), new Vector(0,0,2));
        assertNull("The findIntersections() method returned unexpected results in case the ray is" +
                "just intersecting the triangle plane next to vertex", triangle.findIntersections(testedRay));
        //endregion

        //region TC03: non intersection [the ray starts next to the triangle edge]
        testedRay = new Ray(new Point3D(-3,-1,-1), new Vector(0,0,2));
        assertNull("The findIntersection() method returned unexpected result in case the ray is" +
                " starting next to one of the triangle's edges", triangle.findIntersections(testedRay));
        //endregion

        //endregion

        //region =============== Boundary Values Tests ==================

        //region Group: The ray begins at the plane

        //region TC11: non intersection [the ray starts on the plane - on the edge]
        testedRay = new Ray(new Point3D(-1,-1,0), new Vector(0,0,3));
        assertNull("The findIntersection() method returned unexpected result in case the " +
                "ray is starting on the triangle plane - on the edge", triangle.findIntersections(testedRay));
        //endregion

        //region TC12: non intersection [the ray starts on the plane - on the vertex]
        testedRay = new Ray(new Point3D(0,-2,0), new Vector(0,0,-2));
        assertNull("The findIntersection() method returned unexpected result in case the " +
                "ray is starting on the triangle plane - on the vertex", triangle.findIntersections(testedRay));
        //endregion

        //region TC13: non intersection [the ray starts on the continue of the edge]
        testedRay = new Ray(new Point3D(-3,-2,0), new Vector(0,0,-3));
        assertNull("The findIntersection() method returned unexpected result in case the " +
                "ray is starting on the triangle plane - on the continue of the edge", triangle.findIntersections(testedRay));
        //endregion

        //endregion

        //region Group: The ray begins before the plane

        //region TC14: non intersection [the ray starting above the triangle's plane - on the vertex]
        triangle = new Triangle(new Point3D(5,0,0), new Point3D(0,2,0), new Point3D(1,0,0));
        testedRay = new Ray(new Point3D(5,0,2), new Vector(0,0,-2));
        assertNull("The findIntersections() method returned unexpected results in case the ray" +
                " is starting above the triangle's plane - on the vertex", triangle.findIntersections(testedRay));
        //endregion

        //region TC15: non intersection [the ray starting above the triangle's plane - on the edge]
        testedRay = new Ray(new Point3D(3,0,-2), new Vector(0,0,2));
        assertNull("The findIntersections() method returned unexpected results in case the ray" +
                " is starting above the triangle's plane - on the edge", triangle.findIntersections(testedRay));
        //endregion

        //region TC16: non intersection [the ray starting above the triangle's plane - on the continue of edge]
        testedRay = new Ray(new Point3D(6,0,2), new Vector(0,0,-2));
        assertNull("The findIntersections() method returned unexpected results in case the ray" +
                " is starting above the triangle's plane - on the continue of the vertex", triangle.findIntersections(testedRay));
        //endregion

        //endregion

        //region Group: The ray begins at the plane

        //region TC17: non intersection [the ray starting at the triangle's plane - on the vertex]
        testedRay = new Ray(new Point3D(5,0,0), new Vector(0,0,-2));
        assertNull("The findIntersections() method returned unexpected results in case the ray" +
                " is starting at the triangle's plane - on the vertex", triangle.findIntersections(testedRay));
        //endregion

        //region TC18: non intersection [the ray starting at the triangle's plane - on the edge]
        testedRay = new Ray(new Point3D(3,0,0), new Vector(0,0,2));
        assertNull("The findIntersections() method returned unexpected results in case the ray" +
                " is starting at the triangle's plane - on the edge", triangle.findIntersections(testedRay));
        //endregion

        //region TC19: non intersection [the ray starting at the triangle's plane - on the continue of the edge]
        testedRay = new Ray(new Point3D(6,0,0), new Vector(0,0,-2));
        assertNull("The findIntersections() method returned unexpected results in case the ray" +
                " is starting above the triangle's plane - on the continue of the vertex", triangle.findIntersections(testedRay));
        //endregion

        //endregion

        //endregion

    }
}