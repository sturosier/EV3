package com.r2jp2.colorsensor;

public interface IDetector {
	public enum BillDetected {
		UNKNOWN, ONE, FIVE, TEN, TWENTY
	}

	boolean isBillDetected();

	void setThreshold();

	void newSample(Float[] sample);

	BillDetected recognizeBill();
}
