package de.c3ma.proto.fctypes;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;


/**
 * created at 12.04.2013 - 21:58:14<br />
 * creator: ollo<br />
 * project: FullcricleClient<br />
 * $Id: $<br />
 * @author ollo<br />
 */
public class RequestTest {

    @Test
    public void testWikiInput() { 
        /*
         req_snip {
          color: "rot"
          seqId: 1
          meta {
            frames_per_second: 24
            width: 1
            height: 1
            generator_name: "Test"
            generator_version: "0.0"
          }
        }
         */
        Meta meta = new Meta(24, 1, 1, "Test", "0.0");
        Request r = new Request("rot", 1, meta);
        byte[] actuals = r.serialize();
        byte[] expecteds = { 0x08, 0x04, 0x72, 0x1A, 0x0A, 0x03, 0x72, 0x6F, 0x74, 0x10, 0x01, 0x1A, 0x11, 0x08, 0x18, 0x10, 0x01, 0x18, 0x01, 0x22, 0x04, 0x54, 0x65, 0x73, 0x74, 0x2A, 0x03, 0x30, 0x2E, 0x30 };
        assertArrayEquals(expecteds, actuals);
        
    }
}
