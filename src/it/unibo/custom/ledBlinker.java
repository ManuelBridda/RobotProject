package it.unibo.custom;

import it.unibo.qactors.akka.QActor;

public class ledBlinker {
	// private static boolean blinking = false;
	public static Thread t1 = null;
	public static boolean blinking = false;

	public static synchronized void initLed(QActor actor) {
		it.unibo.custom.customBlsGui.createCustomLedGui(actor);
		initThread(actor);

	}

	private static void initThread(QActor actor) {
		t1 = new Thread() {
			public void run() {
				try {
					while (true) {
						//System.out.println("Cycling");
						Thread.sleep(400);
						it.unibo.custom.customBlsGui.setLed(actor, "on");
						Thread.sleep(400);
						it.unibo.custom.customBlsGui.setLed(actor, "off");
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					it.unibo.custom.customBlsGui.setLed(actor, "off");
					//System.out.println("Interrotto!");
				}

			}
		};
	}

	public static synchronized void startBlinking(QActor actor) throws InterruptedException {
		//System.out.println("Starting");
		if(!blinking) {
			blinking = true;
			t1.start();
		}
			
	}

	public static synchronized void stopBlinking(QActor actor) {
		//System.out.println("Interrupting");
		blinking = false;
		t1.interrupt();
		initThread(actor);
	}
}
