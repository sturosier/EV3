package com.r2jp2.motor;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.naming.InitialContext;

import lejos.hardware.Sound;
import lejos.robotics.Color;

public class BillDetectorVoice {
	
	public static final String ONE_DOLLAR_FILE = "one.wav";
	public static final String FIVE_DOLLAR_FILE = "five.wav";
	public static final String TEN_DOLLAR_FILE = "ten.wav";
	public static final String TWENTY_DOLLAR_FILE = "twenty.wav";
	
	private Map<Integer, String> colorToSound = new HashMap<Integer, String>();
	
	 public BillDetectorVoice(){
	   
	   init();
     }
	 
	 private void init()
	 {
		 colorToSound.put(Color.WHITE, ONE_DOLLAR_FILE);
		 colorToSound.put(Color.PINK, FIVE_DOLLAR_FILE);
		 colorToSound.put(Color.YELLOW, TEN_DOLLAR_FILE);
		 colorToSound.put(Color.GREEN, TWENTY_DOLLAR_FILE);

	 }
	 
	 public void sortBill(int color)
	 {
		String fileName = colorToSound.get(color); 
		
		if(fileName == null)
		{
			return;
		}
		
		play(fileName);
	 }
	 
	 private void play(String voiceFile)
	 {
		 //www.online-convert.com (convert to wav)
		 //ttsreader.com (voice recorder)
		 File myFile = new File(voiceFile);
	     Sound.playSample(myFile); 
	 }
}
