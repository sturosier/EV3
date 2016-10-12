package com.r2jp2.colorsensor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ChenBillDetector extends BaseDetector implements IDetector {
	// private final int MOVING_AVG_SIZE = 10;
	private final int SAMPLE_SIZE = 10;

	public ChenBillDetector() {

	}

	float[] tenBillSpikes = new float[]{0.18518516f, 0.27659574f, 0.5185185f};
	float[] oneBillSpikes = new float[]{0.37837836f,  0.2972973f, 0.13513513f};
	
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
			float sample = normalizeSample(latestSamples.get(latestSamples
					.size() - count));

			// Our background is dark gray so sample is either a big number or a
			// small number
			// Anything in between most likely indicate presence of a bill

			if (sample < threshold*1.3 || sample > 0.92) {
				backgroundSampleCount++;
			} else {
				billSampleCount++;
				candidateThreshold = sample;
			}

			//System.out.println(sample);

			count++;
		}

		if (billSampleCount > backgroundSampleCount)
			return true;
		else
			return false;
		
	}

	// sample should only have one value so just returning that, this may change
	// if we change sensor type
	private float normalizeSample(Float[] sample) {
		//return sample[0].isNaN() ? -1 : sample[0].floatValue();
		float total = 0;
		int count = 0;
		for(int i = 0; i < sample.length; i++){
			if(!sample[i].isNaN()){
				total += sample[i].floatValue();
				count++;
			}
		}
		
		return total/count;
	}

	private boolean compareSample(Float s1, Float s2){
		Float diff = Math.abs(s1 - s2);
		if(diff < 0.0001)  //Allow some error, if difference is less than arbitrary 0.0001, then samples are the same
			return true;
		
		return false;
	}
	
	@Override
	public BillDetected recognizeBill() {
		List<Float> diffs = new ArrayList<Float>();
		int total = 0;
		
		Float prev = Float.NaN;
		//calculate the diffs
		for(Float[] sample : billSamples){
			System.out.println("bill sample-r:" + sample[0].floatValue() + " g:" + sample[1].floatValue() + " b:" + sample[2].floatValue());
			Float current = normalizeSample(sample);
			//System.out.println("bill sample:" + current);
			
			if(!prev.isNaN()){
				diffs.add(Math.abs(current - prev));
			}
			
			prev = current;
			total++;
		}
		
		Float avgDiff = 0f;
		Float totalSample = 0f;
		for(Float d : diffs){
			totalSample += d;
		}
		
		avgDiff = totalSample/diffs.size();
		System.out.println("avgDiff:" + avgDiff);
		
		if(avgDiff > 0.072){
			//FACE UP SIDE
			if(avgDiff > 0.082f)
				return BillDetected.TEN;
			else
				return BillDetected.ONE;
		}
		else{
			
			//FACE DOWN SIDE
			if(avgDiff > 0.063f)
				return BillDetected.TEN;
			else
				return BillDetected.ONE;
		}
		
		//return BillDetected.UNKNOWN;
	}


	/*
	 * if(latestSamples.size() < MOVING_AVG_SIZE) return false; else{
	 * //calculate moving average float[] buffer = new float[MOVING_AVG_SIZE];
	 * List<Float> maList = new ArrayList<Float>(); int bufferIndex = 0; for(int
	 * i = 0; i < latestSamples.size(); i++){ buffer[bufferIndex] =
	 * normalizeSample(latestSamples.get(i))/MOVING_AVG_SIZE; float ma = 0;
	 * for(int j = 0; j < buffer.length; j++){ ma += buffer[j]; }
	 * 
	 * if(i > MOVING_AVG_SIZE) maList.add(ma); bufferIndex = (bufferIndex + 1) %
	 * MOVING_AVG_SIZE; }
	 * 
	 * for(int i = 0; i < maList.size(); i++){
	 * System.out.println(maList.get(i)); }
	 * 
	 * System.out.println("done"); return false; }
	 */
}
