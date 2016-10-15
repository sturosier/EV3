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

	public static final String ONE_DOLLAR_FILE = "one.wav";
	public static final String FIVE_DOLLAR_FILE = "five.wav";
	public static final String TEN_DOLLAR_FILE = "ten.wav";
	public static final String TWENTY_DOLLAR_FILE = "twenty.wav";

	private final int ONE = Color.WHITE;
	private final int FIVE = Color.PINK;
	private final int TEN = Color.YELLOW;
	private final int TWENTY = Color.GREEN;
	private final int DETECTED = 100;
	private final int RETURNED = 101;

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

		// billDetector = new MonopolyBillDetector();
		// colorSensorController = new ColorSensorController(colorSensorPort,
		// ColorSensorModeEnum.RedMode);

		billDetector = new RGBColorDetector();
		colorSensorController = new ColorSensorController(colorSensorPort,
				ColorSensorModeEnum.RGBMode);

		motorController = new MotorController(armMotorPort, rollerMotorPort);
		// billDetector = new ChenBillDetector();
		trainingMode = new TrainingMode(colorSensorController, enterKey,
				billDetector, motorController);

	}

	private void waitForBillDetectorState(boolean isBillDetected) {

		while (billDetector.isBillDetected() != isBillDetected) {
			Float[] sample = colorSensorController.sample();
			billDetector.newSample(sample);

			Delay.msDelay(100);
		}
	}

	/*
	 * private void initThreshold() {
	 * 
	 * while (enterKey.isUp()) { Float[] sample =
	 * colorSensorController.sample(); billDetector.newSample(sample); // wait
	 * Delay.msDelay(100); } billDetector.setThreshold(); }
	 */

	private void playSound(int color) {

		VoicePlayer voice = new VoicePlayer(color);
		Thread t = new Thread(voice);
		t.start();

	}

	public void start() {
		BillDetectorVoice voice = new BillDetectorVoice();

		motorController.lowerArm();
		trainingMode.calibrate();

		// ******** CALIBRATION DONE IN TRAINING MODE *****************

		motorController.raiseArm();

		// End Init Process
		while (escapeKey.isUp()) {
			Delay.msDelay(1000);
			motorController.lowerArm();
			// Wait for Bill Insertion
			Delay.msDelay(1000);
			waitForBillDetectorState(true);
			Delay.msDelay(250);
			// playSound(DETECTED);
			System.out.println("detected bill:");
			motorController.startSpinning();
			Delay.msDelay(1000);
			// <PROCESS BILL>
			// Wait for Bill to Disappear
			waitForBillDetectorState(false);
			System.out.println("done detecting bill.");
			System.out.println("Detected bill:"
					+ billDetector.recognizeBill().toString());
			motorController.stopRoller();
			Delay.msDelay(1000);
			try {

				if (billDetector.recognizeBill().toString().contains("WHITE")) {
					voice.playBeep(ONE);
				} else if (billDetector.recognizeBill().toString()
						.contains("RED")) {
					voice.playBeep(FIVE);
				} else if (billDetector.recognizeBill().toString()
						.contains("BLUE")) {
					voice.playBeep(TEN);
				} else if (billDetector.recognizeBill().toString()
						.contains("GREEN")) {
					voice.playBeep(TWENTY);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			motorController.startSpinningBackward();
			Delay.msDelay(3000);
			motorController.resetMotors();
			billDetector.reset();
			// playSound(RETURNED);
			Delay.msDelay(2000);
		}
		motorController.destroy();
		colorSensorController.destroy();
	}
}
