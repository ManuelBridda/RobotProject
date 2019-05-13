package it.unibo.custom.arduino;


public class realRobotAdapterNoQa {
	public static JSSCSerialComm serialConn = new JSSCSerialComm(null);
	public static SerialPortConnSupport sp = null;

	public static void init( String port) {
		try {
			sp = serialConn.connect(port);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Thread for reading sonar serial events (readALine is a blocking action, so we
		// need a Thread).
		getSonarSerialEvents();
	}

	private static void getSonarSerialEvents() {
		new Thread() {
			public void run() { 
				String msg = null;
				while (true) { // is there a condition to test instead of using true? Like "while the serial
								// connection is open"?
					try {
						msg = sp.receiveALine();
						if(msg.equals("detected\r\n")) {
							System.out.println("Trovato ostacolo");
						}
						
						}
					 catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}.start();

	}



	public static void fw( String time) {
		try {
			if (sp != null)
				sp.sendCmd("w");
				sp.sendCmd(time);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void bw( String time) {
		try {
			if (sp != null)
				sp.sendCmd("s");
				sp.sendCmd(time);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void tl( String time) {
		try {
			if (sp != null)
				sp.sendCmd("a");
				sp.sendCmd(time);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void tr( String time) {
		try {
			if (sp != null)
				sp.sendCmd("d");
				sp.sendCmd(time);
		} catch (Exception e) { 
			e.printStackTrace();
		}
	}

	public static void halt() {
		try {
			if (sp != null)
				sp.sendCmd("h");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
