package marioGame;

/*
 * In order to use user-defined inputs, you must copy and 
 * paste this code block into your main method:
 * 
 * 
 * while(true){
 *	//YOUR CODE HERE
 *	
 *	
 *	MarioGame.processAndRun();
 *}
 *
 *
 *
 */

import graphics.StdDraw;
import java.util.ArrayList;
import java.awt.Font;
import java.io.*;

/**Purpose: To test and to learn about ANNs and how they work/
 * how to operate them.
 * @version 0.6
 * @author William Ritchie
 */

final public class MarioGame {
	public static final int MIDDLE_OF_SCREEN= 400;
	public static final int TICKS_PER_SECOND= 60;
	
	private static int timeLimit;
	
	static int popSize;
	static int numOfInputs;
	static int numOfOutputs;
	static int numOfHiddenLayers;
	static int numOfNeuronsPerHL;
	
	final private static Player PLAYER= new Player();
	public static ArrayList<Player> players= new ArrayList<Player>(); 
	static ArrayList<Toucheable> objects= new ArrayList<Toucheable>();
	static ArrayList<Ground> groundBlocks= new ArrayList<Ground>();
	static ArrayList<Goomba> goombas= new ArrayList<Goomba>();
	
	static Goomba gee= new Goomba(800,MIDDLE_OF_SCREEN);
	
	final static Font TIMER_FONT= new Font("Minecrafter",Font.PLAIN,16);
		
	public static boolean gameOver= false;
	private static int startingNumOfGoombas;
	
	final static int SKIP_TICKS = 1000 / TICKS_PER_SECOND;
	final static int MAX_FRAMESKIP = 3;
	
    static double nextGameTick= System.currentTimeMillis(); //Set it up first
    private static int loops;
    
    private static Integer time= 0;
	public static double ticksElapsed;

	/**
	 * Setup a pre-defined level with user-defined inputs
	 */
	public static void newMarioGame() {
		StdDraw.setCanvasSize(MIDDLE_OF_SCREEN*2,MIDDLE_OF_SCREEN*2);
		StdDraw.setScale(0,MIDDLE_OF_SCREEN*2);
		
		setTimeLimit(4);
		time= timeLimit;
		ticksElapsed= 0;
		
		for(int i= 0; i<(MIDDLE_OF_SCREEN*2)/Ground.WIDTH+3; i++) {
			if(i<10 || i>17)
				groundBlocks.add(new Ground(i*Ground.WIDTH,MIDDLE_OF_SCREEN-Ground.HEIGHT));
			else if(i>=10 && i<=15)
				groundBlocks.add(new Ground(i*Ground.WIDTH,MIDDLE_OF_SCREEN
						+Ground.HEIGHT-Ground.HEIGHT));
		}
		//groundBlocks.add(new Ground(MIDDLE_OF_SCREEN,MIDDLE_OF_SCREEN));
		goombas.add(gee);
		
		objects.addAll(groundBlocks);
		objects.addAll(goombas);
		
		startingNumOfGoombas= goombas.size();
	}
	
	/**
	 * Sets up the Mario game for a NN
	 * @param popSize is the amount of Marios
	 * @param numOfInputs is the number of inputs
	 * @param numOfOutputs is the number of outputs
	 * @param numOfHiddenLayers is the number of hidden layers
	 * @param numOfNeuronsPerHL is teh number of neurons per each hidden layer.
	 */
	public static void newMarioGame(int popSize, int numOfInputs, int numOfOutputs, int numOfHiddenLayers, int numOfNeuronsPerHL) {
		MarioGame.popSize= popSize;
		MarioGame.numOfInputs= numOfInputs;
		MarioGame.numOfOutputs= numOfOutputs;
		MarioGame.numOfHiddenLayers= numOfHiddenLayers;
		MarioGame.numOfNeuronsPerHL= numOfNeuronsPerHL;
		for(int i= 0; i<popSize; i++)
			players.add(new Player());
		
		newMarioGame();
	}
	
	public static void newMarioGame(File levelFile) {
		
		
		StdDraw.setCanvasSize(MIDDLE_OF_SCREEN*2,MIDDLE_OF_SCREEN*2);
		StdDraw.setScale(0,MIDDLE_OF_SCREEN*2);
		
		
	}
	
	final private static void displayDeathScreen() {
		StdDraw.clear(StdDraw.BLACK);
		
		StdDraw.setFont();
		StdDraw.setPenColor(StdDraw.RED);
		StdDraw.text(MIDDLE_OF_SCREEN, MIDDLE_OF_SCREEN, "GAME OVER");
		
		StdDraw.show();
	}
	
	/**
	 * Adds a goomba enemy
	 * @param x is the x coordinate spawn point on the screen from 0-800
	 * @param y is the y coordinate spawn point on the screen from 0-800
	 */
	final public static void addGoomba(double x, double y) {
		Goomba newOne= new Goomba(x,y);
		
		goombas.add(newOne);
		objects.add(newOne);
	}
	/**
	 * Adds a solid ground block
	 * @param x is the x coordinate spawn point on the screen from 0-800
	 * @param y is the y coordinate spawn point on the screen from 0-800
	 */
	public static void addBlock(double x, double y) {
		Ground newOne= new Ground(x,y);
		
		groundBlocks.add(newOne);
		objects.add(newOne);
	}
	
