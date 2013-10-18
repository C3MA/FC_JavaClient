/**
 * This sketch demonstrates how to use an FFT (Fast Fourier Transform) to analyze an
 * AudioBuffer and draw the resulting spectrum. <br />
 * It also allows you to turn windowing on and off,
 * but you will see there is not much difference in the spectrum.<br />
 */

import processing.net.*; 
import de.c3ma.fullcircle.dyn.*;
import de.c3ma.types.SimpleColor;
import de.c3ma.animation.*;
import de.c3ma.proto.fctypes.Pixel;

import ddf.minim.analysis.*;
import ddf.minim.*;
import controlP5.*;
import processing.net.*;
ControlP5 controlP5;

import processing.serial.*;

Minim minim;
String windowName;
String statusMessage = "First insert the host of the Wall";

FullcircleClient fc = new FullcircleClient();

String fullcircle_host = "10.23.42.201";
final int portnumber = 24567;

void setup()
{   
  size(800, 600);
  //    size(512, 200, P3D);
  //  textMode(SCREEN);
  frameRate(25);

  textFont(createFont("Arial", 16));

  controlP5 = new ControlP5(this);
  controlP5.addTextlabel("statusMessage","statusMessage", 10, 10);
  Textfield txt = controlP5.addTextfield("fullcircle_host", 10, 50,400,20);
  txt.setText("10.23.42.201");
}

/**
 * Action, when the host has been set with ENTER
 */
public void fullcircle_host(String theText) {
     println("Host is " + theText );
     if (fc.open(theText, portnumber)) {
       statusMessage = "Connected to : " + theText + ". Wait for start signal";
     }
}

/********** Some variables to move the circle *****/
int globalCounter = 0; /* counter for the color rotation */
int speed = 2; /* Speed for the color rotation */
int radius = 3; /*initial radius of the circle */
int middleX = fc.getWidth() / 2;
int middleY = fc.getHeight() / 2;

void draw()
{
  try {
    // keep the network connection to the wall alive.
    fc.processNetwork();
  
    // when we know the resolution of the wall, prepare the visualization
    if (fc.isOpened() && middleX == 0 && middleY == 0) {
      fc.generateFrame();
      middleX = fc.getWidth() / 2;
      middleY = fc.getHeight() / 2;
      statusMessage = "Establied connection to : " + fullcircle_host;
    }
    
    
     /* Your function to write something */
     if (fc.isOpened())
     {
       sendFrame();
     }
  }
  catch(Exception e)
  {
    
  }
  
  background(0);
  stroke(255);
}

void keyReleased()
{  
  if ( key == 't' )
  {
    speed++;
  }
  else if ( key == 'z' )
  {
    speed--;
  }
  else if ( key == 'q' )
  {
    radius++;
  }
  else if ( key == 'y'  && radius > 0)
  {
    radius--;
  }
}

void stop()
{
  super.stop();
}

void sendFrame() {
  final de.c3ma.proto.fctypes.Frame f = new de.c3ma.proto.fctypes.Frame();
    
  globalCounter += speed;
  RainbowEllipse re = new RainbowEllipse(middleX, middleY, radius, radius) {
            protected void drawPixel(int x, int y, SimpleColor c) {
              Pixel p = new Pixel(Math.min(255, c.getRed() ), 
                        Math.min(255, c.getGreen() ), 
                        Math.min(255, c.getBlue() ), x, y);
              f.add(p); 
            }
        };
  re.drawEllipse(globalCounter);
  fc.sendFrame(f);
}

