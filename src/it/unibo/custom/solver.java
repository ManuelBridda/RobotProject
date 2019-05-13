package it.unibo.custom;

import it.unibo.qactors.akka.QActor;

public class solver {
	static Boolean dxlibera = true;
	static Boolean pulito = false;
	static Boolean goingRight = true;

	public static void puliziaTerminata(QActor qa) {
		if (!t0.isAlive()) {
			if (goingRight) { 
				pulito = true;
				qa.emit("alarm", "alarm(offclean)");
			}
		}
	}

	public static void setPulitoFalse(QActor qa) {
		pulito = false;
	}

	public static boolean getPulito(QActor qa) {
		return pulito;
	}

	public static void setLiberaFalse(QActor qa) {
		dxlibera = false;
	}

	public static void setLiberaTrue(QActor qa) {
		dxlibera = true;
	}

	public static Boolean getLibera(QActor qa) {
		return dxlibera;
	}

	public static Thread t0 = null;
	public static Thread t2 = null;
	public static Thread t3 = null;

	public static void initRobot(QActor qa) throws InterruptedException {
		t0 = new Thread() {
			public void run() {
				try {
					setLiberaTrue(qa);
					while (getLibera(qa)) {
						qa.emit("robotEvent", "move(moveForward,150)");
						Thread.sleep(160);

					}

					// qa.emit("robotEvent", "move(moveForward,3500)");
					// Thread.sleep(4000);
					// setLiberaFalse(qa); //poichè sicuri della parete di fondo, dopo esserci
					// girati imponiamo che la destra non sia libera
					while (true) {
						if (!getLibera(qa)) { // se la destra non è libera (se false)
							setLiberaTrue(qa); // non sapendo se c'è qualcosa, mettiamo true
							qa.emit("robotEvent", "move(turnLeft,250)");
							Thread.sleep(300);
							qa.emit("robotEvent", "move(moveForward,150)");
							Thread.sleep(200);

							/*
							 * if(!getObstacle(qa)){ //se è stata trovata una parete di fondo, siamo in un
							 * angolo qa.emit("robotEvent", "move(turnLeft,150)"); Thread.sleep(500);
							 * setObstacleTrue(qa); }
							 */
						} else {
							// setObstacleTrue(qa); //non sapendo se c'è qualcosa, mettiamo true
							qa.emit("robotEvent", "move(turnRight,150)"); // quando la destra è libera proviamo a
																			// muoverci verso di essa
							Thread.sleep(200);
							qa.emit("robotEvent", "move(moveForward,250)"); // se sbattiamo, il sonar metterà obstacle a
																			// false.
							Thread.sleep(300);
						}
					}

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					// it.unibo.custom.customBlsGui.setLed(actor, "off");
					// System.out.println("Interrotto!");
				}
			}
		};
		t0.start();
	}

	public static void sonar1Reached(QActor qa) throws InterruptedException {
		if (t0.isAlive()) {
			t0.interrupt();
			t2 = new Thread() {
				public void run() {
					try {
						setPulitoFalse(qa);
						goingRight = true;
						setLiberaTrue(qa);

						qa.emit("robotEvent", "move(turnRight,150)");
						Thread.sleep(200);
						while (!getPulito(qa)) {

							while (getLibera(qa)) {
								qa.emit("robotEvent", "move(moveForward,150)");
								Thread.sleep(200);

							}
							if (goingRight) {
								goingRight = false;
								qa.emit("robotEvent", "move(turnRight,150)");
								Thread.sleep(200);
								qa.emit("robotEvent", "move(moveForward,300)");
								Thread.sleep(400);
								qa.emit("robotEvent", "move(turnRight,150)");
								Thread.sleep(200);

								setLiberaTrue(qa);
							} else {
								goingRight = true;
								qa.emit("robotEvent", "move(turnLeft,150)");
								Thread.sleep(200);
								qa.emit("robotEvent", "move(moveForward,300)");
								Thread.sleep(400);
								qa.emit("robotEvent", "move(turnLeft,150)");
								Thread.sleep(200);
								setLiberaTrue(qa);

							}
						}

					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						// it.unibo.custom.customBlsGui.setLed(actor, "off");
						// System.out.println("Interrotto!");
					}

				}
			};
			t2.start();
		}


	}

	public static void stopRobot(QActor qa) {
		if (t0 != null && t0.isAlive()) {
			t0.interrupt();
		}
		if (t2 != null && t2.isAlive()) {
			t2.interrupt();
		}

	}
}