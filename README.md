# 3D-SceneModeling
This project is primarily about learning how to design the code in an orderly fashion according to the methods that are actually performed, striving for the most advanced and efficient cutting.

## Step 1 - Defining primitives
At this point the building blocks are created for the advanced shapes.
- ***Util***: calculation accuracy.
- ***Coordinate***: A single (x,y,z) value that uses Util values.
- ***Vector***: Size and direction, uses three Coordinate values
- ***Ray***: The set of points on a line that are on one side relative to a given point on the line.

## Step 2 - Defining the Geometries 
Once we have defined mathematical values that allow definitions on a Cartesian axis system, we proceed to definitions of shapes
### Flat shape
- ***Plane***: An infinite plane. Defined using the normal vector and a point
- ***Polygon***: Able to define in the Cartesian axis space any shape with n vertices
