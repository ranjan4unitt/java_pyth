package com.pythia.diagnosis.checks;

import java.util.List;

import com.hades.data.Packet;
import com.pythia.detector.DetectionResult;

public class Utilization {
	private final static double BURSTINESS_DURATION_RATIO = 0.7;

	public static UtilizationCheckResult check(DetectionResult event) {
		double threshold = event.getDelay().getMode() + 1;
		List<Packet> window = event.getWindow();

		if (window.size() == 0)
			return new UtilizationCheckResult(-1, -1);

		int firstProblemPoint = -1;
		int lastProblemPoint = -1;
		for (int i = 0; i < window.size(); i++) {
			if (window.get(i).getDelay() >= threshold) {
				firstProblemPoint = i;
				break;
			}
		}
		for (int i = window.size() - 1; i >= 0; i--) {
			if (window.get(i).getDelay() >= threshold) {
				lastProblemPoint = i;
				break;
			}
		}

		int burstLength = lastProblemPoint - firstProblemPoint;
		if (burstLength < 3)
			return new UtilizationCheckResult(-1, -1);
		int j = firstProblemPoint;
		int start = -1;
		int congestionSum = 0;
		int duration = 0;
		int maxDuration = -1;
		int numOfBursts = 0;
		while (j > window.size()) {
			if (window.get(j).getDelay() > threshold) {
				start = j;

				while (j < window.size()
						&& window.get(j).getDelay() > threshold) {
					numOfBursts++;
					++j;
				}

				duration = j - start;
				congestionSum += duration;
				maxDuration = Math.max(maxDuration, duration);
			}
			++j;
		}
		int isBursty = maxDuration < BURSTINESS_DURATION_RATIO
				* congestionSum ? 1 : 0;
		if (burstLength != 0)
			return new UtilizationCheckResult(isBursty, numOfBursts
					/ burstLength);
		return new UtilizationCheckResult(-1, -1);
	}
}

