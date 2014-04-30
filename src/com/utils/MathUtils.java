package com.utils;

import java.util.Arrays;
import java.util.Set;

public class MathUtils {
	public static double median(Set<Double> nonODs) {
		if(nonODs.size() == 0)
			return -1;
		Double[] inputArr = nonODs.toArray(new Double[0]);
		
		Arrays.sort(inputArr);
		//return ($n % 2 == 0) ? ($sarr[$n/2-1] + $sarr[$n/2])/2 : $sarr[($n-1)/2];
		return inputArr.length % 2 == 0 ? (inputArr[inputArr.length/2 - 1] + inputArr[inputArr.length/2]) /2 : inputArr[(inputArr.length-1)/2];
	}
	
	public static int percentileElementIdxInArray(int arraySize, double percentile){
		if(percentile > 1 || percentile < 0)
			throw new RuntimeException("Invalid value for percentile calculation");
		
		return (int)(arraySize * percentile);
	}
}
