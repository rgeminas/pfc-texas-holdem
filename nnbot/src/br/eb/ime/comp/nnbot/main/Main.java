package br.eb.ime.comp.nnbot.main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
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
		System.out.println("Working Directory = " +
	              System.getProperty("user.dir"));
		
		ArrayList<TrainingSet<VowelsInput, ExampleOutput>> tset = new ArrayList<TrainingSet<VowelsInput, ExampleOutput>>();
		BufferedReader cin = new BufferedReader(new FileReader(
				"./testsets/10k_edges.txt"
				));
		FileWriter fw = new FileWriter("./testsets/10k_edges.out");
		String line = cin.readLine();
		while (line != null)
		{
			String[] strings = line.split("[ \t]+");
			TrainingSet<VowelsInput, ExampleOutput> ts = new TrainingSet<VowelsInput, ExampleOutput>();
	        ts.setInput(new VowelsInput(Double.parseDouble(strings[0]), Double.parseDouble(strings[1])));
	        ts.setOutput(new ExampleOutput(strings[2].equals("1")));
	        
	        tset.add(ts);
	        line = cin.readLine();
		}
		cin.close();
		
		NeuralNetworkLearner<VowelsInput, ExampleOutput> nnlearner = new NeuralNetworkLearner<VowelsInput, ExampleOutput>(.1, 0, 2, 6, 1, fw);
		nnlearner.train(tset);
		//nnlearner.
	}

}
