package de.c3ma.proto.fctypes;

import de.c3ma.proto.Proto;


class Meta implements FullcircleTypes {

    int fps = 0;
    int width = 0;
    int height = 0;
    String name = "";
    String version = "";

    public Meta(int fps, int width, int height, String name, String version) {
        this.fps = fps;
        this.width = width;
        this.height = height;
        this.name = name;
        this.version = version;
    }

    public Meta() {
        
    }
    

    byte[] serialize() {
        int offset = 0;
        byte[] buffer = new byte[1024];
        
        offset = Utils.addVariant(buffer, offset, BINARYSEQUENCEMETADATA_FRAMESPERSECOND, fps);
        
        offset = Utils.addVariant(buffer, offset, BINARYSEQUENCEMETADATA_WIDTH, width);
        offset = Utils.addVariant(buffer, offset, BINARYSEQUENCEMETADATA_HEIGHT, height);
        
        offset = Utils.addLengthd(buffer, offset, BINARYSEQUENCEMETADATA_GENERATORNAME, name);
        
        offset = Utils.addLengthd(buffer, offset, BINARYSEQUENCEMETADATA_GENERATORVERSION, version);
        
        byte[] meta = new byte[offset];
        System.arraycopy(buffer, 0, meta, 0, offset);
        return meta;
    }
    
}

/**
 * created at 12.04.2013 - 21:57:22<br />
 * creator: ollo<br />
 * project: FullcircleClient<br />
 * $Id: $<br />
 * @author ollo<br />
 */
public class Request implements FullcircleTypes {

    private static final int DEFAULT_BUFFER = 1024;
    
    private int sequenceId;
    private String color;
    private Meta meta = new Meta();

    public Request(String color, int sequenceId, Meta meta) {
        this.color = color;
        this.sequenceId = sequenceId;
        this.meta = meta;
    }

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
        offset = Utils.addType(buffer, offset, SNIPTYPE_REQUEST);
        offset = Proto.serialize(buffer, offset, SNIP_REQUESTSNIP, Proto.PROTOTYPE_LENGTHD);
        
        
        /**insert the data into a temporary buffer to calculate the length */
        byte[] tmpBuffer = new byte[DEFAULT_BUFFER];
        int tmpOffset = 0;        
        tmpOffset = Utils.addLengthd(tmpBuffer, tmpOffset, REQUESTSNIP_COLOR, this.color);
        
        tmpOffset = Utils.addVariant(tmpBuffer, tmpOffset, REQUESTSNIP_SEQID, this.sequenceId);
        
        tmpOffset = Utils.addLengthd(tmpBuffer, tmpOffset, REQUESTSNIP_META, meta, length_meta);
        
        // use this length for the header
        offset = Proto.serialize_number(buffer, offset, tmpOffset);
        // and copy the generated temporary buffer to its target position after the header into the correct buffer
        System.arraycopy(tmpBuffer, 0, buffer, offset, tmpOffset);
        
        return offset + tmpOffset;

    }   
    
}
