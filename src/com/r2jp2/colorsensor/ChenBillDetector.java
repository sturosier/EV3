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

			if (sample < threshold || sample > 0.92) {
				backgroundSampleCount++;
			} else {
				billSampleCount++;
				candidateThreshold = sample;
			}

			System.out.println(sample);

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
		return sample[0].isNaN() ? -1 : sample[0].floatValue();
	}

	private boolean compareSample(Float s1, Float s2){
		Float diff = Math.abs(s1 - s2);
		if(diff < 0.0001)  //Allow some error, if difference is less than arbitrary 0.0001, then samples are the same
			return true;
		
		return false;
	}
	
	@Override
	public BillDetected recognizeBill() {
		Map<Float, Integer> countMap = new HashMap<Float, Integer>();
		List<Wrapper> sortedList = new ArrayList<Wrapper>();
		int total = 0;
		
		//count the occurrence of unique samples
		for(Float[] sample : billSamples){
			Float key = normalizeSample(sample);
			if(countMap.containsKey(key))
				countMap.put(key, countMap.get(key) + 1);
			else
				countMap.put(key, 0);
			total++;
		}
		
		for(Float key : countMap.keySet()){
			sortedList.add(new Wrapper(key, countMap.get(key)));
		}
		
		//sort it to find samples that happen most frequently
		Collections.sort(sortedList, new Comparator<Wrapper>() {

			@Override
			public int compare(Wrapper o1, Wrapper o2) {
				return Integer.compare(o2.count, o1.count);
			}
			
		});
		
		System.out.println("total samples:" + total);
		System.out.println("unique samples:" + countMap.size());
		
		//compare the top 3 most frequent samples with our hardcoded spikes, if there is a match, we found our bill
		int count = 0;
		while(count < sortedList.size() && count < 3){
			for(int i = 0; i < tenBillSpikes.length; i++){
				if(compareSample(tenBillSpikes[i], sortedList.get(count).key))
					return BillDetected.TEN;
			}
			
			for(int i = 0; i < oneBillSpikes.length; i++){
				if(compareSample(oneBillSpikes[i], sortedList.get(count).key))
					return BillDetected.ONE;
			}
			
			count++;
		}
		
		return BillDetected.UNKNOWN;
	}

	private static class Wrapper{
		
		public Wrapper(Float key, int count){
			this.key = key;
			this.count = count;
		}
		
		public Float key;
		public int count;
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
