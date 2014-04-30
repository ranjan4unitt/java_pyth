package com.pythia.diagnosis.symptom;

import com.pythia.detector.DetectionResult;

public class Loss  {

	public static SymptomResult exists(DetectionResult event) {
		return event.hasLoss() ? SymptomResult.TRUE : SymptomResult.FALSE;
	}

}
