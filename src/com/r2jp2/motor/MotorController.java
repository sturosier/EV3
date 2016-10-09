package com.r2jp2.motor;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class MotorController {
	private RegulatedMotor armMotor;
	private RegulatedMotor rollerMotor;
	
	private final int ARM_MOTOR_UP_ANGLE = -30; //angle at which the wheel is up
	private final int ARM_MOTOR_DOWN_ANGLE = 30; //angle at which the wheel is down

	
	public MotorController(Port armMotorPort, Port rollerMotorPort){
		armMotor = new EV3LargeRegulatedMotor(armMotorPort);
		rollerMotor = new EV3LargeRegulatedMotor(rollerMotorPort);
		rollerMotor.setSpeed((int)Math.floor(armMotor.getMaxSpeed()*0.1));
		armMotor.setSpeed((int)Math.floor(armMotor.getMaxSpeed()*0.1));
	}
	
	//rollerMotor stop spinning, armMotor needs to be up
	public void resetMotors(){
		armMotor.rotateTo(ARM_MOTOR_UP_ANGLE, true);
		rollerMotor.stop();
	}

	public void stopRoller(){
		rollerMotor.stop();
	}

	
	public void lowerArm(){
		armMotor.rotateTo(ARM_MOTOR_DOWN_ANGLE, true);
	}
	public void raiseArm(){
		armMotor.rotateTo(ARM_MOTOR_UP_ANGLE, true);
	}
	
	public void startSpinning(){
		rollerMotor.forward();
	}
	
	public void startSpinningBackward(){
		rollerMotor.backward();
	}
	
	public void destroy(){
		armMotor.close();
		rollerMotor.close();
	}
	
}
