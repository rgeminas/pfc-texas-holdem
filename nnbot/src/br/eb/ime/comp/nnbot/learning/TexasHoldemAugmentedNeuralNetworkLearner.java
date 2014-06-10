package br.eb.ime.comp.nnbot.learning;

import java.io.FileWriter;

import br.eb.ime.comp.nnbot.model.Action;
import br.eb.ime.comp.nnbot.model.ProfileAugmentedSituation;
import br.eb.ime.comp.nnbot.model.Situation;

public class TexasHoldemAugmentedNeuralNetworkLearner extends
NeuralNetworkLearner<ProfileAugmentedSituation, Action> {

	public TexasHoldemAugmentedNeuralNetworkLearner(double learningRate,
			double momentum, int _sizeInput, int _sizeHidden, int _sizeOutput,
	FileWriter _fw) {
		super(learningRate, momentum, _sizeInput, _sizeHidden, _sizeOutput, _fw);
	}
}
