package de.c3ma.fullcircle.animation;

import java.awt.Color;

/**
 * created at 17.07.2013 - 18:47:42<br />
 * creator: ollo<br />
 * project: FullcricleClient<br />
 * $Id: $<br />
 * @author ollo<br />
 */
public class ConsoleRainbowEllipse extends RainbowEllipse {

    public ConsoleRainbowEllipse(int xm, int ym, int a, int b) {
        super(xm, ym, a, b);
    }

    @Override
    protected void drawPixel(int x, int y, Color c) {
        System.out.println(x + "x" + y + "\t" + c);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        ConsoleRainbowEllipse cre = new ConsoleRainbowEllipse(7,7,6,6);
        cre.drawEllipse();
    }

}
