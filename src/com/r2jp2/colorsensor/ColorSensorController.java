package com.r2jp2.colorsensor;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.UARTSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.AbstractFilter;

public class ColorSensorController {

	private EV3ColorSensor sensor;
	private SampleProvider reflectedLight;
	private int sampleSize;
	Port port;

	public ColorSensorController(Port p) {
		this.sensor = new EV3ColorSensor(p);
		this.port = p;
		/*
		 * Get the Red mode of the sensor.
		 * 
		 * In Red mode the sensor emmits red light and measures reflectivity of
		 * a surface. Red mode is the best mode for line following.
		 * 
		 * Alternatives to the method below are: sensor.getMode(1) or
		 * sensor.getRedMode()
		 */
		SampleProvider redMode = sensor.getRedMode();

		/*
		 * Use a filter on the sample. The filter needs a source (a sensor or
		 * another filter) for the sample. The source is provided in the
		 * constructor of the filter
		 */
		reflectedLight = new autoAdjustFilter(redMode);

		/*
		 * Create an array of floats to hold the sample returned by the
		 * sensor/filter
		 * 
		 * A sample represents a single measurement by the sensor. Some modes
		 * return multiple values in one measurement, hence the array
		 */
		sampleSize = reflectedLight.sampleSize();
	}

	public Float[] sample() {

		float[] sample = new float[sampleSize];
		reflectedLight.fetchSample(sample, 0);

		Float[] floatObjSample = new Float[sampleSize];
		for (int i = 0; i < sample.length; i++) {
			floatObjSample[i] = sample[i];
		}

		return floatObjSample;
	}



	public void destroy() {
		sensor.close();
	}

	/**
	 * This filter dynamicaly adjust the samples value to a range of 0-1. The
	 * purpose of this filter is to autocalibrate a light Sensor to return
	 * values between 0 and 1 no matter what the light conditions. Once the
	 * light sensor has "seen" both white and black it is calibrated and ready
	 * for use.
	 * 
	 * The filter could be used in a line following robot. The robot could
	 * rotate to calibrate the sensor.
	 * 
	 * @author Aswin
	 * 
	 */
	public class autoAdjustFilter extends AbstractFilter {
		/*
		 * These arrays hold the smallest and biggest values that have been
		 * "seen:
		 */
		private float[] minimum;
		private float[] maximum;

		public autoAdjustFilter(SampleProvider source) {
			super(source);
			/*
			 * Now the source and sampleSize are known. The arrays can be
			 * initialized
			 */
			minimum = new float[sampleSize];
			maximum = new float[sampleSize];
			reset();
		}

		public void reset() {
			/* Set the arrays to their initial value */
			for (int i = 0; i < sampleSize; i++) {
				minimum[i] = Float.POSITIVE_INFINITY;
				maximum[i] = Float.NEGATIVE_INFINITY;
			}
		}

		/*
		 * To create a filter one overwrites the fetchSample method. A sample
		 * must first be fetched from the source (a sensor or other filter).
		 * Then it is processed according to the function of the filter
		 */
		public void fetchSample(float[] sample, int offset) {
			super.fetchSample(sample, offset);
			for (int i = 0; i < sampleSize; i++) {
				if (minimum[i] > sample[offset + i])
					minimum[i] = sample[offset + i];
				if (maximum[i] < sample[offset + i])
					maximum[i] = sample[offset + i];
				sample[offset + i] = (sample[offset + i] - minimum[i])
						/ (maximum[i] - minimum[i]);
			}
		}

	}
}
