package com.r2jp2.colorsensor;

public class BillDetector extends BaseDetector implements IDetector {

	public BillDetector() {

	}

	@Override
	protected boolean detectBill() {
		int sampleSize = 0;
		int total = 0;
		double overallAvg = 0;
		if (latestSamples.size() > 21) {
			double sampleAvg = 0;
			while (sampleSize < 20) {
				Float[] sample = latestSamples.get(latestSamples.size()
						- sampleSize - 1);
				
				for (Float f : sample) {
					if (!f.isNaN() && f.doubleValue() != 0.0 && f.doubleValue() != 1.0) {
						total++;
						sampleAvg += f.doubleValue();
						//System.out.println(f.doubleValue());
					}

				}
				
				//sampleAvg /= total;
				//overallAvg += sampleAvg;
				sampleSize++;
			}
			
			if(total == 0)
				return false;
			
			sampleAvg /= total;
			System.out.println("avg: " + sampleAvg);
			return sampleAvg > 0.30;
		} else
			return false;
	}

}
