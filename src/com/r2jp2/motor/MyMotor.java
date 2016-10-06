package com.r2jp2.motor;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.robotics.RegulatedMotor;

import com.r2jp2.colorsensor.MotorRequest;

public class MyMotor {
	private RegulatedMotor m1;
	private RegulatedMotor m2;

	private static final int ROTATE_SLOW = 50;
	private static final int ROTATE_MEDIUM = 200;
	private static final int ROTATE_FAST = 500;

	private MotorRequest currentRequest = MotorRequest.STOP;

	public MyMotor(Port engine1, Port engine2) {
		m1 = new EV3LargeRegulatedMotor(engine1);
		m2 = new EV3LargeRegulatedMotor(engine2);
	}

	public void addRequest(MotorRequest request) {
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
	}

	public void stop() {

		m1.close();
		m2.close();
	}

	public boolean isStopped() {
		return currentRequest == MotorRequest.STOP;
	}

}
