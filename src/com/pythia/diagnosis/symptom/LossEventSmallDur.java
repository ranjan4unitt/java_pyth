package com.pythia.diagnosis.symptom;

import com.pythia.detector.DetectionResult;
import com.pythia.diagnosis.checks.loss.LossRelatedChecks;

public class LossEventSmallDur   {

	public static SymptomResult exists(DetectionResult event) {
		if (LossRelatedChecks.checkShortOutage(event) == 1)
			return SymptomResult.TRUE;
		else
			return SymptomResult.FALSE;
	}

}
