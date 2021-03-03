package renderer;

import elements.*;
import scene.Scene;
import primitives.*;
import java.util.LinkedList;
import java.util.List;
import static primitives.Util.*;
import static geometries.Intersectable.GeoPoint;

/**
 * Render class implementation
 */
public class Render {
    private ImageWriter writeImage;
    private Scene scene;
    //turn on and off features
    private boolean softShadow = false;
    private boolean superSampling = false;
    // The amount of rays which would be generated to create the soft Shadow effect
    private int softShadowRays = 30;
    // Max depth of adaptive super sampling recursion
    private int maxSamplingLevel = 2;

    private int threads = 1;
    private final int SPARE_THREADS = 2; // Spare threads if trying to use all the cores
    private boolean print = false; // printing progress percentage

    // In order to stop the recursion, we must add some MAX calculation constant values
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    // The minimum factor in the recursion. values that reached under that, should be defined as shadow
    private static final double MIN_CALC_COLOR_K = 0.001;



    //region Constructors

    /**
     * Constructor
     * Soft Shadow feature is turned off
     * @param Iw    Used to create a jpg file
     * @param scene the scene to render
     */
    public Render(ImageWriter Iw, Scene scene) {
        writeImage = Iw;
        this.scene = scene;
    }

    /**
     * Constructor
     * @param writeImage Used to create a jpg file
     * @param scene the scene to render
     * @param softShadows Number of Shadow Rays.
     * @param superSamplings Number of pixel sampling
     */
    public Render(ImageWriter writeImage, Scene scene, int softShadows, int superSamplings) {
        this(writeImage, scene);
        setSoftShadowRays(softShadows);
        setSuperSamplings(superSamplings);
    }

    /**
     * Constructor
     * @param writeImage Used to create a jpg file
     * @param scene the scene to render
     * @param softShadow turn on/off the Soft Shadow feature.
     * @param superSampling turn on/off the Super Sampling feature
     */
    public Render(ImageWriter writeImage, Scene scene, boolean softShadow, boolean superSampling) {
        this(writeImage, scene);
        this.softShadow = softShadow;
        this.superSampling = superSampling;
    }

    //endregion

    //region Pixel Management
    /**
     * Pixel is an internal helper class whose objects are associated with a Render object that
     * they are generated in scope of. It is used for multithreading in the Renderer and for follow up
     * its progress.
     * There is a main follow up object and several secondary objects - one in each thread.
     */
    public class Pixel {
        private long maxRows = 0;     // Ny
        private long maxCols = 0;     // Nx
        private long pixels = 0;      // Total number of pixels: Nx*Ny
        public volatile int row = 0;   // Last processed row
        public volatile int col = -1;  // Last processed column
        private long counter = 0;     // Total number of pixels processed
        private int percents = 0;     // Percent of pixels processed
        private long nextCounter = 0;

        /**
         * The constructor for initializing the main follow up Pixel object
         * @param maxRows the amount of pixel rows
         * @param maxCols the amount of pixel columns
         */
        public Pixel(int maxRows, int maxCols) {
            this.maxRows = maxRows;
            this.maxCols = maxCols;
            pixels = maxRows * maxCols;
            nextCounter = pixels / 100;
            // reaching to the Render object and using the instance to check if user wants to print progress
            if (Render.this.print) System.out.println("0%");
        }

        /**
         *  Default constructor for secondary Pixel objects
         */
        public Pixel() {}

        /**
         * Public function for getting next pixel number into secondary Pixel object.
         * The function prints also progress percentage in the console window.
         * @param target target secondary Pixel object to copy the row/column of the next pixel
         * @return true if the work still in progress, -1 if it's done
         */
        public boolean nextPixel(Pixel target) {
            int percents = nextP(target);
            if (print && percents > 0)
                System.out.println(percents + "%");
            if (percents >= 0) return true;
            if (print)
                System.out.println("100%");
            return false;
        }

