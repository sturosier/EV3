package com.r2jp2.colorsensor;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;

public class MyColorSensor implements Runnable {

	private EV3ColorSensor sensor;

	private boolean isRunning = true;

	private int currentColor = 0;

	public MyColorSensor(Port port) {
		sensor = new EV3ColorSensor(port);
	}

	@Override
	public void run() {
		while (isRunning) {
			
			currentColor = sensor.getColorID();
			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public int getCurrentColor() {
		return currentColor;
	}

	public void stop() {
		sensor.close();
	}
}
