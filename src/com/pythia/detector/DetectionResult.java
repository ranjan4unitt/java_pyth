package com.pythia.detector;

import java.util.List;

import com.hades.data.Packet;

public class DetectionResult {
	private List<Packet> window;
	private LossMetrics loss;
	private ReorderingMetrics reOrdering;
	private DelayMetrics delay;
	private boolean hasDelay;
	private boolean hasLoss;
	// yet to figure out reordering logic
	private boolean hasReordering;
	private static final double DELAY_WIDTH_THRESHOLD = 2; // in ms
	public static final Double DENSITY_CUTOFF = 0.4;

	public DetectionResult(List<Packet> window, LossMetrics loss,
			ReorderingMetrics reOrdering, DelayMetrics delay, boolean hasDelay,
			boolean hasLoss, boolean hasReordering) {
		super();
		this.window = window;
		this.loss = loss;
		this.reOrdering = reOrdering;
		this.delay = delay;
		this.hasDelay = hasDelay;
		this.hasLoss = hasLoss;
		this.hasReordering = hasReordering;
	}

	public DetectionResult(List<Packet> window) {
		super();
		this.window = window;
		this.loss = EventDetector.detectLoss(window);
		this.reOrdering = EventDetector.detectReordering(window);
		this.delay = EventDetector.detectDelay(window);
		if (this.delay != null
				&& (this.delay.getDensity() < DENSITY_CUTOFF || this.delay
						.getMode() - this.delay.getModeStart() > DELAY_WIDTH_THRESHOLD))
			this.hasDelay = true;
		else
			this.hasDelay = false;

		if (this.loss != null && loss.getLostPktsCount() > 0) {
			this.hasLoss = true;
		}
	}

	public List<Packet> getWindow() {
		return window;
	}

	public LossMetrics getLoss() {
		return loss;
	}

	public ReorderingMetrics getReOrdering() {
		return reOrdering;
	}

	public boolean hasDelay() {
		return hasDelay;
	}

	public boolean hasLoss() {
		return hasLoss;
	}

	public boolean hasReordering() {
		return hasReordering;
	}

	public int getDuration() {
		if (this.window == null)
			return 0;

		return -1 * this.window
				.get(0)
				.getSendTime()
				.subtract(this.window.get(this.window.size() - 1).getRecvTime())
				.intValue();
	}

	public int getStartTime() {
		if (this.window == null)
			return 0;
		return this.window.get(0).getSendTime().intValue();
	}

	public int getEndTime() {
		if (this.window == null)
			return 0;
		return this.window.get(this.window.size() - 1).getSendTime().intValue();
	}

	public DelayMetrics getDelay() {
		return delay;
	}
	
}
