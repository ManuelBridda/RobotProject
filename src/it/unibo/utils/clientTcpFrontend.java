package it.unibo.utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import org.json.JSONObject;
import it.unibo.qactors.akka.QActor;

public class clientTcpFrontend   {
private String name="ClientTcpFrontend";
private static String hostName= "localhost";
private static int port = 7777;
private static String sep=";";
protected static Socket clientSocket ;
protected static PrintWriter outToServer;
protected static BufferedReader inFromServer;

	public static void initClientConn(QActor qa ) throws Exception {
		 initClientConn(qa, hostName, ""+port);
	 }
	public static void initClientConn(QActor qa, String hostNameStr, String portStr) throws Exception {
		 hostName = hostNameStr;
		 port     = Integer.parseInt(portStr);
		 clientSocket = new Socket(hostName, port);
		 inFromServer = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()) );  
		 outToServer  = new PrintWriter(clientSocket.getOutputStream());
		 startTheReader(  qa );
	}

	public static void sendMsg(QActor qa, String jsonString) throws Exception {
		JSONObject jsonObject = new JSONObject(jsonString);
		String msg = sep+jsonObject.toString()+sep;
		outToServer.println(msg);
		outToServer.flush();
	}

	protected void println(String msg) {
		System.out.println(name + ": " + msg);
	}
	
	
	protected static void startTheReader(final QActor qa) {
		
		new Thread() {
			public void run() {
				while( true ) {
					 
					try {
						String inpuStr = inFromServer.readLine();
						JSONObject jsonObject = new JSONObject(inpuStr);
						switch (jsonObject.getString("type") ) {
						case "start" : {
							qa.sendMsg("usercmd", "conditioncontrol", "dispatch", "cmd(console, consmock, on)"); //verificare se funziona
							break;
						}
						case "stop" : {
							qa.sendMsg("usercmd", "conditioncontrol", "dispatch", "cmd(console, consmock, off)"); 
							break;
						}
						};
 					} catch (IOException e) {
 						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	

	

}
