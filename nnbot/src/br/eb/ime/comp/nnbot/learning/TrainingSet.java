package br.eb.ime.comp.nnbot.learning;

public class TrainingSet<InputClass, OutputClass> {
	private InputClass input;
	private OutputClass output;
	
	public TrainingSet(InputClass ic, OutputClass oc)
	{
		input = ic;
		output = oc;
	}
	
	public InputClass getInput() {
		return input;
	}
	public void setInput(InputClass input) {
		this.input = input;
	}
	public OutputClass getOutput() {
		return output;
	}
	public void setOutput(OutputClass output) {
		this.output = output;
	}
}
