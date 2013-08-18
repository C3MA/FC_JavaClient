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
AudioInput in;
FFT fft;
String windowName;
String statusMessage = "First insert the host of the Wall";

FullcircleClient fc = new FullcircleClient();

String fullcircle_host = "10.23.42.201";
final int portnumber = 24567; /* default = 24567, debug = 24568 */

int micFactor = 100;
int rotationFactor = 100;
int radiusFactor = 100;

boolean slomotion = false;

void setup()
{
    
  size(800, 600);
  //    size(512, 200, P3D);
  //  textMode(SCREEN);
  frameRate(25);

  minim = new Minim(this);

  // get a line in from Minim, default bit depth is 16
  in = minim.getLineIn(Minim.STEREO,512, 44100); /* default 44100 */

  // create an FFT object that has a time-domain buffer 
  // the same size as jingle's sample buffer
  // note that this needs to be a power of two 
  // and that it means the size of the spectrum
  // will be 512. see the online tutorial for more info.
  fft = new FFT(in.bufferSize(), in.sampleRate());

  textFont(createFont("Arial", 16));

  strechPeak = round((width * 1.0) / fft.specSize());

  windowName = "None";
  startVisualisationY = height - 150;

  // Offset of the leftest input "LED"
  final int INPUT_OFFSET = 5;

  controlP5 = new ControlP5(this);


  // a Hamming window can be used to shape the sample buffer that is passed to the FFT
  // this can reduce the amount of noise in the spectrum
  fft.window(FFT.HAMMING);
  windowName = "Hamming";

  controlP5.addToggle("slomotion", false, 700, startVisualisationY + 60, 80, 20);
  controlP5.addSlider("micFactor",1,100,50,10,startVisualisationY + 60,300,10);
  Textfield txt = controlP5.addTextfield("fullcircle_host", 10,startVisualisationY + 10,400,20);
  txt.setText("10.23.42.201");
  controlP5.addSlider("radiusFactor",1,100,50,10,startVisualisationY + 80,300,10);
  controlP5.addSlider("rotationFactor",1,100,50,10,startVisualisationY + 100,300,10);
}

/**
 * Action, when the host has been set with ENTER
 */
public void fullcircle_host(String theText) {
     println("Host is " + theText );
     if (fc.open(theText, portnumber)) {
       statusMessage = "Connected to : " + theText + ". Wait for start signal";
     }
//  println("controlEvent: accessing a string from controller '"++"': "+theEvent.controller().stringValue());
}

/********** Some variables to calculate the audio frequencies *****/
int[] output = null;
int slotsize;

int strechPeak;
int startVisualisationY;

void draw()
{
  try {
  // keep the network connection to the wall alive.
  fc.processNetwork();
  
  // when we know the resolution of the wall, prepare the visualization
  if (fc.isOpened() && output == null) {
    output = new int[fc.getWidth()];
    slotsize = round((fft.specSize() * 1.0) / fc.getWidth());
    fc.generateFrame();
  }
  
  background(0);
  stroke(255);
  // perform a forward FFT on the samples in jingle's left buffer
  // note that if jingle were a MONO file, 
  // this would be the same as using jingle.right or jingle.left
  fft.forward(in.mix);

  //FIXME testing: fft.inverse(in.mix);

  int outi=0;
  int value;
  boolean shrinkValue = false;

  if (output != null) {
    if (!slomotion) {
      for(int i=0; i < output.length; i++)
        output[i] = 0;
    }
    fft.linAverages(output.length);  
  }


  maxValue = 0; // reset the global variable
  for(int i = 0; i < fft.specSize(); i++) // shrink spectrum, and use only 80% of the spectrum
  {
    stroke(255);
    value = ceil((fft.getBand(i) * micFactor) * 4 * (i / 4));
    //    value = (int) (fft.getBand(i) * 5);
    //      value = (int) max( ((fft.getBand(i) - 0.54) * 0.4  ) / 80, startVisualisationY);
    //    value = (int) (fft.getBand(i) * (((i * 2 + 1) >> i) ));



    rect(i* strechPeak, startVisualisationY, strechPeak, -value);


  /*** wait for a connection in order to know the resolution ***/
  if (output != null)
  {
    if (i > 0 && i % slotsize == 0)
    {
      // draw a horizontal line for each slot
      stroke(color(255,0,0));
      line(outi * (slotsize * strechPeak), startVisualisationY - output[outi], (outi + 1) * (slotsize * strechPeak), startVisualisationY - output[outi]);

      // draw a vertical line to arrange the input bangs
      stroke(color(255,255,255));
      line((outi + 1) * (slotsize * strechPeak), 0, (outi + 1) * (slotsize * strechPeak), height);

      if (slomotion) 
      { // only modify the item once
        if (shrinkValue)
          output[outi] -= 10; // fade slowly to the bottom
      }

      shrinkValue = false;
      outi++;    // use the next slot
      if (outi >= output.length)
        outi = output.length - 1;
    }

    if (slomotion)
    {
      if (value < output[outi])
        shrinkValue=true;
      else
        output[outi] = value;
    }
    else
    {
//      output[outi] = max(output[outi],  value);
        output[outi] = (int) ((output[outi] +  value) / 2);
    }
    maxValue = max(maxValue, value);
   }
  }
  
  if (fc.isConnected()) {
    sendFrame();
    statusMessage = "Connection established";
  }

  fill(255);
  // keep us informed about the window being used
  
  text(statusMessage, 5, 20);
  } catch(Exception e) {
    e.printStackTrace();
  }
}

void keyReleased()
{  
  if ( key == 's' )
  {
    slomotion = !slomotion; 
    if (slomotion)
      windowName = "Slomotion activated";
    else
      windowName = "Hamming";
  }
}

void stop()
{
  // always close Minim audio classes when you finish with them
  in.close();
  minim.stop();

  super.stop();
}

int maxValue;

int magic(int number) {
  //  return (int) (255.0 * number / maxValue);
  return (int) (255.0 * number); //TODO No Magic no longer
}

int globalCounter=0;

void sendFrame() {
  final de.c3ma.proto.fctypes.Frame f = new de.c3ma.proto.fctypes.Frame();
  int height = fc.getHeight();
  int radius = Math.min(fc.getHeight(), fc.getWidth());
  
  radius = radius / 2;
  
  // tiny check
  if (output.length < 3)
    return;
  
  int loud = Math.min(radius, output[1] / radiusFactor);
  loud = Math.max(1, loud);

  final int louder = output[3];
  globalCounter += output[fc.getWidth()/2] / rotationFactor;
  RainbowEllipse re = new RainbowEllipse(radius, radius, loud, loud) {
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

