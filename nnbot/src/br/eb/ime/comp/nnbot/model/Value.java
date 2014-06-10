package br.eb.ime.comp.nnbot.model;

public enum Value {
	TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE;
	
	public static Value getByValue(int v)
	{
		switch(v)
		{
		case 12: return ACE;
		case 11: return KING;
		case 10: return QUEEN;
		case 9: return JACK;
		case 8: return TEN;
		case 7: return NINE;
		case 6: return EIGHT;
		case 5: return SEVEN;
		case 4: return SIX;
		case 3: return FIVE;
		case 2: return FOUR;
		case 1: return THREE;
		case 0: return TWO;
		}
		return TWO;
	}
}
