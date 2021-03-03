package Tests.UnitTests;

import primitives.*;
import geometries.Sphere;
import org.junit.Test;
import java.util.List;
import java.util.ArrayList;
import static org.junit.Assert.*;
import static geometries.Intersectable.GeoPoint.convertPointsToGPoints;
/**
 * Unit tests for geometries.Sphere class
 */
public class SphereTest {

    /**
     * Test method for {@link geometries.Sphere#getNormal(Point3D)}.
     */
    @Test
    public void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // Test that the getNormal in the Sphere class returns the correct normal

        //let's generate Point and Radius that lives on the Sphere
        //we'll calculate known vector and from them - Normal

        // For example, this Sphere equation is => (x-1)^2 + (y-3)^2 + (z-2)^2 = 9;
        Point3D SphereCenter = new Point3D(1,3,2);
        Point3D pointOnTheSphere = new Point3D(-1,1,1);
        double radius = 3;

        // If given:
        // Point3D - center of the Sphere which called - O
        // Point3D - a Point on the Sphere casing - P
        // The calculation of the correct normal is - normalize(P-O)
        // In our case (-1,1,1)-(1,3,2) => (-2,-2,-1)
        // And the normalized - (-2/3, -2/3, -1/3)
        Vector correctNormal = new Vector(-2,-2,-1);
        correctNormal.normalize();

        //let's create Sphere object and check if the getNormal method works correctly
        Sphere checkSphere = new Sphere(radius, SphereCenter);
        Vector checkNormal = checkSphere.getNormal(pointOnTheSphere);

