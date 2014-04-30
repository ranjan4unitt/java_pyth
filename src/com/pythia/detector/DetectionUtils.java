package com.pythia.detector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hades.data.Packet;

public class DetectionUtils {
	private static final int MIN_EVENT_LEGTH_THRESHOLD = 0;

	public static DetectionResult mergeCloseEvents(DetectionResult first,
			DetectionResult second) {
		if (first == null)
			return second;
		if (second == null)
			return first;

		if (first.getEndTime() - second.getStartTime() < MIN_EVENT_LEGTH_THRESHOLD) {
			List<Packet> newWindow = new ArrayList<>();
			newWindow.addAll(first.getWindow());
			newWindow.addAll(second.getWindow());
			Map<Integer, Double> netLoss= new HashMap<>();
			netLoss.putAll(first.getLoss().getLostSeqTimeStamp());
			netLoss.putAll(second.getLoss().getLostSeqTimeStamp());
			LossMetrics newLoss = new LossMetrics(first.getLoss()
					.getLostPktsCount() + second.getLoss().getLostPktsCount(), netLoss);

			return new DetectionResult(newWindow, newLoss, null, first.getDelay(),
					first.hasDelay() || second.hasDelay(), first.hasLoss()
							|| second.hasLoss(), first.hasReordering()
							|| second.hasReordering());
		}
		return second; // return the last window. Ignore the first one as it is one off and scattered
	}
}
