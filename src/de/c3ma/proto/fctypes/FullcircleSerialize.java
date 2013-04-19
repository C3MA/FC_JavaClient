package de.c3ma.proto.fctypes;

import java.io.IOException;

/**
 * created at 18.04.2013 - 15:35:11<br />
 * creator: ollo<br />
 * project: FullcircleClient<br />
 * $Id: $<br />
 * @author ollo<br />
 */
public interface FullcircleSerialize {
    
    public void deserialize(final byte[] bytecode, final int actualOffset)  throws IOException;
}
