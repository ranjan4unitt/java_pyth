package com.pythia.detector;

import java.util.Map;

public class LossMetrics {
	Integer lostPktsCount;
	Map<Integer, Double> lostSeqTimeStamp;

	public LossMetrics(Integer lostPktsCount, Map<Integer, Double> lostSeqTimeStamp) {
		super();
		this.lostPktsCount = lostPktsCount;
		this.lostSeqTimeStamp = lostSeqTimeStamp;
	}

	public Integer getLostPktsCount() {
		return lostPktsCount;
	}

	public Map<Integer, Double> getLostSeqTimeStamp() {
		return lostSeqTimeStamp;
	}
	
}
