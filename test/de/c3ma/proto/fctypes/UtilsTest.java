package de.c3ma.proto.fctypes;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * created at 18.04.2013 - 13:33:54<br />
 * creator: ollo<br />
 * project: FullcricleClient<br />
 * $Id: $<br />
 * @author ollo<br />
 */
public class UtilsTest {

    @Test
    public void testStringFormat() {
        assertEquals("         2", String.format("%10d", 2));
    }
    
    @Test
    public void testPrefixHeader() {
        byte[] payload = new byte[] { 1 , 2, 3};        
        byte[] actuals = Utils.prefixHeader(payload);
        
        final String header = "         3";
        assertEquals(10, header.length());
        byte[] expecteds = new byte[header.length() + 3];
        System.arraycopy(header.getBytes(), 0, expecteds, 0, header.length());
        System.arraycopy(payload, 0, expecteds, header.length(), payload.length); 
        assertArrayEquals(expecteds, actuals);
    }

}
