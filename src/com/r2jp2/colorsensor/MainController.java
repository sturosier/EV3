package com.r2jp2.colorsensor;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Key;
import lejos.hardware.port.Port;
import lejos.robotics.Color;
import lejos.utility.Delay;

import com.r2jp2.motor.BillDetectorVoice;
import com.r2jp2.motor.MotorController;

public class MainController {

	private Brick brick;
	private Key escapeKey;
	private Key enterKey;
	private MotorController motorController;
	private ColorSensorController colorSensorController;
	// private ProgramStatus status = ProgramStatus.Pause;
	private IDetector billDetector;

	public MainController() {
		brick = BrickFinder.getDefault();
		escapeKey = brick.getKey("Escape");

		Port colorSensorPort = brick.getPort("S4");
		Port armMotorPort = brick.getPort("A");
		Port rollerMotorPort = brick.getPort("B");

		colorSensorController = new ColorSensorController(colorSensorPort);
		motorController = new MotorController(armMotorPort, rollerMotorPort);
		billDetector = new BillDetector();
	}

	private void waitForBillDetectorState(boolean isBillDetected) {

		while (billDetector.isBillDetected() != isBillDetected) {
			Float[] sample = colorSensorController.sample();
			//System.out.println("ts:" + System.currentTimeMillis());
			/*
			 * for (Float f : sample) {
			 * System.out.println(f.doubleValue()+", "); }
			 */
			billDetector.newSample(sample);

			Delay.msDelay(100);
		}
	}

	public void start() {

		BillDetectorVoice voice = new BillDetectorVoice();
		
		voice.sortBill(Color.PINK);
//		while (escapeKey.isUp()) {
//			//voice.sortBill(Color.WHITE);
//			
//			Delay.msDelay(6000);
//			
//			// wait for someone to insert bill
//			waitForBillDetectorState(true);
//			motorController.lowerArm();
//
//			Delay.msDelay(1000);
//			motorController.startSpinning();
//			waitForBillDetectorState(false);
//			motorController.resetMotors();
//		
//			Delay.msDelay(1000);
//	
//		}
		motorController.destroy();
		colorSensorController.destroy();
		//
		// motorController.startSpinningBackward();
		// Delay.msDelay(2000);
		// motorController.startSpinning();
		// Delay.msDelay(6000);
		// motorController.lowerArm();
		// while (escapeKey.isUp()) {
		//
		// double avg = 0;
		// Float[] sample = colorSensorController.sample();
		// for (Float f : sample) {
		// System.out.println(f.doubleValue()+", ");
		// avg += f.doubleValue();
		// }
		// //System.out.println("Sample: " + sample);
		// //System.out.println("avg: " + avg / sample.length);
		// Delay.msDelay(500);
		// }

		//
		// motorController.lowerArm();
		// motorController.startSpinning();
		//
		// //wait for bill to scroll across the sensor
		// waitForBillDetectorState(false);
		//
		// motorController.resetMotors();
		//
		// BillDetected billType = billDetector.recognizeBill();
		//
		// //Output bill type
		//

	}
}
