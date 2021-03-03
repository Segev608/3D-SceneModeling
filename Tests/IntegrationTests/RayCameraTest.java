package Tests.IntegrationTests;

import primitives.*;
import geometries.*;
import elements.Camera;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;
import static geometries.Intersectable.GeoPoint;

/**
 * Unit tests for camera-ray integration
 */
public class RayCameraTest {

    /**
     * calculating the amount of intersections camera ray and shape using findIntersection()
     * @param shape using polymorphism, enabling the option to pass any shape to calculate
     * @param camera the camera instance
     * @return integer value of the amount
     */
    private int intersectionCounter(Geometry shape, Camera camera){
        int sumIntersectionCamera = 0;
        Ray ray;
        List<GeoPoint> intersections;
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                ray = camera.constructRayThroughPixel(3,3,j,i,1,3,3);
                intersections = shape.findIntersections(ray);
                sumIntersectionCamera += intersections == null? 0 : intersections.size();
            }
        }
        return sumIntersectionCamera;
    }

    /**
     * Tests camera-ray integration on sphere
     */
    @Test
    public void testCameraSphere(){
        //region Initializing parameters
        Camera camera;
        Sphere testSphere;
        int sumIntersectionCamera;
        //endregion

        //region TC01: 3X3 view-plane, radios = 1, sphere starts at (0,0,3), 2 Intersections

        camera = new Camera(Point3D.ZERO, new Vector(0,0,1), new Vector(0,-1,0));
        testSphere = new Sphere(1, new Point3D(0,0,3));
        sumIntersectionCamera = intersectionCounter(testSphere, camera);
        assertEquals("Wrong number of intersection to the sphere", 2, sumIntersectionCamera);
        //endregion

        //region TC02: 3X3 view-plane, radios = 2.5, sphere starts at (0,0,2.5), 18 Intersections

        camera = new Camera(new Point3D(0,0,-0.5), new Vector(0,0,1), new Vector(0,-1,0));
        testSphere = new Sphere(2.5, new Point3D(0,0,2.5));
        sumIntersectionCamera = intersectionCounter(testSphere, camera);
       assertEquals("Wrong number of intersection to the sphere", 18, sumIntersectionCamera);
        //endregion

        //region TC03: 3X3 view-plane, radios = 2, sphere starts at (0,0,2), 10 Intersections

        camera = new Camera(new Point3D(0,0,-0.5), new Vector(0,0,1), new Vector(0,-1,0));
        testSphere = new Sphere(2, new Point3D(0,0,2));
        sumIntersectionCamera = intersectionCounter(testSphere, camera);
        assertEquals("Wrong number of intersection to the sphere", 10, sumIntersectionCamera);
        //endregion

        //region TC04 3X3 view-plane radios = 4, sphere start at (0,0,2), 9 Intersections

        camera = new Camera(new Point3D(0,0,-0.5), new Vector(0,0,1), new Vector(0,-1,0));
        testSphere = new Sphere(4.0, new Point3D(2,0,0));
        sumIntersectionCamera = intersectionCounter(testSphere, camera);
        assertEquals("Wrong number of intersections to the sphere", 9, sumIntersectionCamera);
        //endregion

        //region TC05: 3X3 view-plane, radios = 0.5, sphere starts at (0,0,-1), 0 Intersections

        camera = new Camera(Point3D.ZERO, new Vector(0,0,1), new Vector(0,-1,0));
        testSphere = new Sphere(0.5, new Point3D(0,0,-1));
        sumIntersectionCamera = intersectionCounter(testSphere, camera);
        assertEquals("Wrong number of intersections to the sphere", 0, sumIntersectionCamera);
        //endregion
    }

    /**
     *  Tests camera-ray integration on plane
     */
    @Test
    public void testCameraPlane() {
        Camera cam = new Camera(Point3D.ZERO, new Vector(0,0,1), new Vector(0,-1,0));
        Plane plane;
        int count;

        // TC1: Parallel plane and view plane
        plane = new Plane(new Point3D(0,0,3), new Vector(0,0,1));
        count = intersectionCounter(plane, cam);
        assertEquals("wrong value TC1 on plane", 9, count);


        // TC2: Not parallel plane and view plane
        plane = new Plane(new Point3D(0,0,10), new Vector(0,-0.5,1));
        count = intersectionCounter(plane, cam);
        assertEquals("wrong value TC2 on plane", 9, count);

        // TC3: 6 Intersections
        plane = new Plane(new Point3D(0,0,10), new Vector(0,-1,1));
        count = intersectionCounter(plane, cam);
        assertEquals("wrong value TC3 on plane", 6, count);


    }

    /**
     *  Tests camera-ray integration on triangle
     */
    @Test
    public void testCameraTriangle(){

        //region Initializing parameters
        Camera camera = new Camera(Point3D.ZERO, new Vector(0,0,1), new Vector(0,-1,0));
        Triangle testTriangle;
        int sumIntersectionCamera = 0;
        //endregion

        //region TC01: 3X3 view-plane, 1 Intersection

        testTriangle = new Triangle(new Point3D(0,-1,2), new Point3D(1,1,2), new Point3D(-1,1,2));
        sumIntersectionCamera = intersectionCounter(testTriangle, camera);
        assertEquals("Wrong number of intersections to the triangle", 1, sumIntersectionCamera);
        //endregion

        //region TC02: 3X3 view-plane, 2 Intersections

        testTriangle = new Triangle(new Point3D(0,-20,2), new Point3D(1,1,2), new Point3D(-1,1,2));
        sumIntersectionCamera = intersectionCounter(testTriangle, camera);
        assertEquals("Wrong number of intersections to the triangle", 2, sumIntersectionCamera);
        //endregion
    }
}
