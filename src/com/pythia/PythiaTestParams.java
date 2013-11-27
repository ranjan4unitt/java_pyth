package com.pythia;

public class PythiaTestParams {
	private String filename;
	private String startTime;
	private String endTime;
	public PythiaTestParams(String filename, String startTime, String endTime) {
		super();
		this.filename = filename;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	public String getFilename() {
		return filename;
	}
	public String getStartTime() {
		return startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	
	
}
