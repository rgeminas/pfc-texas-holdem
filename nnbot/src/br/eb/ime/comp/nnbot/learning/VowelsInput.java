package br.eb.ime.comp.nnbot.learning;

public class VowelsInput implements Arrayable {
	public int getArraySize() { return 1; };
	private char character;
	private double x;
	private double y;
	
	// This is mostly a test.
	public VowelsInput(double c, double d)
	{
		//character = c;
		x = c;
		y = d;
	}
	
	public double[] toArray()
	{
		/*
		double[] d = new double[1];
		d[0] = ((double) (character - 'a'))/ 13. - 1;
		return d;
		*/
		/*
		double[] d = new double[26];
		d[character - 'a'] = 1;
		return d;
		*/
		double[] d = new double[2];
		d[0] = x;
		d[1] = y;
		return d;
	}

}
