package de.c3ma.fullcircle.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.MouseInputListener;

import de.c3ma.fullcircle.dyn.Dynamic;
import de.c3ma.fullcircle.dyn.OnFullcirclePaint;

/**
 * created at 31.07.2013 - 18:06:12<br />
 * creator: ollo<br />
 * project: FullcricleClient<br />
 * $Id: $<br />
 * @author ollo<br />
 */
public class MouseInputStation extends JFrame implements KeyListener, MouseMotionListener, MouseInputListener {

    private static final int RANDOM_COLOR_MIN = 100;
    private static final int RANDOM_COLOR_MAX = 250 - RANDOM_COLOR_MIN;
    private static final int BAD_COLOR_FLOW = 1;
    private Point mMouseCursor;
    private boolean mClicked = false;
    private OnFullcirclePaint mWallupdate = new OnFullcirclePaint() {

        private Color mActualColor;

        @Override
        public void paint(Graphics g, int width, int height) {
            if (mMouseCursor != null) {
                /* Some more creative stuff, by not cleaning the last image */
                d.setOverdraw(true);
                
                if (mClicked || mActualColor == null)
                {
                    mClicked = false;
                    int red = (int )(Math.random() * RANDOM_COLOR_MAX + RANDOM_COLOR_MIN);
                    int green = (int )(Math.random() * RANDOM_COLOR_MAX + RANDOM_COLOR_MIN);
                    int blue = (int )(Math.random() * RANDOM_COLOR_MAX + RANDOM_COLOR_MIN);
                    mActualColor = new Color(red, green, blue);
                    System.out.println("Generated a new color : " + mActualColor);
                }
                
                /* We have a bad pencil -> make the color worse */
                if (frame_index % 10 == 0)
                {
                    mActualColor = new Color(Math.max(0, mActualColor.getRed() - BAD_COLOR_FLOW), 
                            Math.max(0, mActualColor.getGreen() - BAD_COLOR_FLOW), 
                            Math.max(0, mActualColor.getBlue() - BAD_COLOR_FLOW));
                }
                
                g.setColor(mActualColor);
                g.fillRect((int) (mMouseCursor.getX() % width), (int) (mMouseCursor.getY() % height), 1, 1);
            } else {
                /* some demo to show the wall */
                if (frame_index > width)
                    frame_index = 0;
                g.setColor(Color.orange);
                // move a small rectangle from the upper left corner to the right lower one and from the other upper corner
                g.fillRect(frame_index, frame_index, 1, 1);
                g.fillRect(width - frame_index, frame_index, 1, 1);
            }
            frame_index++;    
        } 
    };
    
    /**
     * 
     */
    private static final long serialVersionUID = -7086209257561642901L;
    private int frame_index = 0;

    private Dynamic d;
    
    public MouseInputStation(String[] args) {
        // some default stuff, that is needed to show a window and handle Button reaction.
        this.addKeyListener(this);
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
        this.setSize(480, 320);
        this.setTitle("FC - Mouse Capture");
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(new JLabel("Interagieren Sie mit dieser Maus mit der Wand."), BorderLayout.NORTH);
        
        
        final JTextField txtConnect = new JTextField("Enter IP manually");
        
        // now create the connection to the wall
        if (args.length >= 1) {
            try {
                d = new Dynamic(args[0]);
                d.setOnPaint(mWallupdate);
            } catch (IOException e) {
                getContentPane().add(txtConnect, BorderLayout.SOUTH);
                txtConnect.setText("Error : " + e.getMessage());
            }
        } else {
            System.err.println("Usage: <IP of Wall>");
            JLabel lbl = new JLabel("<html>Start from the <b>Terminal</b> and<br/> append the <b>IP address</b> from the Wall as argument.<br/><hr>Or use the Textfield below:</html>");
            getContentPane().add(lbl, BorderLayout.CENTER);
            getContentPane().add(txtConnect, BorderLayout.SOUTH);
            txtConnect.addKeyListener(new KeyListener()   { 
                public void keyTyped(KeyEvent e) {}
                public void keyReleased(KeyEvent e) {}
                public void keyPressed(KeyEvent e) {
                     int key = e.getKeyCode();
                     if (key == KeyEvent.VK_ENTER) {         
                         try {
                             d = new Dynamic(txtConnect.getText());
                             d.setOnPaint(mWallupdate);
                         } catch (IOException ioe) {
                             txtConnect.setText("Error : " + ioe.getMessage());
                         }
                     }}
            });
        }
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    /**
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) {
        new MouseInputStation(args);
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
            System.out.println("KEY : " + e.getID() + " [expected=" + Event.KEY_ACTION + "] "+ e.getKeyCode());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        this.mMouseCursor = e.getLocationOnScreen();
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
        this.mClicked  = true;
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub
        
    }
}
