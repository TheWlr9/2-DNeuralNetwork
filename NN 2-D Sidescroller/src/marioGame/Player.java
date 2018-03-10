package marioGame;

import graphics.StdDraw;
import java.util.ArrayList;
import neuralNetwork.NeuralNetwork;

final public class Player {
	private double x;
	private double y;
	private double startingX, startingY;
	private double xCor; //xCoordinate
	private double yCor; //yCoordinate
	private double xSpeed;
	private double ySpeed;
	private double xAcceleration;
	private double yAcceleration;
	private int directionHeaded; //-1==LEFT; 0==HALT; 1==RIGHT
	private boolean falling;
	private boolean jumping;
	private boolean walking;
	private boolean slowingDown;
	
	private static int numOfPlayersGenerated= 0;
	
	public double fitness;
	private NeuralNetwork itsBrain;
	
	final private static int LEFT= -1;
	final private static int HALT= 0;
	final private static int RIGHT= 1;
	static final int HEIGHT= 50;
	static final int WIDTH= 35;
	static final int MIN_VELOCITY= -6;
	static final int MAX_VELOCITY= 6;
	static final int MAX_ACCELERATION= 3;
	static final int MIN_ACCELERATION= -3;
	private final static int STOPPING_POWER= 1;
	private final static double ACCELERATION= 0.7;
	final Integer ID;
	
	Player() {
		x= MarioGame.MIDDLE_OF_SCREEN-300;
		y= MarioGame.MIDDLE_OF_SCREEN+100;
		startingX= x;
		startingY= y;
		xCor= x;
		yCor= y;
		xSpeed= 0;
		ySpeed= 0;
		xAcceleration= 0;
		yAcceleration= 0;
		directionHeaded= HALT;
		falling= false;
		jumping= false;
		walking= false;
		slowingDown= false;
		fitness= 0;
		itsBrain= new NeuralNetwork(MarioGame.numOfInputs,MarioGame.numOfOutputs,MarioGame.numOfHiddenLayers,MarioGame.numOfNeuronsPerHL);
		ID= numOfPlayersGenerated;
		numOfPlayersGenerated++;
	}
	private Player(double x, double y){
		this.x= x;
		this.y= y;
		startingX= x;
		startingY= y;
		xCor= x;
		yCor= y;
		xSpeed= 0;
		ySpeed= 0;
		xAcceleration= 0;
		yAcceleration= 0;
		directionHeaded= HALT;
		falling= false;
		jumping= false;
		walking= false;
		slowingDown= false;
		fitness= 0;
		itsBrain= new NeuralNetwork(MarioGame.numOfInputs,MarioGame.numOfOutputs,MarioGame.numOfHiddenLayers,MarioGame.numOfNeuronsPerHL);
		ID= numOfPlayersGenerated;
		numOfPlayersGenerated++;
	}
	
	void movePlayer() {
		if(directionHeaded==LEFT) {
			slowingDown= false;
			walking= true;
			
			xAcceleration= -ACCELERATION;
			xAcceleration= Math.max(xAcceleration, MIN_ACCELERATION);
			
			setSpeed(xSpeed-(Math.pow(xAcceleration,2))/2.0,ySpeed);
			setSpeed(Math.max(xSpeed, MIN_VELOCITY),ySpeed);
			
			collisionManager(MarioGame.groundBlocks);
		}
		else if(directionHeaded==RIGHT) {
			slowingDown= false;
			walking= true;
			
			xAcceleration= ACCELERATION;
			xAcceleration= Math.min(xAcceleration, MAX_ACCELERATION);
			
			setSpeed(xSpeed+(Math.pow(xAcceleration,2)/2.0),ySpeed);
			setSpeed(Math.min(xSpeed, MAX_VELOCITY),ySpeed);
			
			collisionManager(MarioGame.groundBlocks);
		}
		else {
			walking = false;
			
			if(xSpeed!=0) {
				if(!slowingDown) {
					//This is to determine which sign to give xAcceleration
					if(xSpeed>0)
						xAcceleration= -STOPPING_POWER;
					else
						xAcceleration= STOPPING_POWER;
					
					slowingDown= true;
				}
				
				if(xAcceleration<0) {
					setSpeed(xSpeed-(Math.pow(xAcceleration,2))/2.0,ySpeed);
				}
				else {
					setSpeed(xSpeed+(Math.pow(xAcceleration,2))/2.0,ySpeed);
				}
				
				if(xSpeed<1 && xSpeed> -1)
					setSpeed(0,ySpeed); //Stop him
			}
		}
		xCor+= xSpeed;
		yCor+= ySpeed;
		fitness= xCor;
		fitness= Math.max(fitness, 0);
		
		movePlayer(xSpeed,ySpeed); //Update him
	}
	
