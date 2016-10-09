package com.r2jp2.colorsensor;

import java.util.ArrayList;
import java.util.List;

public class BaseDetector implements IDetector{

	protected boolean isBillDetected = false;
	protected BillDetected billDetected = BillDetected.UNKNOWN;
	
	private final int MAX_SAMPLE_SIZE = 500;
	protected List<Float[]> latestSamples = new ArrayList<Float[]>(); //Oldest sample at beginning of list
	

	@Override
	public void newSample(Float[] sample) {
		if(latestSamples.size() == MAX_SAMPLE_SIZE)
			latestSamples.remove(0);
		
		latestSamples.add(sample);
		isBillDetected = detectBill();
	}

	//need to be overwritten
	protected boolean detectBill(){
		return false;
	}
	
	@Override
	public boolean isBillDetected(){
		return isBillDetected;
	}
	
	
	//need to be overwritten
	@Override
	public BillDetected recognizeBill() {
		return BillDetected.UNKNOWN;
	}



}
