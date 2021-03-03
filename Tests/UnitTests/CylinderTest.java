package Tests.UnitTests;

import primitives.*;
import org.junit.Test;
import geometries.Tube;
import geometries.Cylinder;
import java.util.List;
import java.util.ArrayList;
import static org.junit.Assert.*;
import static geometries.Intersectable.GeoPoint.convertPointsToGPoints;
import static geometries.Intersectable.GeoPoint;

/**
 * Unit tests for geometries.Cylinder class
 */
public class CylinderTest {


    /**
     * Test method for {@link geometries.Cylinder#getNormal(Point3D)}.
     */
    @Test
    public void testGetNormal() {
        Ray ray = new Ray(Point3D.ZERO, new Vector(0, 0, 1));
        Tube tube = new Tube(1, ray);
        Cylinder cyl = new Cylinder(tube, 2);

        // ============ Equivalence Partitions Tests ==============
        // Test for a point in the cylinder casing
        assertEquals("getNormal(Point3D) wrong value for a point in the cylinder casing.",
                new Vector(0,1,0), cyl.getNormal(new Point3D(0,1,1)));
        // Test for a point in the cylinder first base
        assertEquals("getNormal(Point3D) wrong value for a point in the cylinder first base.",
                new Vector(0,0,-1), cyl.getNormal(new Point3D(0.5,0.5,0)));
        // Test for a point in the cylinder second base
        assertEquals("getNormal(Point3D) wrong value for a point in the cylinder second base.",
                new Vector(0,0,1), cyl.getNormal(new Point3D(0,0,2)));

        // =============== Boundary Values Tests ==================
        // Test for a point in the cylinder first base intersection
        assertEquals("getNormal(Point3D) wrong value for a point in the cylinder first base intersection.",
                new Vector(0,0,-1), cyl.getNormal(new Point3D(1,0,0)));
        // Test for a point in the cylinder second base intersection
        assertEquals("getNormal(Point3D) wrong value for a point in the cylinder second base intersection.",
                new Vector(0,0,1), cyl.getNormal(new Point3D(1,0,2)));
    }

