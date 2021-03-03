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

![camera definition](https://github.com/Segev608/3D-SceneModeling/blob/main/camera.png)

## Step 4 - Creating the camera and generating the rays
At this point, the goal is to create the ***camera*** so that we have several options:
- Using three vectors, we will control its orientation in space.
- Create the rays in all directions in order to scan the whole scene (the more rays there are - the higher the resolution of the image. A ray per pixel)

## Step 5 - Introducing lighting into the scene
By controlling the colors and multiplying them by some coefficient, you can control the feeling and give the image three-dimensionality.
In this section, the light would be inserted as solid value - Ambient light (the light in the scene 'room')
In this section there are several departments that perform the following roles:
- ***Color***: Responsible for RGB colors
- ***ImageWriter***: 
    - Responsible for creating PNG files
    - Color the pixels in the defined colors
    - Stores the properties of the image (resolution and size)
- ***Scene***: Saving all the elements of the scene. for example:
    - What lighting will be in the scene?
    - What geometries are modeled?
    - Save the camera and its location in space
    - Distance from camera to screen, view-plane. (The screen through which the rays are launched in the direction of the geometries in the image)

Two-Color Image             |  Multi-Color Image        |  Playing with image creator
:-------------------------:|:-------------------------:|:-------------------------:
![base_img](https://github.com/Segev608/3D-SceneModeling/blob/main/Results/base%20render%20test.jpg)  |  ![color_img](https://github.com/Segev608/3D-SceneModeling/blob/main/Results/color%20render%20test.jpg)  |  ![playing_img](https://github.com/Segev608/3D-SceneModeling/blob/main/Results/test.jpg)

Until now, the Intensity in a given point has been calculated by this formula:

![image](https://user-images.githubusercontent.com/57367786/109837379-ca1a9d80-7c4d-11eb-99dd-8a520a08caca.png)

## Step 6 - Integration of material in the geometric shapes and improvement of the lighting for the Pong model

