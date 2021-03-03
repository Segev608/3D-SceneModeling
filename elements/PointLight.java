package elements;

import primitives.*;

/**
 * Point light class
 */
public class PointLight extends FiniteLight {
    protected Point3D position;
    protected double kC;    // Constant
    protected double kL;    // Linear
    protected double kQ;    // Quadratic

    //region Constructors

    /**
     * Constructor
     * @param intensity the strength of the light in the scene
     * @param position Light current position
     * @param kC scalar Constant
     * @param kL scalar Linear
     * @param kQ scalar Quadratic
     */
    public PointLight(Color intensity, Point3D position, double kC, double kL, double kQ) {
        super(intensity);
        this.position = position;
        this.kC = kC;
        this.kL = kL;
        this.kQ = kQ;
    }

    /**
     * Constructor
     * @param intensity the strength of the light in the scene
     * @param radius the radios of the light 'bulb'
     * @param position Light current position
     * @param kC scalar Constant
     * @param kL scalar Linear
     * @param kQ scalar Quadratic
     */
    public PointLight(Color intensity, double radius, Point3D position, double kC, double kL, double kQ) {
        this(intensity, position, kC, kL, kQ);
        this.radius = radius;
    }
    //endregion

    //region Overrides

    @Override
    public Color getIntensity(Point3D p) {
        //  returned intensityL calculated as: ((I_0) / (kC + kL * d + kQ * d^2))
        Color intensityL = new Color(intensity);
        double distance = position.distance(p);
        double denominator = Util.alignZero(kC + kL*distance + kQ * distance * distance);
        return intensityL.reduce(denominator);

    }

    @Override
    public Vector getL(Point3D p) {
        return p.subtract(position);
    }

    @Override
    public double getDistance(Point3D point) {
        return position.distance(point);
    }
    //endregion
}
