package geometries;

import primitives.Color;

/**
 * Abstract class that represents geometric shapes with radius
 */
public abstract class RadialGeometry extends Geometry {

    private double radius;

    //region Constructors

    /**
     * Constructor
     * @param r initial value for radios
     */
    public RadialGeometry(double r){
        radius = r;
    }

    /**
     * Constructor
     * @param radius initial value for radios
     * @param color The emission
     */
    public RadialGeometry(double radius, Color color) {
        super(color);
        this.radius = radius;
    }

    //endregion

    //region Getters & Overrides

    /**
     * @return double radios value
     */
    public double getRadius() {
        return radius;
    }

    /**
     * @return double radios squared value
     */
    public double getRadiusSquared() {
        return radius*radius;
    }

    @Override
    public String toString() {
        return "radius=" + radius;
    }
    //endregion

}
