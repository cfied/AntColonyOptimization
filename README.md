# AntColonyOptimization
The project models an ant colony that finds the shortest way between their nest and a food source.

## Manual
### Changing properties
The properties height and width of the world, patch size, number of ants, 
position of nest, feed and walls can be changed in the ants.properties before the application is started.
Positions need to be entered in the following format: <br />
Nest: x,y &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; The nest has a fixed size of one patch. <br />
Feed: x1,y1,x2,y2&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Where the xs represent a x-coordinate, the ys a y-coordinate. <br />
Walls: x1,y1,x2,y2-x3,y3,x4,y4&nbsp;&nbsp;&nbsp;&nbsp;You can create multiple walls by seperating them with a hyphen. <br />

### Using the application 
Right Click: <br />
When the application is started you have the opportunity to create walls by 
right clicking on two patches. 

Space bar: <br />
Pressing space bar will start/pause the application.

Key d: <br />
Pressing d before closing the application will create the debug file, that contains some 
information about the ants.


## Credits:
Idea: Severin Adler, Tobias Schneider, Leon Rheinert, Jonas Klein, Carina Fiedler <br />
Basic implementation: Severin Adler, Carina Fiedler <br />
Debugging, Testing, Extension: Carina Fiedler <br />
