package de.c3ma.fullcircle.dyn;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import de.c3ma.fullcircle.RawClient;
import de.c3ma.proto.fctypes.Abort;
import de.c3ma.proto.fctypes.Frame;
import de.c3ma.proto.fctypes.FullcircleSerialize;
import de.c3ma.proto.fctypes.InfoAnswer;
import de.c3ma.proto.fctypes.Meta;
import de.c3ma.proto.fctypes.Pixel;
import de.c3ma.proto.fctypes.Start;
import de.c3ma.proto.fctypes.Timeout;

/**
 * created at 20.10.2012 - 16:37:57<br />
 * creator: ollo<br />
 * project: FullcircleClient<br />
 * $Id: $<br />
 * @author ollo<br />
 */
public class Dynamic {

    private static final int DEFAULT_CYCLETIME = 1000;

    private static final String CLIENT_NAME = "java";

    private static final String CLIENT_VERSION = "1.0";

    private static final int MS_OF_A_SECOND = 1000;
    
    private int width;
    private int height;
    private int fps;
    
    private long mLastmodification = 0;

    private boolean mOverdraw = false;
    private BufferedImage mImage;

    private DynamicAutoUdpater mDynamicUpdater;

    /**
     * The connection to the serve
     */
    private RawClient client;

    /**
     * Create a new dynamic view to send something frequently to the WALL.
     * @param width in pixel of the wall
     * @param height in pixel of the wall
     * @param fps that will be send to the wall 
     * @param host ip address, the wall could be controlled with
     * @throws IOException when the server is not ready 
     */
    public Dynamic(final int width, final int height, final int fps, String host) throws IOException {
        this.width = width;
        this.height = height;
        this.fps = fps;
        
        if (fps > MS_OF_A_SECOND) {
            throw new IOException("You are expecting more than 1000fps! This is too much.");
        }
        
        try {
            this.client = new RawClient(host);
        } catch (UnknownHostException e) {
            throw new IOException("Wrong server", e);
        }
        
        connect();
    }

    public Dynamic(String address) throws IOException {
        try {
            this.client = new RawClient(address);
        } catch (UnknownHostException e) {
            throw new IOException("Wrong server", e);
        }
        
        this.client.requestInformation();        
    }

    /**
     * connect to the server
     * @throws IOException on wrong resolution or, wrong fps.
     */
    private void connect() throws IOException {
        /* when we got the resolution of the map, in this example we now want to start to send something */
        client.requestStart(CLIENT_NAME, 1, new Meta(fps, width, height, CLIENT_NAME, CLIENT_VERSION));
    }
    
    /**
     * Set a dynamic view
     * @param onpaint
     */
    public void setOnPaint(final OnFullcirclePaint onpaint) {
        this.mDynamicUpdater = new DynamicAutoUdpater(onpaint, this);
        this.mDynamicUpdater.start();
    }
    
    public void removeAllOnPaint() {
        if (this.mDynamicUpdater == null)
            return;
        this.mDynamicUpdater.close();
        this.mDynamicUpdater = null;
    }
    
    
    
    /**
     * <b>PUSH-pattern</b>
     * Generate a graphics, to paint.
     * This is updated by the ®see {@link #updateGraphics()} function.
     * @return Graphics, that could be modified
     */
    public Graphics getGraphics() {
        if (!mOverdraw || mImage == null)
            mImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        return mImage.getGraphics();
    }
    
    /**
     * <b>PUSH-pattern, update</b>
     * Updates the "image", that was drawn on the Graphics @see {@link #getGraphics()}
     * @throws IOException on wrong resolution
     * @throws TimeoutException when updating too fast. 
     */
    void updateGraphics() throws IOException, TimeoutException {
        update(mImage);
    }
    
    /**
     * Display this image NOW on the wall.
     * @param image
     * @throws IOException on wrong image resolution.
     * @throws TimeoutException when a new frame are send too fast
     */
    private void update(final BufferedImage image) throws IOException, TimeoutException {
        if (image.getHeight(null) != height &&
                image.getWidth(null) != width)
        {
            throw new IOException("Wrong resolution");
        }
        
        // fps -> offset in milliseconds is 25 f / s == 1000 / 25 -> 40ms
        if ((System.currentTimeMillis() - mLastmodification) < getUpdateTime())
        {
            throw new TimeoutException();
        }
        
        sendFrame(client, image);
        
        mLastmodification = System.currentTimeMillis();
    }

    int getUpdateTime() {
        if (fps <= 0) {
            return DEFAULT_CYCLETIME;
        } else {
            return MS_OF_A_SECOND / fps;
        }
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
    
    /**
     * Deactivates or activates the cleaning of a sent frame before sending a new one. 
     * @param overdrawing <code>true</code> the last image remains and will be used as background
     * <code>false</code> always draw on a new image (the default)
     */
    public void setOverdraw(boolean overdrawing) {
        this.mOverdraw = overdrawing;
    }
    

    void processNetwork() throws IOException {
        FullcircleSerialize got = client.readNetwork();
        if (got != null) {
            if (got instanceof InfoAnswer) {
                /* Extract the expected resolution and use these values for the request */
                InfoAnswer ia = ((InfoAnswer) got);
                this.fps = ia.getFPS();
                this.width = ia.getWidth();
                this.height = ia.getHeight();
                connect();
            } else if (got instanceof Start) {
                System.out.println("We have a GOOO send some data!");
                mDynamicUpdater.setVisible(true);
            } else if (got instanceof Timeout) {
                System.out.println("Too slow, so we close the session");
                client.close();
            } else if (got instanceof Abort) {
                System.out.println("The server wants us to leave");
                client.close();
                
            }
        }
    }

    /**
     * Convert a given image into a fullcircle image for protobuf
     * @param image
     * @throws IOException
     */
    public static void sendFrame(RawClient client, BufferedImage image) throws IOException {
      Frame f = new Frame();  
      for(int y=0; y < image.getHeight(null); y++) {
          for(int x=0; x < image.getWidth(); x++) {
              Color c = new Color(image.getRGB(x, y));
//              System.out.print(", " 
//                      + String.format("%02X", c.getRed())
//                      + String.format("%02X", c.getGreen())
//                      + String.format("%02X", c.getBlue()));
              f.add(new Pixel(c.getRed(), c.getGreen(), c.getBlue(), x, y));
          }
//          System.out.println();
      }
      client.sendFrame(f);  
    }
}
