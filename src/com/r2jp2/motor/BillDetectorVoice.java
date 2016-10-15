package com.r2jp2.motor;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import lejos.hardware.Audio;
import lejos.hardware.Sound;
import lejos.robotics.Color;

public class BillDetectorVoice {

	public static final String ONE_DOLLAR_FILE = "one.wav";
	public static final String FIVE_DOLLAR_FILE = "five.wav";
	public static final String TEN_DOLLAR_FILE = "ten.wav";
	public static final String TWENTY_DOLLAR_FILE = "twenty.wav";
	public static final String DETECTED = "bill_detected.wav";
	public static final String RETURNED = "bill_returned.wav";

	private Map<Integer, String> colorToSound = new HashMap<Integer, String>();
	private Map<Integer, Integer> colorToBeep = new HashMap<Integer, Integer>();

	public BillDetectorVoice() {

		init();
	}

	private void init() {

		colorToSound.put(Color.WHITE, ONE_DOLLAR_FILE);
		colorToSound.put(Color.PINK, FIVE_DOLLAR_FILE);
		colorToSound.put(Color.YELLOW, TEN_DOLLAR_FILE);
		colorToSound.put(Color.GREEN, TWENTY_DOLLAR_FILE);

		colorToBeep.put(Color.WHITE, 1); // one dollar
		colorToBeep.put(Color.PINK, 2); // five dollar
		colorToBeep.put(Color.YELLOW, 3); // ten dollar
		colorToBeep.put(Color.GREEN, 4); // twenty dollar
	}

	public void sortBill(int color) {
		String fileName = colorToSound.get(color);

		if (fileName == null) {

			System.out.println("did not find wav file.");
			return;
		}

		play(fileName);
	}

	public boolean playBeep(int color) throws InterruptedException {
		if (colorToBeep.get(color) == null) {
			return false;
		}

		int numberOfBeeps = colorToBeep.get(color);

		for (int i = 0; i < numberOfBeeps; i++) {
			Sound.beep();
			Thread.sleep(1000);
		}

		return true;
	}

	private void play(String voiceFile) {
		// www.online-convert.com (convert to wav)
		// ttsreader.com (voice recorder)
		File myFile = new File(voiceFile);

		System.out.println("About to play for: "
				+ Sound.playSample(myFile, 100));
	}
}
