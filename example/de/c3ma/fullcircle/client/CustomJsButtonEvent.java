package de.c3ma.fullcircle.client;

public class CustomJsButtonEvent extends java.util.EventObject {

        /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
		public static final int UP = 2;
		public static final int DOWN = 1;
		public static final int LEFT = 0;
		public static final int RIGHT = 3;

		public static final int SELECT = 8;

		public static final int START = 9;

		public static final int LEFT_DOWN = 4;
		public static final int LEFT_UP = 6;
		public static final int RIGHT_DOWN = 5;
		public static final int RIGHT_UP = 7;
		
		private int button;

		//here's the constructor
        public CustomJsButtonEvent(Object source, int button) {
            super(source);
        	this.button = button;
        
        }
        
        public int getButton() {
        	return button;
        }
        
        @Override
        public String toString() {
        	return "Joystickevent = " + button;
        }
}
