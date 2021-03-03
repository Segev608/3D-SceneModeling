package geometries;

import primitives.*;
import java.util.List;
import java.util.LinkedList;
import static primitives.Util.*;

/**
 * Polygon class represents two-dimensional polygon in 3D Cartesian coordinate
 * system
 * 
 * @author Dan
 */
public class Polygon extends Geometry {

    /**
     * List of polygon's vertices
     */
    protected List<Point3D> _vertices;
    /**
     * Associated plane in which the polygon lays
     */
    protected Plane _plane;

    //region Constructors

    /**
     * Polygon constructor based on vertices list. The list must be ordered by edge
     * path. The polygon must be convex.
     * 
     * @param vertices list of vertices according to their order by edge path
     * @throws IllegalArgumentException in any case of illegal combination of
     *                                  vertices:
     *                                  <ul>
     *                                  <li>Less than 3 vertices</li>
     *                                  <li>Consequent vertices are in the same
     *                                  point
     *                                  <li>The vertices are not in the same
     *                                  plane</li>
     *                                  <li>The order of vertices is not according
     *                                  to edge path</li>
     *                                  <li>Three consequent vertices lay in the
     *                                  same line (180&#176; angle between two
     *                                  consequent edges)
     *                                  <li>The polygon is concave (not convex></li>
     *                                  </ul>
     */
    public Polygon(Point3D... vertices) {
        if (vertices.length < 3)
            throw new IllegalArgumentException("A polygon can't have less than 3 vertices");
        _vertices = List.of(vertices);
        // Generate the plane according to the first three vertices and associate the
        // polygon with this plane.
        // The plane holds the invariant normal (orthogonal unit) vector to the polygon
        _plane = new Plane(vertices[0], vertices[1], vertices[2]);
        if (vertices.length == 3) return; // no need for more tests for a Triangle

        Vector n = _plane.getNormal();

        // Subtracting any subsequent points will throw an IllegalArgumentException
        // because of Zero Vector if they are in the same point
        Vector edge1 = vertices[vertices.length - 1].subtract(vertices[vertices.length - 2]);
        Vector edge2 = vertices[0].subtract(vertices[vertices.length - 1]);

        // Cross Product of any subsequent edges will throw an IllegalArgumentException
        // because of Zero Vector if they connect three vertices that lay in the same
        // line.
        // Generate the direction of the polygon according to the angle between last and
        // first edge being less than 180 deg. It is hold by the sign of its dot product
        // with
        // the normal. If all the rest consequent edges will generate the same sign -
        // the
        // polygon is convex ("kamur" in Hebrew).
        boolean positive = edge1.crossProduct(edge2).dotProduct(n) > 0;
        for (int i = 1; i < vertices.length; ++i) {
            // Test that the point is in the same plane as calculated originally
            if (!isZero(vertices[i].subtract(vertices[0]).dotProduct(n)))
                throw new IllegalArgumentException("All vertices of a polygon must lay in the same plane");
            // Test the consequent edges have
            edge1 = edge2;
            edge2 = vertices[i].subtract(vertices[i - 1]);
            if (positive != (edge1.crossProduct(edge2).dotProduct(n) > 0))
                throw new IllegalArgumentException("All vertices must be ordered and the polygon must be convex");
        }
    }

    /**
     * For more info, go to {@link Polygon#Polygon(Point3D...)}
     * @param color The emission
     * @param vertices List of vertices according to their order by edge path
     */
    public Polygon(Color color, Point3D... vertices) {
        this(vertices);
        emission = color;
    }

    /**
     * Constructor
     * @param material The polygon material
     * @param color The polygon self-color
     * @param vertices The polygon's amount of vertices and their definition
     */
    public Polygon(Material material, Color color, Point3D... vertices){
        this(color, vertices);
        this.material = material;
    }
    //endregion

    //region Overrides

    @Override
    public Vector getNormal(Point3D point) {
        return _plane.getNormal();
    }

    @Override
    public List<GeoPoint> findIntersections(Ray ray) {
        // let's check if the ray has any intersection with the polygon's Plane
        List<GeoPoint> planeInter = _plane.findIntersections(ray);
        if(planeInter == null)
            return null;

        //Let's create lists to save our data [vectors and normals]
        List<Vector> vectorList = new LinkedList<>();
        List<Vector> normalList = new LinkedList<>();

        //Calculate the vectors on the Polygon
        for(int i=0; i<_vertices.size(); i++){
            vectorList.add(new Vector(_vertices.get(i).subtract(ray.getStart())));
        }

        //Calculate the normals on the Polygon
        for(int i=0; i<vectorList.size(); i++){
            //computing cross product between every close vertexes
            normalList.add((new Vector(vectorList.get(i).crossProduct(vectorList.get((i+1) % vectorList.size()))))
                    .normalize());
        }

        double sign = Util.alignZero(ray.getDirection().dotProduct(normalList.get(0)));
        if (sign == 0) return null; // value should not be 0.0

        // in order to separate the cases, an initialization accomplished for the first element
        boolean determineSignValue = sign > 0;

        for(int i=1; i<normalList.size(); i++) {
            double value = Util.alignZero(ray.getDirection().dotProduct(normalList.get(i)));

            if(determineSignValue) { // in case the expected sign is (+)
                if (value <= 0)
                    // Just one calculation which has different sign determine that the ray is not inside the polygon
                    return null;
            }
            else // in case the expected sign is (-)
                if(value >= 0)
                    // Just one calculation which has different sign determine that the ray does not inside the polygon
                    return null;
        }

        // if the run made it 'til here - it can return inside point
        planeInter.get(0).geometry = this;
        return planeInter;
    }
    //endregion
}
