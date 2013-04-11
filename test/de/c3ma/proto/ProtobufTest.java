package de.c3ma.proto;

import static org.junit.Assert.*;

import org.junit.Test;

import de.c3ma.proto.Proto;


/**
 * created at 08.02.2013 - 21:22:26<br />
 * creator: ollo<br />
 * project: FullcircleClient<br />
 * $Id: $<br />
 * @author ollo<br />
 */
public class ProtobufTest {

    @Test
    public void testStartSnippet() {
        byte[] buffer = { 0x08, 0x05, 0x7A, 0x00 }; 
        ReturnIdType extracted = Proto.parse(buffer, 0);
        /* 
         *      0x08 = typ:0 fid:1
    0x05 = value: 5 (Sniptype: 5)
    0x7A = typ:2 fid:15
    0x00 = length: 0 
         */
        assertEquals(extracted.type, 0);
        assertEquals(extracted.id,  1);
    }
}
