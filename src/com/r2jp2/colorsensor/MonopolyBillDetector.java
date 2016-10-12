package com.r2jp2.colorsensor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MonopolyBillDetector extends BaseDetector implements IDetector {

	// private final int MOVING_AVG_SIZE = 10;
	private final int SAMPLE_SIZE = 10;

	public MonopolyBillDetector() {

	}

	@Override
	protected boolean detectBill() {

		// accumulate a history of sample first
		if (latestSamples.size() < SAMPLE_SIZE)
			return false; // intial state is NO BILL DETECTED

		int count = 1;
		int backgroundSampleCount = 0; // the number of samples detecting
		// background
		int billSampleCount = 0; // the number of samples detecting presence of
		// a bill

		// read the LATEST number of samples (at the end of latestSamples list)
		while (count <= SAMPLE_SIZE) {
			float sample = Util.normalizeSample(latestSamples.get(latestSamples
					.size() - count));

			// Our background is dark gray so sample is either a big number or a
			// small number
			// Anything in between most likely indicate presence of a bill

			if (sample < Util.normalizeSample(this.threshold)*1.3 || sample > 0.92) {
				backgroundSampleCount++;
			} else {
				billSampleCount++;
			}

			//System.out.println(sample);

			count++;
		}

		if (billSampleCount > backgroundSampleCount)
			return true;
		else
			return false;

	}


	@Override
	public BillDetected recognizeBill() {
		
		int count = 0;
		float totalSample = 0;

		//calculate the avg value of the samples
		for(Float[] sample : billSamples){

			Float normalized = Util.normalizeSample(sample);
			if(!normalized.isNaN()){
				totalSample += normalized.floatValue();
				count++;
			}
		}

		Float avgValue = 0f;
		if(count > 0)
			avgValue = totalSample/count;
		

		Float white = Float.NaN;
		if(this.white != null)
			white = Util.normalizeSample(this.white);
		
		Float red = Float.NaN;
		if(this.red != null)
			red = Util.normalizeSample(this.red);
		
		Float blue = Float.NaN;
		if(this.blue != null)
			blue = Util.normalizeSample(this.blue);
		
		Float green = Float.NaN;
		if(this.green != null)
			green = Util.normalizeSample(this.green);
		
		System.out.println("white:" + white);
		System.out.println("red:" + red);
		System.out.println("blue:" + blue);
		System.out.println("green:" + green);
		System.out.println("avgValue:" + avgValue);
		
		//Diff the avgValue with each of our pre-set white, red, blue, green
		List<MoneyMapper> sortedList = new ArrayList<MoneyMapper>();
		sortedList.add(new MoneyMapper(BillDetected.MONOPOLY_WHITE, Math.abs(avgValue - white)));
		sortedList.add(new MoneyMapper(BillDetected.MONOPOLY_RED, Math.abs(avgValue - red)));
		sortedList.add(new MoneyMapper(BillDetected.MONOPOLY_BLUE, Math.abs(avgValue - blue)));
		sortedList.add(new MoneyMapper(BillDetected.MONOPOLY_GREEN, Math.abs(avgValue - green)));
		
		
		//Sort it ASC
		Collections.sort(sortedList, new Comparator<MoneyMapper>() {

			@Override
			public int compare(MoneyMapper o1, MoneyMapper o2) {
				return Float.compare(o1.diff, o2.diff);
			}
		});
		
		//The one with smallest difference should be our bill
		return sortedList.get(0).bill;
	}
	
	private static class MoneyMapper{
		private BillDetected bill;
		private Float diff;
		
		public MoneyMapper(BillDetected bill, Float diff){
			this.bill = bill;
			this.diff= diff;
		}
	}
}
