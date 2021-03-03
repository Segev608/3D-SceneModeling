package elements;

import primitives.*;
import java.util.List;
import java.util.LinkedList;

/**
 * Camera class implementation
 * [The position where all the rays are coming from
 * (In order to avoid the opposite implementation
 * in which the camera 'gathering' all the rays from the environment)]
 */
public class Camera {
    private Point3D position;
    private Vector vUp;
    private Vector vTo;
    private Vector vRight;

    //region Constructor

    /**
     * Constructor
     *
     * @param pos the current position of the camera
     * @param vT  the vector toward the view plane
     * @param vU  the vector up (parallel to the view plane)
     */
    public Camera(Point3D pos, Vector vT, Vector vU) {
        if (vT.dotProduct(vU) != 0)
            throw new IllegalArgumentException("Vectors aren't orthogonal");
        vRight = vT.crossProduct(vU).normalize();
        vTo = vT.normalized();
        vUp = vU.normalized();
        position = new Point3D(pos);
    }
    //endregion

    //region Getters

    /**
     * get the position of the camera
     *
     * @return position value
     */
    public Point3D getPosition() {
        return position;
    }

    /**
     * get the Vector up of the camera
     *
     * @return Vector up value
     */
    public Vector getVUp() {
        return vUp;
    }

    /**
     * get the Vector towards the view plane, of the camera
     *
     * @return Vector towards value
     */
    public Vector getVTo() {
        return vTo;
    }

    /**
     * get the Vector right of the camera
     *
     * @return Vector right value
     */
    public Vector getVRight() {
        return vRight;
    }
    //endregion

    //region Construct Rays

    /**
     * Main method to handle *single-ray* projection through view plane
     * @param nX             Total number columns
     * @param nY             Total number rows
     * @param j              Column of the given cell
     * @param i              Row of the given cell
     * @param screenDistance Distance from camera to the view plane
     * @param screenWidth    Width of the screen
     * @param screenHeight   Height of the screen
     * @return A ray from the camera to the center of the given cell
     */
    public Ray constructRayThroughPixel(int nX, int nY,
                                        int j, int i, double screenDistance,
                                        double screenWidth, double screenHeight) {
        //The center of the view plane is defined by the camera position
        //+ the distance we chose to the view plane
        Point3D pCenter = position.add(vTo.scale(screenDistance));
        double rX = screenWidth / nX; //pixel width
        //The formula in line 98 and 100 refers to the vector offset
        //that we need to calculate in order to obtain the (j,i) cell
        //in the view plane
        double xJ = (j - nX / 2.0) * rX + rX / 2;
        double rY = screenHeight / nY;//pixel height
        double yI = (i - nY / 2.0) * rY + rY / 2;
        Point3D pIJ = pCenter;
        //Applying the vectors offsets that we've just found on the
        //center ViewPlane's point to find (j,i) cell
        if (xJ != 0) pIJ = pIJ.add(vRight.scale(xJ));
        if (yI != 0) pIJ = pIJ.add(vUp.scale(-yI));
        Vector dir = pIJ.subtract(position);
        return new Ray(position, dir);
    }

    /**
     * Secondary method to handle *multiple-ray* projection through pixel in view plane
     * @param nX             Total number columns
     * @param nY             Total number rows
     * @param j              Column of the given cell
     * @param i              Row of the given cell
     * @param screenDistance Distance from camera to the view plane
     * @param screenWidth    Width of the screen
     * @param screenHeight   Height of the screen
     * @param interX         Total number of inner columns
     * @param interY         Total number of inner rows
     * @return               List of all rays through the given pixel
     */
    public List<Ray> constructRaysThroughPixel(int nX, int nY,
                                          int j, int i, double screenDistance,
                                          double screenWidth, double screenHeight,
                                          int interX, int interY) {
        List<Ray> rays = new LinkedList<>();
        //In order to achieve the 'sub-pixel' effect, there's need to
        //multiply the rows/columns by the amount of sub pixels we want
        int newNX = nX*interX;
        int newNY = nY*interY;
        int startJ = j*interX;
        int startI = i*interY;
        // The idea is to start iterating from the first pixel in the grid and by using our
        // new division (in order to create beam in certain pixel) we adding those offset (interX & interY) 'til
        // we've reached the final 'new' pixel in the grid
        for(int interJ = startJ; interJ<startJ + interX; interJ++)
            for (int interI = startI; interI<startI + interY;interI++) {
                //We're referring every pixel as a whole new grid!
                rays.add(constructRayThroughPixel(newNX, newNY, interJ, interI, screenDistance, screenWidth, screenHeight));
            }
        return rays;
    }

    /**
     * method to handle *4* projection through pixel, side-function for the adaptiveSuperSampling recursion method
     * @param nX             Total number columns
     * @param nY             Total number rows
     * @param j              Column of the given cell
     * @param i              Row of the given cell
     * @param screenDistance Distance from camera to the view plane
     * @param screenWidth    Width of the screen
     * @param screenHeight   Height of the screen
     * @return list of 4 rays to the wanted positions in the pixel
     */
    public List<Ray> constructRaysThroughPixelEdges(int nX, int nY,
                                               int j, int i, double screenDistance,
                                               double screenWidth, double screenHeight) {
        Point3D pCenter = position.add(vTo.scale(screenDistance));
        //getting the topLeft cell by using the central pixel to move [up + left]
        Point3D topLeft = pCenter.add(vRight.scale(-screenWidth/2));
        topLeft = topLeft.add(vUp.scale(screenHeight/2));

        //get pixel sizes
        double rX = screenWidth / nX;
        double rY = screenHeight / nY;

        List<Ray> ret = new LinkedList<>();
        Point3D offset;
        for (int r=0; r<=1; ++r)
            for (int c=0; c<=1; ++c) {
                //in order to obtain the specific point (upper left/right lower left/right)
                double s1 = rX*(j + r); //moving point right j+1 steps if r=1
                double s2 = -rY*(i + c); //moving the point down i+1 steps if c=1

                //Avoiding multiplication by zero
                offset = s1 == 0? topLeft : topLeft.add(vRight.scale(s1));
                offset = s2 == 0? offset : offset.add(vUp.scale(s2));
                Vector dir = offset.subtract(position);
                ret.add(new Ray(position, dir));
            }
        return ret;
    }
    //endregion

}
