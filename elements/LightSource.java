package elements;

import primitives.*;

/**
 * Light source interface
 */
public interface LightSource {

    /**
     * Getter method for Color value to a given point
     * @param p The given point
     * @return the Color value
     */
    public Color getIntensity(Point3D p);

    /**
     * getter method for Light vector to a given point
     * @param p A given point
     * @return Vector toward the given position
     */
    public Vector getL(Point3D p);

    /**
     * get distance from this current light source to a given point
     * @param point the given point
     * @return distance from light to given point
     */
    double getDistance(Point3D point);

}

