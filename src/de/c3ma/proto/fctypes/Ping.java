package de.c3ma.proto.fctypes;

import java.io.IOException;

import de.c3ma.proto.Proto;

/**
 * created at 11.04.2013 - 18:08:04<br />
 * creator: ollo<br />
 * project: FullcircleClient<br />
 * $Id: $<br />
 * @author ollo<br />
 */
public class Ping implements FullcircleTypes, FullcircleSerialize {

    private int counter = 0;
    
    public Ping(final int counter) {
        this.counter = counter;
    }
    
    byte[] serialize() {
        int offset = 0;
        byte[] buffer = new byte[1024];
     
        offset = serializePing(buffer, 0, this.counter);
        
        byte[] ping = new byte[offset];
        System.arraycopy(buffer, 0, ping, 0, offset);
        return ping;
    }
    
    /**
     * 
     * @param buffer
     * @param offset
     * @param counter value of the counter, to write
     * @return new offset in the buffer
     */
    private int serializePing(byte[] buffer, int offset, int counter) {
        
        offset = Utils.addType(buffer, offset, SNIPTYPE_PING);
        
        /*
         * Write the header for Ping structure
         */
        offset = Proto.serialize(buffer, offset, SNIP_PINGSNIP, Proto.PROTOTYPE_LENGTHD);
        offset = Proto.serialize_number(buffer, offset, Utils.calculateVariantLength(PINGSNIP_COUNT, counter));
        
        /*
         * store the value into the buffer
         */
        offset = Utils.addVariant(buffer, offset, PINGSNIP_COUNT, counter);
        
        return offset;
    }


    @Override
    public void deserialize(byte[] bytecode, int actualOffset) throws IOException {
        // TODO Auto-generated method stub
        
    }
}