	private void movePlayer(double xDisplacement, double yDisplacement) {
		for(Toucheable t : MarioGame.objects)
			t.move(xDisplacement,yDisplacement);
	}
	
	private void movePlayer(double xDisplacement, double yDisplacement, Ground ground) {
		ground.move(xDisplacement, yDisplacement);
	} 
	
	void draw() {
		StdDraw.setPenColor(); //Setup
		
		if(!walking && !jumping) {
			StdDraw.picture(x, y, "16-bit Mario.png",HEIGHT,HEIGHT); //To draw him correctly
			StdDraw.rectangle(x, y, WIDTH/2,HEIGHT/2); //Hit-box
		}
		else if(!jumping){
			StdDraw.picture(x, y, "Super Mario World small Mario walking.gif",HEIGHT,HEIGHT);
			StdDraw.rectangle(x, y, WIDTH/2, HEIGHT/2);
		}
		else {
			StdDraw.picture(x, y, "Super Mario World small Mario jumping.png",HEIGHT,HEIGHT);
			StdDraw.rectangle(x, y, WIDTH/2, HEIGHT/2);
		}
	}
	
	private boolean isTouching(Toucheable object) {
		return x-WIDTH/2.0<object.getX()+object.getWidth()/2.0 
				&& x+WIDTH/2.0>object.getX()-object.getWidth()/2.0 
				&& y-HEIGHT/2.0<object.getY()+object.getWidth()/2.0 
				&& y+HEIGHT/2.0>object.getY()-object.getWidth()/2.0;
	}
	
	void collisionManager(ArrayList<Ground> gList) {
		ArrayList<Ground> underPlayer= new ArrayList<Ground>();
		boolean doAgain= true; //Boolean to oversee the jumping code
		
		for(Ground g : gList) 
			if(Math.abs((int)(x-g.getX()))<Ground.WIDTH) {
				underPlayer.add(new Ground(g.getX(),g.getY()));
			}

		if(!underPlayer.isEmpty()) {
			for(Ground g : underPlayer) {
				if(!isTouching(g)) {
					if(doAgain) { /*This is just to prevent the fall
						method to be called twice, therefore forcing the Player
						 to fall twice as fast.*/
						fall();
						doAgain= false;
					}
				}
				else {
					if(new Player(x,y+HEIGHT/2.0).isTouching(g)) { //Is this block blocking the Player's way?
						
						/*Test to see which side the wall block is on*/
						if(new Player(x+WIDTH/2.0,y+HEIGHT/2.0).isTouching(g) && directionHeaded==RIGHT) {
							directionHeaded= HALT;
							
							//This is to stop Mario from clipping inside the wall
							while(isTouching(g)) {
								movePlayer(-1,ySpeed,g);
								movePlayer(-1,ySpeed);
								xCor+= -1;
								yCor+= ySpeed;
							}
						}
						else if(new Player(x-WIDTH/2.0,y+HEIGHT/2.0).isTouching(g) && directionHeaded==LEFT) {
							directionHeaded= HALT;
							
							//This is to stop Mario from clipping inside the wall
							while(isTouching(g)) {
								movePlayer(1,ySpeed);
								movePlayer(1,ySpeed,g);
								xCor+= 1;
							}
						}
					}
					else if(jumping) {
						setSpeed(xSpeed, MAX_VELOCITY*1.5);
					}
					else {
						yAcceleration= 0;
						
						//This is to stop Mario from clipping inside the ground
						while(isTouching(g) && falling) {
							movePlayer(0,-1*ySpeed,g);
							movePlayer(0,-1*ySpeed);
							yCor+= -1*ySpeed;
						}
						
						setSpeed(xSpeed, 0);
						
						falling= false;
					}
				}
			}
		}
		else
			fall();
	}
	
