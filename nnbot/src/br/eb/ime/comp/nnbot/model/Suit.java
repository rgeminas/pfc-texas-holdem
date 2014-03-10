package br.eb.ime.comp.nnbot.model;

public enum Suit {
	HEARTS, DIAMONDS, SPADES, CLUBS;
	public static Suit getByValue(int v)
	{
		switch (v)
		{
		case 0: return HEARTS;
		case 1: return DIAMONDS;
		case 2: return SPADES;
		case 3: return CLUBS;
		}
		return HEARTS;
	}
}
