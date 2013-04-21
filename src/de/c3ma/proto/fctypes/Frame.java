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
    
    public Frame() {
        
    }
    
    public void add(Pixel p)
    {
        this.mPixels.add(p);
    }
    
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
    
}