	void collisionManager(Goomba g) {
		if(Math.abs(x-g.getX())<g.getWidth()) {
			if(isTouching(g)) {
				if(falling && (int)(y-HEIGHT/2.0)>=(int)(g.getY()+g.getHeight()/4.0)) {
					MarioGame.objects.remove(g); //Remove from the big list
					MarioGame.goombas.remove(g); //Remove from the sub-list
					if(jumping)
						setSpeed(xSpeed,MAX_VELOCITY*1.5);
					else
						setSpeed(xSpeed,MAX_VELOCITY);
				}
				else
					MarioGame.gameOver= true;
			}
		}
	}
	
	private void collisionManager(Toucheable t) {
		if(t instanceof Goomba)
			collisionManager((Goomba) t);
		else
			collisionManager((Ground) t);
	}
	
	private void fall() {
		falling= true;
		yAcceleration= -0.75;
		
		setSpeed(xSpeed,ySpeed-Math.max(MIN_VELOCITY,(Math.pow(yAcceleration,2))/2.0));
	}
	
	
	/**
	 * If using a NN, this method handles the outputs based on the inputs.
	 * @param goombas Goomba array input
	 * @param groundBlocks Ground array input
	 */
	public void update(ArrayList<Goomba> goombas, ArrayList<Ground> groundBlocks) {
		double[] inputs= new double[MarioGame.numOfInputs];
		int currentIndex= 0;
		boolean found= false;
		
		for(int i= 0; i<3; i++) {
			for(int j= 0; j<3; j++) {
				StdDraw.circle(x+j*WIDTH-WIDTH, i*HEIGHT+y-HEIGHT, 15);
				for(Toucheable t : MarioGame.objects)
					if(t.isOnTopOf(j*WIDTH+x-WIDTH,i*HEIGHT+y-HEIGHT)) {
						if(t instanceof Goomba) {
							inputs[currentIndex]= -15;
							currentIndex++;
							found= true;
							
							break;
						}
						else if(t instanceof Ground) {
							inputs[currentIndex]= 15;
							currentIndex++;
							found= true;
							
							break;
						}
					}
				if(!found) {
					inputs[currentIndex]= 0;
					currentIndex++;
				}
				found= false;
			}
		}
		
		double[] outputs= itsBrain.update(inputs);
		//Error check for size issues.
		if(outputs.length< MarioGame.numOfOutputs)
			System.err.println("Messed up with output length in Player class (line: 272 I think?)");
		
		if(outputs[0]>0.5)
			jump();
		if(outputs[1]>0.622)
			goRight();
		else if(outputs[1]<0.378)
			goLeft();
		else
			halt();
	}
	
	/**
	 * Resets the Player instance.
	 */
	public void reset() {
		fitness= 0;
		
		x= startingX;
		y= startingY;
	}
	
	/**
	 * Sets the weights
	 * @param weights the new weights
	 */
	public void setWeights(double[] weights) {
		itsBrain.setWeights(weights);
	}
	
	/**
	 * Makes the player jump
	 */
	public void jump() {
		jumping= true;
	}
	
	/**
	 * Makes the player move right
	 */
	public void goRight() {
		directionHeaded= RIGHT;
	}
	
	/**
	 * Makes the player move left
	 */
	public void goLeft() {
		directionHeaded= LEFT;
	}
	
	private void setSpeed(double xSpeed, double ySpeed) {
		this.xSpeed= xSpeed;
		this.ySpeed= ySpeed;
	}
	void setJumping(boolean setting) {
		jumping= setting;
	}
	void halt() {
		directionHeaded= HALT;
	}
	
	/**
	 * Resets the Player instance
	 */
	void resetPos() {
		for(Toucheable t : MarioGame.objects)
			t.reset();
		
		x= startingX;
		y= startingY;
		yCor= startingY;
		xCor= startingX;
	}
	
	public double getXSpeed() {
		return xSpeed;
	}
	public double getYSpeed() {
		return ySpeed;
	}
	public double getY() {
		return yCor;
	}
	public double getX() {
		return xCor;
	}
}
