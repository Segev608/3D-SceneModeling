package Tests.UnitTests;

import primitives.*;
import org.junit.Test;
import java.util.List;
import java.util.ArrayList;
import geometries.Polygon;
import static org.junit.Assert.*;
import static geometries.Intersectable.GeoPoint.convertPointsToGPoints;

/**
 * Testing Polygons
 * @author Dan
 *
 */
public class PolygonTests {

    /**
     * Test method for* {@link geometries.Polygon(primitives.Point3D, primitives.Point3D, primitives.Point3D, primitives.Point3D)}.
     */
    @Test
    public void testConstructor() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Correct concave quadrangular with vertices in correct order
        try {
            new Polygon(new Point3D(0, 0, 1), new Point3D(1, 0, 0),
                    new Point3D(0, 1, 0), new Point3D(-1, 1, 1));
        } catch (IllegalArgumentException e) {
            fail("Failed constructing a correct polygon");
        }

        // TC02: Wrong vertices order
        try {
            new Polygon(new Point3D(0, 0, 1), new Point3D(0, 1, 0),
                    new Point3D(1, 0, 0), new Point3D(-1, 1, 1));
            fail("Constructed a polygon with wrong order of vertices");
        } catch (IllegalArgumentException e) {}

        // TC03: Not in the same plane
        try {
            new Polygon(new Point3D(0, 0, 1), new Point3D(1, 0, 0),
                    new Point3D(0, 1, 0), new Point3D(0, 2, 2));
            fail("Constructed a polygon with vertices that are not in the same plane");
        } catch (IllegalArgumentException e) {}

        // TC04: Concave quadrangular
        try {
            new Polygon(new Point3D(0, 0, 1), new Point3D(1, 0, 0),
                    new Point3D(0, 1, 0), new Point3D(0.5, 0.25, 0.5));
            fail("Constructed a concave polygon");
        } catch (IllegalArgumentException e) {}

        // =============== Boundary Values Tests ==================

        // TC10: Vertix on a side of a quadrangular
        try {
            new Polygon(new Point3D(0, 0, 1), new Point3D(1, 0, 0),
                    new Point3D(0, 1, 0), new Point3D(0, 0.5, 0.5));
            fail("Constructed a polygon with vertix on a side");
        } catch (IllegalArgumentException e) {}

        // TC11: Last point = first point
        try {
            new Polygon(new Point3D(0, 0, 1), new Point3D(1, 0, 0),
                    new Point3D(0, 1, 0), new Point3D(0, 0, 1));
            fail("Constructed a polygon with vertice on a side");
        } catch (IllegalArgumentException e) {}

