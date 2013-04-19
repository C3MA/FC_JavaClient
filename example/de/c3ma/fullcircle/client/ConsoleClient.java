package de.c3ma.fullcircle.client;

import java.io.IOException;
import java.net.UnknownHostException;

import de.c3ma.fullcircle.RawClient;
import de.c3ma.proto.fctypes.FullcircleSerialize;

/**
 * created at 18.04.2013 - 13:56:21<br />
 * creator: ollo<br />
 * project: FullcircleClient<br />
 * $Id: $<br />
 * @author ollo<br />
 */
public class ConsoleClient {

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
            Thread.sleep(200);
            FullcircleSerialize got = rc.readNetwork();
            if (got != null) {
                System.out.println(got);
            }
        }
    }

}
