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
        ce.drawEllipse(5, 5, 3, 3);
        System.out.println("elemens=" + ce.countEllipseElements(5, 5, 3, 3));
        
        ce.drawEllipse(5, 5, 4, 4);
        System.out.println("elemens=" + ce.countEllipseElements(5, 5, 4, 4));
        
        ce.drawEllipse(5, 5, 5, 5);
        System.out.println("elemens=" + ce.countEllipseElements(5, 5, 5, 5));
        
        ce.drawEllipse(5, 5, 6, 6);
        System.out.println("elemens=" + ce.countEllipseElements(5, 5, 6, 6));
    }
}
