package de.c3ma.fullcircle.client;

import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Random;

import javax.swing.JFrame;

import de.c3ma.animation.RainbowEllipse;
import de.c3ma.fullcircle.dyn.Dynamic;
import de.c3ma.fullcircle.dyn.OnFullcirclePaint;
import de.c3ma.types.SimpleColor;

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
        
    private int x_pos, y_pos, x_enemy, y_enemy, height, width, x_stat, stat;

	private int count;
    
    private static final int SIZE = 2;
    

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
    public void paint(final Graphics g, int width, int height) {
    	if(this.height==0 && this.width==0){
    		this.height = height;
        	this.width = width;
        	this.x_pos = width/2;
        	this.y_pos = height-SIZE;
        	this.y_enemy = height+1;
        	this.x_stat = 1;
        	this.stat=1;
    	}
        
        
        boolean hit = this.enemy_pos();
        if(hit)
        {
        	g.setColor(Color.orange);
        	new RainbowEllipse(width/2, height/2, width/2, height/2) {

                @Override
                protected void drawPixel(int x, int y, SimpleColor c) {
                	Color c2 = new Color(c.getRed(), c.getGreen(), c.getBlue());
					g.setColor(c2);
                    g.fillRect(x, y, 1, 1);
                }
                
            }.drawEllipse(count++);
        }
        else
        {
        	
        g.setColor(new Color(0, stat,0));
        g.fillRect(0, 0 , x_stat, 1);
        g.setColor(Color.blue);
        g.fillRect(x_pos, y_pos, SIZE, SIZE);
        g.setColor(Color.red);
        g.fillRect(x_enemy, y_enemy, 1, 1);
        stat+=5;
        if(stat>=255){
        	stat=0;
        	x_stat++;
        }
        }
    } 

    @Override
    public void keyPressed(KeyEvent e) {
        
        if (e.getKeyCode() == KeyEvent.VK_UP)
        {
            System.out.println("UP");
            y_pos--;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN)
        {
            System.out.println("DOWN");
            y_pos++;
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT)
        {
            System.out.println("LEFT");
            x_pos--;
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            System.out.println("RIGHT");
            x_pos++;
        } 
        else if(e.getKeyCode() == KeyEvent.VK_SPACE)
        {
        	y_enemy=height+1;
        	x_stat=1;
        	stat=1;
        }
        else {
            System.out.println("KEY : " + e.getID() + " [expected=" + Event.KEY_ACTION + "] "+ e.getKeyCode());
        }
    }
    
    private boolean enemy_pos()
    {
    	if(y_enemy > this.height)
    	{
    		y_enemy=0;
    		Random rand = new Random();
    		x_enemy= rand.nextInt(width);
    	}
    	else
    	{
    		if(this.x_enemy >= this.x_pos && this.x_enemy < this.x_pos+SIZE && this.y_enemy == this.y_pos) return true;
    		this.y_enemy++;
    	}
    	return false;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }}
