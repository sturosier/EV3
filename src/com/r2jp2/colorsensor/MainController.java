package com.r2jp2.colorsensor;

import java.awt.Button;

import com.r2jp2.colorsensor.IDetector.BillDetected;
import com.r2jp2.motor.MotorController;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Key;
import lejos.hardware.port.Port;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.utility.Delay;

public class MainController {

	/*
	 * public enum ProgramStatus{ Pause, Running, Quit }
	 */

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

		while (billDetector.isBillDetected() == isBillDetected) {
			Float[] sample = colorSensorController.sample();
			billDetector.newSample(sample);

			Delay.msDelay(50);
		}
	}

	public void start() {

		// motorController.resetMotors();
		// Delay.msDelay(1000);
		// motorController.startSpinning();
		// Delay.msDelay(4000);
		// motorController.startSpinningBackward();
		// Delay.msDelay(2000);
		// motorController.startSpinning();
		// Delay.msDelay(6000);
		// motorController.lowerArm();
		while (escapeKey.isUp()) {
			double avg = 0;
			Float[] sample = colorSensorController.sample();
			for (Float f : sample) {
				System.out.print(f.doubleValue()+",");
				avg += f.doubleValue();
			}
			//System.out.println("Sample: " + sample);
			System.out.println("avg: " + avg / sample.length);
			Delay.msDelay(500);
		}

		// //wait for someone to insert bill
		// waitForBillDetectorState(true);
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
		// motorController.destroy();
		// colorSensorController.destroy();

	}
}
