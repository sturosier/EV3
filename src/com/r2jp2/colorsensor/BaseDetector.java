package com.r2jp2.colorsensor;

import java.util.ArrayList;
import java.util.List;

public class BaseDetector implements IDetector {

	protected boolean isBillDetected = false;
	//protected BillDetected billDetected = BillDetected.UNKNOWN;
	protected Float[] threshold;
	
	//FOR MONOPOLOY MONEY
	protected Float[] white;
	protected Float[] red;
	protected Float[] blue;
	protected Float[] green;
	
	private final int MAX_SAMPLE_SIZE = 100;
	protected List<Float[]> latestSamples = new ArrayList<Float[]>(); // Oldest sample at beginning of list
	protected List<Float[]> billSamples = new ArrayList<Float[]>(); //Contain samples during the presence of a bill under the sensor

	@Override
	public void newSample(Float[] sample) {
		if (latestSamples.size() == MAX_SAMPLE_SIZE)
			latestSamples.remove(0);

		System.out.println("r:" + sample[0].floatValue() + " g:" + sample[1].floatValue() + " b:" + sample[2].floatValue());
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
	public void setThreshold(Float[] threshold) {
		this.threshold = threshold;
	}
	
	@Override
	public void setWhite(Float[] white) {
		this.white = white;
	}
	
	@Override
	public void setRed(Float[] red) {
		this.red = red;
	}
	
	@Override
	public void setBlue(Float[] blue) {
		this.blue = blue;
	}
	
	@Override
	public void setGreen(Float[] green) {
		this.green = green;
	}
	
	@Override
	public void reset(){
		latestSamples.clear();
		billSamples.clear();
		isBillDetected = false;
	}

}
