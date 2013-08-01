package de.c3ma.fullcircle.dyn;

import static org.junit.Assert.fail;

import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.junit.Test;

import de.c3ma.fullcircle.dyn.Dynamic;


/**
 * created at 20.10.2012 - 17:55:13<br />
 * creator: ollo<br />
 * project: FullcricleClient<br />
 * $Id: $<br />
 * @author ollo<br />
 */
public class TestDynamic {

    @Test
    public void testUpdate() {
        try {
            Dynamic d = new Dynamic(6, 10, 25, "192.168.0.1");
            Graphics g = d.getGraphics();
            g.setColor( Color.BLUE ); 
            g.fillRect(0,0, 2, 2);
            
            g.setColor(Color.PINK);
            g.fillOval(2, 0, 4, 8);
            
            d.updateGraphics();
        } catch (IOException e) {
            fail(e.getMessage());
        } catch (TimeoutException e) {
            fail("Timing-Problem: " +  e.getMessage());
        }        
    }
    
    @Test
    public void testFrequency() {
        try {
            Dynamic d = new Dynamic(6, 10, 25, null);
            Graphics g = d.getGraphics();
            g.setColor( Color.BLUE ); 
            g.fillRect(0,0, 2, 2);
            d.updateGraphics();
            d.updateGraphics();
            fail("There should be a timeout exception");
        } catch (IOException e) {
            fail(e.getMessage());
        } catch (TimeoutException e) {
            /* This is expected. */
        }
    }
    
    @Test
    public void testFrequencyWithTimeout() {
        try {
            Dynamic d = new Dynamic(6, 10, 25, null);
            Graphics g = d.getGraphics();
            g.setColor( Color.BLUE ); 
            g.fillRect(0,0, 2, 2);
            d.updateGraphics();
            Thread.sleep(40);
            d.updateGraphics();
        } catch (IOException e) {
            fail(e.getMessage());
        } catch (TimeoutException e) {
            fail("Timing-Problem: " +  e.getMessage());
        } catch (InterruptedException e) {
            fail("Sleeping failed");
        }
    }
}
