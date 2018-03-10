package neuralNetwork;

import java.util.ArrayList;

/**This class is the base bone for a NN.
 * @version 0.9
 * @author Will Ritchie
 *
 */
public class NeuralNetwork {
	private int numOfInputs;
	private int numOfOutputs;
	private int numOfHiddenLayers;
	private int numOfNeuronsPerHL;
	ArrayList<NeuronLayer> layers;
	
	/**
	 * Sets up the NN
	 * @param numOfInputs number of inputs
	 * @param numOfOutputs number of outputs
	 * @param numOfHiddenLayers number of hidden layers
	 * @param numOfNeuronsPerHL number of neurons per each hidden layer
	 */
	public NeuralNetwork(int numOfInputs, int numOfOutputs, int numOfHiddenLayers, int numOfNeuronsPerHL) {
		this.numOfInputs= numOfInputs;
		this.numOfOutputs= numOfOutputs;
		this.numOfHiddenLayers= numOfHiddenLayers;
		this.numOfNeuronsPerHL= numOfNeuronsPerHL;
		layers= new ArrayList<NeuronLayer>();
		
		//Check to see if hiddenLayers>0?
		
		layers.add(new NeuronLayer(numOfNeuronsPerHL,numOfInputs)); //Setup the first hidden layer to the inputs
		for(int i= 0; i<numOfHiddenLayers-1; i++)
			layers.add(new NeuronLayer(numOfNeuronsPerHL,numOfNeuronsPerHL)); //Setup the layer
		layers.add(new NeuronLayer(numOfOutputs,numOfNeuronsPerHL)); //Setup the last layer as the output neurons
	}
	
	/**
	 * This method is called to update the outputs with the inputs.
	 * @param inputs The inputs to be used to calculate the outputs.
	 * @return The outputs
	 */
	public double[] update(double[] inputs) {
		double[] outputs= new double[0];
		int weightIndex= 0;
		//if (inputs.length != numOfInputs){ return outputs;}
		
		//This goes through every layer
		for(int i= 0; i<numOfHiddenLayers+1; i++) {
			if(i>0) {
				inputs= new double[outputs.length];
				inputs= outputs;
			}
			
			outputs= new double[layers.get(i).getNumOfNeurons()];
			int outputIndex= 0;
			weightIndex= 0;
			//For each neuron in the layer...
			for(int j= 0; j<layers.get(i).getNumOfNeurons(); j++) {
				double netInput= 0;
				
				int currentNumOfInputs= layers.get(i).getNeuronsInLayer().get(j).getNumOfInputs();
				
				//For each weight...
				for(int k= 0; k<currentNumOfInputs-1; k++) { //-1 because the last weight is the bias				
					netInput+= layers.get(i).getNeuronsInLayer().get(j).getWeights()[k]*inputs[weightIndex]; //k instead of weightIndex???
					weightIndex++;
				}
				
				netInput+= layers.get(i).getNeuronsInLayer().get(j).getWeights()[currentNumOfInputs-1]*(-1); //Add the bias
				outputs[outputIndex]= sigmoid(netInput);
				outputIndex++;
				
				weightIndex= 0; //Reset the weight index
			}
		}
		return outputs;
	}
	
	/**
	 * Sets the weights
	 * @param weights The new weights
	 */
	public void setWeights(double[] weights) {
		int weightIndex= 0;
		
		for(int i= 0; i<numOfHiddenLayers+1; i++) //For each layer...
			for(int j= 0; j<layers.get(i).getNumOfNeurons(); j++) //For each neuron...
				for(int k= 0; k<layers.get(i).getNeuronsInLayer().get(j).getNumOfInputs(); k++) { //For each weight...
					layers.get(i).getNeuronsInLayer().get(j).getWeights()[k]= weights[weightIndex];
					weightIndex++;
				}
	}
	
	/**
	 * This little function is just a sigmoid function
	 * for the use of the NN class
	 * @param x
	 * @return sigmoid of x
	 */
	private double sigmoid(double x) {
		return 1/(1+Math.pow(Math.E,-x));
	}
}
