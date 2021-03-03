package elements;

import primitives.Color;

/**
 * AmbientLight class implementation
 * [The light of the background scene. (places where there is no intersections)]
 */
public class AmbientLight extends Light {
    /**
     * Constructor
     * @param c color
     * @param k Attenuation coefficient (brightness scalar)
     */
    public AmbientLight(Color c, double k){
        super(c.scale(k));
    }

}
