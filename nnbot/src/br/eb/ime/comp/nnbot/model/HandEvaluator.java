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
		
		// check for straight
		int lastCard = -1;
		Suit lastSuit;
		int cardsTilStraight = 4;
		boolean isStraighting = true;
		ArrayList<Card> flushableCards = new ArrayList<Card>();
		for (Card a : mine)
		{
			if (lastCard == -1) 
			{
				lastCard = a.getValue().ordinal();
				flushableCards.add(a);
				continue;
			}
			else if (a.getValue().ordinal() == lastCard)
			{
				flushableCards.add(a);
				continue;
			}
			if (Math.abs(a.getValue().ordinal() - lastCard) == 1)
			{
				lastCard = a.getValue().ordinal();
				flushableCards.add(a);
				cardsTilStraight--;
				if (cardsTilStraight == 0) break;
			}
			else
			{
				flushableCards.clear();
				lastCard = a.getValue().ordinal();
				flushableCards.add(a);
				cardsTilStraight = 4;
			}
		}
		// Then we have a straight. Check for straight flush.
		if (cardsTilStraight == 0)
		{
			
		}
		
		return true;
	}
}
