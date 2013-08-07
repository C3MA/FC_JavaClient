package de.c3ma.fullcircle.client;

import java.util.ArrayList;
import java.util.Iterator;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.JoystickButtonEvent;

public class JoystickButtonEventSource {

	private ArrayList<JoystickButtonEventListener> _listeners = new ArrayList<JoystickButtonEventListener>();

	public synchronized void addEventListener(
			JoystickButtonEventListener listener) {
		_listeners.add(listener);
	}

	public synchronized void removeEventListener(
			JoystickButtonEventListener listener) {
		_listeners.remove(listener);
	}

	Runnable myThread = new Runnable() {
		
		@Override
		public void run() {
			// Create the window
			RenderWindow window = new RenderWindow();
			window.create(new VideoMode(640, 480), "Hello JSFML!");

			// Limit the framerate
			window.setFramerateLimit(30);

			// Main loop
			while (window.isOpen()) {
				// Fill the window with red
				window.clear(Color.RED);

				// Display what was drawn (... the red color!)
				window.display();

				// Handle events
				for (Event event : window.pollEvents()) {
					if (event.type == Event.Type.CLOSED) {
						// The user pressed the close button
						window.close();
					}

					if (event.type == Event.Type.JOYSTICK_BUTTON_PRESSED) {
						JoystickButtonEvent jbe = (JoystickButtonEvent) event;
						System.out.println(jbe.button + "\t" + jbe.type);
						CustomJsButtonEvent customEvent = new CustomJsButtonEvent(jbe, jbe.button);
						fireEvent(customEvent);
					}
				}
			}
		}
	};
	
	
	public JoystickButtonEventSource() {
		new Thread(myThread).start();
	}

	// call this method whenever you want to notify
	// the event listeners of the particular event
	private synchronized void fireEvent(CustomJsButtonEvent event) {
		Iterator<JoystickButtonEventListener> i = _listeners.iterator();
		while (i.hasNext()) {
			((JoystickButtonEventListener) i.next())
					.handleJoystickButtonEvent(event);
		}
	}

}
