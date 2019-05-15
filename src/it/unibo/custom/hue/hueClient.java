package it.unibo.custom.hue;
import it.unibo.qactors.akka.QActor;

public class hueClient {
	public static HttpUtils client = null;
	
	public static void init(QActor qa) {
		client = new HttpUtils();
	}
	public static void on(QActor qa) { //hardcoded user and light id, can be parametrized
		try {
			client.doPutOrPost("http://localhost:8000/api/newdeveloper/lights/1/state", "{\"on\":true}}", "PUT");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void off(QActor qa) {
		try {
			client.doPutOrPost("http://localhost:8000/api/newdeveloper/lights/1/state", "{\"on\":false}}", "PUT");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

