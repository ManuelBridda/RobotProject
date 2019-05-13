package it.unibo.custom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import it.unibo.qactors.akka.QActor;

public class consoleInput {

	public static void read(QActor qa) throws IOException {
		System.out.println("Inserisci comando mock (1-START 2-STOP): ");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		String newTemp = br.readLine();

		if (newTemp.startsWith("1")) {
			qa.replaceRule("cmd(X)", "cmd(on)");
		} else if (newTemp.startsWith("2")) {
			qa.replaceRule("cmd(X)", "cmd(off)");
		} else {
			System.err.println("Errore!");
		}
	}
}
