package br.eb.ime.comp.nnbot.model;

import br.eb.ime.comp.nnbot.learning.Arrayable;

public class Action implements Arrayable {

	public double foldOdds;
	public double checkOdds;
	public double raiseOdds;
	public double raiseAmt;
	
	@Override
	public double[] toArray() {
		double[] result = new double[4];
		result[0] = foldOdds;
		result[1] = checkOdds;
		result[2] = raiseOdds;
		result[3] = raiseAmt > 0 ? 1 / raiseAmt : 0;
		return result;
	}

	@Override
	public int getArraySize() {
		return 4;
	}

}
