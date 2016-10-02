package com.r2jp2.motor;

import java.util.LinkedList;
import java.util.Queue;

import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.robotics.RegulatedMotor;

public class MyMotor implements Runnable {
	private RegulatedMotor m1;
	private RegulatedMotor m2;

	private Queue<MotorRequest> requests = new LinkedList<>();

	private boolean isRunning = true;

	private static final int ROTATE_SLOW = 50;
	private static final int ROTATE_MEDIUM = 200;
	private static final int ROTATE_FAST = 500;

	private MotorRequest currentRequest = MotorRequest.STOP;
	
	public enum MotorRequest {
		FORWARD_FAST, FORWARD_MEDIUM, FORWARD_SLOW,
		FORWARD_ROTATE_RIGHT, FORWARD_ROTATE_LEFT, 
		STOP, ROTATE_SPOT_LEFT, ROTATE_SPOT_RIGHT
	}

	public MyMotor(Port engine1, Port engine2) {
		m1 = new EV3LargeRegulatedMotor(engine1);
		m2 = new EV3LargeRegulatedMotor(engine2);
	}

	@Override
	public void run() {
		
		while (isRunning) {
			
			if (!requests.isEmpty()) {
				
				MotorRequest request = requests.poll();
				LCD.clearDisplay();
				LCD.drawString("Request " + request, 0, 0);
				
				if (request == MotorRequest.FORWARD_SLOW) {
					
					m1.setSpeed(ROTATE_SLOW);
					m2.setSpeed(ROTATE_SLOW);
					m1.forward();
					m2.forward();
				} 
				else if (request == MotorRequest.FORWARD_MEDIUM) {
					m1.setSpeed(ROTATE_MEDIUM);
					m2.setSpeed(ROTATE_MEDIUM);
					m1.forward();
					m2.forward();
				} 
				else if (request == MotorRequest.FORWARD_FAST) {
					m1.setSpeed(ROTATE_FAST);
					m2.setSpeed(ROTATE_FAST);
					m1.forward();
					m2.forward();
				} else if (request == MotorRequest.FORWARD_ROTATE_RIGHT) {
					m1.rotate(ROTATE_MEDIUM);
					m2.rotate(ROTATE_SLOW);
					m1.forward();
					m2.forward();
				} else if (request == MotorRequest.FORWARD_ROTATE_LEFT) {
					m1.rotate(ROTATE_SLOW);
					m2.rotate(ROTATE_MEDIUM);
					m1.forward();
					m2.forward();
				} 
				else if (request == MotorRequest.STOP) {
					m1.stop(true);
					m2.stop();
				} 
				else if (request == MotorRequest.ROTATE_SPOT_RIGHT) {
					m1.rotate(ROTATE_SLOW);
					m2.rotate(ROTATE_SLOW);
					m1.forward();
					m2.backward();
				} 
				else if (request == MotorRequest.ROTATE_SPOT_LEFT) {
					m1.rotate(ROTATE_SLOW);
					m2.rotate(ROTATE_SLOW);
					m1.backward();
					m2.forward();
				}
				currentRequest = request;
			}
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void addRequest(MotorRequest request) {
		requests.add(request);
	}

	public void stop() {
		isRunning = false;
		m1.close();
		m2.close();
	}

	public boolean isStopped() {
		return currentRequest == MotorRequest.STOP;
	}	
	
}
