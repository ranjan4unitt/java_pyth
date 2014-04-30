package com.pythia.detector;

public class DelayMetrics {
	private Double density;
	private Double mode;
	private Double modeStart;
	private Double modeEnd;

	public DelayMetrics(Double density, Double mode, Double modeStart,
			Double modeEnd) {
		super();
		this.density = density;
		this.mode = mode;
		this.modeStart = modeStart;
		this.modeEnd = modeEnd;
	}

	public Double getDensity() {
		return density;
	}

	public Double getMode() {
		return mode;
	}

	public Double getModeStart() {
		return modeStart;
	}

	public Double getModeEnd() {
		return modeEnd;
	}

}
