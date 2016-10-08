package com.r2jp2.worker;


public class MyColorFinder implements Runnable {

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	// private MyMotor motorThread = null;
	// private MyColorSensor colorSensorThread;
	// private boolean isRunning = true;
	// private boolean isFinished = false;
	// private int currentColor;
	//
	// private Map<Integer, Integer> colorToSound = new HashMap<Integer,
	// Integer>();
	//
	// public MyColorFinder(MyMotor motorThread, MyColorSensor
	// colorSensorThread) {
	// this.motorThread = motorThread;
	// this.colorSensorThread = colorSensorThread;
	//
	// colorToSound.put(Color.YELLOW, 1); // $1 bill
	// colorToSound.put(Color.RED, 2); // $5 bill
	// colorToSound.put(Color.BLUE, 3); // $10 bill
	// }
	//
	// @Override
	// public void run() {
	//
	// motorThread.addRequest(MotorRequest.FORWARD_SLOW);
	//
	// while (isRunning) {
	//
	// currentColor = colorSensorThread.getCurrentColor();
	//
	// isFinished = makeSound();
	//
	// if (isFinished) {
	// motorThread.addRequest(MotorRequest.STOP);
	// }
	//
	// try {
	// Thread.sleep(20);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// }
	// }
	//
	// /**
	// * Will return 'true' to stop the robot if a sound was made.
	// *
	// * @return
	// */
	// public boolean makeSound() {
	// if (currentColor == Color.YELLOW || currentColor == Color.RED
	// || currentColor == Color.BLUE) {
	// for (int i = 0; i < colorToSound.get(currentColor); i++) {
	//
	// Sound.beep();
	// }
	// return true;
	// }
	//
	// return false;
	// }
	//
	// public boolean isFinished() {
	// return isFinished;
	// }

}
