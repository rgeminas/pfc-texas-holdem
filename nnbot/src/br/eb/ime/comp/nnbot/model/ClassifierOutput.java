package br.eb.ime.comp.nnbot.model;

import br.eb.ime.comp.nnbot.learning.Arrayable;

public class ClassifierOutput implements Arrayable {

	int numProfiles;
	int selectedProfile;
	
	@Override
	public int getArraySize() {
		// TODO Auto-generated method stub
		return numProfiles;
	}

	@Override
	public double[] toArray() {
		// TODO Auto-generated method stub
		double[] result = new double[getArraySize()];
		for (int i = 0; i < result.length; i++)
		{
			if (i == selectedProfile)
			{
				result[i] = 1;
			}
			else
			{
				result[i] = 0;
			}
		}
		return result;
	}

}
