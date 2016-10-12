package com.r2jp2.colorsensor;

public class Util {

	public static float normalizeSample(Float[] sample) {
		//return sample[0].isNaN() ? -1 : sample[0].floatValue();
		
		Float total = 0f;
		int count = 0;
		for(int i = 0; i < sample.length; i++){
			if(!sample[i].isNaN()){
				total += sample[i].floatValue();
				count++;
			}
		}
		
		return total/count;
	}
}
