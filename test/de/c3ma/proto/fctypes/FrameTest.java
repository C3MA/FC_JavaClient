package de.c3ma.proto.fctypes;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;


/**
 * created at 11.04.2013 - 20:12:07<br />
 * creator: ollo<br />
 * project: FullcricleClient<br />
 * $Id: $<br />
 * @author ollo<br />
 */
public class FrameTest {

    @Test
    public void wikiTest()
    {
        /*
         * pixel {
      red: 1
      green: 2
      blue: 3
      x: 4
      y: 5
    }
    pixel {
      red: 99
      green: 98
      blue: 97
      x: 96
      y: 95
    }
         */
        Frame f = new Frame();
        f.add(new Pixel(1, 2, 3, 4, 5));
        f.add(new Pixel(99, 98, 97, 96, 95));
        byte[] actuals = f.serialize();
        byte[] expecteds = { 0x08, 0x06, (byte) 0x82, 0x01, 0x1A, 0x0A, 0x18, 0x0A, 0x0A, 0x08, 0x01, 0x10, 0x02, 0x18, 0x03, 0x20, 0x04, 0x28, 0x05, 0x0A, 0x0A, 0x08, 0x63, 0x10, 0x62, 0x18, 0x61, 0x20, 0x60, 0x28, 0x5F };
        assertArrayEquals(expecteds, actuals);
    }
    
    @Test
    public void wikiBigFrame()
    {
       /*
frame {
  pixel {
    red: 255
    green: 255
    blue: 255
    x: 1024
    y: 1024
  }
  pixel {
    red: 255
    green: 255
    blue: 255
    x: 1024
    y: 1024
  }
  pixel {
    red: 255
    green: 255
    blue: 255
    x: 1024
    y: 1024
  }
  pixel {
    red: 255
    green: 255
    blue: 255
    x: 1024
    y: 1024
  }
  pixel {
    red: 255
    green: 255
    blue: 255
    x: 1024
    y: 1024
  }
}
        */
        Frame f = new Frame();
        f.add(new Pixel(255, 255, 255, 1024, 1024));
        f.add(new Pixel(255, 255, 255, 1024, 1024));
        f.add(new Pixel(255, 255, 255, 1024, 1024));
        f.add(new Pixel(255, 255, 255, 1024, 1024));
        f.add(new Pixel(255, 255, 255, 1024, 1024));
        byte[] actuals = f.serialize();
        byte[] expecteds = {0x08, 0x06, (byte) 0x82, 0x01, 0x57, 0x0A, 0x55, 0x0A, 0x0F, 0x08, (byte)0xFF, 0x01, 0x10, (byte) 0xFF, 0x01, 0x18, (byte) 0xFF, 0x01, 0x20, (byte)0x80, 0x08
                , 0x28, (byte)0x80, 0x08, 0x0A, 0x0F, 0x08, (byte)0xFF, 0x01, 0x10,(byte) 0xFF, 0x01, 0x18, (byte)0xFF, 0x01, 0x20,(byte) 0x80, 0x08, 0x28, (byte)0x80, 0x08, 0x0A, 0x0F, 0x08, (byte)0xFF, 0x01, 0x10, (byte)0xFF, 0x01, 0x18, (byte)0xFF, 0x01, 0x20, (byte)0x80, 0x08, 0x28, (byte)0x80, 0x08, 0x0A, 0x0F, 0x08, (byte)0xFF, 0x01, 0x10, (byte)0xFF, 0x01, 0x18, (byte)0xFF, 0x01, 0x20,(byte) 0x80, 0x08, 0x28, (byte)0x80, 0x08, 0x0A, 0x0F, 0x08,(byte) 0xFF, 0x01, 0x10,(byte) 0xFF, 0x01, 0x18,(byte) 0xFF, 0x01, 0x20, (byte)0x80, 0x08, 0x28,(byte) 0x80, 0x08};
        assertArrayEquals(expecteds, actuals);
    }
}