        /**
         * Internal function for thread-safe manipulating of main follow up Pixel object - this function is
         * critical section for all the threads, and main Pixel object data is the shared data of this critical
         * section.
         * The function provides next pixel number each call.
         * @param target target secondary Pixel object to copy the row/column of the next pixel
         * @return the progress percentage for follow up: if it is 0 - nothing to print, if it is -1 - the task is
         * finished, any other value - the progress percentage (only when it changes)
         */
        private synchronized int nextP(Pixel target) {
            //increase progress for each access
            ++col; ++counter;
            if (col < maxCols) {
                target.row = this.row; target.col = this.col;
                if (print && counter == nextCounter) {
                    ++percents;nextCounter = pixels * (percents + 1) / 100; return percents;
                }
                return 0;
            }
            ++row;
            if (row < maxRows) {
                col = 0;
                if (print && counter == nextCounter) {
                    ++percents;
                    nextCounter = pixels * (percents + 1) / 100; return percents;
                }
                writeToImage();
                return 0;
            }
            return -1;
        }

    }
    //endregion

    //region Public Methods

    //region Setters

    /**
     * Set multithreading
     * if the parameter is 0 - number of coress less SPARE (2) is taken
     * @param threads number of threads
     * @return the Render object itself
     */
    public Render setMultithreading(int threads) {
        if (threads < 0) throw new IllegalArgumentException("Multithreading must be 0 or higher");
        if (threads != 0) this.threads = threads;
        else {
            // initialize no. of thread to the amount of spare cores
            int cores = Runtime.getRuntime().availableProcessors() - SPARE_THREADS;
            this.threads = cores <= 2 ? 1 : cores;
        }
        return this;
    }

    /**
     * Set debug printing on
     * @return the Render object itself
     */
    public Render setDebugPrint() { print = true; return this; }

    public Render setSoftShadowRays(int softShadows) {
        if(softShadows > 0) {
            this.softShadow = true;
            this.softShadowRays = softShadows;
        } else
            softShadow = false;
        return this;
    }

    public Render setSuperSamplings(int superSamplings) {
        if(superSamplings > 0) {
            this.superSampling = true;
            this.maxSamplingLevel = superSamplings;
        } else
            this.superSampling = false;
        return this;
    }
    //endregion

    //region Picture Management

    /**
     * uses the ImageWriter instance to write to image
     */
    public void writeToImage() {
        writeImage.writeToImage();
    }

    /**
     * takes the camera and the ImageWriter
     * and creates color to the view plane based on the data from the intersections
     * (in case he find intersection => color it, else => color in background color)
     */
    public void renderImage() {
        // Initialization [camera & viewPlane properties]
        int nX = writeImage.getNx();
        int nY = writeImage.getNy();
        Color background = scene.getBackground();
        Camera camera = scene.getCamera();
        double dist = scene.getDistance();
        double width = writeImage.getWidth();
        double height = writeImage.getHeight();

        // Constructing the threads
        // Main pixel management object, In case he is the nextP - It's mean rendering completed
        // this pixel holds the values between changes in the threads scenario.
        // In case threads starts to work, he checks it and gets the right values in order to
        // proceed the render job.
        final Pixel thePixel = new Pixel(nY, nX);
        Thread[] threadPool = new Thread[threads];
        for (int i = threads - 1; i >= 0; --i) { // create all threads
            threadPool[i] = new Thread(() -> {
                // Takes one pixel to work on
                Pixel pixel = new Pixel(); // Auxiliary thread’s pixel object
                while (thePixel.nextPixel(pixel)) {
                    Color paint;
                    if (superSampling) {
                        paint = adaptiveSuperSampling(nX, nY, pixel.col, pixel.row, maxSamplingLevel);
                    } else {
                        Ray ray = camera.constructRayThroughPixel(nX, nY, pixel.col, pixel.row, dist, width, height);
                        GeoPoint cp = findClosestIntersection(ray);
                        paint = cp==null? background : calcColor(cp, ray);
                    }
                    writeImage.writePixel(pixel.col, pixel.row, paint.getColor());
                }});
        }
        for (Thread thread : threadPool) thread.start(); // Start all the threads
        // Wait for all threads to finish [just thread that finished his job could go on into line 228]
        for (Thread thread : threadPool) try { thread.join(); } catch (Exception ignored) {}
        if (print) System.out.println("100%"); // Print 100%

    }

    /**
     * insert colorful grid into the scene
     * @param interval the distance between each grid line
     * @param color    the color of the grid's lines
     */
    public void printGrid(int interval, java.awt.Color color) {
        for (int i = 0; i < writeImage.getNy(); i++) {
            for (int j = 0; j < writeImage.getNx(); j++) {
                if (i % interval == 0 || j % interval == 0)
                    writeImage.writePixel(j, i, color);
            }
        }
    }
    //endregion

