package com.pythia.diagnosis.symptom;

import com.pythia.detector.DetectionResult;

public class Delay {

	public static SymptomResult exists(DetectionResult event) {
		if(event.hasDelay()) return SymptomResult.TRUE;
		else return SymptomResult.FALSE;
	}


}
