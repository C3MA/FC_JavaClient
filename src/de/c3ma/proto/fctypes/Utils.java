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

    public static final int HEADER_SIZE = 10;

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
        int n = 0;

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
    
    /*
     * @param[in|out] buffer
     * @param[in] offset
     * @param[in] proto_id
     * @param[in] data buffer
     * @param[in] length of data
     * @return the new offset
     */
    static int addLengthd(byte[] buffer, int offset, int proto_id ,byte[] data, int length)
    {
        // Error
        if (offset == -1) {
            return -1;
        }
        offset = Proto.serialize(buffer, offset, proto_id, Proto.PROTOTYPE_LENGTHD);
        offset = Proto.serialize_number(buffer, offset, (int) length);
        // memcpy(buffer+offset, data, length);
        System.arraycopy(data, 0, buffer, offset, length); 
        offset +=  length;
        return offset;
    }

    static int addLengthd(byte[] buffer, int offset, int proto_id, String text) {
        byte[] bytes = text.getBytes();
        return addLengthd(buffer, offset, proto_id, bytes, bytes.length);
    }
    
    /**
     * Add the specific fullcircle header in front of a payload
     * @param payload the data to transmit
     * @return complete data containing the header and the payload
     */
    public static byte[] prefixHeader(final byte[] payload) {
        String header = String.format("%"+HEADER_SIZE+"d", payload.length); // format string should be always %10d 
        byte[] output = new byte[10 + payload.length];
        System.arraycopy(header.getBytes(), 0, output, 0, HEADER_SIZE);
        System.arraycopy(payload, 0, output, HEADER_SIZE, payload.length);
        return output;
    }

    public static FullcircleSerialize parseRequest() {
        // TODO Auto-generated method stub
        return null;
    }
}
