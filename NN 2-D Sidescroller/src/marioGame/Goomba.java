package marioGame;

import graphics.StdDraw;
import java.util.ArrayList;

final class Goomba extends Enemy{
	private double x;
	private double y; //private
	private double startingX, startingY;
	private double xSpeed;
	private double ySpeed;
	private double yAcceleration;
	private boolean falling;
	
	private static final int WIDTH= 38;
	private static final int HEIGHT= WIDTH;
	private static final int MIN_VELOCITY= -5;
	
	Goomba(double spawnX, double spawnY) {
		x= spawnX;
		y= spawnY;
		startingX= spawnX;
		startingY= spawnY;
		xSpeed= -1;
		ySpeed= 0;
		yAcceleration= 0;
		falling= false;
	}
	
	void draw() {
		super.draw();
		
		StdDraw.picture(x, y, "Super Mario World goomba.gif",WIDTH,WIDTH); //To draw it correctly
		StdDraw.square(x, y, WIDTH/2); //Hit-box
	}
	
	void move(double xDisplacement, double yDisplacement) {
		x-= xDisplacement;
		y-= yDisplacement;
	}
	
	private void priMove(double xDisplacement, double yDisplacement) {
		x+= xDisplacement;
		y+= yDisplacement;
	}
	
	void move() {
		priMove(xSpeed,ySpeed);
	}
	
	void collisionManager(ArrayList<Ground> gList) {
		ArrayList<Ground> underGoomba= new ArrayList<Ground>();
		for(Ground g : gList)
			if(Math.abs((int)(x-g.getX()))<Ground.WIDTH) {
				underGoomba.add(new Ground(g.getX(),g.getY()));
			}
		
		if(!underGoomba.isEmpty()) {
			for(Ground g : underGoomba) {
				if(!isTouching(g)) {
					falling= true;
					yAcceleration= -0.75;
					
					ySpeed-= Math.max(MIN_VELOCITY,(Math.pow(yAcceleration,2))/2.0); //Set speed
				}
				else {
					if(new Goomba(x,y+WIDTH/2).isTouching(g)) //Maybe change to y+(-1*ySpeed)
						xSpeed*= -1;
					else {
						yAcceleration= 0;
						
						while(isTouching(g) && falling)
							move(0,-1*ySpeed);
						
						ySpeed= 0; //Set speed
						
						falling= false;
					}
				}
			}
		}
		else {
			falling= true;
			yAcceleration= -0.75;
			
			ySpeed-= Math.max(MIN_VELOCITY,(Math.pow(yAcceleration,2))/2.0); //Set speed
		}
	}
	
	private boolean isTouching(Toucheable object) {
		return x-WIDTH/2.0<object.getX()+object.getWidth()/2.0 
				&& x+WIDTH/2.0>object.getX()-object.getWidth()/2.0 
				&& y-HEIGHT/2.0<object.getY()+object.getWidth()/2.0 
				&& y+HEIGHT/2.0>object.getY()-object.getWidth()/2.0;
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
		ySpeed= 0;
		yAcceleration= 0;
		falling= false;
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
}
