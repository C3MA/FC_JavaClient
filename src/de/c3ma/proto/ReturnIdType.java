package de.c3ma.proto;

/**
 * created at 11.04.2013 - 17:33:53<br />
 * creator: ollo<br />
 * project: FullcircleClient<br />
 * $Id: $<br />
 * 
 * @author ollo<br />
 */
public class ReturnIdType extends ReturnOffset {

    protected int id;
    protected int type;

    public ReturnIdType() {
        super();
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }
    
    @Override
    public String toString() {
        return "Id=" + id +"; type=" + type;
    }
}
