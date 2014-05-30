package br.eb.ime.comp.nnbot.model;

public class ProfileAugmentedSituation extends Situation {
	public int[] profileIds;
	
	@Override
	public double[] toArray() {
		double[] d = super.toArray();
		double[] result = new double[d.length + 11];
		for (int i = 0; i < d.length; i++)
		{
			result[i] = d[i];
		}
		for (int i = d.length; i < result.length; i++)
		{
			result[i] = profileIds[i - d.length];
		}
		return result;
	}
	
	@Override
	public int getArraySize() {
		return super.getArraySize() + 11;
	}
	
}