    //endregion

    //region Private Methods

    //region calcColor

    /**
     * checks for closest intersections and calculates average color
     * [closest Intersections for ray - get color
     * No Intersection - background color]
     * @param rays Ray beam from the camera
     * @return average color
     */
    private Color calcColor(List<Ray> rays){
        Color background = scene.getBackground();
        Color avg = new Color(0,0,0);

        for (Ray ray : rays) {
            GeoPoint closestPoint = findClosestIntersection(ray);
            if (closestPoint == null)
                avg = avg.add(background);
            else
                avg = avg.add(calcColor(closestPoint, ray));
        }
        return avg.reduce(rays.size());
    }

    /**
     * uses the main calcColor method to determine the specific color in the scene
     * @param geoP The current 3D point it about to color (or not)
     * @param inRay The ray that intersects with the geometry firstly, from View plane
     * @return Accurate color + lighting
     */
    private Color calcColor(GeoPoint geoP, Ray inRay){
        if (geoP == null)
            return scene.getBackground();
        Color color = calcColor(geoP, inRay, MAX_CALC_COLOR_LEVEL, 1.0);
        color = color.add(scene.getAmbientLight().getIntensity());
        return color;
    }


    /**
     * Calculate color based on current point - the main method
     * @param p     the current 3D point which it being colored
     * @param inRay the ray from the view plane
     * @param level The remaining depth of the recursion
     * @param k     Current attenuation factor
     * @return the Intensity from scene
     */
    private Color calcColor(GeoPoint p, Ray inRay, int level, double k) {

        //Stop condition
        if (level == 0 || k < MIN_CALC_COLOR_K)
            return Color.BLACK;

        //region Initialization
        Color color = p.geometry.getEmission();
        Vector v = inRay.getDirection();
        Vector n = p.geometry.getNormal(p.point);
        int nSh = p.geometry.getMaterial().getNShininess();
        double kd = p.geometry.getMaterial().getKD();
        double ks = p.geometry.getMaterial().getKS();
        //endregion

        //region Handle Light Sources

        Vector l;
        List<LightSource> sceneLightSources = scene.getLights();
        if (sceneLightSources != null) {
            for (LightSource light : sceneLightSources) {
                l = light.getL(p.point).normalize();
                if (Math.signum(n.dotProduct(l)) == Math.signum(n.dotProduct(v))) {
                    // Just if the ray from the current GeoPoint and the light source
                    // has intersection in the middle => define the current place as shaded
                    double ktr = transparency(light, l, n, p);

                    // Now, the *transparency* would create the shadow's effect
                    if (ktr * k > MIN_CALC_COLOR_K) {
                        Color lightInt = (light.getIntensity(p.point)).scale(ktr);
                        color = color.add(calcDiffusive(kd, l, n, lightInt),
                                calcSpecular(ks, l, n, v, nSh, lightInt));
                    }
                }
            }
        }
        //endregion

        //region Handle Deflected Rays
        double kr = p.geometry.getMaterial().getKR();
        double kkr = k * kr;
        if (kkr > MIN_CALC_COLOR_K) {
            Ray reflectedRay = constructReflectedRay(n, p.point, inRay);
            GeoPoint reflectedPoint = findClosestIntersection(reflectedRay);

             // In order to avoid working with null point
            if (reflectedPoint != null) {
                // Recursive call for a reflected ray
                Color reflectedLight = calcColor(reflectedPoint, reflectedRay, level - 1, kkr).scale(kr);
                color = color.add(reflectedLight);
            }
        }

        double kt = p.geometry.getMaterial().getKT();
        double kkt = k * kt;
        if (kkt > MIN_CALC_COLOR_K) {
            Ray refractedRay = constructRefractedRay(n, p.point, inRay);
            GeoPoint refractedPoint = findClosestIntersection(refractedRay);

            // In order to avoid working with null point
            if (refractedPoint != null) {
                // Recursive call for a refracted ray
                Color refractedLight = calcColor(refractedPoint, refractedRay, level - 1, kkt).scale(kt);
                color = color.add(refractedLight);
            }
        }
        //endregion

        return color;
    }
    //endregion

    //region Deflected Rays

