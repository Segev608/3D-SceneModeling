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

![basic formula](https://user-images.githubusercontent.com/57367786/109837379-ca1a9d80-7c4d-11eb-99dd-8a520a08caca.png)

## Step 6 - Integration of material in the geometric shapes and improvement of the lighting for the Pong model
Below are the functions for calculating the lighting at a particular point depending on the type of lighting

Directional Light Source    |  Point Light Source       |  Spot Light Source
:-------------------------:|:-------------------------:|:-------------------------:
![direct formula](https://user-images.githubusercontent.com/57367786/109837901-51681100-7c4e-11eb-848c-2ca039945f23.png)  |  ![point formula](https://user-images.githubusercontent.com/57367786/109837962-5f1d9680-7c4e-11eb-8074-92956aa4f57e.png)  |  ![spot formula](https://user-images.githubusercontent.com/57367786/109838109-883e2700-7c4e-11eb-886a-69ac62653a08.png)

### Phong Light Model
In this model the lighting is broken down into two characteristics: 
- The diffuse part where the light is scattered everywhere but weakens as the angle viewed from it and the angle of impact of the light increases 
 
 ![image](https://user-images.githubusercontent.com/57367786/109839372-b708cd00-7c4f-11eb-8722-5459b850896d.png)
 
 This is because |l*n| represents the Scalar product between the direction of the light & normal to the surface. by multiply this value in the intensity from the light source, we can find the Diffusive value in this current location (kD is the scalar which defined in the material of the geometry - changing that value will give different look)

- The specular part where there is a single reflection of light that creates a kind of flash across the shape which gives it 3D looking.

![image](https://user-images.githubusercontent.com/57367786/109839447-c851d980-7c4f-11eb-9c78-a969e5d0e914.png)

This is because we taking the maximum value of 0 and the casting value which created from the camera angle & specular angle (the Refractive angle created by the light source). the more you parallel to the Refractive angle, you see the specular lighting better. All of that is raised to the power of n-shininess (can be controlled in the material of the object) in order to make the specular circle as small as possible - like in real life. 

The final intensity in a given point can be formulated into:

![image](https://user-images.githubusercontent.com/57367786/109839254-9cceef00-7c4f-11eb-9a52-79fc6d3f6a10.png)

*Open the Result folder and see the product so far*...

## Step 7 - Adding shading, refraction and reflection

