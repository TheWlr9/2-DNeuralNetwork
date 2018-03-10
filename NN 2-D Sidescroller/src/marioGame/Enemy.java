package marioGame;

abstract class Enemy extends Toucheable {
	double getX() {
		return 0; //Dummy method
	}
	
	double getY() {
		return 0; //Dummy method
	}
	
	double getWidth() {
		return 0; //Dummy method
	}
	
	double getHeight() {
		return 0; //Dummy method
	}
	
	void move(double xAmount, double yAmount) {
		//Dummy method
	}
	
	void draw() {
		
	}
	
	boolean isOnTopOf(double x, double y) {
		return false; //Dummy method
	}
	
	void reset() {
		//Dummy method
	}
}
