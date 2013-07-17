package de.c3ma.fullcircle.animation;

/**
 * created at 17.07.2013 - 17:10:44<br />
 * creator: ollo<br />
 * project: FullcricleClient<br />
 * $Id: $<br />
 * 
 * @author ollo<br />
 */
public abstract class GeneralEllipse {

    public abstract void setPixel(int x, int y, int number);

    /**
     * 
     * @param xm
     * @param ym
     * @param a
     * @param b
     * @return amount of different elements
     */
    public int countEllipseElements(int xm, int ym, int a, int b) {
        return ellipse(xm, ym, a, b, true);
    }
    
    public void drawEllipse(int xm, int ym, int a, int b) {
        ellipse(xm, ym, a, b, false);
    }

    private int ellipse(int xm, int ym, int a, int b, boolean silent) {
        int dx = 0, dy = b; /* im I. Quadranten von links oben nach rechts unten */
        long a2 = a * a, b2 = b * b;
        long err = b2 - (2 * b - 1) * a2, e2; /* Fehler im 1. Schritt */

        /* variables needed for the rainbow effect */
        int counter = 0;
        int quadrantAmount = 0;
        
        if (!silent) {
            quadrantAmount = ellipse(xm, ym, a, b, true) / 4;
        }
        
        do {
            if (!silent) {
                setPixel(xm + dx, ym + dy, (0 * quadrantAmount) + counter); /* I. Quadrant */
                setPixel(xm - dx, ym + dy, (1 * quadrantAmount) + counter); /* II. Quadrant */
                setPixel(xm - dx, ym - dy, (2 * quadrantAmount) + counter); /* III. Quadrant */
                setPixel(xm + dx, ym - dy, (3 * quadrantAmount) + counter); /* IV. Quadrant */
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
