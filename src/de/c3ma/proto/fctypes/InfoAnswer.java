package de.c3ma.proto.fctypes;

import de.c3ma.proto.Proto;

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
    public void deserialize(byte[] bytecode) {
        //TODO insert some logic here!
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

}
