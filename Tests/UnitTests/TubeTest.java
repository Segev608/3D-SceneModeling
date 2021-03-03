package Tests.UnitTests;

import primitives.*;
import geometries.Tube;
import org.junit.Test;
import java.util.List;
import java.util.ArrayList;
import static org.junit.Assert.*;

import static geometries.Intersectable.GeoPoint.convertPointsToGPoints;

/**
 * Unit tests for geometries.Tube class
 */
public class TubeTest {

    /**
     * Test method for {@link geometries.Tube#getNormal(Point3D)}.
     */
    @Test
    public void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // Test that the getNormal in the Tube class returns the correct normal

        // Let's create a Ray and radius in order to define a Tube
        // We'll define one Tube with well-known values in order to check the method
        // Remind - Tube is defined by (ray[start[Point3D], direction[Vector]], radius[double])

        Point3D startPosition = new Point3D(5,0,0);
        Vector directionFromStart = new Vector(1,0,0);
        Ray tubeRay = new Ray(startPosition, directionFromStart);
        double tubeRadius = 3.0;

        // In this simplified scenario, we've chosen a Tube that defined
        // to be on and to the same direction of X axes
        // in addition, the point that we calculate the normal on makes
        // our normal calculation easier - we should get: normal = (0,0,1)

        // creating that vector which we calculated above, and it's already normalized
        Vector normal = new Vector(0,0,1);

        Tube checkTube = new Tube(tubeRadius, tubeRay);
        Vector checkNormal = checkTube.getNormal(new Point3D(3,0,3));

