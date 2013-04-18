package de.c3ma.proto.fctypes;

/**
 * created at 18.04.2013 - 15:39:53<br />
 * creator: ollo<br />
 * project: FullcircleClient<br />
 * $Id: $<br />
 * @author ollo<br />
 */
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
