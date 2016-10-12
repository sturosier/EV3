package com.r2jp2.colorsensor;

public interface IDetector {
	public enum BillDetected {
		UNKNOWN, ONE, FIVE, TEN, TWENTY, MONOPOLY_WHITE, MONOPOLY_RED, MONOPOLY_BLUE, MONOPOLY_GREEN
	}

	boolean isBillDetected();

	void setThreshold(Float[] threshold);

	void newSample(Float[] sample);

	BillDetected recognizeBill();
	
	void reset();
	
	void setWhite(Float[] white);
	void setRed(Float[] red);
	void setBlue(Float[] blue);
	void setGreen(Float[] green);
}
