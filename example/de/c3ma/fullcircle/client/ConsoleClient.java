package de.c3ma.fullcircle.client;

import java.io.IOException;
import java.net.UnknownHostException;

import de.c3ma.fullcircle.RawClient;
import de.c3ma.proto.fctypes.Frame;
import de.c3ma.proto.fctypes.FullcircleSerialize;
import de.c3ma.proto.fctypes.InfoAnswer;
import de.c3ma.proto.fctypes.Meta;
import de.c3ma.proto.fctypes.Pixel;
import de.c3ma.proto.fctypes.Start;
import de.c3ma.proto.fctypes.Timeout;

/**
 * created at 18.04.2013 - 13:56:21<br />
 * creator: ollo<br />
 * project: FullcircleClient<br />
 * $Id: $<br />
 * @author ollo<br />
 */
public class ConsoleClient {

    private static boolean mSendFrames = false;

    /**
     * @param args First argument MUST be the IP address of the host
     * @throws IOException 
     * @throws UnknownHostException 
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
        RawClient rc = new RawClient(args[0]);
        
        rc.requestInformation();
        while(true) {
            Thread.sleep(10);
            FullcircleSerialize got = rc.readNetwork();
            if (got != null) {
                System.out.println(got);
                if (got instanceof InfoAnswer) {
                    /* Extract the expected resolution and use these values for the request */
                    Meta meta = ((InfoAnswer) got).getMeta();

                    /* when we got the resolution of the map, in this example we now want to start to send something */
                    rc.requestStart("java", 1, meta);    
                } else if (got instanceof Start) {
                    System.out.println("We have a GOOO send some data!");
                    mSendFrames = true;
                } else if (got instanceof Timeout) {
                    System.out.println("Too slow, so we close the session");
                    rc.close();
                    System.exit(1);
                }
            }
            
            // send something... NOW
            if (mSendFrames) {
                Frame f = new Frame();
                f.add(new Pixel(255, 0, 0, 0, 0));
                rc.sendFrame(f);
            }
        }
    }

}
