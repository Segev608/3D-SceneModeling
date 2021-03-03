package Tests.AcceptnaceTest;

import elements.*;
import geometries.*;
import org.junit.Test;
import primitives.*;
import renderer.*;
import scene.Scene;

/**
 * Test rendering complicated image
 */
public class FinalPictureTest {

    /**
     * Builds the final scene to render
     * @return The final scene
     */
    private Scene buildFinalScene() {
        double kds = 0.1;
        int nsh = 20;
        Scene scene = new Scene("Test scene");
        scene.setCamera(new Camera(new Point3D(170, 170, 170), new Vector(-1, -1, -1), new Vector(-3, -3, 6)));
        scene.setDistance(100);
        scene.setBackground(Color.BLACK);
        scene.setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), 0.15));

        scene.addGeometries(
                new Plane(Point3D.ZERO, new Vector(0,0,1), new Color(0,153,204), new Material(kds,kds,nsh, 0, 0)),
                new Polygon(new Material(kds, kds, nsh, 0,1), new Color(51,100,51), new Point3D(-40,-40,0),
                        new Point3D(100,-40,0), new Point3D(100,-40,150), new Point3D(-40,-40,150)),
                new Triangle(new Color(51,153,51), new Material(kds,kds, nsh,0.3,0), new Point3D(16 - 20,67 + 20,0), new Point3D(67 - 20,12 + 20,0), new Point3D(56 - 20,49 + 20,40)),
                new Triangle(new Color(51,153,51), new Material(kds,kds, nsh,0.3,0), new Point3D(83 - 20,76 + 20,0), new Point3D(16 - 20,67 + 20,0), new Point3D(56 - 20,49 + 20,40)),
                new Triangle(new Color(51,153,51), new Material(kds,kds, nsh,0.3,0), new Point3D(83 - 20,76 + 20,0), new Point3D(67 - 20,12 + 20,0), new Point3D(56 - 20,49 + 20,40)),
                new Tube(20,new Ray(new Point3D(100,100,0), new Vector(-1,1,0)),new Color(50,150,50), new Material(kds,kds,nsh,0.5,0)),
                new Sphere(new Color(0,255,255), new Material(kds,kds,nsh, 0,0), 20, new Point3D(-70, 60, 120)),
                new Sphere(new Color(0,0,255), new Material(kds,kds,nsh,0,0), 20, new Point3D(20,20,20)),
                new Sphere(new Color(100,50,50), new Material(kds,kds,nsh,0.4,0), 200, Point3D.ZERO),
                new Cylinder(new Ray(new Point3D(-70,60,0), new Vector(0,0,1)), 30, 100, new Color(0,255,0), new Material(kds,kds,nsh, 0,0.5))
        );

        scene.addLights(
                new DirectionalLight(new Color(java.awt.Color.YELLOW), new Vector(0,1,-1)),
                new PointLight(new Color(1000,1000,0), 20, new Point3D(-70,40,170), 1,0.0001,0.000001),
                new SpotLight(new Color(500,500,500), 20, new Point3D(130,130,30), 1,0.000001,0.000000001, new Vector(-1,0,-1))
        );

        return scene;
    }

    /**
     * Produces a complicated picture
     * BONUS 1. 10 Shapes.
     */
    @Test
    public void finalPictureTest() {
        Scene scene = buildFinalScene();

        ImageWriter imageWriter = new ImageWriter("FinalPicture", 200, 200, 800, 800);
        Render render = new Render(imageWriter, scene);

        render.renderImage();
        render.writeToImage();
    }

    /**
     * BONUS 2. Same scene, different camera angle.
     */
    @Test
    public void finalPictureTestCamera1() {
        Scene scene = buildFinalScene();
        scene.setCamera(new Camera(new Point3D(10, 170, 180), new Vector(0, -1, -1), new Vector(0, -3, 3)));

        ImageWriter imageWriter = new ImageWriter("FinalPictureCam1", 200, 200, 800, 800);
        Render render = new Render(imageWriter, scene);

        render.renderImage();
        render.writeToImage();
    }

    /**
     * Final scene with Soft Shadow feature
     */
    @Test
    public void finalSoftPictureTest() {
        Scene scene = buildFinalScene();

        ImageWriter imageWriter = new ImageWriter("FinalSoftPicture", 200, 200, 800, 800);
        Render render = new Render(imageWriter, scene, true, false);

        render.renderImage();
        render.writeToImage();
    }

    /**
     * Final scene with Super Sampling feature
     */
    @Test
    public void finalSuperPictureTest() {
        Scene scene = buildFinalScene();

        ImageWriter imageWriter = new ImageWriter("FinalSuperPicture", 200, 200, 800, 800);
        Render render = new Render(imageWriter, scene, false, true).setMultithreading(3).setDebugPrint().setSuperSamplings(1);

        render.renderImage();
        render.writeToImage();
    }

    /**
     * Final scene with both Soft Shadow and Super Sampling features
     */
    @Test
    public void finalSuperSoftPictureTest() {
        Scene scene = buildFinalScene();

        ImageWriter imageWriter = new ImageWriter("FinalSuperSoftPicture", 200, 200, 800, 800);
        Render render = new Render(imageWriter, scene, 35, 1).setMultithreading(8).setDebugPrint();

        render.renderImage();
        render.writeToImage();
    }

}
