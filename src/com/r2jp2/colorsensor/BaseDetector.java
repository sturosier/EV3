package com.r2jp2.colorsensor;

import java.util.ArrayList;
import java.util.List;

public class BaseDetector implements IDetector {

	protected boolean isBillDetected = false;
	//protected BillDetected billDetected = BillDetected.UNKNOWN;
	protected double threshold;
	protected double candidateThreshold;
	private final int MAX_SAMPLE_SIZE = 100;
	protected List<Float[]> latestSamples = new ArrayList<Float[]>(); // Oldest sample at beginning of list
	protected List<Float[]> billSamples = new ArrayList<Float[]>(); //Contain samples during the presence of a bill under the sensor

	@Override
	public void newSample(Float[] sample) {
		if (latestSamples.size() == MAX_SAMPLE_SIZE)
			latestSamples.remove(0);

		latestSamples.add(sample);
		isBillDetected = detectBill();
		if(isBillDetected)
			billSamples.add(sample);
	}

	// need to be overwritten
	protected boolean detectBill() {
		return false;
	}


	@Override
	public boolean isBillDetected() {
		return isBillDetected;
	}

	// need to be overwritten
	public BillDetected recognizeBill() {
		return BillDetected.UNKNOWN;
	}

	@Override
	public void setThreshold() {
		this.threshold = this.candidateThreshold;
	}
	
	@Override
	public void reset(){
		latestSamples.clear();
		billSamples.clear();
		isBillDetected = false;
	}

}
