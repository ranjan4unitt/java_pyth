package com.pythia.diagnosis.checks;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.hades.data.Packet;
import com.pythia.detector.DetectionResult;
import com.utils.MathUtils;

public class NTPshift {
	private final double NTPDIFF_THRESHOLD = 3; // in ms

	public int check(DetectionResult event) {

		List<Packet> window = event.getWindow();
		if (window.size() <= 1)
			return 0;

		double starttime = window.get(0).getSendTimeMilli();
		double oldSnd = 0;
		double oldRcv = window.get(0).getDelay();
		Set<Double> slopes = new HashSet<>();
		for (int i = 0; i < window.size(); ++i) {
			double snd = .001 * (window.get(i).getSendTimeMilli() - starttime);
			double rcv = window.get(i).getDelay() + snd;
			if (snd - oldSnd != 0)
				slopes.add(rcv - oldRcv / snd - oldSnd);
			oldSnd = snd;
			oldRcv = rcv;
		}
		double medianSlope = MathUtils.median(slopes);

		double sine = Math.sin(Math.atan2(medianSlope, 1));
		double cosine = Math.cos(Math.atan2(medianSlope, 1));

		Set<Double> left = new HashSet<>();
		Set<Double> right = new HashSet<>();
		for (int i = 0; i < window.size() * 0.2; ++i) {
			double snd = .001 * (window.get(i).getSendTimeMilli() - starttime);
			double rcv = window.get(i).getDelay() + snd;
			left.add(-1 * snd * sine + rcv * cosine);

			snd = .001 * (window.get(window.size() - 1 - i).getSendTimeMilli() - starttime);
			rcv = window.get(window.size() - 1 - i).getDelay() + snd;
			right.add(-1 * snd * sine + rcv * cosine);
		}

		double medianLeft = MathUtils.median(left);
		double medianRight = MathUtils.median(right);

		return medianRight > medianLeft + NTPDIFF_THRESHOLD
				|| medianRight < medianLeft - NTPDIFF_THRESHOLD ? 1 : 0;

	}
}
