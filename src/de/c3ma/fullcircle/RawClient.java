package de.c3ma.fullcircle;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import de.c3ma.proto.fctypes.Frame;
import de.c3ma.proto.fctypes.FullcircleSerialize;
import de.c3ma.proto.fctypes.InfoRequest;
import de.c3ma.proto.fctypes.Meta;
import de.c3ma.proto.fctypes.Pixel;
import de.c3ma.proto.fctypes.Request;
import de.c3ma.proto.fctypes.Utils;

/**
 * created at 18.04.2013 - 13:52:50<br />
 * creator: ollo<br />
 * project: FullcircleClient<br />
 * $Id: $<br />
 * @author ollo<br />
 */
public class RawClient {
    
    private Socket mSocket;

    /**
     * Global entry point
     * @param host the name or IP address to connect to.
     * @throws IOException 
     * @throws UnknownHostException 
     */
    public RawClient(final String host) throws UnknownHostException, IOException {
        this.mSocket = new Socket( host, 24567 );
    }
    
    /**
     * Request information about the current used resolution of the wall
     * @throws IOException
     */
    public void requestInformation() throws IOException {
        OutputStream netout = this.mSocket.getOutputStream();
        netout.write(Utils.prefixHeader(new InfoRequest().serialize()));
        netout.flush();
    }

    /**
     * This method should be called cyclic to detect incoming information
     * @throws IOException
     * @return <code>null</code> when no data was found or the Data is invalid
     */
    public FullcircleSerialize readNetwork() throws IOException {
        InputStream netin = this.mSocket.getInputStream();
        if (netin.available() > 0) {
            System.out.println("WE have data!");
            byte[] buffer = new byte[ Utils.HEADER_SIZE];
            netin.read(buffer , 0, 10);
            String text = new String(buffer).trim();
            try {
                int payloadLength = Integer.parseInt(text);
                System.out.println("Got " + payloadLength + " bytes of data");
                // now read the payload
                byte[] payload = new byte[payloadLength];
                netin.read(payload);
                return Utils.parseRequest(payload);
            } catch (NumberFormatException nfe) {
                System.err.println("Fatal ERROR, there was no HEADER found");
            }
        }
        return null;
        
    }

    public void requestStart(final String color, final int id, final Meta meta) throws IOException {
        OutputStream netout = this.mSocket.getOutputStream();
        netout.write(Utils.prefixHeader(new Request(color, id, meta).serialize()));
        netout.flush();
    }

    public void close() {
        if (mSocket != null) {
            try {
                mSocket.close();
            } catch (IOException e) {
                System.err.println("Cannot close the socket " + e.getMessage());
            }
        }
    }

    /**
     * Send a complete image to the wall
     * @param frame
     * @throws IOException 
     */
    public void sendFrame(Frame frame) throws IOException {
        OutputStream netout = this.mSocket.getOutputStream();
        netout.write(Utils.prefixHeader(frame.serialize()));
        netout.flush();
    }

    public void sendFrame(BufferedImage image) throws IOException {
      Frame f = new Frame();  
      for(int y=0; y < image.getHeight(null); y++) {
          for(int x=0; x < image.getWidth(); x++) {
              Color c = new Color(image.getRGB(x, y));
//              System.out.print(", " 
//                      + String.format("%02X", c.getRed())
//                      + String.format("%02X", c.getGreen())
//                      + String.format("%02X", c.getBlue()));
              f.add(new Pixel(c.getRed(), c.getGreen(), c.getBlue(), x, y));
          }
//          System.out.println();
      }
      sendFrame(f);  
    }
    
}
