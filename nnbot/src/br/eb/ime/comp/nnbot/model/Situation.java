package br.eb.ime.comp.nnbot.model;

import br.eb.ime.comp.nnbot.learning.Arrayable;

public class Situation implements Arrayable {

	public double bestHandProbability;
	public double cashInHand;
	public double numPlayers;
	public double[] cashByPlayer; // 11 players
	public double cashInPot;
	public boolean isPreFlop;
	public boolean isFlop;
	public boolean isTurn;
	public boolean isRiver;
	
	@Override
	public double[] toArray() {
		double[] result = new double[16];
		result[0] = bestHandProbability;
		if (cashInHand > 0)
			result[1] = 1 / cashInHand;
		else
			result[1] = 0;
		result[2] = numPlayers / 11;
		for (int i = 0; i < 11; i++)
		{
			if (cashByPlayer[i] > 0)
				result[3 + i] = 1 / cashByPlayer[i];
			else
				result[3 + i] = 0;
		}
		// cashInPot is never 0, there's always the blinds
		result[14] = 1 / cashInPot;
		result[15] = isPreFlop ? 0 : 1;
		result[16] = isFlop ? 0 : 1;
		result[17] = isTurn ? 0 : 1;
		result[18] = isRiver ? 0 : 1;
		return result;
	}

	@Override
	public int getArraySize() {
		return 19;
	}

}
