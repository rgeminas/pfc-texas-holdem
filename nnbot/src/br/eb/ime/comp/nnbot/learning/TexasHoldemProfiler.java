package br.eb.ime.comp.nnbot.learning;

import java.io.FileWriter;

import br.eb.ime.comp.nnbot.model.ClassifierOutput;
import br.eb.ime.comp.nnbot.model.Situation;
import br.eb.ime.comp.nnbot.model.SlidingWindowInput;

public class TexasHoldemProfiler extends
		NeuralNetworkLearner<SlidingWindowInput<Situation>, ClassifierOutput> {

	public TexasHoldemProfiler(double learningRate, double momentum,
			int _sizeInput, int _sizeHidden, int _sizeOutput, FileWriter _fw) {
		super(learningRate, momentum, _sizeInput, _sizeHidden, _sizeOutput, _fw);
		// TODO Auto-generated constructor stub
	}

}
