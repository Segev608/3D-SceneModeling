package elements;

import primitives.*;

/**
 * Light class which represents SpotLight
 */
public class SpotLight extends PointLight {
    protected Vector direction;

    //region Constructors

    /**
     * Constructor
     * @param intensity intensity of the light
     * @param pos position of the light in the scene
     * @param dir the spot direction
     * @param kC scalar Constant
     * @param kL scalar Linear
     * @param kQ scalar Quadratic
     */
    public SpotLight(Color intensity, Point3D pos, Vector dir, double kC, double kL, double kQ) {
        super(intensity, pos, kC, kL, kQ);
        direction = dir.normalized();
    }

    /**
     * Constructor
     * @param radius the radios of the light 'bulb'
     * @param intensity intensity of the light
     * @param position position of the light in the scene
     * @param direction the spot direction
     * @param kC scalar Constant
     * @param kL scalar Linear
     * @param kQ scalar Quadratic - has the most influence (it's recommended to keep it as low as possible)
     */
    public SpotLight(Color intensity, double radius, Point3D position, double kC, double kL, double kQ, Vector direction) {
        super(intensity, radius, position, kC, kL, kQ);
        this.direction = direction.normalized();
    }
    //endregion

    //region Overrides

    @Override
    public Color getIntensity(Point3D p) {
        //Intensity calculated by ((I_0 * max(0, dot(dir, dirL))/(kC + kL * d + kQ * d^2))
        //Calculate enumerator
        Vector directionLight = p.subtract(position).normalized();
        double dotProduct = direction.dotProduct(directionLight);
        double scale = Math.max(0, dotProduct);
        Color enumerator = intensity.scale(scale);

        //Calculate Denominator
        double distance = position.distance(p);
        double denominator = kC + kL * distance + kQ * distance * distance;

        //final calculation
        return enumerator.reduce(denominator);
    }

    @Override
    public double getDistance(Point3D point) {
        return super.getDistance(point);
    }
    //endregion
}
