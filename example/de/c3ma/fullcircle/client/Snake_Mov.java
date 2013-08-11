package de.c3ma.fullcircle.client;

public class Snake_Mov {

	int x;
	int y;
	
	public final static Snake_Mov LEFT = new Snake_Mov(-1,0);
	public final static Snake_Mov RIGHT = new Snake_Mov(1,0);
	public final static Snake_Mov UP = new Snake_Mov(0,-1);
	public final static Snake_Mov DOWN = new Snake_Mov(0,1);
	
	public Snake_Mov(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
}
