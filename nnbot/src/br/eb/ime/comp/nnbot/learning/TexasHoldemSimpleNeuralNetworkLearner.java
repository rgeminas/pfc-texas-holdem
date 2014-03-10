package br.eb.ime.comp.nnbot.learning;

import java.io.FileWriter;
import java.util.ArrayList;

import br.eb.ime.comp.nnbot.model.Action;
import br.eb.ime.comp.nnbot.model.Situation;

public class TexasHoldemSimpleNeuralNetworkLearner extends
		NeuralNetworkLearner<Situation, Action> {

	public TexasHoldemSimpleNeuralNetworkLearner(double learningRate,
			double momentum, int _sizeInput, int _sizeHidden, int _sizeOutput,
			FileWriter _fw) {
		super(learningRate, momentum, _sizeInput, _sizeHidden, _sizeOutput, _fw);
	}
}
