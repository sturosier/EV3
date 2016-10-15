package com.r2jp2.motor;


public class VoicePlayer implements Runnable {

	private int COLOR;

	public VoicePlayer(int color) {
		this.COLOR = color;
	}

	public void run() {
		BillDetectorVoice voice = new BillDetectorVoice();
		voice.sortBill(COLOR);
	}

}
