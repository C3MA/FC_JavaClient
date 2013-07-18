package de.c3ma.fullcircle.client;

import java.awt.Color;
import java.io.IOException;
import java.net.UnknownHostException;

import de.c3ma.animation.RainbowEllipse;
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
    
    private static boolean mSendFrames = false;
    private static int mWidth;
    private static int mHeight;
    
    private static int TIMER_RESET = 6;
    private static int CIRCE_CHANGE = 80;

    /**
     * @param args First argument MUST be the IP address of the host
     * @throws IOException 
     * @throws UnknownHostException 
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
        RawClient rc = new RawClient(args[0]);
        
        rc.requestInformation();
        

        int xmittel, ymittel, r;
        int count = 0;
        int timer = TIMER_RESET;
        
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
                    r--; // start with a smaller circle
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

                final Frame f = new Frame();
                
                new RainbowEllipse(xmittel, ymittel, r, r) {

                    @Override
                    protected void drawPixel(int x, int y, Color c) {
                        f.add(new Pixel(x, y, c));                        
                    }
                    
                }.drawEllipse(count);
                
                /* move the rainbow through the circle*/
                if (timer <= 0) {
                    timer = TIMER_RESET;
                    count++;
                    /* play around with the radius */
                    if (count % CIRCE_CHANGE == 0) {
                        r--;
                    } else if (count % CIRCE_CHANGE == (CIRCE_CHANGE / 2)) {
                        r++;
                    }
                }
                timer--;
                
                rc.sendFrame(f);
            }
        }
    }

}
