package de.c3ma.proto.fctypes;

import de.c3ma.proto.Proto;

/**
 * created at 18.04.2013 - 13:22:31<br />
 * creator: ollo<br />
 * project: FullcircleClient<br />
 * $Id: $<br />
 * @author ollo<br />
 */
public class InfoRequest implements FullcircleTypes {

    
    public byte[] serialize() {
        int offset = 0;
        byte[] buffer = new byte[1024];
        
        offset = Utils.addType(buffer, offset, SNIPTYPE_INFOREQUEST);
        offset = Proto.serialize(buffer, offset, SNIP_INFOREQUEST, Proto.PROTOTYPE_LENGTHD);
        
        //The length of information is always zero:
        offset = Proto.serialize_number(buffer, offset, 0);
        
        byte[] info = new byte[offset];
        System.arraycopy(buffer, 0, info, 0, offset);
        return info;
    }
}
