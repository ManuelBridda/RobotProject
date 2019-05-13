package it.unibo.custom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import it.unibo.qactors.akka.QActor;

public class tempInput {

	public static void read(QActor qa) throws IOException {
		System.out.println("Inserisci temperatura mock: ");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String newTemp = br.readLine();
		
		qa.replaceRule("temp(X)", "temp(" + newTemp + ")");
	}
}
