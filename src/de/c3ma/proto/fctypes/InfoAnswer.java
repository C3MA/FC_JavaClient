package de.c3ma.proto.fctypes;

import java.io.IOException;

import de.c3ma.proto.Proto;
import de.c3ma.proto.ReturnIdType;

/**
 * created at 18.04.2013 - 15:34:22<br />
 * creator: ollo<br />
 * project: FullcircleClient<br />
 * $Id: $<br />
 * @author ollo<br />
 */
public class InfoAnswer implements FullcircleSerialize, FullcircleTypes {


    private Meta meta = new Meta();
    
    public InfoAnswer(int fps, int width, int height, String name, String version) {
        meta.fps = fps;
        meta.width = width;
        meta.height = height;
        meta.name = name;
        meta.version = version;
    }

    public InfoAnswer() { }

    public byte[] serialize() {
        byte[] buffer = new byte[DEFAULT_BUFFER];
        int offset = 0;
        
        byte[] metaBuffer = meta.serialize(); 
        offset = send_infoanswer(buffer, offset, metaBuffer, metaBuffer.length);
        
        byte[] request = new byte[offset];
        System.arraycopy(buffer, 0, request, 0, offset);
        return request;
    }

    private int send_infoanswer(byte[] buffer, int offset, byte[] meta, int length_meta) {
        // build the static headers
        offset = Utils.addType(buffer, offset, SNIPTYPE_INFOANSWER);
        offset = Proto.serialize(buffer, offset, SNIP_INFOANSWERSNIP, Proto.PROTOTYPE_LENGTHD);
        
        
        /**insert the data into a temporary buffer to calculate the length including the header */
        byte[] tmpBuffer = new byte[DEFAULT_BUFFER];
        int tmpOffset = 0;        
        tmpOffset = Utils.addLengthd(tmpBuffer, tmpOffset, INFOANSWERSNIP_META, meta, length_meta);
        
        // use this length for the header
        offset = Proto.serialize_number(buffer, offset, tmpOffset);
        // and copy the generated temporary buffer to its target position after the header into the correct buffer
        System.arraycopy(tmpBuffer, 0, buffer, offset, tmpOffset);
        
        return offset + tmpOffset;

    }   
    
    @Override
    public void deserialize(byte[] bytecode, final int actualOffset) throws IOException {
        //TODO insert some logic here!
        
        /* First parse the header to determine the correct type */
        ReturnIdType ret = Proto.parse(bytecode, actualOffset);
        if (ret.getId() != SNIP_INFOANSWERSNIP || ret.getType() != Proto.PROTOTYPE_LENGTHD) {
            throw new IOException("Unexpected header for InfoAnswer");
        }
        
        int metaLength = Proto.parse_number(bytecode, ret.getOffset(), ret);
        if (metaLength + ret.getOffset() > bytecode.length) {
            throw new IOException("Meta should have " + metaLength + " bytes, but " + (bytecode.length- ret.getOffset()) + " are only available."); 
        }
        
        /* The body containing the meta information needs to be unpacked */
        ret = Proto.parse(bytecode, ret.getOffset());
        if (ret.getId() != INFOANSWERSNIP_META || ret.getType() != Proto.PROTOTYPE_LENGTHD) {
            throw new IOException("Unexpected body with meta information");
        }
        
        int metaContent = Proto.parse_number(bytecode, ret.getOffset(), ret);
        if (metaContent + ret.getOffset() > bytecode.length) {
            throw new IOException("Meta data content should have " + metaLength + " bytes, but " + (bytecode.length- ret.getOffset()) + " are only available."); 
        }
        
        meta.deserialize(bytecode, ret.getOffset());
    }

    public int getFPS() {
        return meta.fps;
    }

    public int getWidth() {
        return meta.width;
    }

    public int getHeight() {
        return meta.height;
    }

    public String getName() {
        return meta.name;
    }

    public String getVersion() {
        return meta.version;
    }

    
    @Override
    public String toString() {
        return "InfoAnswer { " + meta.fps + ", " + meta.width + "x" + meta.height + ", " 
            + meta.name + ", " + meta.version + "}";
    }
}
