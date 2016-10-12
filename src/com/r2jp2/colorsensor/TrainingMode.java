package com.r2jp2.colorsensor;

import lejos.hardware.Key;
import lejos.utility.Delay;

public class TrainingMode {

	private ColorSensorController colorSensorController;
	private Key enterKey;
	private IDetector billDetector;
	public TrainingMode(ColorSensorController colorSensorController, Key enterKey, IDetector billDetector){
		this.colorSensorController = colorSensorController;
		this.enterKey = enterKey;
		this.billDetector = billDetector;
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
		
		Delay.msDelay(2000);
		
		
		//Setting the threshold just like before
		Float[] sample = Trainer("background");
		billDetector.setThreshold(sample);
		Delay.msDelay(2000);
		
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
		
		
		System.out.println("Training done!!");
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
