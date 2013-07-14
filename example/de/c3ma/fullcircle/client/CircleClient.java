package de.c3ma.fullcircle.client;

import java.awt.Color;
import java.io.IOException;
import java.net.UnknownHostException;

import de.c3ma.fullcircle.RawClient;
import de.c3ma.proto.fctypes.Frame;
import de.c3ma.proto.fctypes.FullcircleSerialize;
import de.c3ma.proto.fctypes.InfoAnswer;
import de.c3ma.proto.fctypes.Meta;
import de.c3ma.proto.fctypes.Pixel;
import de.c3ma.proto.fctypes.Start;
import de.c3ma.proto.fctypes.Timeout;

/**
 * created at 14.07.2013 - 16:30:23<br />
 * creator: ollo<br />
 * project: FullcircleClient<br />
 * $Id: $<br />
 * @author ollo<br />
 */
public class CircleClient {

    private static final int MAX_COLOR_VALUE = 255;
    private static final int STATIC_FACTOR = 47;
    private static int RED = 255;
    private static int GREEN = 0;
    private static int BLUE = 0;
    
    private static boolean mSendFrames = false;
    private static int mWidth;
    private static int mHeight;

    /**
     * @param args First argument MUST be the IP address of the host
     * @throws IOException 
     * @throws UnknownHostException 
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
        RawClient rc = new RawClient(args[0]);
        
        rc.requestInformation();
        
        int counter = 0;

        int x, y, xmittel, ymittel, r, dx, dy;
        
        xmittel = 1;
        ymittel = 1;
        r = 0;
        
        while(true) {
            Thread.sleep(10);
            FullcircleSerialize got = rc.readNetwork();
            if (got != null) {
                System.out.println(got);
                if (got instanceof InfoAnswer) {
                    /* Extract the expected resolution and use these values for the request */
                    InfoAnswer ia = ((InfoAnswer) got);
                    Meta meta = ia.getMeta();
                    mWidth = ia.getWidth();
                    xmittel =  mWidth / 2;
                    mHeight = ia.getHeight();
                    ymittel = mHeight / 2;
                    r = Math.min(xmittel, ymittel);
                    System.out.println("Draw cicle with center [" + xmittel + "x" + ymittel + "] and radius of " + r);
                    
                    /* when we got the resolution of the map, in this example we now want to start to send something */
                    rc.requestStart("java", 1, meta);    
                } else if (got instanceof Start) {
                    System.out.println("We have a GOOO send some data!");
                    mSendFrames = true;
                } else if (got instanceof Timeout) {
                    System.out.println("Too slow, so we close the session");
                    rc.close();
                    System.exit(1);
                }
            }

            // send something... NOW
            if (mSendFrames) {
                

                Frame f = new Frame();
                
                /* ALgorithm: http://de.wikipedia.org/wiki/Bresenham-Algorithmus */
                /*Bresenham-Algorithmus für einen Achtelkreis in Pseudo-Basic */
                /*gegeben seien r, xmittel, ymittel*/
                /*Initialisierungen für den ersten Oktanten*/
                x = r;
                y = 0;
                int fehler = r;
                /* "schnelle" Richtung ist hier y! */
                /* SETPIXEL xmittel + x, ymittel + y */
                f.add(new Pixel(xmittel + x, ymittel + y, generateColor(counter++)));
                
                /*Pixelschleife: immer ein Schritt in schnelle Richtung, hin und wieder auch einer in langsame*/
                while(y < x) {
                   /* Schritt in schnelle Richtung */
                   dy = y*2+1; /* REM bei Assembler-Implementierung *2 per Shift */
                   y = y+1;
                   fehler = fehler-dy;
                   if (fehler<0 ) {
                      /* Schritt in langsame Richtung (hier negative x-Richtung) */
                      dx = 1-x*2; /* bei Assembler-Implementierung *2 per Shift */
                      x = x-1;
                      fehler = fehler-dx;
                   }
                   /* SETPIXEL xmittel + x, ymittel + y */
                   f.add(new Pixel(xmittel + x, ymittel + y, generateColor(counter++)));
                   
                   /* Wenn es um einen Bildschirm und nicht mechanisches Plotten geht,
                    kann man die anderen Oktanten hier gleich mit abdecken: */
                   /*SETPIXEL xmittel-x, ymittel+y*/
                   f.add(new Pixel(xmittel - x, ymittel + y, generateColor(counter++)));
                   /*SETPIXEL xmittel-x, ymittel-y*/
                   f.add(new Pixel(xmittel - x, ymittel - y, generateColor(counter++)));
                   /*SETPIXEL xmittel+x, ymittel-y*/
                   f.add(new Pixel(xmittel + x, ymittel - y, generateColor(counter++)));
                   /*SETPIXEL xmittel+y, ymittel+x*/
                   f.add(new Pixel(xmittel + y, ymittel + x, generateColor(counter++)));
                   /* SETPIXEL xmittel-y, ymittel+x */
                   f.add(new Pixel(xmittel - y, ymittel + x, generateColor(counter++)));
                   /* SETPIXEL xmittel-y, ymittel-x */
                   f.add(new Pixel(xmittel - y, ymittel - x, generateColor(counter++)));
                   /* SETPIXEL xmittel+y, ymittel-x */
                   f.add(new Pixel(xmittel + y, ymittel - x, generateColor(counter++)));
                }
                
                rc.sendFrame(f);
                counter = 1; /* reset -> static picture */
            }
        }
    }

    private static Color generateColor(int counter) {
        int r = 0;
        int g = 0;
        int b = 0;
        int parts = (counter * STATIC_FACTOR) / MAX_COLOR_VALUE;
        int last = (counter * STATIC_FACTOR) % MAX_COLOR_VALUE;
        if (parts == 0) {
            r = last;
        }
        if (parts > 0) {
            r = MAX_COLOR_VALUE;
            g = last;
        }
        if (parts > 1) {
            g = MAX_COLOR_VALUE;
            b = last;
        }
        Color c = new Color(r, g, b);
        System.out.println(counter + "\t[" + (counter * STATIC_FACTOR) + "]\t" + c.getRed() + "," + c.getGreen() + "," + c.getBlue());
        return c;
    }

}