    /**
     * Test method for {@link geometries.Cylinder#findIntersections(Ray)}.
     */
    @Test
    public void testFindIntersections(){

        Ray defineCylinder = new Ray(new Point3D(0,0,-2), new Vector(0,0,1));
        Cylinder cylinder = new Cylinder(defineCylinder, 2.0, 4.0);
        Ray ray; // The ray we will use for the tests
        List<Point3D> exp; // The expected result list for the tests
        List<GeoPoint> expected;

        // First we'll define 3 areas of the tube that contains the cylinder:
        // 1 - below the bottom base
        // 2 - between the bases
        // 3 - above the upper base
        // Now we'll test according to where the intersections are in the infinite tube.

        // region ============ Equivalence Partitions Tests ==============

        //region Group: Two intersections with the tube

        // region TC01: first point at 1 and second at 1 (0 points)
        ray = new Ray(new Point3D(-4,-4,-4), new Vector(4,4,0));
        exp = null;
        assertEquals("findIntersection() wrong value when first point at 1 and second at 1 (TC01)",
                convertPointsToGPoints(cylinder, exp), cylinder.findIntersections(ray));
        //endregion

        // region TC02: first point at 1 and second at 2 (2 points)
        ray = new Ray(new Point3D(0,-3,1), new Vector(0,3,-3));
        exp = new ArrayList<>();
        exp.add(new Point3D(0,-2,0));
        exp.add(new Point3D(0,0,-2));
        expected = convertPointsToGPoints(cylinder, exp);
        ray.sortPointByT(expected);
        assertTrue("findIntersection() wrong value when first point at 1 and second at 2 (TC02)",
                (expected).equals(cylinder.findIntersections(ray)));
        //endregion

        // region TC03: first point at 1 and second at 3 (2 points)
        ray = new Ray(new Point3D(0,-3,6), new Vector(0,2,-4));
        exp = new ArrayList<>();
        exp.add(new Point3D(0,-1,2));
        exp.add(new Point3D(0,1,-2));
        expected = convertPointsToGPoints(cylinder, exp);
        ray.sortPointByT(expected);
        assertTrue("findIntersection() wrong value when first point at 1 and second at 3 (TC03)",
                (expected).equals(cylinder.findIntersections(ray)));
        //endregion

        // region TC04: first point at 2 and second at 2 (2 points)
        ray = new Ray(new Point3D(0,-6,2), new Vector(0,4,-1));
        exp = new ArrayList<>();
        exp.add(new Point3D(0,-2,1));
        exp.add(new Point3D(0,2,0));
        expected = convertPointsToGPoints(cylinder, exp);
        ray.sortPointByT(expected);
        assertTrue("findIntersection() wrong value when first point at 2 and second at 2 (TC04)",
                (expected).equals(cylinder.findIntersections(ray)));
        //endregion

        // region TC05: first point at 2 and second at 3 (2 points)
        ray = new Ray(new Point3D(0,-2,4), new Vector(0,4,-4));
        exp = new ArrayList<>();
        exp.add(new Point3D(0,0,2));
        exp.add(new Point3D(0,2,0));
        expected = convertPointsToGPoints(cylinder, exp);
        ray.sortPointByT(expected);
        assertTrue("findIntersection() wrong value when first point at 2 and second at 3 (TC05)",
                (expected).equals(cylinder.findIntersections(ray)));
        //endregion

        // region TC06: first point at 3 and second at 3 (0 points)
        ray = new Ray(new Point3D(0,-6,5), new Vector(0,4,-1));
        exp = null;
        assertEquals("findIntersection() wrong value when first point at 3 and second at 3 (TC06)",
                convertPointsToGPoints(cylinder, exp), cylinder.findIntersections(ray));
        //endregion

        //endregion

        // region Group: One intersection with the tube (Ray starts inside)

        // region TC07: point at 1 (1 points)
        ray = new Ray(new Point3D(0,0,1), new Vector(1,1,-3));
        exp = new ArrayList<>();
        exp.add(new Point3D(1,1,-2));
        assertTrue("findIntersection() wrong value when Ray starts inside and point at 1 (TC07)",
                (convertPointsToGPoints(cylinder, exp)).equals(cylinder.findIntersections(ray)));
        //endregion

        // region TC08: point at 2 (1 points)
        ray = new Ray(new Point3D(0,0,1), new Vector(0,2,-1));
        exp = new ArrayList<>();
        exp.add(new Point3D(0,2,0));
        assertTrue("findIntersection() wrong value when Ray starts inside and point at 2 (TC08)",
                (convertPointsToGPoints(cylinder, exp)).equals(cylinder.findIntersections(ray)));
        //endregion

        // region TC09: point at 3 (1 points)
        ray = new Ray(new Point3D(0,0,1), new Vector(0,2,2));
        exp = new ArrayList<>();
        exp.add(new Point3D(0,1,2));
        assertTrue("findIntersection() wrong value when Ray starts inside and point at 3 (TC09)",
                (convertPointsToGPoints(cylinder, exp)).equals(cylinder.findIntersections(ray)));
        //endregion

        //endregion

        //endregion

        //region =============== Boundary Values Tests ==================

        // We'll refer to the border between tow areas x,y as x/y.
        // These are basically the intersection circle between a base and the tube.

        //region Group: intersection(s) in x/y

        // region TC11: first point at 1 and second at 1/2 (0 points)
        ray = new Ray(new Point3D(0,-6,0), new Vector(0,4,-2));
        exp = null;
        assertEquals("findIntersection() wrong value when first point at 1 and second at 1/2 (TC11)",
                convertPointsToGPoints(cylinder, exp), cylinder.findIntersections(ray));
        //endregion

        // region TC12: first point at 2 and second at 1/2 (1 points)
        ray = new Ray(new Point3D(0,-6,-4), new Vector(0,4,2));
        exp = new ArrayList<>();
        exp.add(new Point3D(0,2,0));
        assertTrue("findIntersection() wrong value when first point at 2 and second at 1/2 (TC12)",
                (convertPointsToGPoints(cylinder, exp)).equals(cylinder.findIntersections(ray)));
        //endregion

        // region TC13: first point at 3 and second at 1/2 (1 points)
        ray = new Ray(new Point3D(0,-6,-10), new Vector(0,4,8));
        exp = new ArrayList<>();
        exp.add(new Point3D(0,0,2));
        assertTrue("findIntersection() wrong value when first point at 3 and second at 1/2 (TC13)",
                (convertPointsToGPoints(cylinder, exp)).equals(cylinder.findIntersections(ray)));
        //endregion

        // region TC14: first point at 1 and second at 2/3 (1 points)
        ray = new Ray(new Point3D(0,-6,10), new Vector(0,4,-8));
        exp = new ArrayList<>();
        exp.add(new Point3D(0,0,-2));
        assertTrue("findIntersection() wrong value when first point at 1 and second at 2/3 (TC14)",
                (convertPointsToGPoints(cylinder, exp)).equals(cylinder.findIntersections(ray)));
        //endregion

        // region TC15: first point at 2 and second at 2/3 (1 points)
        ray = new Ray(new Point3D(0,-6,4), new Vector(0,4,-2));
        exp = new ArrayList<>();
        exp.add(new Point3D(0,2,0));
        assertTrue("findIntersection() wrong value when first point at 2 and second at 2/3 (TC15)",
                (convertPointsToGPoints(cylinder, exp)).equals(cylinder.findIntersections(ray)));
        //endregion

        // region TC16: first point at 3 and second at 2/3 (0 points)
        ray = new Ray(new Point3D(0,-6,-2), new Vector(0,4,4));
        exp = null;
        assertEquals("findIntersection() wrong value when first point at 3 and second at 2/3 (TC16)",
                convertPointsToGPoints(cylinder, exp), cylinder.findIntersections(ray));
        //endregion

        // region TC17: first point at 1/2 and second at 2/3 (0 points)
        ray = new Ray(new Point3D(0,-6,6), new Vector(0,4,-4));
        exp = null;
        assertEquals("findIntersection() wrong value when first point at 1/2 and second at 2/3 (TC17)",
                convertPointsToGPoints(cylinder, exp), cylinder.findIntersections(ray));
        //endregion

        // region TC18: first point at 1/2 and second at 1/2 (0 points)
        ray = new Ray(new Point3D(0,-6,-2), new Vector(0,4,0));
        exp = null;
        assertEquals("findIntersection() wrong value when first point at 1/2 and second at 1/2 (TC18)",
                convertPointsToGPoints(cylinder, exp), cylinder.findIntersections(ray));
        //endregion

        // region TC19: first point at 2/3 and second at 2/3 (0 points)
        ray = new Ray(new Point3D(0,-6,2), new Vector(0,4,0));
        exp = null;
        assertEquals("findIntersection() wrong value when first point at 2/3 and second at 2/3 (TC19)",
                convertPointsToGPoints(cylinder, exp), cylinder.findIntersections(ray));
        //endregion

        //endregion

        //region Group: Ray is parallel to the tube's center line

        // region TC20: The ray is not in the tube's center line (2 points)
        ray = new Ray(new Point3D(0,1,-4), new Vector(0,0,6));
        exp = new ArrayList<>();
        exp.add(new Point3D(0,1,-2));
        exp.add(new Point3D(0,1,2));
        expected = convertPointsToGPoints(cylinder, exp);
        ray.sortPointByT(expected);
        assertTrue("findIntersection() wrong value when Ray is parallel to the tube's center line when ray is not in the tube's center line (TC20)",
                (expected).equals(cylinder.findIntersections(ray)));
        //endregion

        // region TC21: The ray is in the tube's center line (2 points)
        ray = new Ray(new Point3D(0,0,-4), new Vector(0,0,6));
        exp = new ArrayList<>();
        exp.add(new Point3D(0,0,-2));
        exp.add(new Point3D(0,0,2));
        expected = convertPointsToGPoints(cylinder, exp);
        ray.sortPointByT(expected);
        assertTrue("findIntersection() wrong value when Ray is parallel to the tube's center line when ray is in the tube's center line (TC21)",
                (expected).equals(cylinder.findIntersections(ray)));
        //endregion

        //endregion

        // region Group: One intersection with the tube (Ray starts inside)

        // region TC22: point at 1/2 (0 points)
        ray = new Ray(new Point3D(0,0,1), new Vector(0,2,-3));
        exp = null;
        assertEquals("findIntersection() wrong value when Ray starts inside and point at 1/2 (TC22)",
                convertPointsToGPoints(cylinder, exp), cylinder.findIntersections(ray));
        //endregion

        // region TC23: point at 2/3 (0 points)
        ray = new Ray(new Point3D(0,0,1), new Vector(0,2,1));
        exp = null;
        assertEquals("findIntersection() wrong value when Ray starts inside and point at 2/3 (TC23)",
                convertPointsToGPoints(cylinder, exp), cylinder.findIntersections(ray));
        //endregion

        //endregion

        //endregion

    }

}