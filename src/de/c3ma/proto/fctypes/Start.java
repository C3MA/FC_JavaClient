package de.c3ma.proto.fctypes;

import java.io.IOException;

/**
 * created at 19.04.2013 - 09:23:29<br />
 * creator: ollo<br />
 * project: FullcircleClient<br />
 * $Id: $<br />
 * @author ollo<br />
 */
public class Start implements FullcircleTypes, FullcircleSerialize {

    @Override
    public void deserialize(byte[] bytecode, int actualOffset) throws IOException {
        // TODO Auto-generated method stub
    }

    @Override
    public String toString() {
        return "Start Message";
    }
}
