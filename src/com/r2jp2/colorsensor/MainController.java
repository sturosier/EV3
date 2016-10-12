package com.r2jp2.colorsensor;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Key;
import lejos.hardware.port.Port;
import lejos.robotics.Color;
import lejos.utility.Delay;

import com.r2jp2.colorsensor.ColorSensorController.ColorSensorModeEnum;
import com.r2jp2.motor.BillDetectorVoice;
import com.r2jp2.motor.MotorController;
import com.r2jp2.motor.VoicePlayer;

public class MainController {

	private Brick brick;
	private Key escapeKey;
	private Key enterKey;
	private MotorController motorController;
	private ColorSensorController colorSensorController;
	private TrainingMode trainingMode;
	private IDetector billDetector;

	public MainController() {
		brick = BrickFinder.getDefault();
		escapeKey = brick.getKey("Escape");
		enterKey = brick.getKey("Enter");
		Port colorSensorPort = brick.getPort("S4");
		Port armMotorPort = brick.getPort("A");
		Port rollerMotorPort = brick.getPort("B");

		colorSensorController = new ColorSensorController(colorSensorPort, ColorSensorModeEnum.RedMode);
		motorController = new MotorController(armMotorPort, rollerMotorPort);
		billDetector = new ChenBillDetector();
		trainingMode = new TrainingMode(colorSensorController, enterKey, billDetector);
	}

	private void waitForBillDetectorState(boolean isBillDetected) {

		while (billDetector.isBillDetected() != isBillDetected) {
			Float[] sample = colorSensorController.sample();
			billDetector.newSample(sample);

			Delay.msDelay(100);
		}
	}

	/*
	private void initThreshold() {

		while (enterKey.isUp()) {
			Float[] sample = colorSensorController.sample();
			billDetector.newSample(sample);
			// wait
			Delay.msDelay(100);
		}
		billDetector.setThreshold();
	}*/

	private void playSound(int color) {

		VoicePlayer voice = new VoicePlayer(color);
		Thread t = new Thread(voice);
		t.start();
	}

	public void start() {

		BillDetectorVoice voice = new BillDetectorVoice();
		
		trainingMode.calibrate();
		
		//******** CALIBRATION DONE IN TRAINING MODE *****************
		// Begin Init Process
		//motorController.lowerArm();
		/* Note:
		 * To initialize: 
		 * 1) Wait for sensor values to stabalize
		 * 2) Wait until arm comes to rest in the up position		 
		 */
		//initThreshold();
		//motorController.raiseArm();

		playSound(Color.WHITE);

		// End Init Process
		while (escapeKey.isUp()) {
			Delay.msDelay(1000);
			motorController.lowerArm();
			// Wait for Bill Insertion
			Delay.msDelay(1000);
			waitForBillDetectorState(true);
			Delay.msDelay(250);
			System.out.println("detected bill:");
			motorController.startSpinning();
			Delay.msDelay(1000);
			// <PROCESS BILL>
			// Wait for Bill to Disappear
			waitForBillDetectorState(false);
			System.out.println("done detecting bill.");
			System.out.println("Detected bill:"
					+ billDetector.recognizeBill().toString());

			// TODO: Potentially spit bill back out?
			motorController.startSpinningBackward();
			Delay.msDelay(4000);
			motorController.resetMotors();
			billDetector.reset();
			Delay.msDelay(3000);
		}
		motorController.destroy();
		colorSensorController.destroy();
	}
}