        //Test result
        assertEquals("getNormal() gave unexpected result", correctNormal, checkNormal);

    }

    /**
     * Test method for {@link geometries.Sphere#findIntersections(Ray)}.
     */
    @Test
    public void testFindIntersections() {
        Point3D center = new Point3D(0, 0, 3);
        Sphere sphere = new Sphere(2, center);
        Ray ray; // The ray we will use for the tests
        List<Point3D> exp; // The expected result list for the tests

        // region ============ Equivalence Partitions Tests ==============

        // region TC01: Ray starts before and crosses the sphere (2 points)
        ray = new Ray(new Point3D(0,0,0.5), new Vector(0,0,1));
        exp = new ArrayList<>();
        exp.add(new Point3D(0,0, 1));
        exp.add(new Point3D(0,0, 5));
        assertEquals("findIntersections() wrong value when Ray starts before and crosses the sphere (TC01)",
               convertPointsToGPoints(sphere, exp), sphere.findIntersections(ray));
        //endregion

        // region TC02: Ray starts inside the sphere (1 point)
        ray = new Ray(new Point3D(0,0,2), new Vector(2,0,1));
        exp = new ArrayList<>();
        exp.add(new Point3D(2,0, 3));
        assertTrue("findIntersections() wrong value when Ray starts inside the sphere (TC02)",
               convertPointsToGPoints(sphere, exp).equals(sphere.findIntersections(ray)));
        //endregion

        // region TC03: Ray starts after the sphere (0 points)
        ray = new Ray(new Point3D(-2,0,0), new Vector(-2,0,-2));
        exp = null;
        assertEquals("findIntersections() wrong value when Ray starts after the sphere (TC03)",
               convertPointsToGPoints(sphere, exp),  sphere.findIntersections(ray));
        //endregion

        // region TC04: Ray's line is outside the sphere (0 points)
        ray = new Ray(new Point3D(-4,0,0), new Vector(4,0,8));
        exp = null;
        assertEquals("findIntersections() wrong value when Ray's line is outside the sphere (TC04)",
               convertPointsToGPoints(sphere, exp), sphere.findIntersections(ray));
        //endregion

        //endregion

        // region =============== Boundary Values Tests ==================

        // region Group: Ray's line crosses the sphere (but not the center)

        // region TC11: Ray starts at sphere and goes inside (1 points)
        ray = new Ray(new Point3D(2,0,3), new Vector(-2,0,-2));
        exp = new ArrayList<>();
        exp.add(new Point3D(0,0, 1));
        assertTrue("findIntersections() wrong value for Ray's line crosses the sphere (but not the center) case when Ray starts at sphere and goes inside (TC11)",
               convertPointsToGPoints(sphere, exp).equals(sphere.findIntersections(ray)));
        //endregion

        // region TC12: Ray starts at sphere and goes outside (0 points)
        ray = new Ray(new Point3D(2,0,3), new Vector(1,0,-3));
        exp = null;
        assertEquals("findIntersections() wrong value for Ray's line crosses the sphere (but not the center) case when Ray starts at sphere and goes outside (TC12)",
               convertPointsToGPoints(sphere, exp), sphere.findIntersections(ray));
        //endregion

        //endregion

        // region Group: Ray's line goes through the center

        // region TC13: Ray starts before the sphere (2 points)
        ray = new Ray(new Point3D(3,0,3), new Vector(-1,0,0));
        exp = new ArrayList<>();
        exp.add(new Point3D(2,0, 3));
        exp.add(new Point3D(-2,0, 3));
        assertTrue("findIntersections() wrong value for Ray's line goes through the center case when Ray starts before the sphere (TC13)",
               convertPointsToGPoints(sphere, exp).equals(sphere.findIntersections(ray)));
        //endregion

        // region TC14: Ray starts at sphere and goes inside (1 points)
        ray = new Ray(new Point3D(2,0,3), new Vector(-1,0,0));
        exp = new ArrayList<>();
        exp.add(new Point3D(-2,0, 3));
        assertTrue("findIntersections() wrong value for Ray's line goes through the center case when Ray starts at sphere and goes inside (TC14)",
               convertPointsToGPoints(sphere, exp).equals(sphere.findIntersections(ray)));
        //endregion

        // region TC15: Ray starts inside (1 points)
        ray = new Ray(new Point3D(0,0,2), new Vector(0,0,-1));
        exp = new ArrayList<>();
        exp.add(new Point3D(0,0, 1));
        assertTrue("findIntersections() wrong value for Ray's line goes through the center case when Ray starts inside (TC15)",
               convertPointsToGPoints(sphere, exp).equals(sphere.findIntersections(ray)));
        //endregion

        // region TC16: Ray starts at the center (1 points)
        ray = new Ray(center, new Vector(2,0,0));
        exp = new ArrayList<>();
        exp.add(new Point3D(2,0, 3));
        assertTrue("findIntersections() wrong value for Ray's line goes through the center case when Ray starts at the center (TC16)",
               convertPointsToGPoints(sphere, exp).equals(sphere.findIntersections(ray)));
        //endregion

        // region TC17: Ray starts at sphere and goes outside (0 points)
        ray = new Ray(new Point3D(0,0,1), new Vector(0,0,-1));
        exp = null;
        assertEquals("findIntersections() wrong value for Ray's line goes through the center case when Ray starts at sphere and goes outside (TC17)",
               convertPointsToGPoints(sphere, exp), sphere.findIntersections(ray));
        //endregion

        // region TC18: Ray starts after sphere (0 points)
        ray = new Ray(new Point3D(0,0,0.5), new Vector(0,0,-1));
        exp = null;
        assertEquals("findIntersections() wrong value for Ray's line goes through the center case when Ray starts after sphere (TC18)",
               convertPointsToGPoints(sphere, exp), sphere.findIntersections(ray));
        //endregion

        //endregion

        // region Group: Ray's line is tangent to the sphere (all tests 0 points)

        // region TC19: Ray starts before the tangent point
        ray = new Ray(new Point3D(2,0,0), new Vector(0,0,1));
        exp = null;
        assertEquals("findIntersections() wrong value for Ray's line is tangent to the sphere case when Ray starts before the tangent point (TC19)",
               convertPointsToGPoints(sphere, exp), sphere.findIntersections(ray));
        //endregion

        // region TC20: Ray starts at the tangent point
        ray = new Ray(new Point3D(2,0,3), new Vector(0,0,1));
        exp = null;
        assertEquals("findIntersections() wrong value for Ray's line is tangent to the sphere case when Ray starts at the tangent point (TC20)",
               convertPointsToGPoints(sphere, exp), sphere.findIntersections(ray));
        //endregion

        // region TC21: Ray starts after the tangent point
        ray = new Ray(new Point3D(2,0,4), new Vector(0,0,1));
        exp = null;
        assertEquals("findIntersections() wrong value for Ray's line is tangent to the sphere case when Ray starts after the tangent point (TC21)",
               convertPointsToGPoints(sphere, exp), sphere.findIntersections(ray));
        //endregion

        //endregion

        // region Group: Special cases

        // region TC22: Ray's line is outside, ray is orthogonal to ray start to sphere's center line (0 points)
        ray = new Ray(new Point3D(3,0,3), new Vector(0,0,1));
        exp = null;
        assertEquals("findIntersections() wrong value when Ray's line is outside, ray is orthogonal to ray start to sphere's center line (TC22)",
               convertPointsToGPoints(sphere, exp), sphere.findIntersections(ray));
        //endregion

        //endregion

        //endregion

    }
}
