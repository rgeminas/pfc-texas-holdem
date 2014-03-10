package br.eb.ime.comp.nnbot.model;

import java.util.ArrayList;

public class OddsEstimator {
	
	public static final double[][] preFlopOdds = {
	    {85, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
	    {65, 82, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
	    {64, 61, 80, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
	    {64, 61, 58, 78, 00, 00, 00, 00, 00, 00, 00, 00, 00},
	    {63, 60, 57, 55, 75, 00, 00, 00, 00, 00, 00, 00, 00},
	    {61, 58, 56, 53, 52, 72, 00, 00, 00, 00, 00, 00, 00},
	    {60, 56, 54, 52, 50, 48, 69, 00, 00, 00, 00, 00, 00},
	    {59, 55, 52, 50, 48, 47, 46, 66, 00, 00, 00, 00, 00},
	    {58, 54, 51, 48, 46, 45, 44, 43, 63, 00, 00, 00, 00},
	    {58, 53, 50, 47, 44, 43, 42, 41, 40, 60, 00, 00, 00},
	    {56, 52, 49, 46, 43, 41, 40, 38, 38, 38, 57, 00, 00},
	    {56, 51, 48, 45, 42, 40, 38, 37, 36, 36, 34, 54, 00},
	    {55, 50, 47, 44, 42, 39, 37, 35, 34, 34, 33, 31, 50}
	};

	public static final int triesPerOpponent = 1000000;
	
	double estimateOddsOnPreFlop(Card first, Card second, int opponentCount)
	{
		if (first.getValue().ordinal() > second.getValue().ordinal())
		{
			Card tmp = first;
			first = second;
			second = tmp;
		}
		double baseOdds = preFlopOdds[first.getValue().ordinal()][second.getValue().ordinal()];
		// This is a crude approximation
		if (first.getSuit() == second.getSuit())
		{
			baseOdds += 4.;
		}
		return baseOdds / 100;
	}
	
	double estimateFlopOddsByMonteCarlo(Card m1, Card m2, Card t1, Card t2, Card t3)
	{
		ArrayList<Card> cards = new ArrayList<Card>();
		cards.add(m1); cards.add(m2); cards.add(t1); cards.add(t2); cards.add(t3);
		
		return 0;
	}
	double estimateFlopOddsByMonteCarlo(Card m1, Card m2, Card t1, Card t2, Card t3, Card t4)
	{
		
		return 0;
	}
	double estimateFlopOddsByMonteCarlo(Card m1, Card m2, Card t1, Card t2, Card t3, Card t4, Card t5)
	{
		
		return 0;
	}
}
