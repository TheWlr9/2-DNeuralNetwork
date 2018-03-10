package geneticAlgorithm;

public class Genome {
	double fitness;
	double[] weights;
	
	/**
	 * Constructs a Genome object
	 * @param fitness Sets the fitness
	 * @param weights Initializes the weights
	 */
	Genome(double fitness, double[] weights){
		this.fitness= fitness;
		this.weights= weights;
	}
	/**
	 * Dummy constructor
	 */
	Genome(){
		fitness= 0;
	}
	/**
	 * Constructor with the setting for the array size.
	 * @param arraySize Size of the array.
	 */
	Genome(int arraySize){
		fitness= 0;
		weights= new double[arraySize];
		//for(int i= 0; i<weights.length; i++)
			//weights[i]= Math.random()*2-1;
	}
	
	/**
	 * Sets the fitness score
	 * @param fitness The score to be set.
	 */
	public void setFitness(double fitness) {
		this.fitness= fitness;
	}
	public double[] getWeights() {
		return weights;
	}
	public double getFitness() {
		return fitness;
	}
}
