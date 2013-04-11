package de.c3ma.proto.fctypes;

import de.c3ma.proto.Proto;

/**
 * created at 11.04.2013 - 18:28:32<br />
 * creator: ollo<br />
 * project: FullcricleClient<br />
 * $Id: $<br />
 * 
 * @author ollo<br />
 */
public class Pixel implements FullcircleTypes {

    private static final int HEADER_LENGTH = 7;
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

    public byte[] serialize() {
        int lenght_pixel = Utils.calculateVariantLength(RGBVALUE_RED, red)
                + Utils.calculateVariantLength(RGBVALUE_GREEN, green)
                + Utils.calculateVariantLength(RGBVALUE_BLUE, blue) 
                + Utils.calculateVariantLength(RGBVALUE_X, x)
                + Utils.calculateVariantLength(RGBVALUE_Y, y);

        byte[] tmpPixelBuffer = new byte[HEADER_LENGTH + lenght_pixel];
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
}
