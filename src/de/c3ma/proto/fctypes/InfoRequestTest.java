package de.c3ma.proto.fctypes;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * created at 18.04.2013 - 13:26:38<br />
 * creator: ollo<br />
 * project: FullcricleClient<br />
 * $Id: $<br />
 * @author ollo<br />
 */
public class InfoRequestTest {

    @Test
    public void wikiTest() {
        
        /*
         => type: INFO_REQUEST
        inforequest_snip {
        }
         */
        InfoRequest r = new InfoRequest();
        byte[] actuals = r.serialize();
        byte[] expecteds = { 0x08, 0x0C, (byte) 0xB2, 0x01, 0x00 };
//        System.out.println("Actual: " + new String(actuals));
//        System.out.println("Expecteds: " + new String(expecteds));
//        dumparray("actual:\t\t", actuals);
//        dumparray("expecteds:\t", expecteds);
        assertArrayEquals(expecteds, actuals);
    }

}
