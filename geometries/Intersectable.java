package geometries;

import primitives.*;
import java.util.ArrayList;
import java.util.List;

/**
 * In order to force geometry implement findIntersections()
 */
public interface Intersectable {

    /**
     * the method calculate the intersection points on the Intersectable with a ray
     * @param ray the given ray
     * @return list of intersections as Point3D
     */
    List<GeoPoint> findIntersections(Ray ray);


    /**
     * static class
     * By using Intersectable.GeoPoint we'll have access to
     * all the shapes in Geometry and it'll help us later in
     * Identify the shapes in the scene by a point
     */
    class GeoPoint{
        public Geometry geometry;
        public Point3D point;

        /**
         * Constructor
         * @param geometry the current geometry
         * @param point a point on the geometry (one of the intersections)
         */
        public GeoPoint(Geometry geometry, Point3D point){
            this.geometry = geometry;
            this.point = point;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false; //definitely not this
            GeoPoint geoPoint = (GeoPoint) obj;
            // The equality is checking that it's the same geometry shape AND the points are identical
            return geometry.equals(geoPoint.geometry) && point.equals(geoPoint.point);
        }

        /**
         * Useful function mainly for the Geometry that implements findIntersection() method
         * which in their test, dealing with comparison between List<Point3D> and List<GeoPoints>.
         * It's even more useful due to the ability to send every Geometry type object and activating
         * the polymorphism to get it.
         * @param shape in order to convert Point3D to GeoPoints we need to determine the sort of the shape
         * @param points the list of the points which we need to convert into List<GeoPoints>
         * @return List<GeoPoints>
         */
        public static List<GeoPoint> convertPointsToGPoints(Geometry shape, List<Point3D> points){
            if(points == null)
                return null;
            List<GeoPoint> ret = new ArrayList<>();
            for(int i=points.size() - 1; i>=0; i--)
                //return new GeoPoint - signed by this shape
                ret.add(new GeoPoint(shape, points.get(i)));
            return ret;
        }
    }

}
