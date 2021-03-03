# 3D-SceneModeling
This project is primarily about learning how to design the code in an orderly fashion according to the methods that are actually performed, striving for the most advanced and efficient cutting.

## Step 1 - Defining primitives
At this point the building blocks are created for the advanced shapes.
- ***Util***: calculation accuracy.
- ***Coordinate***: A single (x,y,z) value that uses Util values.
- ***Vector***: Size and direction, uses three Coordinate values
- ***Ray***: The set of points on a line that are on one side relative to a given point on the line.

## Step 2 - Defining the Geometries 
Once we have defined mathematical values that allow definitions on a Cartesian axis system, we proceed to definitions of shapes.
### Flat shapes
- ***Plane***: An infinite plane. Defined using the normal vector and a point
- ***Polygon***: Able to define in the Cartesian axis space any shape with n vertices
### Radial Shapes
All of these entities, except for their usual definitions, need a radius property [***Shpere***, ***Tube***, ***Cylinder***]. This step also includes TDD (i.e. designing tests and writing the appropriate code) but you can simply look at the unittest package and see what was designed (both EP and BVA level).

## Step 3 - Implementation of intersection bewteen a beam and a geometry
Physically, the rays of light reach our eyes from the environment after hitting a variety of objects around us. Given that in the natural model there are an infinity of rays that hits our eyes, it will not be possible to implement software that processes all the information coming from the rays. Thus, the idea would be to change the direction and set the rays out of the viewer's gaze so that the rays could be launched to all areas of the scene.

Thus, we must allow in the software the mathematics capable of calculating the intersections between the rays and the geometries and returning the points of intersection (for the sake of three-dimensional modeling)

## Step 4 - Creating the camera and generating the rays
At this point, the goal is to create the ***camera*** so that we have several options:
- Using three vectors, we will control its orientation in space.
- Create the rays in all directions in order to scan the whole scene (the more rays there are - the higher the resolution of the image. A ray per pixel)
