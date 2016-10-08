package com.r2jp2.colorsensor;

import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.Color;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.AbstractFilter;
import lejos.utility.Delay;

import com.r2jp2.motor.MyMotor;

//public class MyColorSensor implements Runnable {
//
//	private EV3ColorSensor sensor;
//
//	private boolean isRunning = true;
//
//	private int currentColor = 0;
//
//	public MyColorSensor(Port port) {
//
//		sensor = new EV3ColorSensor(port);
//	}
//
//	@Override
//	public void run() {
//		MyMotor motor = new MyMotor(MotorPort.B, MotorPort.C);
//		/*
//		 * Get the Red mode of the sensor.
//		 * 
//		 * In Red mode the sensor emmits red light and measures reflectivity of
//		 * a surface. Red mode is the best mode for line following.
//		 * 
//		 * Alternatives to the method below are: sensor.getMode(1) or
//		 * sensor.getRedMode()
//		 */
//
//		SampleProvider colorMode = sensor.getColorIDMode();// sensor.getRedMode();
//
//		// sensor.setFloodlight(Color.RED);
//
//		/*
//		 * Use a filter on the sample. The filter needs a source (a sensor or
//		 * another filter) for the sample. The source is provided in the
//		 * constructor of the filter
//		 */
//		SampleProvider reflectedLight = new autoAdjustFilter(colorMode);
//
//		/*
//		 * Create an array of floats to hold the sample returned by the
//		 * sensor/filter
//		 * 
//		 * A sample represents a single measurement by the sensor. Some modes
//		 * return multiple values in one measurement, hence the array
//		 */
//		int sampleSize = reflectedLight.sampleSize();
//		float[] sample = new float[sampleSize];
//		// sensor.setFloodlight(true);
//		
//		BillDetectorVoice detector = new BillDetector();
//		while (isRunning) {
//			// sensor.setFloodlight(Color.RED);
//			/*
//			 * Get a fresh sample from the filter. (The filter gets it from its
//			 * source, the redMode)
//			 */
//			try {
//				motor.addRequest(MotorRequest.FORWARD_MEDIUM);
//				
//				reflectedLight.fetchSample(sample, 0);
//				/* Display individual values in the sample. */
//				for (int i = 0; i < sampleSize; i++) {
//					// System.out.print(sample[i] + " ");
//				}
//				System.out.println();
//
//				currentColor = sensor.getColorID();
//				System.out.println("Color: " + currentColor);
//                
//				detector.sortBill(currentColor);
//
//				Delay.msDelay(500);
//			} catch (IndexOutOfBoundsException e) {
//				System.out.println("error reading value");
//
//			}
//
//		}
//	}
//
//	public int getCurrentColor() {
//		return currentColor;
//	}
//
//	public void stop() {
//		sensor.close();
//	}
//
//	/**
//	 * This filter dynamicaly adjust the samples value to a range of 0-1. The
//	 * purpose of this filter is to autocalibrate a light Sensor to return
//	 * values between 0 and 1 no matter what the light conditions. Once the
//	 * light sensor has "seen" both white and black it is calibrated and ready
//	 * for use.
//	 * 
//	 * The filter could be used in a line following robot. The robot could
//	 * rotate to calibrate the sensor.
//	 * 
//	 * @author Aswin
//	 * 
//	 */
//	public class autoAdjustFilter extends AbstractFilter {
//		/*
//		 * These arrays hold the smallest and biggest values that have been
//		 * "seen:
//		 */
//		private float[] minimum;
//		private float[] maximum;
//
//		public autoAdjustFilter(SampleProvider source) {
//			super(source);
//			/*
//			 * Now the source and sampleSize are known. The arrays can be
//			 * initialized
//			 */
//			minimum = new float[sampleSize];
//			maximum = new float[sampleSize];
//			reset();
//		}
//
//		public void reset() {
//			/* Set the arrays to their initial value */
//			for (int i = 0; i < sampleSize; i++) {
//				minimum[i] = Float.POSITIVE_INFINITY;
//				maximum[i] = Float.NEGATIVE_INFINITY;
//			}
//		}
//
//		/*
//		 * To create a filter one overwrites the fetchSample method. A sample
//		 * must first be fetched from the source (a sensor or other filter).
//		 * Then it is processed according to the function of the filter
//		 */
//		public void fetchSample(float[] sample, int offset) {
//			super.fetchSample(sample, offset);
//			for (int i = 0; i < sampleSize; i++) {
//				if (minimum[i] > sample[offset + i])
//					minimum[i] = sample[offset + i];
//				if (maximum[i] < sample[offset + i])
//					maximum[i] = sample[offset + i];
//				sample[offset + i] = (sample[offset + i] - minimum[i])
//						/ (maximum[i] - minimum[i]);
//			}
//		}
//
//	}
//}
