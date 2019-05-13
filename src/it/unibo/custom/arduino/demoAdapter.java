package it.unibo.custom.arduino;

public class demoAdapter {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		it.unibo.custom.arduino.realRobotAdapterNoQa.init("COM4");
		Thread.sleep(2000);
		it.unibo.custom.arduino.realRobotAdapterNoQa.fw("10000"); 
		Thread.sleep(2000);
		it.unibo.custom.arduino.realRobotAdapterNoQa.bw("10000");
		Thread.sleep(1000); 
		it.unibo.custom.arduino.realRobotAdapterNoQa.fw("10000");
		Thread.sleep(2000);
		it.unibo.custom.arduino.realRobotAdapterNoQa.bw("10000");
		Thread.sleep(12000);
	}

}
