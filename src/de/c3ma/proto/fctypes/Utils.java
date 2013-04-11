package de.c3ma.proto.fctypes;

import de.c3ma.proto.Proto;

/**
 * created at 11.04.2013 - 18:11:09<br />
 * creator: ollo<br />
 * project: FullcircleClient<br />
 * $Id: $<br />
 * @author ollo<br />
 */
public class Utils implements FullcircleTypes {

    /**
     * 
     * @param buffer
     * @param offset
     * @param typ, value to write
     * @return new offset
     */
    static int addType(byte[] buffer, int offset, int typ)
    {
        // Error
        if (offset == -1) {
            return -1;
        }
        offset = addVariant(buffer, offset, SNIP_TYPE, typ);
        return offset;
    }
    
    /**
     * 
     * @param buffer
     * @param offset
     * @param proto_id
     * @param value
     * @return
     */
    static int addVariant(byte[] buffer, int offset, int proto_id ,int value)
    {
        // Error
        if (offset == -1) {
            return -1;
        }
        offset = Proto.serialize(buffer, offset, proto_id, Proto.PROTOTYPE_VARIANT);
        offset = Proto.serialize_number(buffer, offset, value);
        return offset;
    }
    
    /**
     * Calculate the amount of bytes needed for the corresponding value
     * @param proto_id
     * @param value
     * @return number of bytes needed for <code>value</code>
     */
    static int calculateVariantLength(int proto_id ,int value)
    {
        int n = 1; // start with one byte, as we need always one byte for the header

        //Check if proto_id serialized more than one Byte is
        if (proto_id >> 7 != 0) {
            n += 1;
        }
        // calculate the number of bytes for the Value
        do {
            value >>= 7;
            n++;
        } while (value != 0);
        return n;
    }
}
