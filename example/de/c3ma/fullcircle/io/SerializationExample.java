package de.c3ma.fullcircle.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import de.c3ma.proto.fctypes.Frame;

/**
 * created at 12.08.2013 - 19:19:09<br />
 * creator: ollo<br />
 * project: FullcricleClient<br />
 * $Id: $<br />
 * @author ollo<br />
 */
public class SerializationExample {

    private static final int WIDTH = 7;
    private static final int HEIGHT = 10;

    /**
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        /* generate some dummy data */
        Sequence s = new Sequence(12, WIDTH, HEIGHT);
        Frame f = new Frame(WIDTH, HEIGHT);
        f.updatePixel(0, 255, 255, 2, 0);
        s.addFrame(f);
        Frame f2 = new Frame(WIDTH, HEIGHT);
        f2.updatePixel(0, 0, 255, 4, 1);
        s.addFrame(f);
        Frame f3 = new Frame(WIDTH, HEIGHT);
        f3.updatePixel(0, 255, 0, 2, 4);
        s.addFrame(f);
        
        /* Serialize it */
        FileOutputStream fos = new FileOutputStream(new File("test.seq"));
        fos.write(s.serialize());
        fos.close();
        System.out.println("File generated :-)");
    }

}
