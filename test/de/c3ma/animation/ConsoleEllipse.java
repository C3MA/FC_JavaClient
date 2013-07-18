package de.c3ma.animation;



/**
 * created at 17.07.2013 - 17:15:37<br />
 * creator: ollo<br />
 * project: FullcricleClient<br />
 * $Id: $<br />
 * @author ollo<br />
 */
public class ConsoleEllipse extends GeneralEllipse {

    public ConsoleEllipse(int xm, int ym, int a, int b) {
        super(xm, ym, a, b);
    }

    @Override
    public void setPixel(int x, int y, int number) {
        System.out.println(""+x+"x"+y+"\t"+number);
    }

    public static void main(String[] args) {
        ConsoleEllipse ce = new ConsoleEllipse(5, 5, 3, 3);
        ce.drawEllipse();
        System.out.println("elemens=" + ce.countElements());
        
        ce = new ConsoleEllipse(5, 5, 4, 4);
        ce.drawEllipse();
        System.out.println("elemens=" + ce.countElements());
        
        ce = new ConsoleEllipse(5, 5, 5, 5);
        ce.drawEllipse();
        System.out.println("elemens=" + ce.countElements());
        
        ce = new ConsoleEllipse(5, 5, 6, 6);
        ce.drawEllipse();
        System.out.println("elemens=" + ce.countElements());
    }
}
