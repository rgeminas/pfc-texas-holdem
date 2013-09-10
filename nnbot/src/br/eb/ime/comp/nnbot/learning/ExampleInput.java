package br.eb.ime.comp.nnbot.learning;

public class ExampleInput extends Arrayable {
	public static final int arraySize = 16;
	private double[] outputs;
	
	public ExampleInput(double[] o)
	{
		assert(o.length == 16);
		outputs = o;
	}
	
	public double[] toArray()
	{
		return outputs;
	}

}
