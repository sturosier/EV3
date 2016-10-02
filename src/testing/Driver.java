package testing;
import com.r2jp2.colorsensor.MyColorSensor;
import com.r2jp2.motor.MyMotor;
import com.r2jp2.worker.MyColorFinder;

import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;

public class Driver {

	public static void main(String[] args) throws InterruptedException {
		
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

	}

}
