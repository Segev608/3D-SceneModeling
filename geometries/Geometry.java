package geometries;

import primitives.*;

/**
 * Used by any geometrical body
 */
public abstract class Geometry implements Intersectable {

    protected Color emission;
    protected Material material;

    //region Constructors

    /**
     * Constructor
     * @param color The emission
     */
    public Geometry(Color color) {
        emission = color;
        material = new Material(0,0,0);
    }

    /**
     * Default C'tor
     */
    public Geometry() {
        emission = new Color(java.awt.Color.BLACK);
        material = new Material(0,0,0);
    }
    //endregion

    //region Getters

    /**
     * @return Emission field
     */
    public Color getEmission() {
        return emission;
    }

    /**
     * Getter for material value
     * @return Material value
     */
    public Material getMaterial(){
        return material;
    }
    //endregion

    /**
     * @param point3D A point on the geometrical body
     * @return The normal at the given point
     */
    public abstract Vector getNormal(Point3D point3D);

}
