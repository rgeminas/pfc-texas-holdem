package br.eb.ime.comp.nnbot.main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import br.eb.ime.comp.nnbot.learning.ExampleOutput;
import br.eb.ime.comp.nnbot.learning.NeuralNetworkLearner;
import br.eb.ime.comp.nnbot.learning.TrainingSet;
import br.eb.ime.comp.nnbot.learning.VowelsInput;;

public class Main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		System.out.println("winning");
		
		ArrayList<TrainingSet<VowelsInput, ExampleOutput>> tset = new ArrayList<TrainingSet<VowelsInput, ExampleOutput>>();
		BufferedReader cin = new BufferedReader(new FileReader(
				"C:/Users/Renan Gemignani/Development/pfc-texas-holdem/nnbot/testsets/10k_vowels.txt"
				));
		String line = cin.readLine();
		while (line != null)
		{
			String[] strings = line.split("[ \t]+");
			TrainingSet<VowelsInput, ExampleOutput> ts = new TrainingSet<VowelsInput, ExampleOutput>();
	        ts.setInput(new VowelsInput(strings[0].charAt(0)));
	        ts.setOutput(new ExampleOutput(strings[1].charAt(0) == '1'));
	        
	        tset.add(ts);
	        line = cin.readLine();
		}
		System.out.println("");
		
		NeuralNetworkLearner<VowelsInput, ExampleOutput> nnlearner = new NeuralNetworkLearner<VowelsInput, ExampleOutput>(.05, 0.1, 1, 26, 1);
		nnlearner.train(tset);
		//nnlearner.
	}

}
