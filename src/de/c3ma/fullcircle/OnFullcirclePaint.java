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
     */
    public void paint(final Graphics g );
}
