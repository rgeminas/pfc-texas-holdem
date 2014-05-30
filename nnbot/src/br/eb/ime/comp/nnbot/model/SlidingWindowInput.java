package br.eb.ime.comp.nnbot.model;

import java.util.ArrayList;

import br.eb.ime.comp.nnbot.learning.Arrayable;

public class SlidingWindowInput<T extends Arrayable> implements Arrayable {
	int maxSize;
	ArrayList<T> window;
	int sizePerWindow;
	
	public SlidingWindowInput(int _windowSize, int _sizePerWindow)
	{
		maxSize = _windowSize;
		sizePerWindow = _sizePerWindow;
	}
	
	public int getArraySize()
	{
		return maxSize * sizePerWindow;	
	}
	
	public double[] toArray()
	{
		double[] result = new double[getArraySize()];
		int i;
		for (i = 0; i < window.size(); i++)
		{
			for (int j = 0; j < window.get(i).getArraySize(); j++)
			{
				double[] arr = window.get(i).toArray();
				result[sizePerWindow * i + j] = arr[j];
			}
		}
		while (i < maxSize)
		{
			result[i] = 0;
		}
		return result;
	}
}
