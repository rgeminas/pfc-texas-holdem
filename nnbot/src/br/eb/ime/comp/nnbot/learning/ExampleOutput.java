package br.eb.ime.comp.nnbot.learning;

public class ExampleOutput extends Arrayable {
	public static final int arraySize = 1;
	private double output;
	
	public ExampleOutput()
	{
		
	}
	
	public double[] toArray()
	{
		double[] d = new double[1];
		d[0] = output;
		return d;
	}
}
