package de.c3ma.fullcircle;

import java.awt.Graphics;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * created at 20.10.2012 - 20:11:59<br />
 * creator: ollo<br />
 * project: FullcricleClient<br />
 * $Id: $<br />
 * @author ollo<br />
 * 
 * 
 * This class takes the fps information an calls the 
 * {@link OnFullcirclePaint} frequently
 */
public class DynamicAutoUdpater extends Thread {

    
    private OnFullcirclePaint mOnpaint;
    private Dynamic mDynamic;
    
    /**
     * flag, needed to stop the thread normally
     */
    private boolean mRunning = true;
    
    /**
     * if the stack is ready to update something
     */
    private boolean mVisible = false;

    /**
     * Worker class, that updates frequently the user application
     * @param onpaint
     * @param dynamic
     */
    public DynamicAutoUdpater(final OnFullcirclePaint onpaint, final Dynamic dynamic) {
        this.mOnpaint = onpaint;
        this.mDynamic = dynamic;
    }
    
    @Override
    public void run() {
        while(mRunning) {
            try {
                Thread.sleep(mDynamic.getUpdateTime());
                Graphics g = mDynamic.getGraphics();

                mDynamic.processNetwork();
                
                if (mVisible) {
                    synchronized (g) {
                        mOnpaint.paint(g);
                    } 
                    mDynamic.updateGraphics();
                }
            } catch (InterruptedException e) {
                mRunning = false;
                System.err.println("Background thread was killed.");
            } catch (IOException e) {
                mRunning = false;
                System.err.println("Update-Problem: " + e.getMessage());
            } catch (TimeoutException e) {
                /* do nothing, there was a timing problem */
                System.err.println("Timing Problem, give it another try: " + e.getMessage());
            }
        }
    }

    public void close() {
        this.mRunning = false;        
    }

    public void setVisible(boolean b) {
        this.mVisible  = b;
    }

}
