package de.c3ma.proto.fctypes;


import de.c3ma.proto.Proto;
import de.c3ma.types.SimpleColor;

/**
 * created at 11.04.2013 - 18:28:32<br />
 * creator: ollo<br />
 * project: FullcricleClient<br />
 * $Id: $<br />
 * 
 * @author ollo<br />
 */
public class Pixel implements FullcircleTypes {

    private static final int RGBVALUE_HEADER_SIZES = 2;
    private static final int HEADER_LENGTH = 5;
    
    private static final int RGB_MAX_VALUE = 255;
    
    private int red;
    private int green;
    private int blue;
    private int x;
    private int y;

    public Pixel(int red, int green, int blue, int x, int y) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.x = x;
        this.y = y;
    }
    
    public Pixel(int x, int y, SimpleColor c) {
        this.red = c.getRed();
        this.green = c.getGreen();
        this.blue = c.getBlue();
        this.x = x;
        this.y = y;
    }
    
    public int getSerializedLength() {
        return getSerializedDataLength() + RGBVALUE_HEADER_SIZES;
    }
    
    private int getSerializedDataLength() {
        return Utils.calculateVariantLength(RGBVALUE_RED, this.red)
        + Utils.calculateVariantLength(RGBVALUE_GREEN, this.green)
        + Utils.calculateVariantLength(RGBVALUE_BLUE, this.blue) 
        + Utils.calculateVariantLength(RGBVALUE_X, this.x)
        + Utils.calculateVariantLength(RGBVALUE_Y, this.y)
        + HEADER_LENGTH;
    }
    
    public byte[] serialize() {
        int lenght_pixel = getSerializedDataLength();

        byte[] tmpPixelBuffer = new byte[ lenght_pixel + RGBVALUE_HEADER_SIZES];
        int offset = 0;

        /*
         * Write the header for pixel structure
         */
        offset = Proto.serialize(tmpPixelBuffer, offset, BINARYFRAME_PIXEL, Proto.PROTOTYPE_LENGTHD);
        offset = Proto.serialize_number(tmpPixelBuffer, offset, lenght_pixel);

        /*
         * store the value into the buffer
         */
        offset = Utils.addVariant(tmpPixelBuffer, offset, RGBVALUE_RED, red);
        offset = Utils.addVariant(tmpPixelBuffer, offset, RGBVALUE_GREEN, green);
        offset = Utils.addVariant(tmpPixelBuffer, offset, RGBVALUE_BLUE, blue);
        offset = Utils.addVariant(tmpPixelBuffer, offset, RGBVALUE_X, x);
        offset = Utils.addVariant(tmpPixelBuffer, offset, RGBVALUE_Y, y);

        return tmpPixelBuffer;
    }

    @Override
    public String toString() {
        return x + "x" + y + " #" + Integer.toHexString(red) + Integer.toHexString(green) + Integer.toHexString(blue);
    }

    public void update(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    /**
     * Compare the coordinates
     * @param p
     * @return <code>true</code> the same position, <code>false</code> different positions
     */
    public boolean samePosition(Pixel p) {
        return this.x == p.x && this.y == p.y;
    }

    /**
     * Increment given the colors to this Pixel
     * @param p
     */
    public void increment(Pixel p) {
        this.red = Math.min(RGB_MAX_VALUE, this.red     + p.red);
        this.green = Math.min(RGB_MAX_VALUE, this.green + p.green);
        this.blue = Math.min(RGB_MAX_VALUE, this.blue   + p.blue);
    }

    
    /**
     * Dirty Hack, so the Wall only gets valid Pixel.
     * The x value is reduced to mWidth - 1, if too large.
     * The y value is reduced to mHeight - 1, if too large.
     * @param width    The width of the wall.
     * @param height   The height of the wall.
     */
    public void secureDimension(int width, int height) {
        if (x >= width) {
            x = width - 1;
        }
        
        if (y >= height) {
            y = height - 1;
        }
    }
}