        // TC12: Collocated points
        try {
            new Polygon(new Point3D(0, 0, 1), new Point3D(1, 0, 0),
                    new Point3D(0, 1, 0), new Point3D(0, 1, 0));
            fail("Constructed a polygon with vertice on a side");
        } catch (IllegalArgumentException e) {}

    }

    /**
     * Test method for {@link geometries.Polygon#getNormal(primitives.Point3D)}.
     */
    @Test
    public void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: There is a simple single test here
        Polygon pl = new Polygon(new Point3D(0, 0, 1), new Point3D(1, 0, 0), new Point3D(0, 1, 0),
                new Point3D(-1, 1, 1));
        double sqrt3 = Math.sqrt(1d / 3);
        assertEquals("Bad normal to triangle", new Vector(sqrt3, sqrt3, sqrt3), pl.getNormal(new Point3D(0, 0, 1)));
    }

    /**
     * Test method for* {@link geometries.Polygon#findIntersections(Ray)}.
     */
    @Test
    public void testFindIntersection(){
        //region  ============ Equivalence Partitions Tests ==============

        //region TC01: The Ray and the Polygon intersects
        // Polygon with 5 Vertices (if it works with 5 it could handle more...)
        Polygon intersectionPentagon = new Polygon(
                new Point3D(3,3,0),
                new Point3D(4,0,0),
                new Point3D(2,0,0),
                new Point3D(-1,1,0),
                new Point3D(1,3,0));

        // Ray initialization
        Ray intersectionRay = new Ray(new Point3D(0,0,4), new Vector(2,2,-4));

        // create an intersection list which contain the expected points from
        // the findIntersection() method in Polygon

        List<Point3D> expectedIntersections = new ArrayList<>();
        expectedIntersections.add(new Point3D(2,2,0));

        // Test result - comparing two lists using the equals method
        // [checks that all corresponding pairs of elements in the two lists are equal.]
        assertEquals("The findIntersections() method returned unexpected result",
               convertPointsToGPoints(intersectionPentagon, expectedIntersections), intersectionPentagon.findIntersections(intersectionRay));
        //endregion

        //region TC02: The Ray and the Polygon has no intersections
        //Initializing an octagon in the 3D space
        Polygon noIntersectionOctagon = new Polygon(
                new Point3D(-1,-3,0),
                new Point3D(-1,-5,0),
                new Point3D(-2,-6,0),
                new Point3D(-3,-6,0),
                new Point3D(-4,-5,0),
                new Point3D(-4,-3,0),
                new Point3D(-3,-2,0),
                new Point3D(-2,-2,0));
        // Initializing the parallel Ray to the Polygon plane
        Ray parallelRay = new Ray(new Point3D(0,-4,0), new Vector(0,4,1));

        // Empty list - no intersections
        List<Point3D> expectedNoIntersection = null;

        //Test result
        assertEquals("The findIntersections() should return 0 point",
                expectedNoIntersection, noIntersectionOctagon.findIntersections(parallelRay));
        //endregion

        //endregion

        //region =============== Boundary Values Tests ==================

        //region Initializing a Pentagon to the BVT tests
        Polygon polygonBVT = new Polygon(
                new Point3D(0,5,0),
                new Point3D(0,2,0),
                new Point3D(-2,0,0),
                new Point3D(-5,0,0),
                new Point3D(-4,4,0));
        Ray testRay;
        List<Point3D> expectedIntersectionsBVT;
        //endregion

        //region Group: begin at the plane [as the first scenario]

        //region TC11: next to the vertex ray - non intersections
        testRay = new Ray(new Point3D(-5,5,0), new Vector(0,0,-2));
        assertEquals("The findIntersection() method returned unexpected result in case the ray" +
                " is next to one of the Polygon's vertexes", null, polygonBVT.findIntersections(testRay));
        //endregion

        //region TC12: inside the plane ray - one intersection
        testRay = new Ray(new Point3D(-2,2,0), new Vector(0,0,2));
        assertEquals("The findIntersections() method returned unexpected result in case the ray" +
                " is defined on the plane", null, polygonBVT.findIntersections(testRay));
        //endregion

        //region TC13: next to the edge plane ray - non intersection
        testRay = new Ray(new Point3D(-2,5,0), new Vector(0,0,-2));
        assertEquals("The findIntersections() method returned unexpected result in case the ray" +
                "is defined next to the edge", null, polygonBVT.findIntersections(testRay));
        //endregion

        //endregion

        //region Group: begin *before* the plane

        //region TC14: The ray defined on vertex
        testRay = new Ray(new Point3D(0,2,3), new Vector(0,0,-2));
        assertEquals("The findIntersection() method returned unexpected result in case the ray is" +
                " before and above vertex", null, polygonBVT.findIntersections(testRay));
        //endregion

        //region TC15: The ray defined on edge
        testRay = new Ray(new Point3D(-3,0,2), new Vector(0,0,-2));
        assertEquals("The findIntersection() method returned unexpected result in case the ray is" +
                " before and above edge", null, polygonBVT.findIntersections(testRay));
        //endregion

        //region TC16: The ray defined on the continue of edge
        testRay = new Ray(new Point3D(-6,0,-2), new Vector(0,0,2));
        assertEquals("The findIntersection() method returned unexpected result in case the ray is" +
                " before and on the continue of the edge", null, polygonBVT.findIntersections(testRay));
        //endregion

        //endregion

        //region Group: begin *at* the plane

        //region TC17: The ray defined on vertex
        testRay = new Ray(new Point3D(0,2,0), new Vector(0,0,-2));
        assertEquals("The findIntersection() method returned unexpected result in case the ray is" +
                " at vertex", null, polygonBVT.findIntersections(testRay));
        //endregion

        //region TC18: The ray defined on edge
        testRay = new Ray(new Point3D(-3,0,0), new Vector(0,0,-2));
        assertEquals("The findIntersection() method returned unexpected result in case the ray is" +
                " at edge", null, polygonBVT.findIntersections(testRay));
        //endregion

        //region TC19: The ray defined on the continue of edge
        testRay = new Ray(new Point3D(-6,0,0), new Vector(0,0,2));
        assertEquals("The findIntersection() method returned unexpected result in case the ray is" +
                " at the continue of the edge", null, polygonBVT.findIntersections(testRay));
        //endregion

        //endregion

        //endregion

    }

}
