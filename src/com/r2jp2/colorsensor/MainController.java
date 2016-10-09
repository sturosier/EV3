package com.r2jp2.colorsensor;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Key;
import lejos.hardware.port.Port;
import lejos.utility.Delay;

import com.r2jp2.motor.BillDetectorVoice;
import com.r2jp2.motor.MotorController;

public class MainController {

	private Brick brick;
	private Key escapeKey;
	private Key enterKey;
	private MotorController motorController;
	private ColorSensorController colorSensorController;
	private ChenBillDetector newBillDetector;
	// private ProgramStatus status = ProgramStatus.Pause;
	private IDetector billDetector;

	public MainController() {
		brick = BrickFinder.getDefault();
		escapeKey = brick.getKey("Escape");
		enterKey = brick.getKey("Enter");
		Port colorSensorPort = brick.getPort("S4");
		Port armMotorPort = brick.getPort("A");
		Port rollerMotorPort = brick.getPort("B");

		colorSensorController = new ColorSensorController(colorSensorPort);
		motorController = new MotorController(armMotorPort, rollerMotorPort);
		billDetector = new ChenBillDetector();
		// newBillDetector = new ChenBillDetector();
	}

	private void waitForBillDetectorState(boolean isBillDetected) {

		while (billDetector.isBillDetected() != isBillDetected) {
			Float[] sample = colorSensorController.sample();
			/*
			 * for (Float f : sample) {
			 * System.out.println(f.doubleValue()+", "); }
			 */
			billDetector.newSample(sample);

			Delay.msDelay(100);
		}
	}

	private void initThreshold() {

		while (enterKey.isUp()) {
			Float[] sample = colorSensorController.sample();
			billDetector.newSample(sample);
			// wait
			Delay.msDelay(100);
		}
		billDetector.setThreshold();
	}

	public void start() {

		BillDetectorVoice voice = new BillDetectorVoice();
		motorController.lowerArm();
		initThreshold();
		motorController.raiseArm();
		while (escapeKey.isUp()) {
			Delay.msDelay(1000);
			motorController.lowerArm();
			// wait for someone to insert bill
			Delay.msDelay(1000);
			waitForBillDetectorState(true);
			Delay.msDelay(750);
			//motorController.lowerArm();
			System.out.println("detected bill:");
			motorController.startSpinning();
			Delay.msDelay(1000);
			// wait for the bill to disappear
			waitForBillDetectorState(false);
			System.out.println("done detecting bill.");
			// motorController.startSpinningBackward();
			// //detect bill going backward
			// waitForBillDetectorState(true);
			// //wait for the bill to disappear (user takes bill)
			// waitForBillDetectorState(false);
			motorController.resetMotors();
			Delay.msDelay(4000);
		}
		motorController.destroy();
		colorSensorController.destroy();
		//
		//
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
