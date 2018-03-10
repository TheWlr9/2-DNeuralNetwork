package neuralNetwork;

class Neuron {
	private int numOfInputs;
	private double[] weights;
	
	Neuron(int numOfInputs){
		this.numOfInputs= numOfInputs;
		weights= new double[this.numOfInputs+1];
		
		for(int i= 0; i<this.numOfInputs+1; i++) { //Why numOfInputs+1? Because the last addition is for the bias which controls the threshold value of the neuron.
			weights[i]= Math.random()*2-1; //A value between -1 and 1
		}
	}
	
	int getNumOfInputs() {
		return numOfInputs;
	}
	
	double[] getWeights() {
		return weights;
	}
}
