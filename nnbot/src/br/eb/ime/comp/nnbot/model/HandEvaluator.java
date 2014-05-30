package br.eb.ime.comp.nnbot.model;

import java.util.ArrayList;
import java.util.Collections;

public class HandEvaluator {
	public static Boolean compare(ArrayList<Card> mine, ArrayList<ArrayList<Card>> opponents, ArrayList<Card> table)
	{
		ArrayList<Card> allm = new ArrayList<Card>();
		allm.addAll(mine); allm.addAll(table);
		for (ArrayList<Card> opp : opponents)
		{
			ArrayList<Card> allo = new ArrayList<Card>();
			allo.addAll(opp); allo.addAll(table);
			if (getPower(allm).ordinal() < getPower(allo).ordinal() )
			{
				return false;
			}
		}
		return true;
	}
	
	public static HandPower getPower(ArrayList<Card> m)
	{
		ArrayList<Card> mine = m;
		Collections.sort(mine);
		
		int lastCard = -1;
		int cardsTilStraight = 5;
		boolean isStraighting = true;
		ArrayList<Integer> countPerSuit = new ArrayList<Integer>();
		ArrayList<Card> maxCardPerSuit = new ArrayList<Card>();
		Card highCard;
		for (int i = 0; i < 4; i++)
		{
			countPerSuit.add(0);
		}
		// For checking pairs, threes and quads;
		ArrayList<Integer> countPerValue = new ArrayList<Integer>();
		for (int i = 0; i < 13; i++)
		{
			countPerValue.add(0);
		}
		for (Card a : mine)
		{
			countPerSuit.set(a.getSuit().ordinal(), countPerSuit.get(a.getSuit().ordinal()) + 1);
			
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
		boolean isStraight = cardsTilStraight == 0;
		boolean isFlush = Collections.max(countPerSuit) >= 5;
		
		if (isStraight && isFlush)
		{
			return HandPower.STRAIGHT_FLUSH;
		}
		boolean isFour = Collections.max(countPerValue) == 4;
		if (isFour)
		{
			return HandPower.FOUR;
		}
		boolean hasThree = Collections.max(countPerValue) == 3;
		if (hasThree)
		{
			for (int i = 0; i < countPerValue.size(); i++)
			{
				if (countPerValue.get(i) == 2) return HandPower.FULL_HOUSE;
			}
		}
		if (isFlush)
		{
			return HandPower.FLUSH;
		}
		if (isStraight)
		{
			return HandPower.STRAIGHT;
		}
		if (hasThree) return HandPower.THREE;
		if (Collections.max(countPerValue) == 2)
		{
			int doublecount = 0;
			for (int i = 0; i < countPerValue.size(); i++)
			{
				if (countPerValue.get(i) >= 2) doublecount++;
			}
			if (doublecount >= 2) return HandPower.TWO_PAIR;
			else return HandPower.PAIR;
		}
		return HandPower.HI_CARD;
	}
}
