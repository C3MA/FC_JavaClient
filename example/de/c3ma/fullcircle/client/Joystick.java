package de.c3ma.fullcircle.client;

import java.io.IOException;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.JoystickButtonEvent;

public class Joystick {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		//Create the window
		RenderWindow window = new RenderWindow();
		window.create(new VideoMode(640, 480), "Hello JSFML!");

		//Limit the framerate
		window.setFramerateLimit(30);

		//Main loop
		while(window.isOpen()) {
		    //Fill the window with red
		    window.clear(Color.RED);

		    //Display what was drawn (... the red color!)
		    window.display();

		    //Handle events
		    for(Event event : window.pollEvents()) {
		        if(event.type == Event.Type.CLOSED) {
		            //The user pressed the close button
		            window.close();
		        }
		        
		        if (event.type == Event.Type.JOYSTICK_BUTTON_PRESSED)
		        {
		        	JoystickButtonEvent jbe = (JoystickButtonEvent) event;
		        	System.out.println(jbe.button + "\t" + jbe.type); 
		        }
		    }
		}
	}

}