	/**
	 * Runs the game.
	 * @param PLAYER The Mario you want to run
	 */
	final public static void processAndRun(Player PLAYER) {
		if(time<0 || PLAYER.getY()<0)
        	gameOver= true;
        else
        	gameOver= false;
		
		if(gameOver) {
			//PLAYER.fitness= PLAYER.getX();
			displayDeathScreen();
		}
    	else {
	    	StdDraw.clear(); //Clear the screen for animation
	    	StdDraw.setFont(TIMER_FONT);
	        loops = 0;
	        
	        while (System.currentTimeMillis() > nextGameTick
	                && loops < MAX_FRAMESKIP) { //The engine is faster than time!
	        	/*Update shit*/
	        	
	        	/*Manage ground hitboxes*/
        		for(Goomba go : goombas)
        			go.collisionManager(groundBlocks);
        		
        		PLAYER.collisionManager(groundBlocks);
	        	
	        	try {
	        		for(Goomba g : goombas) 
	        			PLAYER.collisionManager(g);
	        	}
    			catch(RuntimeException cme){
    				break;
	        	}
	        	
	        	/*Determine the next course of action 
	        	 * for everything that can act.*/
	        	for(Goomba go : goombas)
	        		go.move();
	        	
	        	if(popSize!=0) //Buffer for non-NN players
	        		PLAYER.update(goombas, groundBlocks);
	        	
	            PLAYER.movePlayer();

	            
	            nextGameTick += SKIP_TICKS; //Up the ante for System.currentTimeMillis() to beat.
	            ticksElapsed++;
	            loops++; //Update loops.
	            
	            if(ticksElapsed%60==0)
		        	time--;
	        }
	        
	        /*Draw shit*/
	        StdDraw.text(MIDDLE_OF_SCREEN*2, MIDDLE_OF_SCREEN*2, time.toString());
	        StdDraw.text(0, 750, "ID: "+PLAYER.ID.toString());
	        StdDraw.text(0,700,"Y: "+(int)PLAYER.getY());
	        StdDraw.text(0, 650, "X: "+(int)PLAYER.getX());
	        //Maybe add generation count somehow?
	        
	        PLAYER.draw();
	        for(Toucheable i : objects)
	        	i.draw();
	        
	        StdDraw.show(0); //Display the objects on-screen for animation
	        
	        /*Reset some things.*/
	        PLAYER.setJumping(false);
	        PLAYER.halt();
	    }
    }
	
	/**
	 * Runs the game without any inputs
	 */
	final public static void run() {
		final int SKIP_TICKS = 1000 / TICKS_PER_SECOND;
		final int MAX_FRAMESKIP = 3;
		
	    double nextGameTick = System.currentTimeMillis(); //Set it up first
	    int loops;

	    while (true) { //Begin.
	    	if(gameOver) {
				displayDeathScreen();
			}
	    	else {
		    	/*Register input.*/
	    		
		    	//PLAYER.jump();
		    	
		    	StdDraw.clear(); //Clear the screen for animation
		    	
		        loops = 0;
		        
		        while (System.currentTimeMillis() > nextGameTick
		                && loops < MAX_FRAMESKIP) { //The engine is faster than time!
		        	/*Update shit*/
		        	
		        	/*Manage ground hitboxes*/
	        		for(Goomba go : goombas)
	        			go.collisionManager(groundBlocks);
	        		PLAYER.collisionManager(groundBlocks);
		        	
		        	try {
		        		for(Goomba g : goombas)
		        			PLAYER.collisionManager(g);
		        	}
	    			catch(RuntimeException cme){
	    				break;
		        	}
		        	
		        	/*Determine the next course of action 
		        	 * for everything that can act.*/
		        	for(Goomba go : goombas)
		        		go.move();
		            PLAYER.movePlayer();
	
		            
		            nextGameTick += SKIP_TICKS; //Up the ante for System.currentTimeMillis() to beat.
		            ticksElapsed++;
		            loops++; //Update loops.
		        }
		        /*Draw shit*/
		        
		        PLAYER.draw();
		        for(Toucheable i : objects)
		        	i.draw();
		        
		        StdDraw.show(0); //Display the objects on-screen for animation
		        
		        /*Reset some things.*/
		        PLAYER.setJumping(false);
		        PLAYER.halt();
		    }
	    }
	}
	
	/**
	 * This method resets the stage for the next Mario/next time.
	 */
	final public static void reset() {
		gameOver= false;
		time= timeLimit;
		ticksElapsed= 0;
		if(popSize!=0) //Buffer for NN
			for(Player p : players)
				p.resetPos();
		
		boolean update= false;
		while(goombas.size()<startingNumOfGoombas) { //Goomba respawner
			goombas.add(new Goomba(800,MIDDLE_OF_SCREEN));
			update= true;
		}
		if(update)
			objects.addAll(goombas);
	}
	
	/**
	 * Use this method whenever you want the player to perform an action.
	 * @return The player object
	 */
	final public static Player player() {
		return PLAYER;
	}
	
	final public static void setTimeLimit(int limit) {
		timeLimit= limit;
	}
}