        //Test result
        assertEquals("getNormal() gave unexpected result", normal, checkNormal);
    }

    @Test
    public void testFindIntersections() {
        Ray dir = new Ray(new Point3D(0, 0, -4), new Vector(0, 0, 1));
        Tube tube = new Tube(2, dir);
        Ray ray; // The ray we will use for the tests
        List<Point3D> exp; // The expected result list for the tests

        // region ============ Equivalence Partitions Tests ==============

        // region TC01: Ray starts before and crosses the tube (2 points)
        ray = new Ray(new Point3D(-4, 2, 0), new Vector(4, -4, 0));
        exp = new ArrayList<>();
        exp.add(new Point3D(-2, 0, 0));
        exp.add(new Point3D(0, -2, 0));
        assertTrue("findIntersections() wrong value when Ray starts before and crosses the tube (TC01)",
               convertPointsToGPoints(tube, exp).equals(tube.findIntersections(ray)));
        //endregion

        // region TC02: Ray starts inside the tube (1 point)
        ray = new Ray(new Point3D(-1, -1, 0), new Vector(4, -4, 0));
        exp = new ArrayList<>();
        exp.add(new Point3D(0, -2, 0));
        assertTrue("findIntersections() wrong value when Ray starts inside the tube (TC02)",
               convertPointsToGPoints(tube, exp).equals(tube.findIntersections(ray)));
        //endregion

        // region TC03: Ray starts after the tube (0 points)
        ray = new Ray(new Point3D(4, -6, 0), new Vector(4, -4, 0));
        exp = null;
        assertEquals("findIntersections() wrong value when Ray starts after the tube (TC03)",
                convertPointsToGPoints(tube, exp), tube.findIntersections(ray));
        //endregion

        // region TC04: Ray's line is outside the tube (0 points)
        ray = new Ray(new Point3D(-4, 2, 0), new Vector(4, -8, 1));
        exp = null;
        assertEquals("findIntersections() wrong value when Ray's line is outside the tube (TC04)",
               convertPointsToGPoints(tube, exp), tube.findIntersections(ray));
        //endregion

        //endregion

        // region =============== Boundary Values Tests ==================

        // region Group: Ray's line crosses the tube (but not the center)

        // region TC11: Ray starts at sphere and goes inside (1 points)
        ray = new Ray(new Point3D(-2, 0, 0), new Vector(4, -4, 0));
        exp = new ArrayList<>();
        exp.add(new Point3D(0, -2, 0));
        assertEquals("findIntersections() wrong value for Ray's line crosses the tube (but not the center) case when Ray starts at sphere and goes inside (TC11)",
               convertPointsToGPoints(tube, exp), tube.findIntersections(ray));
        //endregion

        // region TC12: Ray starts at sphere and goes outside (0 points)
        ray = new Ray(new Point3D(0, -2, 0), new Vector(4, -4, 0));
        exp = null;
        assertEquals("findIntersections() wrong value for Ray's line crosses the tube (but not the center) case when Ray starts at sphere and goes outside (TC12)",
               convertPointsToGPoints(tube, exp), tube.findIntersections(ray));
        //endregion

        //endregion

        // region Group: Ray's line goes through the center line

        // region TC13: Ray starts before the tube (2 points)
        ray = new Ray(new Point3D(-4,0,0), new Vector(8,0,4));
        exp = new ArrayList<>();
        exp.add(new Point3D(-2,0, 1));
        exp.add(new Point3D(2,0, 3));
        assertTrue("findIntersections() wrong value for Ray's line goes through the center line case when Ray starts before the tube (TC13)",
               convertPointsToGPoints(tube, exp).equals(tube.findIntersections(ray)));
        //endregion

        // region TC14: Ray starts at tube and goes inside (1 points)
        ray = new Ray(new Point3D(-2,0,1), new Vector(8,0,4));
        exp = new ArrayList<>();
        exp.add(new Point3D(2,0, 3));
        assertTrue("findIntersections() wrong value for Ray's line goes through the center line case when Ray starts at tube and goes inside (TC14)",
               convertPointsToGPoints(tube, exp).equals(tube.findIntersections(ray)));
        //endregion

        // region TC15: Ray starts inside (1 points)
        ray = new Ray(new Point3D(-1,0,1.5), new Vector(8,0,4));
        exp = new ArrayList<>();
        exp.add(new Point3D(2,0, 3));
        assertTrue("findIntersections() wrong value for Ray's line goes through the center line case when Ray starts inside (TC15)",
               convertPointsToGPoints(tube, exp).equals(tube.findIntersections(ray)));
        //endregion

        // region TC16: Ray starts at the center line (1 points)
        ray = new Ray(new Point3D(0,0,2), new Vector(8,0,4));
        exp = new ArrayList<>();
        exp.add(new Point3D(2,0, 3));
        assertTrue("findIntersections() wrong value for Ray's line goes through the center line case when Ray starts at the center line (TC16)",
               convertPointsToGPoints(tube, exp).equals(tube.findIntersections(ray)));
        //endregion

        // region TC17: Ray starts at tube and goes outside (0 points)
        ray = new Ray(new Point3D(2,0,3), new Vector(8,0,4));
        exp = null;
        assertEquals("findIntersections() wrong value for Ray's line goes through the center line case when Ray starts at tube and goes outside (TC17)",
               convertPointsToGPoints(tube, exp), tube.findIntersections(ray));
        //endregion

        // region TC18: Ray starts after tube (0 points)
        ray = new Ray(new Point3D(4,0,4), new Vector(8,0,4));
        exp = null;
        assertEquals("findIntersections() wrong value for Ray's line goes through the center line case when Ray starts after tube (TC18)",
               convertPointsToGPoints(tube, exp), tube.findIntersections(ray));
        //endregion

        //endregion

        // region Group: Ray's line is tangent to the tube (all tests 0 points)

        // region TC19: Ray starts before the tangent point
        ray = new Ray(new Point3D(-2,2,0), new Vector(2,0,1));
        exp = null;
        assertEquals("findIntersections() wrong value for Ray's line is tangent to the tube case when Ray starts before the tangent point (TC19)",
               convertPointsToGPoints(tube, exp), tube.findIntersections(ray));
        //endregion

        // region TC20: Ray starts at the tangent point
        ray = new Ray(new Point3D(0,2,1), new Vector(2,0,1));
        exp = null;
        assertEquals("findIntersections() wrong value for Ray's line is tangent to the tube case when Ray starts at the tangent point (TC20)",
               convertPointsToGPoints(tube, exp), tube.findIntersections(ray));
        //endregion

        // region TC21: Ray starts after the tangent point
        ray = new Ray(new Point3D(2,2,2), new Vector(2,0,1));
        exp = null;
        assertEquals("findIntersections() wrong value for Ray's line is tangent to the tube case when Ray starts after the tangent point (TC21)",
               convertPointsToGPoints(tube, exp), tube.findIntersections(ray));
        //endregion

        //endregion

        // region Group: Ray's line is parallel to the tube's center line (all tests 0 points)

        // region TC22: Ray is outside the tube
        ray = new Ray(new Point3D(0,3,0), new Vector(0,0,1));
        exp = null;
        assertEquals("findIntersections() wrong value for Ray's line is parallel to the tube's center line case when Ray is outside the tube (TC22)",
               convertPointsToGPoints(tube, exp), tube.findIntersections(ray));
        //endregion

        // region TC23: Ray is at the tube
        ray = new Ray(new Point3D(0,2,0), new Vector(0,0,1));
        exp = null;
        assertEquals("findIntersections() wrong value for Ray's line is parallel to the tube's center line case when Ray is at the tube (TC23)",
               convertPointsToGPoints(tube, exp), tube.findIntersections(ray));
        //endregion

        // region TC24: Ray is in the tube (but not the tube's center line)
        ray = new Ray(new Point3D(0,1,0), new Vector(0,0,1));
        exp = null;
        assertEquals("findIntersections() wrong value for Ray's line is parallel to the tube's center line case when Ray is in the tube (but not the tube's center line) (TC24)",
               convertPointsToGPoints(tube, exp), tube.findIntersections(ray));
        //endregion

        // region TC25: Ray is the tube's center line
        ray = new Ray(new Point3D(0,0,1), new Vector(0,0,1));
        exp = null;
        assertEquals("findIntersections() wrong value for Ray's line is parallel to the tube's center line case when Ray is the tube's center line (TC25)",
               convertPointsToGPoints(tube, exp), tube.findIntersections(ray));
        //endregion

        //endregion

        // region Group: Special cases

        // region TC26: Ray's line is outside, ray is orthogonal to ray start to tube's center line (0 points)
        ray = new Ray(new Point3D(0,3,0), new Vector(1,0,0));
        exp = null;
        assertEquals("findIntersections() wrong value when Ray's line is outside, ray is orthogonal to ray start to tube's center line (TC26)",
               convertPointsToGPoints(tube, exp), tube.findIntersections(ray));
        //endregion

        //endregion

        //endregion
    }
}