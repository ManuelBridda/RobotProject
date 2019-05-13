package it.unibo.utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import org.json.JSONObject;

import it.unibo.qactors.QActorContext;
import it.unibo.qactors.akka.QActor;

public class clientTcp   {
private String name="ClientTcp";
private static String hostName= "localhost";
private static int port = 8999;
private static String sep=";";
protected static Socket clientSocket ;
//protected static DataOutputStream outToServer;
protected static PrintWriter outToServer;
protected static BufferedReader inFromServer;

	public static void initClientConn(QActor qa ) throws Exception {
		 initClientConn(qa, hostName, ""+port);
	 }
	public static void initClientConn(QActor qa, String hostNameStr, String portStr) throws Exception {
		 hostName = hostNameStr;
		 port     = Integer.parseInt(portStr);
		 clientSocket = new Socket(hostName, port);
		 //outToServer  = new DataOutputStream(clientSocket.getOutputStream());
		 inFromServer = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()) );  
		 outToServer  = new PrintWriter(clientSocket.getOutputStream());
		 startTheReader(  qa );
	}

	public static void sendMsg(QActor qa, String jsonString) throws Exception {
		JSONObject jsonObject = new JSONObject(jsonString);
		String msg = sep+jsonObject.toString()+sep;
		//outToServer.writeBytes(msg);
		outToServer.println(msg);
		outToServer.flush();
	}
	//AGGIUNTO DA MANUEL
	public static void sendMsg(QActor qa, String direzione, String time) throws Exception {
		String jsonString = "{'type':'"+direzione+"','arg': "+time+"}";
		JSONObject jsonObject = new JSONObject(jsonString);
		String msg = sep+jsonObject.toString()+sep;
		//outToServer.writeBytes(msg);
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
						//System.out.println( "reads: " + inpuStr);
						String jsonMsgStr = inpuStr.split(";")[1];
						//System.out.println( "reads: " + jsonMsgStr + " qa=" + qa.getName() );
						JSONObject jsonObject = new JSONObject(jsonMsgStr);
						//System.out.println( "type: " + jsonObject.getString("type"));
						switch (jsonObject.getString("type") ) {
						case "webpage-ready" : System.out.println( "webpage-ready "   );break;
						case "sonar-activated" : {
							//wSystem.out.println( "sonar-activated "   );
							JSONObject jsonArg = jsonObject.getJSONObject("arg");
							String sonarName   = jsonArg.getString("sonarName");							
							int distance       = jsonArg.getInt( "distance" );
							//System.out.println( "sonarName=" +  sonarName + " distance=" + distance);
							qa.emit("sonar", 
								"sonar(NAME, player, DISTANCE)".replace("NAME", sonarName.replace("-", "")).replace("DISTANCE", (""+distance) ));
							break;
						}
						case "collision" : {
							//System.out.println( "collision"   );
							JSONObject jsonArg  = jsonObject.getJSONObject("arg");
							String objectName   = jsonArg.getString("objectName");
							//System.out.println( "collision objectName=" +  objectName  );
							//moving-obstacle-1, static-obstacle-2
							qa.emit("obstacleEvent","obstacleEvent(TARGET)".replace("TARGET", objectName.replace("-", ""))); //era sonarDetect
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
	
//	public void doJob() throws Exception {
//		initClientConn();
//		String jsonString ="";
//		JSONObject jsonObject;
//		String msg="";
//for( int i=1; i<=3; i++ ) {		
//		jsonString = "{ 'type': 'moveForward', 'arg': 800 }";
//		jsonObject = new JSONObject(jsonString);
//		msg = sep+jsonObject.toString()+sep;
//		println("sending msg=" + msg);
//		sendMsg( msg  );
//		Thread.sleep(1000);
//		
//			jsonString = "{ 'type': 'moveBackward', 'arg': 800 }";
// 			jsonObject = new JSONObject(jsonString);
// 			msg = sep+jsonObject.toString()+sep;
// 			println("sending msg=" + msg);
//			sendMsg( msg  );			 
//			Thread.sleep(1000); 
//			
//}			 
//			clientSocket.close();
//	}
//
//	
//	public static void main(String[] args) throws Exception {
//  		new ClientTcp().doJob();
//	}
	

}
