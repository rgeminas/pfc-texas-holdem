package br.eb.ime.comp.nnbot.model;

public enum Value {
	ACE, KING, QUEEN, JACK, TEN, NINE, EIGHT, SEVEN, SIX, FIVE, FOUR, THREE, TWO;
	public static Value getByValue(int v)
	{
		switch(v)
		{
		case 0: return ACE;
		case 1: return KING;
		case 2: return QUEEN;
		case 3: return JACK;
		case 4: return TEN;
		case 5: return NINE;
		case 6: return EIGHT;
		case 7: return SEVEN;
		case 8: return SIX;
		case 9: return FIVE;
		case 10: return FOUR;
		case 11: return THREE;
		case 12: return TWO;
		}
		return TWO;
	}
}
