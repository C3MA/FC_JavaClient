package de.c3ma.proto;

/**
 * created at 11.04.2013 - 17:42:49<br />
 * creator: ollo<br />
 * project: FullcircleClient<br />
 * $Id: $<br />
 * @author ollo<br />
 */
abstract class ReturnOffset {
    protected int actualOffset;

    public ReturnOffset() {
    }
    
    public int getOffset() {
        return actualOffset;
    }
}
