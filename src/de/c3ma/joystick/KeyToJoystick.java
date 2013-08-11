package de.c3ma.joystick;




public class KeyToJoystick {


	/**
	 * Transformiert Tastatureingaben in Joystickeingaben
	 * @param str Name des Keys in GROSSBUCHSTABEN Bsp: "G" oder "LEFT"
	 * @return gibt den int Wert des Joysticks zurueck
	 */
	public static int transform(String str)
	{
		if(str == "UP") return CustomJsButtonEvent.UP;
		else if(str == "DOWN") return CustomJsButtonEvent.DOWN;
		else if(str == "LEFT") return CustomJsButtonEvent.LEFT;
		else if(str == "RIGHT") return CustomJsButtonEvent.RIGHT;
		else if(str == "SPACE") return CustomJsButtonEvent.START;
		else if(str == "G") return CustomJsButtonEvent.SELECT;
		else return 0;
	}

}
