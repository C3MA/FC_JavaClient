package de.c3ma.fullcircle;

import java.awt.Graphics;

/**
 * created at 20.10.2012 - 20:04:09<br />
 * creator: ollo<br />
 * project: FullcricleClient<br />
 * $Id: $<br />
 * @author ollo<br />
 */
public interface OnFullcirclePaint {

    /**
     * This function will be updated from the dynamic function
     * @param g draw, lines, rectangle, oval
     * @param width of the visible picture at the wall
     * @param height of the visible picture at the wall
     */
    public void paint(final Graphics g, final int width, final int height);
}
