package de.c3ma.proto.fctypes;

import java.io.IOException;

import de.c3ma.proto.Proto;
import de.c3ma.proto.ReturnIdType;

/**
 * created at 02.08.2013 - 22:40:53<br />
 * creator: ollo<br />
 * project: FullcricleClient<br />
 * $Id: $<br />
 * @author ollo<br />
 */
public class ErrorType implements FullcircleSerialize, FullcircleTypes {

    private int errorCode;
    private String message;

    @Override
    public void deserialize(byte[] bytecode, int actualOffset) throws IOException {
        /* First parse the header to determine the correct type */
        ReturnIdType ret = Proto.parse(bytecode, actualOffset);
        if (ret.getId() != SNIP_ERRORSNIP || ret.getType() != Proto.PROTOTYPE_LENGTHD) {
            throw new IOException("Unexpected header for Errortype");
        }
        
        int errorLength = Proto.parse_number(bytecode, ret.getOffset(), ret);
        if (errorLength + ret.getOffset() > bytecode.length) {
            throw new IOException("ErrorCode should have " + errorLength + " bytes, but " + (bytecode.length- ret.getOffset()) + " are only available."); 
        }
        
        /* The body containing the meta information needs to be unpacked */
        ret = Proto.parse(bytecode, ret.getOffset());
        if (ret.getId() != ERRORSNIP_ERRORCODE || ret.getType() != Proto.PROTOTYPE_VARIANT) {
            throw new IOException("Missing the ErrorCode");
        }
        this.errorCode = Proto.parse_number(bytecode, ret.getOffset(), ret);
        
        ret = Proto.parse(bytecode, ret.getOffset());
        if (ret.getId() != ERRORSNIP_DESCRIPTION || ret.getType() != Proto.PROTOTYPE_LENGTHD) {
            throw new IOException("Missing the Message WHY!?!");
        }
        this.message = Proto.parse_string(bytecode, ret.getOffset(), ret);
    }

    @Override
    public String toString() {
        return "" + this.errorCode + ":" + this.message;
    }
    
}
