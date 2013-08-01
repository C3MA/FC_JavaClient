package de.c3ma.animation;

import de.c3ma.types.SimpleColor;

/**
 * created at 17.07.2013 - 18:30:27<br />
 * creator: ollo<br />
 * project: FullcricleClient<br />
 * $Id: $<br />
 * @author ollo<br />
 */
public abstract class RainbowEllipse extends GeneralEllipse {
    
    public RainbowEllipse(int xm, int ym, int a, int b) {
        super(xm, ym, a, b);
    }

    /**
     * Map a value to a rainbow color.
     * (Source: http://blog.csharphelper.com/2010/05/20/map-numeric-values-to-colors-in-a-rainbow-in-c.aspx)
     * @param value current value (between 0 and 1023)
     * @param red_value default flavor of red
     * @param blue_value    default flavor of blue
     * @return used color
     */
    private SimpleColor mapRainbowColor(float value, float red_value, float blue_value)
    {
        // Convert into a value between 0 and 1023.
        int int_value = (int)(1023 * (value - red_value) / (blue_value - red_value));

        // Map different color bands.
        if (int_value < 256)
        {
            // Red to yellow. (255, 0, 0) to (255, 255, 0).
            return new SimpleColor(255, int_value, 0);
        }
        else if (int_value < 512)
        {
            // Yellow to green. (255, 255, 0) to (0, 255, 0).
            int_value -= 256;
            return new SimpleColor(255 - int_value, 255, 0);
        }
        else if (int_value < 768)
        {
            // Green to aqua. (0, 255, 0) to (0, 255, 255).
            int_value -= 512;
            return new SimpleColor(0, 255, int_value);
        }
        else
        {
            // Aqua to blue. (0, 255, 255) to (0, 0, 255).
            int_value -= 768;
            return new SimpleColor(0, 255 - int_value, 255);
        }
    
    }
    
    protected abstract void drawPixel(int x, int y, SimpleColor c);
    
    @Override
    protected void setPixel(int x, int y, int number) {
        int maxPixel = this.mQuadrantAmount * 4;
        drawPixel(x, y, mapRainbowColor((this.mOffset + number) % maxPixel, 0, maxPixel));
    }

}
