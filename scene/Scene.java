package scene;

import elements.*;
import geometries.Geometries;
import geometries.Intersectable;
import primitives.Color;
import java.util.List;
import java.util.LinkedList;

/**
 * Represents a 3D scene
 */
public class Scene {

    //region Fields

    private String name;
    private Color background;
    private AmbientLight ambientLight;
    private Geometries geometries;
    private Camera camera;
    private List<LightSource> lights;
    private double distance;
    //endregion


    /**
     * Constructor
     * @param name of the scene
     */
    public Scene(String name) {
        this.name = name;
        geometries = new Geometries();
        lights = new LinkedList<>();
    }

    //region Getters

    /**
     * @return name field
     */
    public String getName() {
        return name;
    }

    /**
     * @return background field
     */
    public Color getBackground() {
        return background;
    }

    /**
     * @return ambientLight field
     */
    public AmbientLight getAmbientLight() {
        return ambientLight;
    }

    /**
     * @return geometries field
     */
    public Geometries getGeometries() {
        return geometries;
    }

    /**
     * @return camera field
     */
    public Camera getCamera() {
        return camera;
    }

    /**
     * @return distance field
     */
    public double getDistance() {
        return distance;
    }

    /**
     * @return lights field
     */
    public List<LightSource> getLights() {
        return lights;
    }
    //endregion

    //region Setters

    /**
     * @param background field to set
     */
    public void setBackground(Color background) {
        this.background = background;
    }

    /**
     * @param ambientLight field to set
     */
    public void setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
    }

    /**
     * @param camera field to set
     */
    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    /**
     * @param distance field to set
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }
    //endregion

    //region Adders

    /**
     * @param geometries Adds the intersectables to the scene
     */
    public void addGeometries(Intersectable... geometries) {
        this.geometries.add(geometries);
    }

    /**
     *
     * @param lights Adds the lights to the scene
     */
    public void addLights(LightSource... lights) {
        for (LightSource l : lights)
            this.lights.add(l);
    }
    //endregion
}
