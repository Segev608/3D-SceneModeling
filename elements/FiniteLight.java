package elements;

import primitives.Color;

/**
 * both Point&Spot light gets area
 * This improve gives us the ability to effect of
 * shadow-value range (instead of 0-1)
 */
public abstract class FiniteLight extends Light implements LightSource {
    // The bigger the radius gets - the scene gets more 'light'.
    // because of the ability to ensure transparency based on
    // light's intersections with shadow rays
    protected double radius;

    //region Constructors

    /**
     * Constructor
     * @param intensity intensity of the light
     */
    public FiniteLight(Color intensity) {
        super(intensity);
        radius = 0;
    }
    //endregion

    /**
     * @return radius value
     */
    public double getRadius() {
        return radius;
    }
}
