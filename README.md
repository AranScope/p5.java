# p5.java
Writing a (very) small subset of P5.js in Java. The ['Math Noise Wave' example.](https://p5js.org/examples/examples/Math_Noise_Wave.php) from the p5.js site.

## Usage
The 'P5' class contains (almost) everything in p5.j, extending this class gives access to the following methods

```Java
void createCanvas(int width, int height)
void background(int brightness)
void fill(int brightness)
void beginShape()
void endShape()
void vertex()
double map(double val, double min, double max, double newMin, double newMax)
double noise(double x, double y)
```

These can be used to implement a p5 program that draws polyshapes to the screen, alters the background / foreground colors and uses simplex noise.

## Examples
To see the 'Math Noise Wave' or 'Boids' examples, do the following:

i.e.
```Java
javac *.java

java MathNoise
or
java Boids
```
## Implementation
Everything is written using the Java standard SDK as well as Java OpenSimplex by Kurt Spencer.

Written using Swing and AWT.
