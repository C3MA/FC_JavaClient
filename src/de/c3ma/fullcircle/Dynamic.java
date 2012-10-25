package de.c3ma.fullcircle;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * created at 20.10.2012 - 16:37:57<br />
 * creator: ollo<br />
 * project: FullcricleClient<br />
 * $Id: $<br />
 * @author ollo<br />
 */
public class Dynamic {

    private static final int MS_OF_A_SECOND = 1000;
    
    private int width;
    private int height;
    private int fps;
    
    private long mLastmodification = 0;

    private BufferedImage mImage;

    private DynamicAutoUdpater mDynamicUpdater;

    /**
     * The connection to the serve
     */
    private String host;

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
        this.host = host;
        
        if (fps > MS_OF_A_SECOND) {
            throw new IOException("You are expecting more than 1000fps! This is too much.");
        }
        
        connect();
    }

    /**
     * connect to the server
     * @throws IOException on wrong resolution or, wrong fps.
     */
    private void connect() throws IOException {
        
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
        mImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        return mImage.getGraphics();
    }
    
    /**
     * <b>PUSH-pattern, update</b>
     * Updates the "image", that was drawn on the Graphics @see {@link #getGraphics()}
     * @throws IOException on wrong resolution
     * @throws TimeoutException when updating too fast. 
     */
    public void updateGraphics() throws IOException, TimeoutException {
        update(mImage);
    }
    
    /**
     * Display this image NOW on the wall.
     * @param image
     * @throws IOException on wrong image resolution.
     * @throws TimeoutException when a new frame are send too fast
     */
    public void update(final BufferedImage image) throws IOException, TimeoutException {
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
        
        //TODO send, the image to the WALL over the network interface.
//        for(int y=0; y < image.getHeight(null); y++) {
//            for(int x=0; x < image.getWidth(); x++) {
//                Color c = new Color(image.getRGB(x, y));
//                System.out.print(", " 
//                        + String.format("%02X", c.getRed())
//                        + String.format("%02X", c.getGreen())
//                        + String.format("%02X", c.getBlue()));
//            }
//            System.out.println();
//        }
        
        mLastmodification = System.currentTimeMillis();
    }

    public int getUpdateTime() {
        return MS_OF_A_SECOND / fps;
    }
    
}
