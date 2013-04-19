package de.c3ma.fullcircle.client;

import java.awt.Event;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.JFrame;

import de.c3ma.fullcircle.Dynamic;
import de.c3ma.fullcircle.OnFullcirclePaint;

/**
 * created at 22.10.2012 - 22:06:12<br />
 * creator: ollo<br />
 * project: FullcricleClient<br />
 * $Id: $<br />
 * @author ollo<br />
 */
public class ExampleInputStation extends JFrame implements OnFullcirclePaint, KeyListener {

    
    /**
     * Unique Id that is needed for the JFrame
     */
    private static final long serialVersionUID = 191520503239122366L;
    
    private int frame_index = 0;

    public ExampleInputStation(String[] args) throws IOException {
        // some default stuff, that is needed to show a window and handle Button reaction.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addKeyListener(this);
        setSize(200, 200);
        setVisible(true);
        // now create the connection to the wall
        if (args.length >= 1) {
            Dynamic d = new Dynamic(args[0]);
            d.setOnPaint(this);
        }
    }
    
    /**
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        new ExampleInputStation(args);
    }

    @Override
    public void paint(Graphics g) {
        if (frame_index > 6)
            frame_index = 0;
        // move a small rectangle from the upper left corner to the right lower one
        g.fillRect(frame_index, frame_index, 2, 2);
        frame_index++;
    } 

    @Override
    public void keyPressed(KeyEvent e) {
        
        if (e.getKeyCode() == KeyEvent.VK_UP)
        {
            System.out.println("UP");
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN)
        {
            System.out.println("DOWN");
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT)
        {
            System.out.println("LEFT");
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            System.out.println("RIGHT");
        } else {
            System.out.println(e.getID() + " [expected=" + Event.KEY_ACTION + "] "+ e.getKeyCode());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }}
