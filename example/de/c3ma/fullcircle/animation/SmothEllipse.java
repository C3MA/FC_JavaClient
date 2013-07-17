package de.c3ma.fullcircle.animation;

/**
 * created at 17.07.2013 - 17:10:44<br />
 * creator: ollo<br />
 * project: FullcricleClient<br />
 * $Id: $<br />
 * 
 * @author ollo<br />
 */
public abstract class SmothEllipse {

    public abstract void setPixel(int x, int y, int number);

    public void ellipse(int xm, int ym, int a, int b) {
        int counter = 0;
        int dx = 0, dy = b; /* im I. Quadranten von links oben nach rechts unten */
        long a2 = a * a, b2 = b * b;
        long err = b2 - (2 * b - 1) * a2, e2; /* Fehler im 1. Schritt */

        do {
            setPixel(xm + dx, ym + dy, (0*5) + counter); /* I. Quadrant */
            setPixel(xm - dx, ym + dy, (1*5) + counter); /* II. Quadrant */
            setPixel(xm - dx, ym - dy, (2*5) + counter); /* III. Quadrant */
            setPixel(xm + dx, ym - dy, (3*5) + counter); /* IV. Quadrant */

            e2 = 2 * err;
            if (e2 < (2 * dx + 1) * b2) {
                dx++;
                err += (2 * dx + 1) * b2;
            }
            if (e2 > -(2 * dy - 1) * a2) {
                dy--;
                err -= (2 * dy - 1) * a2;
            }
            counter++;
        } while (dy >= 0);

        while (dx++ < a) { /* fehlerhafter Abbruch bei flachen Ellipsen (b=1) */
            setPixel(xm + dx, ym, counter++); /*
                                                    * -> Spitze der Ellipse
                                                    * vollenden
                                                    */
            setPixel(xm - dx, ym, counter++);
        }
    }
}
