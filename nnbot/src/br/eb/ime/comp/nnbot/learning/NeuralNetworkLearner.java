package br.eb.ime.comp.nnbot.learning;

import java.util.ArrayList;

public class NeuralNetworkLearner<InputClass extends Arrayable, OutputClass extends Arrayable> implements ILearner<InputClass, OutputClass>{
	protected double learningRate;
	protected double momentum;
	
	protected int sizeInput;
	protected int sizeHidden;
	protected int sizeOutput;
	
	protected double[] inputNeurons;
	protected double[] hiddenNeurons;
	protected double[] outputNeurons;
	
	protected double[][] weightsHiddenFromInputLayer;
	protected double[][] weightsOutputFromHiddenLayer;
	
	protected double[][] deltaHiddenFromInput;
	protected double[][] deltaOutputFromHidden;
	
	protected double[] hiddenErrorGradients;
	protected double[] outputErrorGradients;
	
	public NeuralNetworkLearner(){
		
	}
	
	public void train(ArrayList<TrainingSet<InputClass, OutputClass>> inputs){
		for(TrainingSet<InputClass, OutputClass> ioSet : inputs)
		{
			InputClass input = ioSet.getInput();
			input.toArray();
		}
	}
	
	public ArrayList<OutputClass> validate(ArrayList<InputClass> inputs){
		
		return new ArrayList<OutputClass>();
	}
	
}
