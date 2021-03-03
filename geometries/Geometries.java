package geometries;

import primitives.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * This class works as base to the design pattern 'Composite'
 */
public class Geometries implements Intersectable{
    // in order to execute the findIntersections() function on every shape in the composite
    private List<Intersectable> shapeList;

    //region Constructors

    /**
     * Default constructor + Explanation to the LinkedList choice
     */
    public Geometries(){
        // the LinkedList is better for few reasons:
        // insert() -> in LinkedList it takes O(1) vs ArrayList which is O(n)
        // * linked list saves two references to every element on his neighbors in order
        // to make the deletion in O(1)
        // ** while the ArrayList: in case of delete(item) executed need to shift every element one place back
        //                         in case of insert(item) executed need to shift every element one place further
        shapeList = new LinkedList<>();
    }

    /**
     * Constructor for one or more Intersectable objects
     * @param geometries array which stores the inserted elements
     */
    public Geometries(Intersectable... geometries){
        shapeList = new LinkedList<>();
        add(geometries);
    }
    //endregion

    /**
     * add Intersectable elements
     * @param geometries the Intersectable array which stores the geometries elements
     */
    public void add(Intersectable... geometries){
        Collections.addAll(shapeList, geometries);
    }

    @Override
    public List<GeoPoint> findIntersections(Ray ray) {
        List<GeoPoint> intersectionCollection = null;
        List<GeoPoint> temp;
        // iterating over the shape list
        for(Intersectable shape : shapeList){
            //calculating the findIntersections() on every shape
            temp = shape.findIntersections(ray);
            if (temp != null){ // if the current shape has some intersection - insert!
                //building the returned list
                if (intersectionCollection == null)
                    intersectionCollection = new ArrayList<>();
                for(GeoPoint p : temp)
                    intersectionCollection.add(p);
            }
        }

        ray.sortPointByT(intersectionCollection);
        return intersectionCollection;
    }
}
