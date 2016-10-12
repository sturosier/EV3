package com.r2jp2.colorsensor;

public class Util {

	public static Float normalizeSample(Float[] sample) {
		//return sample[0].isNaN() ? -1 : sample[0].floatValue();
		
		Float total = 0f;
		int count = 0;
		for(int i = 0; i < sample.length; i++){
			if(!sample[i].isNaN()){
				total += sample[i].floatValue();
				count++;
			}
		}
		
		if(count == 0)
			return Float.NaN;
		
		return total/count;
	}
}
