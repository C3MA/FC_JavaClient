package de.c3ma.fullcircle.animation;

import de.c3ma.fullcircle.animation.SmothEllipse;


/**
 * created at 17.07.2013 - 17:15:37<br />
 * creator: ollo<br />
 * project: FullcricleClient<br />
 * $Id: $<br />
 * @author ollo<br />
 */
public class ConsoleEllipse extends SmothEllipse {

    @Override
    public void setPixel(int x, int y, int number) {
        System.out.println(""+x+"x"+y+"\t"+number);
    }

    public static void main(String[] args) {
        ConsoleEllipse ce = new ConsoleEllipse();
        ce.ellipse(8, 8, 8, 8);
    }
}
