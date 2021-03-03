package elements;

import primitives.Color;

/**
 * abstract light class
 */
public abstract class Light {
    protected Color intensity;

    /**
     * Constructor
     * @param intensity intensity of the light
     */
    public Light(Color intensity) {
        this.intensity = intensity;
    }

    /**
     * getter method for intensity
     * @return intensity
     */
    public Color getIntensity() {
        return intensity;
    }
}
