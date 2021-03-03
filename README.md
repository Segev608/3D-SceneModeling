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
The idea in creating the shadow is that whenever we launch a ray from the camera for geometry in the scene, if there is a intersection in the surface, we will use the lighting location (which we already know because we placed them there) and generate another ray for that position. If we seem to have received another intersection between the ray and the light source, we can know that between the initial point of intersection and the light source, there is an object that hides the light and therefore it is necessary to paint in the current place - a shadow.

### Shading
This formula (besides calculating the intensity in the current point using ***ALL*** the light sources in the scene, this is the sigma in the formula) uses the S factor which checks if there is a shadow in the current point. like I said, if there is a shadow, there is no need to color that pixel - make it black (for now)

![image](https://user-images.githubusercontent.com/57367786/109842846-2633f080-7c53-11eb-8a11-9891493d719a.png)

### Refraction and Reflection
In order to get values of refraction and reflection, every time we get a cut in the body, we will create two new beams from the same place, the refraction will work as in the formula for finding the specular value and the reflection beam will continue further into the body. 

It turns out that in geometric shapes that allow reflection, we get a glass look. Of course the creation of the rays will be possible thanks to the recursion performed at the point (the same point where the current color is calculated. Of course we will make sure that the recursion depth is not too large given that there is no end to the amount of refraction and reflection (The same point where the current color is calculated. Of course we will make sure that the depth of the recursion is not too great given that there is no end to the amount of refraction and reflections that can happen in transparent shapes, or between reflective shapes).

![image](https://user-images.githubusercontent.com/57367786/109843627-f0dbd280-7c53-11eb-9d46-931bc929a795.png)

## Step 8 -  Picture Improvement Algorithms
In order to present a smoother image we have introduced the following algorithms:

### Multiple Eye-Rays
The idea is to launch several rays per (through each) pixel and calculate the weighted average of the total color values obtained. Basically if we get that at that point there are two colors, we get a nice "smear" of the values so that the overall picture looks smoother

![image](https://user-images.githubusercontent.com/57367786/109845047-65fbd780-7c55-11eb-8ba8-ada1768703d8.png)


### Multiple Shadow-Rays
As we did above, every time we check the shadow at a certain point, we will launch a few rays from that point towards the light sources, and we will take the weighted average. Here, too, a beautiful "smear" of the values can be formed and we get a pleasant fading of the shadow.

![image](https://user-images.githubusercontent.com/57367786/109845696-179b0880-7c56-11eb-8019-ce4c4488d8d7.png)

## Step 9 - Performance Improvements
After we finished implementing the algorithms and came running, we discovered that creating a single image can take up to **6 hours** !! Therefore, there is a need to add mechanisms that will allow for improved performance and significant reduction of runtime

The first thing we added is the option for parallel running using threads that cooperate and approach coloring together so that if there are 10 of them running together, each calculates the color in its own pixel and once it finishes it takes a new pixel independently of the other threads

The second improvement added is to reduce the amount of rays generated. In previous image enhancements we have seen, lots of new rays have been created that calculate the point color. Now, since the function that calculates the point color is recursive, a single calculation can take a lot of time. Thus, we used an adaptive algorithm to improve the image, the super sampling algorithm.

![image](https://user-images.githubusercontent.com/57367786/109847181-93498500-7c57-11eb-8304-e97811145ac4.png)

## In conclusion
After all of the above, we were able to get this beautiful picture while running for close to **two hours** !!

![](https://github.com/Segev608/3D-SceneModeling/blob/main/Results/FinalSuperSoftPicture.jpg)

