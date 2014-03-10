package br.eb.ime.comp.nnbot.model;

public class Card implements Comparable {
	public Card(Value v, Suit s)
	{
		value = v;
		suit = s;
	}
	public Value getValue() {
		return value;
	}
	public void setValue(Value value) {
		this.value = value;
	}
	public Suit getSuit() {
		return suit;
	}
	public void setSuit(Suit suit) {
		this.suit = suit;
	}
	private Value value;
	private Suit suit;
	
	@Override
	public int compareTo(Object o) {
		if (o instanceof Card)
		{
			return (new Integer(((Card)o).value.ordinal())).compareTo(new Integer(this.value.ordinal()));
		}
		// TODO Auto-generated method stub
		return 0;
	}
}
