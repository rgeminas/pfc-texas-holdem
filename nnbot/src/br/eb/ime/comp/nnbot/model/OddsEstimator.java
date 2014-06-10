package br.eb.ime.comp.nnbot.model;

import java.util.ArrayList;

import evaluation.RandomCardGenerator;

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

	public static final int triesPerOpponent = 500;
	
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
	
	static public double estimateFlopOddsByMonteCarlo(ArrayList<Card> mine, ArrayList<Card> table, ArrayList<ArrayList<Card>> opponents)
	{
		double winCount = 0;
		
		ArrayList<Card> baseAlreadyOut = new ArrayList<Card>();
		baseAlreadyOut.addAll(mine); baseAlreadyOut.addAll(table);
		for (ArrayList<Card> opp : opponents)
		{
			baseAlreadyOut.addAll(opp);
		}
		
		for (int i = 0; i < 500; i++)
		{
			ArrayList<Card> newOut = new ArrayList<Card>();
			newOut.addAll(baseAlreadyOut);


			ArrayList<Card> newTable = new ArrayList<Card>();
			newTable.addAll(table);
			while (newTable.size() < 5)
			{
				Card c = RandomCardGenerator.randomCard(newOut);
				newTable.add(c);
				newOut.add(c);
			}
			
			ArrayList<Card> newMine = new ArrayList<Card>();
			newMine.addAll(mine);
			
			while (newMine.size() < 2)
			{
				Card c = RandomCardGenerator.randomCard(newOut);
				newMine.add(c);
				newOut.add(c);
			}
			newMine.addAll(newTable);

			
			ArrayList<ArrayList<Card>> newOpps = new ArrayList<ArrayList<Card>>(); 
			for (ArrayList<Card> opp : opponents)
			{
				ArrayList<Card> newOpp = new ArrayList<Card>();
				newOpp.addAll(opp);
				
				while (newOpp.size() < 2)
				{
					Card c = RandomCardGenerator.randomCard(newOut);
					newOpp.add(c);
					newOut.add(c);
				}
				newOpp.addAll(newTable);
				newOpps.add(newOpp);
			}
			newMine.addAll(newTable);
			if (HandEvaluator.compare(newMine, newOpps, newTable))
			{
				winCount += 1;
			}
		}
		return winCount / 500;
	}
}
