package primitives;

/**
 * Represents a point in the 3D space
 */
public class Point3D {

    private Coordinate x;
    private Coordinate y;
    private Coordinate z;

    public static final Point3D ZERO = new Point3D(0,0,0);

    //region Constructors

    /**
     * The input here is based on Coordinate type
     * @param x value in the X field
     * @param y value in the Y field
     * @param z value in the Z field
     */
    public Point3D(Coordinate x, Coordinate y, Coordinate z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }


    /**
     * The input here is based on double type
     * @param x value in the X field
     * @param y value in the Y field
     * @param z value in the Z field
     */
    public Point3D(double x, double y, double z) {
        this.x = new Coordinate(x);
        this.y = new Coordinate(y);
        this.z = new Coordinate(z);
    }


    /**
     * @param point a point in the 3D space
     */
    public Point3D(Point3D point) {
        this.x = point.x;
        this.y = point.y;
        this.z = point.z;
    }
    //endregion

    //region GETTERS
    /**
     * @return the X value
     */
    public Coordinate getX() {
        return x;
    }


    /**
     * @return the Y value
     */
    public Coordinate getY() {
        return y;
    }


    /**
     * @return the Z value
     */
    public Coordinate getZ() {
        return z;
    }

    //endregion

    //region METHODS
    /**
     * @param other Point in the 3D space
     * @return the calculation of the subtraction between them
     */
    public Vector subtract(Point3D other) {
        double subX = (this.x.get() - other.x.get());
        double subY = (this.y.get() - other.y.get());
        double subZ = (this.z.get() - other.z.get());

        return new Vector(subX, subY, subZ);
    }


    /**
     * @param other 3D primitives.Vector
     * @return The calculation of the sum between primitives.Vector and Point in the 3D space
     */
    public Point3D add(Vector other) {
        double addX = this.x.get() + other.getEnd().getX().get();
        double addY = this.y.get() + other.getEnd().getY().get();
        double addZ = this.z.get() + other.getEnd().getZ().get();
        return new Point3D(addX, addY, addZ);
    }


    /**
     * @param other Point in the 3D space
     * @return (distance between two points) ^ 2
     */
    public double distanceSquared(Point3D other) {
        double subX = this.x.get() - other.x.get();
        double subY = this.y.get() - other.y.get();
        double subZ = this.z.get() - other.z.get();

        // using pow as a*a and not as Math.pow()
        subX = (subX * subX);
        subY = (subY * subY);
        subZ = (subZ * subZ);

        return (subX + subY + subZ);
    }


    /**
     * @param other Point in the 3D space
     * @return distance between two points
     */
    public double distance(Point3D other){
        return Math.sqrt(distanceSquared(other));
    }


    /**
     * @return Print the class field as - (x,y,z)
     */
    @Override
    public String toString() {
        return "(" + x + "," + y + "," + z + ')';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // if I's me - return true
        if (obj == null || getClass() != obj.getClass()) return false; // if I'm compared to null or to someone who not me - return false
        Point3D point3D = (Point3D) obj;
        return x.equals(point3D.x) && y.equals(point3D.y) && z.equals(point3D.z);
    }


    //endregion

}

