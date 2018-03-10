package marioGame;

import graphics.StdDraw;

class Ground extends Toucheable {
	private double x;
	private double y;
	private int id;
	private double startingX, startingY;
	static int numOfGrounds= 0;
	
	final static int WIDTH= 35;
	final static int HEIGHT= WIDTH;
	
	Ground(double x, double y) {
		this.x= x;
		this.y= y;
		startingX= x;
		startingY= y;
		numOfGrounds++;
		id= numOfGrounds;
	}
	
	void draw() {
		super.draw();
		
		StdDraw.picture(x, y, "Mario brick block.png",WIDTH,WIDTH);
		StdDraw.square(x, y, WIDTH/2.0);
	}
	
	void move(double xAmount, double yAmount) {
		x-= xAmount;
		y-= yAmount;
	}
	
	boolean isOnTopOf(double x, double y) {
		return this.x-WIDTH/2.0<x 
				&& this.x+WIDTH/2.0>x
				&& this.y-HEIGHT/2.0<y
				&& this.y+HEIGHT/2.0>y;
	}
	
	void reset() {
		x= startingX;
		y= startingY;
	}
	
	double getX() {
		return x;
	}
	double getY() {
		return y;
	}
	double getWidth() {
		return WIDTH;
	}
	double getHeight() {
		return HEIGHT;
	}
	int getID() {
		return id;
	}
	public String toString() {
		return x+" "+y;
	}
}
