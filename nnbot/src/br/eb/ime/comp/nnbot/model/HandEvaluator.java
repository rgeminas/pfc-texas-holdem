package br.eb.ime.comp.nnbot.model;

import java.util.ArrayList;
import java.util.Collections;

public class HandEvaluator {
	public static Boolean compare(ArrayList<Card> mine, ArrayList<ArrayList<Card>> opponents, ArrayList<Card> table)
	{
		for (ArrayList<Card> opp : opponents)
		{
			if (!compareTwo(mine, opp, table))
			{
				return false;
			}
		}
		return true;
	}
	
	public static boolean compareTwo(ArrayList<Card> m, ArrayList<Card> o, ArrayList<Card> t)
	{
		ArrayList<Card> mine = new ArrayList<Card>();
		ArrayList<Card> theirs = new ArrayList<Card>();
		mine.add(m.get(0)); mine.add(m.get(1)); mine.add(t.get(0));mine.add(t.get(1));mine.add(t.get(2));mine.add(t.get(3));mine.add(t.get(4));
		theirs.add(o.get(0)); theirs.add(o.get(1)); theirs.add(t.get(0));theirs.add(t.get(1));theirs.add(t.get(2));theirs.add(t.get(3));theirs.add(t.get(4));
		
		Collections.sort(mine);
		Collections.sort(theirs);
		
		int lastCard = -1;
		int cardsTilStraight = 5;
		boolean isStraighting = true;
		ArrayList<Integer> countPerSuit = new ArrayList<Integer>();
		ArrayList<Card> maxCardPerSuit = new ArrayList<Card>();
		Card highCard;
		for (int i = 0; i < 4; i++)
		{
			countPerSuit.add(0);
			maxCardPerSuit.add(new Card(Value.TWO, Suit.SPADES));
		}
		// For checking pairs, threes and quads;
		ArrayList<Integer> countPerValue = new ArrayList<Integer>();
		{
			countPerValue.add(0);
		}
		for (Card a : mine)
		{
			countPerSuit.set(a.getSuit().ordinal(), countPerSuit.get(a.getSuit().ordinal()) + 1);
			if (maxCardPerSuit.get(a.getSuit().ordinal()).compareTo(a) < 0)
			{
				maxCardPerSuit.set(a.getSuit().ordinal(), a);	
			}
			
			countPerValue.set(a.getValue().ordinal(), countPerSuit.get(a.getValue().ordinal()) + 1);
			lastCard = a.getValue().ordinal();
			if (lastCard == -1) 
			{
				continue;
			}
			else if (a.getValue().ordinal() == lastCard)
			{
				continue;
			}
			if (Math.abs(a.getValue().ordinal() - lastCard) == 1)
			{
				cardsTilStraight--;
				highCard = a;
			}
			else
			{
				lastCard = a.getValue().ordinal();
				cardsTilStraight = 4;
			}
		}
		
		
		
		return true;
	}
}
