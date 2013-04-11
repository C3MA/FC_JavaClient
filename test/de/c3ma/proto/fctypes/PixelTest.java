package de.c3ma.proto.fctypes;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * created at 11.04.2013 - 18:37:41<br />
 * creator: ollo<br />
 * project: FullcricleClient<br />
 * $Id: $<br />
 * @author ollo<br />
 */
public class PixelTest {

    @Test
    public void testWikiInput()
    {
        Pixel p = new Pixel(1, 2, 3, 4, 5);
        byte[] actuals = p.serialize();
        byte[] expecteds = { 0x0A, 0x0A, 0x8, 0x01, 0x10, 0x02, 0x18, 0x03, 0x20,0x04, 0x28, 0x05};
        assertArrayEquals(expecteds, actuals);
    }
    
    @Test
    public void testSerialize()
    {
        Pixel p = new Pixel(255, 128, 64, 2, 3);
        byte[] output = p.serialize();
        
    }
}
