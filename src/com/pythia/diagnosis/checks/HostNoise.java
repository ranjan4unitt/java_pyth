package com.pythia.diagnosis.checks;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.hades.data.Packet;
import com.pythia.detector.DetectionResult;
import com.utils.MathUtils;

public class HostNoise {
	public static int check(DetectionResult event) {
		int diff = 0;
		int numOfdiff = 0;
		int d = 0;
		double mind = Integer.MAX_VALUE;
		List<Packet> window = event.getWindow();

		for (int i = 1; i < window.size(); ++i) {
			double prevDelay = window.get(i - 1).getDelay();
			if (prevDelay > event.getDelay().getMode() + 1) {
				diff += Math.abs(prevDelay
						- Math.abs(window.get(i).getDelay() - prevDelay));
				d += prevDelay;
				numOfdiff++;

				mind = Math.max(mind, prevDelay);
			}
		}

		if (numOfdiff == 0)
			return -1;
		diff /= numOfdiff;
		d -= mind;
		d /= numOfdiff;

		Set<Double> neighbors = new HashSet<>();
		Set<Double> nonODs = new HashSet<>();

		for (int i = 1; i < window.size() - 1; i++) {
			if (window.get(i).getDelay() > event.getDelay().getMode() + 1) {
				neighbors.add(window.get(i - 1).getDelay());
				neighbors.add(window.get(i + 1).getDelay());
			} else
				nonODs.add(window.get(i).getDelay());
		}

		double neighborMedian = MathUtils.median(neighbors);
		double nonOdMedian = MathUtils.median(nonODs);

		int diag1 = d > 2 * diff ? 1 : 0;
		int diag2 = neighborMedian < nonOdMedian + 1.1 ? 1 : 0;

		return diag1 | diag2;
	}

}
