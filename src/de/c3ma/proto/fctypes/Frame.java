package de.c3ma.proto.fctypes;

import java.util.ArrayList;

import de.c3ma.proto.Proto;

/**
 * created at 11.04.2013 - 19:21:26<br />
 * creator: ollo<br />
 * project: FullcircleClient<br />
 * $Id: $<br />
 * @author ollo<br />
 */
public class Frame implements FullcircleTypes {

    private static final int HEADER_SIZE = 10;
    private static final int BUFFER_SPACE = 2048;
    private ArrayList<Pixel> mPixels = new ArrayList<Pixel>();
    private int width = 0;
    
    /**
     * The user must add enough Pixel (@see this{@link #add(Pixel)} to reach the necessary resolution.
     */
    public Frame() {
        
    }
    
    /**
     * This constructor needs the resolution of the frame, but provides a direct access to the Pixel.
     * The pixel can be modified on demand
     * @param width
     * @param height
     */
    public Frame(int width, int height) {
        this.width  = width;
        for(int y=0; y < height; y++)
        {
            for(int x=0; x < width; x++)
            {
                mPixels.add(new Pixel(0, 0, 0, x, y));
            }
        }
    }
    
    /**
     * Only if the second constructor was used, and the width is known, the pixels can be updated directly
     * @param red
     * @param green
     * @param blue
     * @param x
     * @param y
     * @return
     */
    public boolean updatePixel(int red, int green, int blue, int x, int y) {
        if (width <= 0)
            return false;
        mPixels.get(y*width + x).update(red, green, blue);
        return true;
    }

    public void add(Pixel p)
    {
        this.mPixels.add(p);
    }
    
    /**
     * Delete all already stored Pixel
     */
    public void clear() {
        this.mPixels.clear();
    }
    
    /**
     * Increments the given color information to an already existing Pixel
     * When there is no Pixel for the given position, this Pixel is added as a new one.
     * @param p color to add an a specific position
     * @return <code>true</code> a Pixel to add was found, <code>false</code> the Pixel was added as new one.
     */
    public boolean increment(Pixel p)
    {
        for (Pixel item : mPixels) {
            if (item.samePosition(p)) {
                item.increment(p);
                return true;
            }
         }

        /* This end is only reached when no pixel fits */
        this.mPixels.add(p);
        return false;
    }
    
    /**
     * Serialize Meta length to tansmit and Data
     * @return
     */
    public byte[] serialize()
    {
        byte [] frame = serializeAllPixels();
        final int pixelDataSize = frame.length;
                
        int offset=0;
        byte[] length_frame_serialized = new byte[HEADER_SIZE]; //TODO: Größe ermitteln
        int lenght_frame_length = Proto.serialize_number(length_frame_serialized, 0, pixelDataSize);
        int outputBufferLength = lenght_frame_length + pixelDataSize + 6;
        byte[] tempBuffer = new byte[outputBufferLength + BUFFER_SPACE];
        
        offset = Utils.addType(tempBuffer, offset, SNIPTYPE_FRAME);
        offset = Proto.serialize(tempBuffer, offset, SNIP_FRAMESNIP, Proto.PROTOTYPE_LENGTHD);
        /*
         * Add header for Frames, with two length values. Calculate first with length + length of next header :-/
         */
        offset = Proto.serialize_number(tempBuffer, offset, pixelDataSize + lenght_frame_length + 1);
        
        if (offset + pixelDataSize >= outputBufferLength)
            throw new NumberFormatException("Need space for " + (offset + pixelDataSize) + " bytes, but only " + outputBufferLength + " are available.");
        
        offset = Utils.addLengthd(tempBuffer, offset, FRAMESNIP_FRAME, frame, pixelDataSize);
        
        /* Shrink the returning buffer to the needed size */
        byte[] retBuffer = new byte[offset];
        System.arraycopy(tempBuffer, 0, retBuffer, 0, offset);
        return retBuffer;
    }
    
    /**
     * Serialize only this frame (all containing pixel)
     * @return
     */
    public byte[] serializeBinaryFrame()
    {
        byte [] frame = serializeAllPixels();
        final int pixelDataSize = frame.length;
                
        int offset=0;
        int outputBufferLength = pixelDataSize + 1;
        byte[] tempBuffer = new byte[outputBufferLength + BUFFER_SPACE ];
        
        if (offset + pixelDataSize >= outputBufferLength)
            throw new NumberFormatException("Need space for " + (offset + pixelDataSize) + " bytes, but only " + outputBufferLength + " are available.");

        offset = Utils.addLengthd(tempBuffer, offset, BINARYSEQUENCE_FRAME, frame, pixelDataSize);
        
        /* Shrink the returning buffer to the needed size */
        byte[] retBuffer = new byte[offset];
        System.arraycopy(tempBuffer, 0, retBuffer, 0, offset);
        return retBuffer;
    }

    private byte[] serializeAllPixels() {
        byte[] buffer = new byte[calculateSerializedPixelLength()];
        int offset = 0;
        for (Pixel p : this.mPixels) {
            byte[] tmp = p.serialize();
            System.arraycopy(tmp, 0, buffer, offset, tmp.length);
            offset += tmp.length;
        }
        return buffer;
    }
        
    private int calculateSerializedPixelLength() {
        int amount = 0;
        for (Pixel p : this.mPixels) {
            amount += p.getSerializedLength();
        }
        return amount;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        if (width == 0)
            return 0;
        else
            return this.mPixels.size() / width;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        for (Pixel p : mPixels) {
            sb.append(p + ", ");
        }
        sb.append("}");
        return sb.toString();
    }

    public int calcMaxSize() {
        return mPixels.size() * new Pixel(255,255,255, 999, 999).getSerializedLength();
    }
    
}
