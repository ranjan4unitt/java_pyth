package com.pythia.diagnosis.checks;

public class UtilizationCheckResult {
	public int isBursty;
	public double utilization;
	public UtilizationCheckResult(int isBursty, double utilization) {
		super();
		this.isBursty = isBursty;
		this.utilization = utilization;
	}
	
}