    /**
     * Calculating the refracted ray (caused by the intersection angle)
     * @param normal the normal of the surface
     * @param p the point where the refraction starts
     * @param r the ray that being calculate in order to create the refraction effect
     * @return refracted ray
     */
    private Ray constructRefractedRay(Vector normal, Point3D p, Ray r) {
        // Because of in snell's law: n2 * sin(thetaR) = n1 * sin(thetaI)
        // but in our case n1 = n2 = 1
        // [The casing thickness size in our geometries is VERY close to zero]
        // that means that the hit angle is equal to the return angel
        // so the ray is basically continue in the same direction
        return new Ray(p, r.getDirection(), normal);
    }

    /**
     * Implementing the calculation of the reflected ray
     * @param normal        the normal to the current surface
     * @param intersectionP the intersection point of V ray
     * @param v             the original ray
     * @return reflected ray from the surface
     */
    private Ray constructReflectedRay(Vector normal, Point3D intersectionP, Ray v) {
        // Calculating the formula r = v - 2(v*n)*n;

        //2*v*n =
        double dotP = alignZero(2 * v.getDirection().dotProduct(normal));

        // Avoiding scaling by 0
        if (dotP == 0)
            return null;

        Vector reflectedVec = v.getDirection().subtract(normal.scale(dotP));
        return new Ray(intersectionP, reflectedVec, normal);
    }
    //endregion

    //region Phong Reflectance Model

    /**
     * Calculating the specular intensity from the surface
     * @param ks Specular factor
     * @param l Vector from light
     * @param n Normal to the surface
     * @param v Vector from the camera
     * @param nSh Shininess factor
     * @param lightInt Intensity of the surface
     * @return The specular intensity
     */
    private Color calcSpecular(double ks, Vector l, Vector n, Vector v, int nSh, Color lightInt) {
        Vector r =  isZero(l.dotProduct(n))? l : l.subtract(n.scale(2 * l.dotProduct(n))).normalize();
        double negvr = Util.alignZero(-v.dotProduct(r));
        return lightInt.scale(ks * Math.pow(Math.max(0, negvr), nSh));
    }

    /**
     * Calculating the diffusive intensity from the surface
     * @param kd Diffusive factor
     * @param l Vector from light
     * @param n Normal to the surface
     * @param lightIntensity Intensity of the surface
     * @return The diffusive intensity
     */
    private Color calcDiffusive(double kd, Vector l, Vector n, Color lightIntensity) {
        return lightIntensity.scale(kd * Math.abs(l.dotProduct(n)));
    }
    //endregion

    //region Shadowing & Transparency

    /**
     * Calculating the transparency factor of a point
     * @param ls the current light source (from the calcColor method)
     * @param l  direction vector of the current light
     * @param n  the normal from the surface
     * @param gp the current geometry (The transparency should be based on the material and so on)
     * @return the transparency factor of the given point from the given light source
     */
    private double transparency(LightSource ls, Vector l, Vector n, GeoPoint gp) {
        // In order to check if there is any intersection from direction of the
        // light source, there is need to create the ray that represents this opposite vector
        Point3D point = gp.point;
        Vector lightDirection = l.scale(-1.0);
        Ray shadowRay = new Ray(point, lightDirection, n);

        return softShadow? getSoftKTR(shadowRay, ls) : getKTR(shadowRay, ls.getDistance(point));
    }

    /**
     * Check if the current point should be shaded.
     * @param l  the Vector that comes from the light source to the scene
     * @param gp the current shape that we calculating intersection with in order to color\to shade
     * @return True if unshaded else false
     */
    private boolean unshaded(Vector l, Vector n, GeoPoint gp) {
        // In order to check if there is any intersection from direction of the
        // light source, there is need to create the ray that represents this opposite vector
        Vector lightDirection = l.scale(-1.0);
        Ray lightRay = new Ray(gp.point, lightDirection, n);

        // Activating the findIntersections() method on the current shape
        List<GeoPoint> intersections = scene.getGeometries().findIntersections(lightRay);

        // In case there is no intersections - the light is getting there and there is need to color that place
        // else - make that place shaded
        if (intersections == null)
            return true;

        for (GeoPoint geoP : intersections)
            if (geoP.geometry.getMaterial().getKT() == 0)
                return false;
        return true;
    }

