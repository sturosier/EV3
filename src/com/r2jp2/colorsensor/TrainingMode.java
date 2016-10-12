package com.r2jp2.colorsensor;

import com.r2jp2.motor.MotorController;

import lejos.hardware.Key;
import lejos.utility.Delay;

public class TrainingMode {

	private ColorSensorController colorSensorController;
	private Key enterKey;
	private IDetector billDetector;
	private MotorController motorController;
	public TrainingMode(ColorSensorController colorSensorController, Key enterKey, IDetector billDetector, MotorController motorController){
		this.colorSensorController = colorSensorController;
		this.enterKey = enterKey;
		this.billDetector = billDetector;
		this.motorController = motorController;
	}
	
	public void calibrate(){
		
		//Put anything COMPLETELY BLACK under the sensor
		System.out.println("Put black under sensor");
		while (enterKey.isUp()) {
			Float[] sample = colorSensorController.sample();
			System.out.println(Util.normalizeSample(sample));
			// wait
			Delay.msDelay(100);
		}
		
		Delay.msDelay(2000);
		
		//Put anything COMPLETELY WHITE under the sensor
		System.out.println("Put white under sensor");
		while (enterKey.isUp()) {
			Float[] sample = colorSensorController.sample();
			System.out.println(Util.normalizeSample(sample));
			// wait
			Delay.msDelay(100);
		}
		
		//Lower arm for remainder of training because arm need to be consistent with actual run
		motorController.lowerArm();
		Delay.msDelay(2000);
		
		
		//Setting the threshold just like before
		Float[] sample = Trainer("background");
		billDetector.setThreshold(sample);
		Delay.msDelay(2000);
		
		
		// *************************************  un-comment if you want to try $10, $1 bill
		/*
		sample = Trainer("$10");
		billDetector.setTenBenchmark(sample);
		Delay.msDelay(2000);
		
		sample = Trainer("$1");
		billDetector.setOneBenchmark(sample);
		Delay.msDelay(2000);
		*/
		
		
		// *************************************  un-comment if you want to try monopoly money
		/*
		sample = Trainer("white monopoly");
		billDetector.setWhite(sample);
		Delay.msDelay(2000);
		
		sample = Trainer("red monopoly");
		billDetector.setRed(sample);
		Delay.msDelay(2000);
		
		sample = Trainer("blue monopoly");
		billDetector.setBlue(sample);
		Delay.msDelay(2000);
		
		sample = Trainer("green monopoly");
		billDetector.setGreen(sample);
		Delay.msDelay(2000);*/
		
		// *************************************  un-comment if you want to train $1, $10 bill samples
		/*
		//Only need to lower arm once, BillTrainer() won't raise it
		BillTrainer("$1");
		billDetector.copyBillSamplesToOneSamples();
		billDetector.reset();
		Delay.msDelay(2000);
		
		BillTrainer("$10");
		billDetector.copyBillSamplesToTenSamples();
		billDetector.reset();
		Delay.msDelay(2000);
		*/
		
		
		motorController.resetMotors();
		System.out.println("Training done!!");
	}
	
	private void waitForBillDetectorState(boolean isBillDetected) {

		while (billDetector.isBillDetected() != isBillDetected) {
			Float[] sample = colorSensorController.sample();
			billDetector.newSample(sample);

			Delay.msDelay(100);
		}
	}
	
	private void BillTrainer(String type){
		
		System.out.println("Put " + type +  " under sensor");
		// Wait for Bill Insertion
		waitForBillDetectorState(true);
		Delay.msDelay(250);
		System.out.println("detected bill:");
		motorController.startSpinning();
		Delay.msDelay(1000);
		waitForBillDetectorState(false);
		System.out.println("done detecting bill.");
		motorController.stopRoller();
	}
	
	private Float[] Trainer(String type){
		System.out.println("Put " + type +  " under sensor");
		Float[] sample = null;
		while (enterKey.isUp()) {
			sample = colorSensorController.sample();
			System.out.println(Util.normalizeSample(sample));
			// wait
			Delay.msDelay(100);
		}
		
		return sample;
	}
}
