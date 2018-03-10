package main;

import marioGame.MarioGame;
import marioGame.Player;
import geneticAlgorithm.*;
import java.util.ArrayList;

public class Controller {
	final static int NUM_OF_INPUTS= 9; //3X3 grid. -15 for Goomba, 15 for ground. Why -15 and 15? Because why not.
	final static int NUM_OF_OUTPUTS= 2; //Jump and left or right
	final static int NUM_OF_HIDDEN_LAYERS= 1;
	final static int NUM_OF_NEURONS_PER_HL= 9; //To be changed whenever you want to experiement
	final static int POP_SIZE= 30;
	final static double MUTATION_RATE= 0.1;
	final static double CROSSOVER_RATE= 0.3;
	final static int NUM_OF_WEIGHTS= (int)(NUM_OF_INPUTS*NUM_OF_NEURONS_PER_HL+Math.pow(NUM_OF_NEURONS_PER_HL,2)*(NUM_OF_HIDDEN_LAYERS-1)+NUM_OF_NEURONS_PER_HL*NUM_OF_OUTPUTS);
	static ArrayList<Genome> thePopulation= new ArrayList<Genome>();
	
	public static int generations= 0;
	
	public static void main(String[] args) {
		//Setup stuff
		MarioGame.newMarioGame(POP_SIZE,NUM_OF_INPUTS,NUM_OF_OUTPUTS,NUM_OF_HIDDEN_LAYERS,NUM_OF_NEURONS_PER_HL);
		GeneticAlgorithm ga= new GeneticAlgorithm(POP_SIZE,MUTATION_RATE,CROSSOVER_RATE,NUM_OF_WEIGHTS);
		thePopulation= ga.getChromos();
		
		int index= 0;
		
		if(POP_SIZE<2)
			System.err.println("popSize too small. Must be larger than 2.");
		else {
			//Start
			while(true) {
				//loop the Marios running each individually at different times
				for(Player p : MarioGame.players) {
					while(!MarioGame.gameOver)
						MarioGame.processAndRun(p); //Only shows one Mario at a time.
					thePopulation.get(index).setFitness(p.fitness); //Save the fitness score to the genome array
					MarioGame.reset();
					
					index++;
				}
				System.out.println("Gen: "+generations);
				double total= 0;
				for(Genome g : thePopulation) {
					System.out.print((int)g.getFitness()+", ");
					total+= g.getFitness();
				}
				System.out.println("Average fitness: "+total/POP_SIZE);
				System.out.println("Best fitness score so far: "+ga.getBestScore());
				
				//Do mutations and stuff
				generations++;
				thePopulation= ga.epoch(thePopulation);
				
				//Input new information
				int counter= 0;
				for(Player p : MarioGame.players) {
					p.setWeights(thePopulation.get(counter).getWeights());
					counter++;
				}
				//Reset some things maybe?
				MarioGame.reset();
				index= 0;
				for(Player p : MarioGame.players)
					p.reset();
				for(Genome g : thePopulation)
					g.setFitness(0);
			}
		}
	}
}
