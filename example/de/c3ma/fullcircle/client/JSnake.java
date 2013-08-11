package de.c3ma.fullcircle.client;

import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import de.c3ma.fullcircle.dyn.Dynamic;
import de.c3ma.joystick.JoystickButtonEventSource;
import de.c3ma.joystick.CustomJsButtonEvent;
import de.c3ma.joystick.JoystickButtonEventListener;
import de.c3ma.animation.RainbowEllipse;
import de.c3ma.fullcircle.dyn.OnFullcirclePaint;
import de.c3ma.types.SimpleColor;


/**
 * created 09.08.2013
 * creator sly
 * Project: Fullcircle Client
 * @author sly
 */
public class JSnake implements OnFullcirclePaint, JoystickButtonEventListener {
	
	private int height, width, x_snack, y_snack;
	private boolean snack_hit, snake_hit;
	private Snake_Mov snake_move;
	private ArrayList<Snake_Pos> snake = new ArrayList<Snake_Pos>();

	public JSnake(String[] args) throws IOException
	{
		JoystickButtonEventSource jbe = new JoystickButtonEventSource();
		jbe.addEventListener(this);
		// now create the connection to the wall
		if (args.length >= 1) {
			System.out.println("Connecting to " + args[0] + "...");
			Dynamic d = new Dynamic(args[0]);
			d.setOnPaint(this);
		}
	}
	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		new JSnake(args);
	}
	@Override
	public void handleJoystickButtonEvent(CustomJsButtonEvent e) {
		

		System.out.println(e.getButton());
		

		if (e.getButton() == CustomJsButtonEvent.UP) {
			System.out.println("UP");
			if (snake.get(0).getY() > 0 && snake_hit == false && snake_move != Snake_Mov.DOWN)
				snake_move = Snake_Mov.UP;
				try {
					Thread.sleep(200);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		} 
		else if (e.getButton() == CustomJsButtonEvent.DOWN) {
			System.out.println("DOWN");
			if (snake.get(0).getY() < height && snake_hit == false && snake_move != Snake_Mov.UP)
				snake_move = Snake_Mov.DOWN;
			try {
				Thread.sleep(200);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		} else if (e.getButton() == CustomJsButtonEvent.LEFT) {
			System.out.println("LEFT");
			if (snake.get(0).getX() > 0 && snake_hit == false && snake_move != Snake_Mov.RIGHT)
				snake_move = Snake_Mov.LEFT;	
			try {
				Thread.sleep(200);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if (e.getButton() == CustomJsButtonEvent.RIGHT) {
			System.out.println("RIGHT");
			if (snake.get(0).getX() < width && snake_hit == false && snake_move != Snake_Mov.LEFT)
				snake_move = Snake_Mov.RIGHT;
			try {
				Thread.sleep(200);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if (e.getButton() == CustomJsButtonEvent.START && snake_hit) {
			snake_reset();
		} 

	}
	@Override
	public void paint(final Graphics g, int width, int height) {
		// TODO Auto-generated method stub
		if (this.height == 0 && this.width == 0) {
			this.height = height;
			this.width = width;
			snake_reset();
		}
		
		if (snake_hit) {
			g.setColor(Color.orange);
			new RainbowEllipse(width / 2, height / 2, Math.min(width / 2,
					snake.size() + 1), Math.min(height / 2, snake.size() + 1)) {

				@Override
				protected void drawPixel(int x, int y, SimpleColor c) {
					Color c2 = new Color(c.getRed(), c.getGreen(), c.getBlue());
					g.setColor(c2);
					g.fillRect(x, y, 1, 1);
				}

			}.drawEllipse(Math.max(1,(snake.size()*5/100)));
		}
		else{
			snake_hit = snake_collide();
			snack_hit = snack_pos();
			snake_steps(snack_hit);
			g.setColor(Color.white);
			g.fillRect(x_snack, y_snack, 1, 1);
			g.setColor(Color.blue);
			for(Snake_Pos a: snake)
			{
				g.fillRect(a.getX(), a.getY(), 1, 1);
			}	
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private boolean snack_pos() {
		if(snack_hit){	
			boolean redo;
			System.out.println("New Snack");
			Random rand = new Random();
			do{
				x_snack = rand.nextInt(width);
				y_snack = rand.nextInt(height);
				redo = false;
				for(Snake_Pos a: snake)
				{
					if(a.getX() == x_snack && a.getY() == y_snack) redo = true;
				}
			}while(redo);
			
		}
		if (this.x_snack == this.snake.get(0).getX() && this.y_snack == this.snake.get(0).getY()) {
			x_snack = -1;
			y_snack = -1;
			return true;
		}
		return false;
	}
	
	private boolean snake_collide(){
		if(!snake_hit){
			int hilfX = -1;
			int hilfY = -1;
			for(Snake_Pos a : snake)
			{
				if(hilfX == a.getX() && hilfY == a.getY()) return true;
				else if(hilfX == -1)
				{
					hilfX = a.getX();
					hilfY = a.getY();
				}
			}
			if(snake.get(0).getX() >= width || snake.get(0).getX() < 0 || snake.get(0).getY() >= height || snake.get(0).getY() < 0)
			{
				return true;
			}
		}
		return false;
	}
	
	private void snake_steps(boolean grow)
	{
		snake.add(0,new Snake_Pos(snake.get(0).getX()+snake_move.x,snake.get(0).getY()+snake_move.y));
		Iterator<Snake_Pos> itr = snake.iterator();
		while(itr.hasNext()) itr.next();
		if(!grow) itr.remove();
	}
	
	private void snake_reset()
	{
		snack_hit = true;
		snake_move = Snake_Mov.RIGHT;
		snake_hit = false;
		snake.clear();
		snake.add(new Snake_Pos(width/2,height/2));
		snake.add(new Snake_Pos(snake.get(0).getX()-1,snake.get(0).getY()));
	}

}
