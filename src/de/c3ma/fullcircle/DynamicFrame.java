package de.c3ma.fullcircle;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import de.c3ma.proto.fctypes.FullcircleSerialize;
import de.c3ma.proto.fctypes.InfoAnswer;
import de.c3ma.proto.fctypes.Meta;
import de.c3ma.proto.fctypes.Start;
import de.c3ma.proto.fctypes.Timeout;

/**
 * created at 14.06.2013 - 10:35:00<br />
 * creator: Käptn<br />
 * project: DynamicFrame<br />
 * $Id: $<br />
 * @author Käptn<br />
 */
public abstract class DynamicFrame {

    private static final int DEFAULT_CYCLETIME = 1000;
    private static final String CLIENT_NAME = "java";
    private static final String CLIENT_VERSION = "1.0";
    private static final int MS_OF_A_SECOND = 1000;
    private int width;
    private int height;
    private int fps;
    private long mLastmodification = 0;
    private BufferedImage mImage;

    class WorkerThread extends Thread {
            boolean visible = false;
        	private boolean running = true;
        	
            public void run() {
            	while(this.running) {
                	try {
                        Thread.sleep(getUpdateTime());

                        processNetwork();
                    
                        if(this.visible) {
                            Graphics g = getGraphics();
                            synchronized (g) {
                            	update(g);
                            } 
                            updateGraphics();
                		}
                        
                	} catch (InterruptedException e) {
                    	this.running = false;
                        System.err.println("Background thread was killed.");
                    } catch (IOException e) {
                    	this.running = false;
                        System.err.println("Update-Problem: " + e.getMessage());
                    } catch (TimeoutException e) {
                        /* do nothing, there was a timing problem */
                        System.err.println("Timing Problem, give it another try: " + e.getMessage());
                    }
            	}
            }};
    
            private WorkerThread updateThread = new WorkerThread();
            
    /**
     * The connection to the server
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
    public DynamicFrame(final int width, final int height, final int fps, String host) throws IOException {
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
        this.updateThread.start();
    }

    public void join() throws InterruptedException {
    	this.updateThread.join();
    }
    
    public DynamicFrame(String address) throws IOException {
        try {
            this.client = new RawClient(address);
        } catch (UnknownHostException e) {
            throw new IOException("Wrong server", e);
        }
        
        this.client.requestInformation();
        this.updateThread.start();
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
     * <b>PUSH-pattern</b>
     * Generate a graphics, to paint.
     * This is updated by the ®see {@link #updateGraphics()} function.
     * @return Graphics, that could be modified
     */
    private Graphics getGraphics() {
        mImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        return mImage.getGraphics();
    }
    
    /**
     * <b>PUSH-pattern, update</b>
     * Updates the "image", that was drawn on the Graphics @see {@link #getGraphics()}
     * @throws IOException on wrong resolution
     * @throws TimeoutException when updating too fast. 
     */
    private void updateGraphics() throws IOException, TimeoutException {
    	prepareImage(mImage);
    }

    public abstract void update(Graphics g);
    
    /**
     * Display this image NOW on the wall.
     * @param image
     * @throws IOException on wrong image resolution.
     * @throws TimeoutException when a new frame are send too fast
     */
    private void prepareImage(final BufferedImage image) throws IOException, TimeoutException {
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
        
        Dynamic.sendFrame(client, image);
        
        mLastmodification = System.currentTimeMillis();
    }

    public int getWidth() { return this.width; }
    public int getHeight() { return this.height; }
    
    private int getUpdateTime() {
        if (fps <= 0) {
            return DEFAULT_CYCLETIME;
        } else {
            return MS_OF_A_SECOND / fps;
        }
    }

    private void processNetwork() throws IOException {
        FullcircleSerialize got = client.readNetwork();
        if (got != null) {
            System.out.println(got);
            if (got instanceof InfoAnswer) {
                /* Extract the expected resolution and use these values for the request */
                InfoAnswer ia = ((InfoAnswer) got);
                this.fps = ia.getFPS();
                this.width = ia.getWidth();
                this.height = ia.getHeight();
                connect();
            } else if (got instanceof Start) {
                System.out.println("We have a GOOO send some data!");
                this.updateThread.visible = true;
            } else if (got instanceof Timeout) {
                System.out.println("Too slow, so we close the session");
                client.close();
            }
        }
    }
}
