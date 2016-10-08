package testing;

import com.r2jp2.colorsensor.MainController;
import com.r2jp2.motor.MyMotor;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.utility.Delay;

public class Driver {

	public static void main(String[] args) throws InterruptedException {
		
		
		MainController controller = new MainController();
		controller.start();
		
		/*
		MyMotor mThread = new MyMotor(MotorPort.B, MotorPort.C);
		
		MyColorSensor colorSensorThread = new MyColorSensor(SensorPort.S3);
		MyColorFinder colorFinder = new MyColorFinder(mThread, colorSensorThread);
		
		Thread motorWorker = new Thread(mThread);
		Thread colorSensorWorker = new Thread(colorSensorThread);
		Thread brainWorker = new Thread(colorFinder);
		
		colorSensorWorker.start();
		motorWorker.start();
		brainWorker.start();
		
		while (!colorFinder.isFinished()) {
			Thread.sleep(20);
		}
		
		colorSensorThread.stop();
        mThread.stop(); 
		*/
	}

}
