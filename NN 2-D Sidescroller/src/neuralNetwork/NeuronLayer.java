package neuralNetwork;

import java.util.ArrayList;

class NeuronLayer {
	private int numOfNeurons;
	private ArrayList<Neuron> neuronsInLayer;
	
	NeuronLayer(int numOfNeurons, int numOfInputsPerNeuron){
		this.numOfNeurons= numOfNeurons;
		neuronsInLayer= new ArrayList<Neuron>(this.numOfNeurons);
		
		for(int i= 0; i<numOfNeurons; i++)
			neuronsInLayer.add(new Neuron(numOfInputsPerNeuron));
	}
	
	int getNumOfNeurons() {
		return numOfNeurons;
	}
	
	ArrayList<Neuron> getNeuronsInLayer(){
		return neuronsInLayer;
	}
}
