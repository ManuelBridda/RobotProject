package it.unibo.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.json.JSONObject;

import it.unibo.qactors.akka.QActor;

public class getTemperatureFromWeb {

	public static void getTemperature(QActor qa, String apiKey) throws IOException {
		URL url;

		HttpURLConnection con = null;
		try {
			url = new URL("https://api.openweathermap.org/data/2.5/weather?id=3181928&units=metric&appid=" + apiKey);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		BufferedReader in;
		StringBuffer content = null;
		try {
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			content = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();
			con.disconnect();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// PARSING
		JSONObject jsonObject = new JSONObject(content.toString());
		JSONObject jsonMain = jsonObject.getJSONObject("main");
		double tempBologna = jsonMain.getDouble("temp");
		
		qa.replaceRule("temp(X)", "temp(" + tempBologna + ")");
	}

}


