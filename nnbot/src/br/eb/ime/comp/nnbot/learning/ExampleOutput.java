package br.eb.ime.comp.nnbot.learning;

public class ExampleOutput implements Arrayable {
	public int getArraySize() { return 1; };
	private boolean out;
	
	public ExampleOutput(boolean b)
	{
		out = b;
	}
	
	public double[] toArray()
	{
		double[] d = new double[1];
		d[0] = out? 1. : 0.;
		return d;
	}
}
