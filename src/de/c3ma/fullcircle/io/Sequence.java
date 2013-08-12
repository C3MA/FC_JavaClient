package de.c3ma.fullcircle.io;

import java.util.ArrayList;

import de.c3ma.proto.fctypes.Frame;
import de.c3ma.proto.fctypes.FullcircleTypes;
import de.c3ma.proto.fctypes.Meta;
import de.c3ma.proto.fctypes.Utils;

/**
 * created at 12.08.2013 - 18:25:24<br />
 * creator: ollo<br />
 * project: FullcricleClient<br />
 * $Id: $<br />
 * @author ollo<br />
 */
public class Sequence implements FullcircleTypes {

    private static final int BUFFER_SPACE = 2048;

    private static final String VERSION = "0.1.1.0";
    
    private int fps;
    private int width;
    private int height;
    
    private ArrayList<Frame> frames = new ArrayList<Frame>();

    public Sequence(final int fps, final int width, final int height) {
        this.fps    = fps;
        this.width  = width;
        this.height = height;
    }
    
    public void addFrame(final Frame f) {
        if (f.getWidth() == this.width && f.getHeight() == this.height)
            this.frames.add(f);
        else
            throw new IllegalArgumentException("All frame must have " + this.width + "x" + this.height + " as resolution.");
    }
    
    public byte[] serialize()
    {
        int offset = 0;
        
        /** extract the frames and append them to a sequence **/
        /* generate the meta information */
        Meta m = new Meta(this.fps, this.width, this.height, "javaser", VERSION);
        byte[] metaBuffer = m.serialize();
        
        /* generate the information about all frames */
        byte[] sequenceBuffer = serializeAllFrames();
        
        byte[] tempBuffer = new byte[metaBuffer.length + sequenceBuffer.length + BUFFER_SPACE];
        offset = Utils.addLengthd(tempBuffer, offset, BINARYSEQUENCE_METADATA, metaBuffer, metaBuffer.length);
        System.arraycopy(sequenceBuffer, 0, tempBuffer, offset, sequenceBuffer.length);
        offset += sequenceBuffer.length;
        
        /* Shrink the returning buffer to the needed size */
        byte[] retBuffer = new byte[offset];
        System.arraycopy(tempBuffer, 0, retBuffer, 0, offset);
        return retBuffer;
    }

    private byte[] serializeAllFrames() {
        if (frames.size() <= 0)
            return new byte[0];
        Frame f = frames.get(0);
        byte[] frame = f.serializeBinaryFrame();
        byte[] tmpBuffer = new byte[frame.length * frames.size()];

        int offset = 0;
        
        System.arraycopy(frame, 0, tmpBuffer, offset, frame.length);
        offset += frame.length;
        
        for (int i = 1; i < frames.size(); i++) {
            f = frames.get(i);
            byte[] tmpFrame = f.serializeBinaryFrame();
            System.arraycopy(tmpFrame, 0, tmpBuffer, offset, tmpFrame.length);
            offset += tmpFrame.length;
        }
        
        return tmpBuffer;
    }
}
