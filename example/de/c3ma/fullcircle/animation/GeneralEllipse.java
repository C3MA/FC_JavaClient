package de.c3ma.fullcircle.animation;

/**
 * created at 17.07.2013 - 17:10:44<br />
 * creator: ollo<br />
 * project: FullcricleClient<br />
 * $Id: $<br />
 * 
 * Source behind:
 * http://de.wikipedia.org/wiki/Bresenham-Algorithmus
 * 
 * @author ollo<br />
 */
public abstract class GeneralEllipse {

    /**
     * Amount of elements in one quadrant (is automatically set before drawing the first pixel)
     */
    protected int mQuadrantAmount = 0;
    
    /**
     * Variables describing an ellipse
     */
    protected int xm;
    protected int ym;
    protected int a;
    protected int b; 
    
    protected abstract void setPixel(int x, int y, int number);

    public GeneralEllipse(int xm, int ym, int a, int b) {
        this.xm = xm;
        this.ym = ym;
        this.a = a;
        this.b = b;
    }
    
    /**
     * 
     * @param xm
     * @param ym
     * @param a
     * @param b
     * @return amount of different elements
     */
    public int countElements() {
        return ellipse(true);
    }
    
    public void drawEllipse() {
        mQuadrantAmount = ellipse(true) / 4;
        ellipse(false);
    }

    private int ellipse(boolean silent) {
        int dx = 0, dy = b; /* im I. Quadranten von links oben nach rechts unten */
        long a2 = a * a, b2 = b * b;
        long err = b2 - (2 * b - 1) * a2, e2; /* Fehler im 1. Schritt */

        int counter = 0;
                
        do {
            if (!silent) {
                setPixel(xm + dx, ym + dy, (0 * mQuadrantAmount) + counter); /* I. Quadrant */
                setPixel(xm - dx, ym + dy, (1 * mQuadrantAmount) + counter); /* II. Quadrant */
                setPixel(xm - dx, ym - dy, (2 * mQuadrantAmount) + counter); /* III. Quadrant */
                setPixel(xm + dx, ym - dy, (3 * mQuadrantAmount) + counter); /* IV. Quadrant */
                counter++;
            } else {
                counter+=4;
            }
            
            e2 = 2 * err;
            if (e2 < (2 * dx + 1) * b2) {
                dx++;
                err += (2 * dx + 1) * b2;
            }
            if (e2 > -(2 * dy - 1) * a2) {
                dy--;
                err -= (2 * dy - 1) * a2;
            }
            
        } while (dy >= 0);

        while (dx++ < a) { /* fehlerhafter Abbruch bei flachen Ellipsen (b=1) */
            if (!silent) {
                setPixel(xm + dx, ym, counter++); /*
                                                    * -> Spitze der Ellipse
                                                    * vollenden
                                                    */
                setPixel(xm - dx, ym, counter++);
            } else {
                counter += 2;
            }
        }
        return counter ;
    }
    
    
    
}
