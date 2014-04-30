package com.pythia.detector;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.graph.DataGenerator;
import com.graph.RowData;
import com.hades.data.Packet;
import com.pythia.kernelestimator.UnivariateKernelEstimator;

public class EventDetector {

	private static double binWidth = 0.1;
	private static Logger logger = Logger.getLogger("EventDetector");
	private static final double DELAY_WIDTH_THRESHOLD = 2; // in ms
	public static final Double DENSITY_CUTOFF = 0.4;

	public static DelayMetrics detectDelay(List<Packet> window) {
		MinMax minMaxDelay = getMin(window);

		// for plotting graph
		List<RowData> denVsDel = new ArrayList<>();
		if ((minMaxDelay.max - minMaxDelay.min) / binWidth < 50)
			binWidth = (minMaxDelay.max - minMaxDelay.min) / 100;
		UnivariateKernelEstimator gauss = new UnivariateKernelEstimator();
		for (Packet pkt : window) {
			gauss.addValue(pkt.getDelay(), 1);
		}
		
	/*	for (Packet pkt : window) {
			denVsDel.add(new RowData(pkt.getDelay()+"", Math.exp(gauss.logDensity(pkt.getDelay())) +""));
		}
		try {
			DataGenerator
					.generateGraphData(
							denVsDel,
							"/home/ranjan/Documents/charts/generated_data/density/",
							"section_",
							"/home/ranjan/Documents/charts/generated_data/density/dictionary.txt",
							"Density values");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		double prevDensity = -1;
		double nextDensity = -1;
		double currDensity = -1000;
		double currMax = -1;
		double density = 0;
		double currDel = 0;

		for (double del = minMaxDelay.min - binWidth; del <= minMaxDelay.max
				+ binWidth; del += binWidth) {
			currDel = del;

			nextDensity = Math.exp(gauss.logDensity(del));
			denVsDel.add(new RowData(del + "", nextDensity + ""));

			if (prevDensity != -1
					&& hasTrough(prevDensity, currDensity, nextDensity)) {
				break;
			}
			if (nextDensity > currDensity)
				currMax = del;
			density += binWidth * nextDensity;
			prevDensity = currDensity;
			currDensity = nextDensity;
		}
		
		return new DelayMetrics(density, currMax, minMaxDelay.min, currDel);
	}

	private static boolean hasTrough(double prevDensity, double currDensity,
			double nextDensity) {
		return prevDensity > currDensity && nextDensity > currDensity;
	}

	public static LossMetrics detectLoss(List<Packet> window) {
		int lostPktCount = 0;
		Map<Integer, Double> lostSeqTimeStamp = new TreeMap<>();
		for (int i = 1; i < window.size(); i++) {
			if (!hasIncreasingSeqNumber(window, i)) {
				for(Integer start = window.get(i-1).getSeqnr().intValue() + 1; start < window.get(i-1).getSeqnr().intValue(); ++start){
					lostSeqTimeStamp.put(start, window.get(i).getSendTimeMilli());
					lostPktCount++;
				}
			}
		}
		return new LossMetrics(lostPktCount,lostSeqTimeStamp);
	}

	public static ReorderingMetrics detectReordering(List<Packet> window) {
		return null;
	}

	private static MinMax getMin(List<Packet> window) {
		double min = 100000;
		double max = -10000;
		for (Packet p : window) {
			if (p.getDelay() > max)
				max = p.getDelay();
			else if (p.getDelay() < min)
				min = p.getDelay();
		}
		MinMax ret = new MinMax();
		ret.min = min;
		ret.max = max;

		return ret;
	}

	private static class MinMax {
		double min;
		double max;
	}

	private static boolean hasIncreasingSeqNumber(List<Packet> window, int i) {
		return window.get(i).getSeqnr().subtract(window.get(i - 1).getSeqnr())
				.compareTo(BigInteger.ONE) == 0;
	}

	public static boolean hasDelayEvent(List<Packet> window) {
		DelayMetrics delay = detectDelay(window);
		if (delay != null
				&& (delay.getDensity() < DENSITY_CUTOFF || delay.getMode()
						- delay.getModeStart() > DELAY_WIDTH_THRESHOLD)) {
			return true;
		}
		return false;

	}

}
