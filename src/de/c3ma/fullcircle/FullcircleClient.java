package de.c3ma.fullcircle;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.UnknownHostException;

import de.c3ma.proto.fctypes.Frame;
import de.c3ma.proto.fctypes.FullcircleSerialize;
import de.c3ma.proto.fctypes.InfoAnswer;
import de.c3ma.proto.fctypes.Meta;
import de.c3ma.proto.fctypes.Start;
import de.c3ma.proto.fctypes.Timeout;

/**
 * created at 21.04.2013 - 16:26:55<br />
 * creator: ollo<br />
 * project: FullcircleClient<br />
 * $Id: $<br />
 * @author ollo<br />
 */
public class FullcircleClient {
    
    private static final String CLIENT_NAME = "java";
    private static final String CLIENT_VERSION = "1.0";
    
    private int width;
    private int height;
    private int fps;
    
    /**
     * The connection to the serve
     */
    private RawClient client;
    private boolean mConnectionEstablished = false;
    private boolean mOpened = false;
    private Frame mStaticFrame;
    
    
    public boolean open(final String host) {
        try {
            this.client = new RawClient(host);
            // ask for the resolution
            this.client.requestInformation();
            return true;
        } catch (UnknownHostException e) {
            System.err.println("Error with hostname : " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Connection problem : " + e.getMessage());
        }
        return false;
    }

    public boolean processNetwork() {
        // without an connection, anything is to be done
        if (client == null)
            return true;
        
        try {            
            FullcircleSerialize got = client.readNetwork();
            if (got != null) {
                System.out.println(got);
                if (got instanceof InfoAnswer) {
                    /* Extract the expected resolution and use these values for the request */
                    InfoAnswer ia = ((InfoAnswer) got);
                    this.fps = ia.getFPS();
                    this.width = ia.getWidth();
                    this.height = ia.getHeight();
                    this.mOpened  = true;
                    connect();
                } else if (got instanceof Start) {
                    System.out.println("We have a GOOO send some data!");
                    this.mConnectionEstablished = true;
                } else if (got instanceof Timeout) {
                    System.out.println("Too slow, so we close the session");
                    client.close();
                }
            }
            return true;
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }
    
    /**
     * connect to the server
     * @throws IOException on wrong resolution or, wrong fps.
     */
    private void connect() throws IOException {
        /* when we got the resolution of the map, in this example we now want to start to send something */
        client.requestStart(CLIENT_NAME, 1, new Meta(fps, width, height, CLIENT_NAME, CLIENT_VERSION));
    }
    
    public boolean sendFrame(Frame frame) {
        // not connected, so nothing to do
        if (!isConnected() || frame == null)
            return false;
        
        try {
            client.sendFrame(frame);
            return true;
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }
    
    public boolean sendFrame(BufferedImage image) {
        // not connected, so nothing to do
        if (!isConnected() || image == null)
            return false;
        
        try {
            Dynamic.sendFrame(client, image);
            return true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return false;
    }
    
    /**
     * entry point to prepare the cyclically sending of frames (all needed objects are allocated here)
     * This mechanism reduces the GC calls and should increase the performance
     */
    public void generateFrame() {
        this.mStaticFrame = new Frame(this.width, this.height);
    }
    
    /**
     * Update the static frame
     * @param red
     * @param green
     * @param blue
     * @param x
     * @param y
     * @return
     */
    public boolean updatePixel(int red, int green, int blue, int x, int y) {
        if (this.mStaticFrame == null)
            return false;
        this.mStaticFrame.updatePixel(red, green, blue, x, y);
        return true;
    }
    
    /**
     * Send this static frame
     * @return success
     */
    public boolean sendFrame() {
        return sendFrame(this.mStaticFrame);
    }
    
    public boolean isConnected() {
        return mConnectionEstablished;
    }
    
    public boolean isOpened() {
        return mOpened;
    }
    
    public int getWidth() {
        return width;
    }


    public int getHeight() {
        return height;
    }


    public int getFps() {
        return fps;
    }

}
