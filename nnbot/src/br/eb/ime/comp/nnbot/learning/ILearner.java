package br.eb.ime.comp.nnbot.learning;

import java.util.ArrayList;

public interface ILearner<InputClass, OutputClass> {
	public void train(ArrayList<TrainingSet<InputClass, OutputClass>> inputs);
	public ArrayList<OutputClass> validate(ArrayList<InputClass> inputs);
}
