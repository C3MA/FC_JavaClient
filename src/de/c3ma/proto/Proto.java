package de.c3ma.proto;

/**
 * created at 08.02.2013 - 21:26:49<br />
 * creator: ollo<br />
 * project: FullcircleClient<br />
 * $Id: $<br />
 * @author ollo<br />
 */
public class Proto {
    
    public static final int PROTOTYPE_VARIANT = 0;
    public static final int PROTOTYPE_LENGTHD = 2;

    /**
     * 
     * @param buffer
     * @param offset
     * @return ProtobufType and the actual offset in the array.
     * @throws NumberFormatException on fault arguments
     */
    public static ReturnIdType parse(byte[] buffer, int offset) {
        int in;
        ReturnIdType ret = new ReturnIdType();
        
        in = parse_number(buffer, offset, ret);
        ret.type = (in & 0x7);
        ret.id = in>>3;
        
        return ret;
    }
    
    /**
     * 
     * @param buffer data working on
     * @param offset where to start
     * @param ret meta information about parser state (like actual offset)
     * @return number, extracted from the read byte part
     * @throws NumberFormatException on fault arguments
     */
    public static int parse_number(byte[] buffer, int offset, ReturnOffset ret) throws NumberFormatException {
        // Error
        if (offset == -1) {
            throw new NumberFormatException("Your offset must be between 0 and " + buffer.length);
        }
        
        int count = -1;
        int value=0;
        
        // check if the first bit is set (so the following byte must be read)
        do {
            count++;
            value += Math.pow(2, 7*count) * (buffer[offset+count] & 0x7F);
        } while ( (buffer[offset+count] & 0x80) > 0);
        ret.actualOffset = count+1+offset;
        
        return value;
    }

    /**
     * 
     * @param buffer
     * @param offset
     * @param id to store in the buffer
     * @param type to store in the buffer
     * @return new offset
     */
    public static int serialize(byte[] buffer, int offset, int id, int type)
    {
        int out;
        // Error
        if (offset == -1) {
            throw new NumberFormatException("Your offset must be between 0 and " + buffer.length);
        }
        out = type & 0x7;
        out = out | (id << 3);
        offset = serialize_number(buffer, offset, out);
        return offset;
    }
    
    /**
     * TODO was passiert bei value 0?
     * @param buffer
     * @param offset
     * @param value
     * @return
     */
    public static int serialize_number(byte[] buffer, int offset, int value) throws NumberFormatException
    {
        // Error
        if (offset == -1) {
            throw new NumberFormatException("Your offset must be between 0 and " + buffer.length);
        }
        
        do {
            buffer[offset] = (byte) (value & 0x7F);
            value = value>>7;
            // always set the first bit to indicate more bytes
            buffer[offset] |= 0x80;
            offset++;
        } while (value > 0);
        
        // last byte ! -> correct the first bit
        buffer[offset - 1] = (byte) (buffer[offset - 1] & 0x7F);
        
        return offset;
    }

    /**
     * read a string starting with the length
     * @param bytecode
     * @param offset start offset; must point to the length in front of the text
     * @param ret contains the position, where the parser stopped
     * @return the read text or <code>null</code> on errors
     */
    public static String parse_string(byte[] bytecode, int offset, ReturnIdType ret) {
        // TODO Auto-generated method stub
        int length = Proto.parse_number(bytecode, offset, ret);
        byte[] tmp = new byte[length];
        System.arraycopy(bytecode, ret.actualOffset, tmp, 0, length);
        ret.actualOffset += length;
        return new String(tmp);
    }
}
