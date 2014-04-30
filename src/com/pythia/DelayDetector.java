package com.pythia;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.hades.data.Packet;
import com.pythia.kernelestimator.UnivariateKernelEstimator;

public class DelayDetector {
	private static int RANGE = 5; // in seconds
	private static Logger logger = Logger.getLogger("DelayDetector");
	private final double binWidth = 0.1;
	
	List<List<Packet>> detectDelay(List<Packet> input){
		mergePacketsWithSmallDelay(input);
		List<List<Packet>> windows = divideBySendTime(input, RANGE);
		List<List<Packet>> toReturn = new ArrayList<>();
		for (List<Packet> w : windows) {
			if(hasDelay(w)){
				toReturn.add(w);
			}
		}
		return null;
	}
	private boolean hasDelay(List<Packet> window) {
		MinMax minMaxDelay = getMin(window);
		UnivariateKernelEstimator gauss = new UnivariateKernelEstimator();
		for (Packet pkt : window) {
			gauss.addValue(pkt.getDelay(), 1);
		}
		// how to find the min delay local maxima ??
		double prevDensity = -1;
		double nextDensity = -1;
		double currDensity = -1000;
		double currMax = -1;
		double density = 0;
		
		for(double del = minMaxDelay.min - binWidth; del < minMaxDelay.max + binWidth;){
			if(prevDensity != -1){
				
			}
			nextDensity = gauss.logDensity(del);
			if(nextDensity > currDensity) currMax = del;
			density += binWidth * nextDensity;
			
			prevDensity = currDensity;
			currDensity = nextDensity;
		}
		
		return false;
	}
	private MinMax getMin(List<Packet> window) {
		double min = 100000;
		double max = -10000;
		for (Packet p : window) {
			if(p.getDelay() > max) max = p.getDelay();
			else if(p.getDelay() < min) min = p.getDelay();
		}
		MinMax ret = new MinMax();
		ret.min = min;
		ret.max = max;
		
		return ret;
	}
	private void mergePacketsWithSmallDelay(List<Packet> input) {
		if(input == null || input.size() == 1) return;
		for(int i = 1; i < input.size(); i++){
			if(input.get(i).getDelay() - input.get(i-1).getDelay() < .1)
				input.get(i).setDelay(input.get(i-1).getDelay());
		}
	}
	private List<List<Packet>> divideBySendTime(List<Packet> input, int range) {
		List<List<Packet>> toReturn = new ArrayList<>();
		BigInteger startVal = input.get(0).getSendTime();
		List<Packet> window = new ArrayList<>();
		for(int i = 0; i < input.size(); i++){
			if(input.get(i).getSendTime().compareTo(startVal.add(new BigInteger(range+""))) < 0){
				window.add(input.get(i));
			}
			else{
				toReturn.add(window);
				window = new ArrayList<>();
				window.add(input.get(i));
				startVal = startVal.add(new BigInteger("5"));
			}
		}
		
		return toReturn;
	}
	
	private static class MinMax{
		double min;
		double max;
	}
}
