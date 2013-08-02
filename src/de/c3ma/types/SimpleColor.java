package de.c3ma.types;

/**
 * created at 01.08.2013 - 14:34:08<br />
 * creator: ollo<br />
 * project: FullcricleClient<br />
 * $Id: $<br />
 * @author ollo<br />
 */
public class SimpleColor {

    private static final int MAX_VALUE = 255;
    
    private int red;
    public int getRed() {
        return Math.min(MAX_VALUE, Math.abs(red));
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return Math.min(MAX_VALUE, Math.abs(green));
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getBlue() {
        return Math.min(MAX_VALUE, Math.abs(blue));
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    private int green;
    private int blue;

    /**
     * 
     * @param red
     * @param green
     * @param blue
     */
    public SimpleColor(int red, int green, int blue) {
       this.red = red;
       this.green = green;
       this.blue = blue;
    }

    @Override
    public String toString() {
        return "" + getRed() + "," + getGreen() + "," + getBlue();
    }
    
}