    /**
     * calculating ray's start ktr
     * @param ray the given ray
     * @param dist the distance from the light source
     * @return ray's start ktr
     */
    private double getKTR(Ray ray, double dist) {
        // Activating the findIntersections() method on the current shape
        List<GeoPoint> intersections = scene.getGeometries().findIntersections(ray);

        // In case there is no intersections - the light is getting there and there is need to color that place
        // else - make that place shaded
        if (intersections == null)
            return 1;
        intersections.removeIf(gp -> Util.alignZero(gp.point.distance(ray.getStart()) - dist) > 0);
        double ktr = 1;
        for (GeoPoint gp : intersections)
            ktr *= gp.geometry.getMaterial().getKT();
            if (ktr < MIN_CALC_COLOR_K)
                return 0;
        return ktr;
    }
    //endregion

    //region Soft Shadow

    /**
     * In order to take care of the KTR factor in the SOFT_SHADOW feature
     * In case the current LightSource isn't type of Finite light (no radios) - calculate with regular KTR (directional)
     * @param mainRay ray from the current scene's intersection to lightSource ls
     * @param ls LightSource in the current calculation
     * @return KTR factor [SOFT_SHADOW_ON\OFF]
     */
    private double getSoftKTR(Ray mainRay, LightSource ls) {
        Point3D start = mainRay.getStart();
        double lightDist = ls.getDistance(start);
        //In case ls is directional light - use the normal KTR method (it has no radios)
        if (!(ls instanceof FiniteLight))
            return getKTR(mainRay, lightDist);

        FiniteLight fls = (FiniteLight)ls;
        List<Vector> dirs = constructSoftVectors(mainRay, lightDist, fls.getRadius());
        double sum = 0;
        //Iterating over all the rays in the beam and checking the rate of intersect
        for (Vector dir : dirs) {
            lightDist = dir.length();
            Ray softRay = new Ray(start, dir);
            sum += getKTR(softRay, lightDist);
        }
        return sum/ softShadowRays;
    }

    /**
     * Creates spiral-shape from rays toward the light source
     * @param mainRay backbone of the entire beam (the center of the spiral)
     * @param dist distance between the start-position of the ray (intersection place) and the light
     * @return list of all the rays in the beam
     */
    private List<Vector> constructSoftVectors(Ray mainRay, double dist, double rad) {
        // implementing Archimedean spiral.
        // in polar coordinate system, the function is r = a + b*theta
        // in our case, a=0 so it starts in the center,
        // and b will make r to be the radius of the circle at max rotation.
        // in our case, the spiral shape has 4 rotation (each has 2 pi)
        double maxRotation = 8*Math.PI;
        double b = rad/maxRotation;
        List<Vector> ret = new LinkedList<>();
        Vector n = mainRay.getDirection();
        Point3D center = mainRay.getStart().add(mainRay.getDirection().scale(dist));
        //using the method from Vector class to calculate orthogonal vector to n
        Vector orth = n.getOrthogonal();
        // creates the rays, every iteration the rotation is increasing by (maxRotation/SOFT_SHADOW_RAYS)
        for (int i = 0; i < softShadowRays; i++) {
            double theta = i*(maxRotation/ softShadowRays);
            Vector rot = orth.rotate(n,theta);
            double r = alignZero(b*theta); // size factor to the center point
            Point3D end = r==0? center : center.add(rot.scale(r));
            Vector dir = end.subtract(mainRay.getStart()); // vector to the current end-point ray
            ret.add(dir);
        }
        return ret;
    }
    //endregion

    //region Super Sampling

