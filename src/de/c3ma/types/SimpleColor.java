package de.c3ma.types;

/**
 * created at 01.08.2013 - 14:34:08<br />
 * creator: ollo<br />
 * project: FullcricleClient<br />
 * $Id: $<br />
 * @author ollo<br />
 */
public class SimpleColor {

    private int red;
    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getBlue() {
        return blue;
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
        return "" + red + "," + green + "," + blue;
    }
    
}
