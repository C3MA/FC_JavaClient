package de.c3ma.proto.fctypes;

import java.io.IOException;

import de.c3ma.proto.Proto;
import de.c3ma.proto.ReturnIdType;

/**
 * created at 18.04.2013 - 15:39:53<br />
 * creator: ollo<br />
 * project: FullcircleClient<br />
 * $Id: $<br />
 * @author ollo<br />
 */
public class Meta implements FullcircleTypes, FullcircleSerialize {
    
    /**
     * frames per second
     */
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
    

    public byte[] serialize() {
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

    @Override
    public void deserialize(byte[] bytecode, int actualOffset) throws IOException {
        /* read fps */
        ReturnIdType ret = Proto.parse(bytecode, actualOffset);
        if (ret.getId() != BINARYSEQUENCEMETADATA_FRAMESPERSECOND || ret.getType() != Proto.PROTOTYPE_VARIANT) {
            throw new IOException("Expected FPS");
        }
        this.fps = Proto.parse_number(bytecode, ret.getOffset(), ret);
        
        /* read width */
        ret = Proto.parse(bytecode, ret.getOffset());
        if (ret.getId() != BINARYSEQUENCEMETADATA_WIDTH || ret.getType() != Proto.PROTOTYPE_VARIANT) {
            throw new IOException("Expected width");
        }
        this.width = Proto.parse_number(bytecode, ret.getOffset(), ret);
        
        /* read height */
        ret = Proto.parse(bytecode, ret.getOffset());
        if (ret.getId() != BINARYSEQUENCEMETADATA_HEIGHT || ret.getType() != Proto.PROTOTYPE_VARIANT) {
            throw new IOException("Expected height");
        }
        this.height = Proto.parse_number(bytecode, ret.getOffset(), ret);
        
        /* read name */
        ret = Proto.parse(bytecode, ret.getOffset());
        if (ret.getId() != BINARYSEQUENCEMETADATA_GENERATORNAME || ret.getType() != Proto.PROTOTYPE_LENGTHD) {
            throw new IOException("Expected name");
        }
        this.name = Proto.parse_string(bytecode, ret.getOffset(), ret);
        
        /* read version */
        ret = Proto.parse(bytecode, ret.getOffset());
        if (ret.getId() != BINARYSEQUENCEMETADATA_GENERATORVERSION || ret.getType() != Proto.PROTOTYPE_LENGTHD) {
            throw new IOException("Expected version");
        }
        this.version = Proto.parse_string(bytecode, ret.getOffset(), ret);
    }
    
}
