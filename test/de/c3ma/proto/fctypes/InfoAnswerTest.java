package de.c3ma.proto.fctypes;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

/**
 * created at 18.04.2013 - 15:44:31<br />
 * creator: ollo<br />
 * project: FullcricleClient<br />
 * $Id: $<br />
 * @author ollo<br />
 */
public class InfoAnswerTest {

    @Test
    public void wikiTest() {
        /*
         * meta {
    frames_per_second: 25
    width: 98
    height: 13
    generator_name: "Katze"
    generator_version: "999.9"
  }
         */
        
        InfoAnswer ia = new InfoAnswer(25, 98, 13, "Katze", "999.9");
        byte[] actuals = ia.serialize();
        byte[] expecteds = { 0x08, 0x0D, (byte) 0xBA, 0x01, 0x16, 0x0A, 0x14, 0x08, 0x19, 0x10, 0x62, 0x18, 0x0D, 0x22, 0x05, 0x4B, 0x61, 0x74, 0x7A, 0x65, 0x2A, 0x05, 0x39, 0x39, 0x39, 0x2E, 0x39 };
        assertArrayEquals(expecteds, actuals);
    }

    @Test
    public void testDeserialize() throws IOException {
        byte[] actuals = { 0x08, 0x0D, (byte) 0xBA, 0x01, 0x16, 0x0A, 0x14, 0x08, 0x19, 0x10, 0x62, 0x18, 0x0D, 0x22, 0x05, 0x4B, 0x61, 0x74, 0x7A, 0x65, 0x2A, 0x05, 0x39, 0x39, 0x39, 0x2E, 0x39 };
        InfoAnswer ia = new InfoAnswer();
        ia.deserialize(actuals, 2);        
        assertEquals(25, ia.getFPS());
        assertEquals(98, ia.getWidth());
        assertEquals(13, ia.getHeight());
        assertEquals("Katze", ia.getName());
        assertEquals("999.9", ia.getVersion());
    }

}
