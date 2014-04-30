package com.pythia.diagnosis.symptom;

import com.pythia.detector.DetectionResult;
import com.pythia.diagnosis.checks.HostNoise;

public class UnipointPeaks  {

	public static SymptomResult exists(DetectionResult event) {
		int result = HostNoise.check(event);
		
		return result == 1 ? SymptomResult.TRUE : SymptomResult.FALSE;
	}
}
