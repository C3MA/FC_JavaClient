package de.c3ma.proto.fctypes;

import java.io.IOException;

/**
 * created at 31.07.2013 - 17:51:50<br />
 * creator: ollo<br />
 * project: FullcricleClient<br />
 * $Id: $<br />
 * @author ollo<br />
 */
public class Ack implements FullcircleSerialize {

    @Override
    public void deserialize(byte[] bytecode, int actualOffset) throws IOException {
        
    }
    
    @Override
    public String toString() {
        return "Oki Doki";
    }

}
