package Tests.UnitTests;

import primitives.*;
import geometries.Plane;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import static geometries.Intersectable.GeoPoint.convertPointsToGPoints;

/**
 * Unit tests for geometries.Plane class
 */
public class PlaneTest {

    /**
     * Test method for {@link geometries.Plane#getNormal(Point3D)}.
     */
    @Test
    public void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // Test that the getNormal in the Plane class returns the correct normal

        //let's generate some points that lives in the same plane
        //we'll calculate for them vectors and from them - Normal
        Point3D firstInPlane = new Point3D(1,-4,5);
        Point3D secondInPlane = new Point3D(2,7,8);
        Point3D thirdInPlane = new Point3D(-3,5,1);

        // First Vector is: (-4,9,-4)
        // Second Vector is: (-5,-2,-7)
        // So, the correct normal is: (-4,9,-4)x(-5,-2,-7) => (-71,-8,53)

        Vector correctNormal = new Vector(-71,-8,53);
        correctNormal.normalize();

        //let's check the getNormal method and see if we got the correct Vector
        Plane checkPlane = new Plane(firstInPlane, secondInPlane, thirdInPlane);
        Vector checkNormal = checkPlane.getNormal();

        //Test result
        assertEquals("getNormal() gave unexpected result", correctNormal, checkNormal);
    }

    /**
     * Test method for {@link geometries.Plane#findIntersections(Ray)}.
     */
    @Test
    public void testFindIntersections(){
        Plane plane = new Plane(new Point3D(3,0,0), new Point3D(0,3,0),
                                            new Point3D(0,0,3));
        Ray ray; // The ray we will use for the tests
        List<Point3D> exp; // The expected result list for the tests

        // region ============ Equivalence Partitions Tests ==============

        //region TC01: Ray intersects the plane
        ray = new Ray(new Point3D(0,0,2), new Vector(2,2,-2));
        exp = new ArrayList<>();
        exp.add(new Point3D(1,1,1));
        assertEquals("The method findIntersections() returned an unexpected result (TC01)",
                convertPointsToGPoints(plane, exp), plane.findIntersections(ray));
        //endregion

        //region TC02: Ray does not intersect the plane [not parallel]
        ray = new Ray(new Point3D(0,0,2), new Vector(0,0,-2));
        exp = null;
        assertEquals("The findIntersections() method should return no intersection [not parallel] (TC02)",
                convertPointsToGPoints(plane, exp), plane.findIntersections(ray));
        //endregion

        //endregion

        // region =============== Boundary Values Tests ==================

        //region Group: Ray is parallel to the plane

        //region TC11: Ray not included in plane
        ray = new Ray(new Point3D(0,0,2), new Vector(2,0,-2));
        exp = null;
        assertEquals("The findIntersections() method should return no intersection [parallel, not included] (TC11)",
                convertPointsToGPoints(plane, exp), plane.findIntersections(ray));
        //endregion

        // region TC12: Ray included in plane
        ray = new Ray(new Point3D(0,0,3), new Vector(3,0,-3));
        exp = null;
        assertEquals("The findIntersections() method should return no intersection [parallel, included] (TC12)",
                convertPointsToGPoints(plane, exp), plane.findIntersections(ray));
        //endregion

        //endregion

        // region Group: Ray is orthogonal to the plane

        //region TC13: Ray starts before the plane
        ray = new Ray(new Point3D(3,3,6), new Vector(-3,-3,-3));
        exp = new ArrayList<>();
        exp.add(new Point3D(0,0,3));
        assertEquals("findIntersections() wrong value when Ray starts before the plane and Ray starts before the plane (TC13)",
                convertPointsToGPoints(plane, exp), plane.findIntersections(ray));
        //endregion

        //region TC14: Ray starts at the plane
        ray = new Ray(new Point3D(0,0,3), new Vector(-3,-3,-3));
        exp = null;
        assertEquals("findIntersections() wrong value when Ray starts before the plane and Ray starts at the plane (TC14)",
                convertPointsToGPoints(plane, exp), plane.findIntersections(ray));
        //endregion

        //region TC15: Ray starts after the plane
        ray = new Ray(new Point3D(-3,-3,0), new Vector(-3,-3,-3));
        exp = null;
        assertEquals("findIntersections() wrong value when Ray starts before the plane and Ray starts after the plane (TC15)",
                convertPointsToGPoints(plane, exp), plane.findIntersections(ray));
        //endregion

        //endregion

        // region Group: Ray is neither orthogonal nor parallel to the plane, special cases

        //region TC16: Ray starts at the plane
        ray = new Ray(new Point3D(0,0,3), new Vector(0,0,-3));
        exp = null;
        assertEquals("findIntersections() wrong value when Ray starts at the plane (TC16)",
                convertPointsToGPoints(plane, exp), plane.findIntersections(ray));
        //endregion

        // region TC17: Ray starts at the reference point of the plane
        ray = new Ray(plane.getPoint(), new Vector(0,0,-3));
        exp = null;
        assertEquals("findIntersections() wrong value when Ray starts at the reference point of the plane (TC17)",
                convertPointsToGPoints(plane, exp), plane.findIntersections(ray));
        //endregion

        //endregion

        //endregion
    }
}