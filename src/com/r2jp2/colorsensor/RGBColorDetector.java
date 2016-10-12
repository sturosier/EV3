package com.r2jp2.colorsensor;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.r2jp2.colorsensor.IDetector.BillDetected;


public class RGBColorDetector extends BaseDetector implements IDetector {

	private final int SAMPLE_SIZE = 10;

	public RGBColorDetector() {

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

	private double getColorDistance(Color c1, Color c2)
	{
	    double rmean = ( c1.getRed() + c2.getRed() )/2;
	    int r = c1.getRed() - c2.getRed();
	    int g = c1.getGreen() - c2.getGreen();
	    int b = c1.getBlue() - c2.getBlue();
	    double weightR = 2 + rmean/256;
	    double weightG = 4.0;
	    double weightB = 2 + (255-rmean)/256;
	    return Math.sqrt(weightR*r*r + weightG*g*g + weightB*b*b);
	} 
	
	private Double calculateAvgColorDistanceBetweenEverySampleAndReferenceColor(Float[] reference){
		//List<Double> distanceList = new ArrayList<Double>();
		Color refColor = new Color(reference[0], reference[1], reference[2]);
		
		Double totalDistance = 0.0;
		int count = 0;
		
		for(Float[] sample : billSamples){
			
			if(sample[0].isNaN() || sample[1].isNaN() || sample[2].isNaN())
				continue;
			
			Color sampleColor = new Color(sample[0], sample[1], sample[2]);
			Double distance = getColorDistance(refColor, sampleColor);
			
			totalDistance += distance;
			count ++;
			//distanceList.add(distance);
		}
		
		return totalDistance/count;
		
	}
	
	@Override
	public BillDetected recognizeBill() {
		
		List<MoneyMapper> sortedList = new ArrayList<MoneyMapper>();

		//calculate the average color distance between every sample and the reference color
		sortedList.add(new MoneyMapper(BillDetected.MONOPOLY_WHITE, calculateAvgColorDistanceBetweenEverySampleAndReferenceColor(this.white)));
		sortedList.add(new MoneyMapper(BillDetected.MONOPOLY_RED, calculateAvgColorDistanceBetweenEverySampleAndReferenceColor(this.red)));
		sortedList.add(new MoneyMapper(BillDetected.MONOPOLY_BLUE, calculateAvgColorDistanceBetweenEverySampleAndReferenceColor(this.blue)));
		sortedList.add(new MoneyMapper(BillDetected.MONOPOLY_GREEN, calculateAvgColorDistanceBetweenEverySampleAndReferenceColor(this.green)));
		
		
		//Sort it ASC
		Collections.sort(sortedList, new Comparator<MoneyMapper>() {

			@Override
			public int compare(MoneyMapper o1, MoneyMapper o2) {
				return Double.compare(o1.distance, o2.distance);
			}
		});
				
		//The one with smallest difference should be our bill
		return sortedList.get(0).bill;
	}
	
	private static class MoneyMapper{
		private BillDetected bill;
		private Double distance;
		
		public MoneyMapper(BillDetected bill, Double distance){
			this.bill = bill;
			this.distance= distance;
		}
	}

}
