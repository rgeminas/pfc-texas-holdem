package br.eb.ime.comp.nnbot.learning;

public class VowelsInput implements Arrayable {
	public int getArraySize() { return 1; };
	private char character;
	
	public VowelsInput(char c)
	{
		character = c;
	}
	
	public double[] toArray()
	{
		double[] d = new double[1];
		d[0] = ((double) (character - 'a'))/ 13. - 1;
		return d;
	}

}
