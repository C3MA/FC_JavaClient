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
        
    private int x_pos, y_pos, x_enemy, y_enemy, height, width, x_stat, stat, enemy_start, y_step, x_step, x_shield, y_shield, shieldtimer, last_shield;

	private int count;
    
    private static int size = 1;
    
    private static final int STEPS = 5;
    
    private boolean hit, shield, godmode;
    

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
        	this.y_pos = height/2;
        	this.y_enemy = height+1;
        	this.x_stat = 0;
        	this.stat=1;
        	this.shieldtimer = 0;
        	this.y_shield = -1;
        	this.x_shield = -1;
    	}
        
        
        hit = this.enemy_pos();
        shield = this.shield_pos();
        if(hit && !shield && !godmode)
        {
        	g.setColor(Color.orange);
        	new RainbowEllipse(width/2, height/2, Math.min(width/2,x_stat+1), Math.min(height/2,x_stat+1)) {

                @Override
                protected void drawPixel(int x, int y, SimpleColor c) {
                	Color c2 = new Color(c.getRed(), c.getGreen(), c.getBlue());
					g.setColor(c2);
                    g.fillRect(x, y, 1, 1);
                }
                
            }.drawEllipse(count+=(x_stat*255+stat)/(STEPS*100));
        }
        else if(hit && (shield || godmode))
        	{
        	y_enemy = height +1;
        	shield = false;
        	}
        else
        {
        if(x_stat==width)
        	{
        		size++;	
        		x_stat=1;
        	}
        else
        {
        switch(size%3)
        {
        case 1: g.setColor(new Color(0,255,0)); break;
        case 2: g.setColor(new Color(255,255,0)); break;
        case 0: g.setColor(new Color(255,0,128)); break;
        }
        g.fillRect(0, 0, x_stat, 1);
        switch(size%3)
        {
        case 1: g.setColor(new Color(0,stat,0)); break;
        case 2: g.setColor(new Color(stat,stat,0)); break;
        case 0: g.setColor(new Color(stat,0,128)); break;
        }
        g.fillRect(x_stat, 0 , 1, 1);
        if(godmode) g.setColor(Color.white);
        else if(shield) g.setColor(Color.yellow);
        else g.setColor(Color.blue);
        g.fillRect(x_pos, y_pos, size, size);
        g.setColor(Color.red);
        g.fillRect(x_enemy, y_enemy, 1, 1);
        g.setColor(Color.white);
        g.fillRect(x_shield,  y_shield, 1, 1);
        stat+=STEPS;
        if(stat>=255){
        	stat=0;
        	x_stat++; 
        }
        try {
			Thread.sleep(Math.max(0,(width-x_stat)*15));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        }
        }
    } 

    @Override
    public void keyPressed(KeyEvent e) {
        
        if (e.getKeyCode() == KeyEvent.VK_UP)
        {
            System.out.println("UP");
            if(y_pos>1 && hit==false) y_pos--;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN)
        {
            System.out.println("DOWN");
            if(y_pos+size<height && hit==false) y_pos++;
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT)
        {
            System.out.println("LEFT");
            if(x_pos>0 &&hit==false) x_pos--;
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            System.out.println("RIGHT");
            if(x_pos+size<width && hit==false) x_pos++;
        } 
        else if(e.getKeyCode() == KeyEvent.VK_SPACE && hit)
        {
        	y_enemy=height+1;
        	x_stat=0;
        	stat=1;
        	size=1;
        	resetShield();
        	shield = false;
        }
        else if(e.getKeyCode() == KeyEvent.VK_CONTROL)
        {
        	if(e.getKeyCode() == KeyEvent.VK_CONTROL)
        	{
        	if(godmode) godmode=false;
        	else godmode = true;
        	}
        }
        else {
            System.out.println("KEY : " + e.getID() + " [expected=" + Event.KEY_ACTION + "] "+ e.getKeyCode());
        }
    }
    
    private boolean enemy_pos()
    {
    	if(y_enemy > this.height || x_enemy > this.width || y_enemy < 1 || x_enemy < 0)
    	{
    		Random rand = new Random();
    		if(x_stat<width/2) enemy_start=0;
    		else enemy_start = rand.nextInt(4);
    		switch(enemy_start){
    		case 0: x_enemy= rand.nextInt(width); x_step=0; y_step=1; y_enemy=1; break;
    		case 1: x_enemy= rand.nextInt(width); x_step=0; y_step=-1; y_enemy=height; break;
    		case 2: y_enemy= rand.nextInt(height)+1; x_step=1; y_step=0; x_enemy=0; break;
    		case 3: y_enemy= rand.nextInt(height)+1; x_step=-1; y_step=0; x_enemy=width; break;
    		}
    		
    		
    	}
    	else
    	{
    		if(this.x_enemy >= this.x_pos && this.x_enemy < this.x_pos+size && this.y_enemy >= this.y_pos && this.y_enemy < this.y_pos+size ) return true;
    		this.y_enemy+=y_step;
    		this.x_enemy+=x_step;
    	}
    	return false;
    }
    
    private boolean shield_pos()
    {
    	if(x_stat==width/2)
    	{
    		if(x_shield == -1 && y_shield == -1 && last_shield!=size)
    		{
    		System.out.println("New Shield");
    		Random rand = new Random();
    		x_shield = rand.nextInt(width);
    		y_shield = rand.nextInt(height-1)+1;
    		last_shield = size;
    		}
    		if(shieldtimer<width+height*2)
    		{
    			if(this.x_shield >= this.x_pos && this.x_shield < this.x_pos+size && this.y_shield >= this.y_pos && this.y_shield < this.y_pos+size )
    				{
    				resetShield();
    				return true;
    				}
    			shieldtimer++;
    		}
    		else
    		{
    			resetShield();
    		}
    	}
    	if(shield) return true;
    	else return false;
    }
    
    public void resetShield()
    {
    	x_shield = -1;
    	y_shield = -1;
    	shieldtimer = 0;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }}
