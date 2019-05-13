package it.unibo.custom.raspberry;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import it.unibo.qactors.akka.QActor;

public class raspLed {
	public static GpioPinDigitalOutput pin = null;
	
	public static void init(QActor qa) {
        System.out.println("<--Pi4J--> GPIO Control Example ... started.");

        // create gpio controller
       GpioController gpio = GpioFactory.getInstance();
        // provision gpio pin #01 as an output pin and turn on //HO CAMBIATO IN PIN12
       pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_12, "MyLED", PinState.HIGH);
        // set shutdown state for this pin
       try {
		Thread.sleep(2000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
       pin.low(); 
       //pin.setShutdownOptions(true, PinState.LOW);

      
	}
	
	public static void setOn(QActor qa) {
		if(pin != null) {
			pin.high();
			//System.out.println("[LedRasp] On");
		}
		//System.out.println("[LedRasp] On");
	}
	
	public static void setOff(QActor qa) {
		if(pin != null) {
			pin.low();
			//System.out.println("[LedRasp] Off");
		}
		//System.out.println("[LedRasp] Off");
	}
}
