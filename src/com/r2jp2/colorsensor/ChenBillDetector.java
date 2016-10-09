package com.r2jp2.colorsensor;

import com.r2jp2.colorsensor.IDetector.BillDetected;

public class ChenBillDetector extends BaseDetector implements IDetector{
	//private final int MOVING_AVG_SIZE = 10;
	private final int SAMPLE_SIZE = 10;
	
	public ChenBillDetector(){
		
	}
	
	@Override
	protected boolean detectBill(){
		
		//accumulate a history of sample first
		if(latestSamples.size() < SAMPLE_SIZE)
			return false; //intial state is NO BILL DETECTED
		
		int count = 1;
		int backgroundSampleCount = 0; //the number of samples detecting background
		int billSampleCount = 0; //the number of samples detecing presence of a bill
		
		//read the LATEST number of samples (at the end of latestSamples list)
		while(count <= SAMPLE_SIZE){
			float sample = normalizeSample(latestSamples.get(latestSamples.size() - count));
			
			//Our background is dark gray so sample is either a big number or a small number
			//Anything in between most likely indicate presence of
			if(sample < 0.12 || sample > 0.92)
				backgroundSampleCount++;
			else
				billSampleCount++;
			
			count++;
		}
		
		if(billSampleCount > backgroundSampleCount)
			return true;
		else
			return false;
		
	}
	
	//sample should only have one value so just returning that, this may change if we change sensor type
	private float normalizeSample(Float[] sample){
		return sample[0].isNaN()?-1:sample[0].floatValue();
	}
	
	@Override
	public BillDetected recognizeBill() {
		return BillDetected.UNKNOWN;
	}
	
	/*
	if(latestSamples.size() < MOVING_AVG_SIZE)
		return false;
	else{
		//calculate moving average
		float[] buffer = new float[MOVING_AVG_SIZE];
		List<Float> maList = new ArrayList<Float>();
		int bufferIndex = 0;
		for(int i = 0; i < latestSamples.size(); i++){
			buffer[bufferIndex] = normalizeSample(latestSamples.get(i))/MOVING_AVG_SIZE;
			float ma = 0;
			for(int j = 0; j < buffer.length; j++){
				ma += buffer[j];
			}
			
			if(i > MOVING_AVG_SIZE)
				maList.add(ma);
			bufferIndex = (bufferIndex + 1) % MOVING_AVG_SIZE;
		}
		
		for(int i = 0; i < maList.size(); i++){
			System.out.println(maList.get(i));
		}
		
		System.out.println("done");
		return false;
	}*/
}
