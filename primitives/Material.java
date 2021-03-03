package primitives;

/**
 * Represents a material.Phong model gives us the ability to describe the way a surface reflects
 * light as a combination of Diffusive & Specular scalars + Ambient (From the scene) + Emission (From object)
 */
public class Material {
    private double kD;
    private double kS;
    private int nShininess;
    private double kT;
    private double kR;

    //region Constructors

    /**
     * Constructor
     * @param kD Diffuse scalar -  the ratio of reflection of the diffuse term of incoming light
     * @param kS Specular scalar -  the ratio of reflection of the specular term of incoming light
     * @param nShininess  shininess constant for this material
     */
    public Material(double kD, double kS, int nShininess) {
        this(kD, kS, nShininess, 0, 0);
    }

    /**
     * Constructor
     * @param kD Diffuse scalar
     * @param kS Specular scalar
     * @param nShininess shininess of the material
     * @param kT Transparency scalar
     * @param kR Reflection scalar
     */
    public Material(double kD, double kS, int nShininess, double kT, double kR) {
        this.kD = kD;
        this.kS = kS;
        this.nShininess = nShininess;
        this.kT = kT;
        this.kR = kR;
    }
    //endregion

    //region Getters

    /**
     * @return kD field
     */
    public double getKD() {
        return kD;
    }

    /**
     * @return kS field
     */
    public double getKS() {
        return kS;
    }

    /**
     * @return nShininess field
     */
    public int getNShininess() {
        return nShininess;
    }

    /**
     * @return kT field
     */
    public double getKT() {
        return kT;
    }

    /**
     * @return kR field
     */
    public double getKR() {
        return kR;
    }
    //endregion
}
