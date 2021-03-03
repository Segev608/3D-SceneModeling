package elements;

import primitives.*;

/**
 * Directional Light class
 */
public class DirectionalLight extends Light implements LightSource {
    private Vector direction;

    /**
     * Constructor
     * @param intensity The intensity of the light
     * @param direction The direction of the light
     */
    public DirectionalLight(Color intensity, Vector direction) {
        super(intensity);
        this.direction = direction.normalized();
    }

    //region Overrides

    @Override
    public Color getIntensity(Point3D p) {
        return intensity;
    }

    @Override
    public Vector getL(Point3D p) {
        return direction;
    }

    @Override
    public double getDistance(Point3D point) {
        return Double.POSITIVE_INFINITY;
    }
    //endregion
}