    /**
     * Improved and efficient method to the SuperSampling feature
     * Instead of creating equal distribution of rays next to the geometry casing
     * This method checks with recursion if the current place has uniform color value:
     * - If it does happens - return this color
     * - Else, start recursion at the center of the pixel and divide it into four parts and check again!
     * @param nX View plane width
     * @param nY View plane height
     * @param j The current pixel we working on (the X axes)
     * @param i The current pixel we working on (the Y axes)
     * @param level The remaining depth of the recursion
     * @return the accurate color to this specific pixel + SuperSampling *efficient* implementation
     */
    private Color adaptiveSuperSampling(int nX, int nY, int j, int i, int level) {
        //Initializations
        Camera camera = scene.getCamera();
        double distance = scene.getDistance();
        double width = writeImage.getWidth();
        double height = writeImage.getHeight();
        //get the rays from the camera which goes through this specific pixel
        List<Ray> edges = camera.constructRaysThroughPixelEdges(nX, nY, j, i, distance, width, height);
        if (level <= 0) // Stopping condition - In order to limit the recursion
            return calcColor(edges);
        Color firstColor = isColorSame(edges);
        if (firstColor != null) // In case they all equal - return one of them
            return firstColor;
        Color ret = new Color(0, 0, 0);
        //Because of we're increasing the resolution of the current pixel
        //(in order to apply the recursion on one of the 4 sub-pixel)
        //There is need to increase the pixel amount (both in width/height) by 2
        int newNX = nX * 2;
        int newNY = nY * 2;
        //In order to iterate in the sub-pixel, it need to be on the same scale)
        int startJ = j * 2;
        int startI = i * 2;
        for (int interJ = startJ; interJ < startJ + 2; interJ++)
            for (int interI = startI; interI < startI + 2; interI++)
                //applying the recursion on 4 sub-pixels
                ret = ret.add(adaptiveSuperSampling(newNX, newNY, interJ, interI, level-1));
        //Eventually, when the recursion stops (in every sub-pixel) it goes here
        //to get the average of the 4 rays color
        return ret.reduce(4);
    }

    /**
     * Renders picture with super sampling
     */
    private void superSampling() {
        //region Initialization
        Camera camera = scene.getCamera();
        Color background = scene.getBackground();
        double distance = scene.getDistance();
        int nX = writeImage.getNx();
        int nY = writeImage.getNy();
        double width = writeImage.getWidth();
        double height = writeImage.getHeight();
        //endregion

        for (int i = 0; i < nY; i++) {
            for (int j = 0; j < nX; j++) {
                Color avg = new Color(0, 0, 0);
                List<Ray> rays = camera.constructRaysThroughPixel(nX, nY, j, i, distance, width, height, 4, 4);
                for (Ray ray : rays) {
                    GeoPoint closestPoint = findClosestIntersection(ray);
                    if (closestPoint == null)
                        avg = avg.add(background);
                    else
                        avg = avg.add(calcColor(closestPoint, ray));
                }
                avg = avg.reduce(rays.size());
                writeImage.writePixel(j, i, avg.getColor());
            }
        }
    }

    /**
     * Renders picture without super sampling
     */
    private void sampling() {
        //region Initialization
        Camera camera = scene.getCamera();
        java.awt.Color background = scene.getBackground().getColor();
        double distance = scene.getDistance();
        int nX = writeImage.getNx();
        int nY = writeImage.getNy();
        double width = writeImage.getWidth();
        double height = writeImage.getHeight();
        //endregion

        for (int i = 0; i < nY; i++) {
            for (int j = 0; j < nX; j++) {
                Ray ray = camera.constructRayThroughPixel(nX, nY, j, i, distance, width, height);
                GeoPoint closestPoint = findClosestIntersection(ray);
                if (closestPoint == null)
                    writeImage.writePixel(j, i, background);
                else
                    writeImage.writePixel(j, i, calcColor(closestPoint, ray).getColor());
            }
        }
    }
    //endregion

    //region Helpful Functions

    /**
     * return the closest GeoPoint in all the intersections
     * @param r the ray from the camera through the view plane
     * @return closest geoPoint
     */
    private GeoPoint findClosestIntersection(Ray r) {
        List<GeoPoint> intersectionsL = scene.getGeometries().findIntersections(r);
        if (intersectionsL == null)
            return null;
        // Geometries.findIntersection is sorting the list by the intersections points location
        return intersectionsL.get(0);
    }

    /**
     * determine if all the rays' closets intersections has the same color
     * @param rays List of rays from the view plane
     * @return SAME- return this same color| NOT_SAME- returns null
     */
    private Color isColorSame(List<Ray> rays) {
        GeoPoint closestPoint = findClosestIntersection(rays.get(0));
        Color firstColor = calcColor(closestPoint, rays.get(0));
        Color currColor;
        boolean isAllSame = true;
        for (int e = 1; e < rays.size(); ++e) {
            closestPoint = findClosestIntersection(rays.get(e));
            currColor = calcColor(closestPoint, rays.get(e));
            if (!firstColor.equals(currColor))
                isAllSame = false;
        }
        return isAllSame? firstColor : null;
    }
    //endregion

    //endregion

}